<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="法律编号" prop="clauseCode">
        <el-select
          v-model="queryParams.clauseCode"
          filterable
          remote
          reserve-keyword
          clearable
          placeholder="请输入法律编号"
          @change="handleQuery">
        <el-option
          v-for="item in clauses"
          :key="item.clauseCode"
          :label="item.clauseCode"
          :value="item.clauseCode"
        />
        </el-select>
      </el-form-item>
      <el-form-item label="法律名称" prop="lawName">
        <el-input
          v-model="queryParams.lawName"
          placeholder="请输入法律名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="条文序号" prop="clauseNumber">
        <el-input
          v-model="queryParams.clauseNumber"
          placeholder="请输入条文序号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['advertisement:clauses:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['advertisement:clauses:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['advertisement:clauses:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['advertisement:clauses:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="clausesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" type="index"  />
      <el-table-column label="法律编号" align="center" prop="clauseCode" />
      <el-table-column label="法律名称" align="center" prop="lawName" />
      <el-table-column label="条文序号" align="center" prop="clauseNumber" />
      <el-table-column label="条文内容" align="center" prop="content" width="600" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['advertisement:clauses:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['advertisement:clauses:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改法律法规管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="法律编号" prop="clauseCode">
          <el-input v-model="form.clauseCode" placeholder="请输入法律编号" />
        </el-form-item>
        <el-form-item label="法律名称" prop="lawName">
          <el-input v-model="form.lawName" placeholder="请输入法律名称" />
        </el-form-item>
        <el-form-item label="条文序号" prop="clauseNumber">
          <el-input v-model="form.clauseNumber" placeholder="请输入条文序号" />
        </el-form-item>
        <el-form-item label="条文内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listClauses, getClauses, delClauses, addClauses, updateClauses, listAll } from "@/api/advertisement/clauses"

export default {
  name: "Clauses",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 法律法规管理表格数据
      clausesList: [],
      // 没有分页的所有条款
      clauses: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        clauseCode: null,
        lawName: null,
        clauseNumber: null,
        content: null,
        isDeleted: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        clauseCode: [
          { required: true, message: "法律编号不能为空", trigger: "blur" },
          { validator: this.validateClauseCodeUnique, message: "法律编号不能重复", trigger: 'blur' }
        ],
        lawName: [
          { required: true, message: "法律名称不能为空", trigger: "blur" }
        ],
        clauseNumber: [
          { required: true, message: "条文序号不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "条文内容不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    listAll().then(response => {
      this.clauses = response.data
    });
  },
  methods: {
    /** 查询法律法规管理列表 */
    getList() {
      this.loading = true
      listClauses(this.queryParams).then(response => {
        this.clausesList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        clauseCode: null,
        lawName: null,
        clauseNumber: null,
        content: null,
        isDeleted: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加法律法规管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getClauses(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改法律法规管理"
      })
    },
    // 验证法律编号是否唯一
    async validateClauseCodeUnique(rule, value, callback) {
      // 如果为空，则不进行验证
      if (!value) {
        callback();
        return;
      }
      const params = {
        clauseCode: value
      };
      try {
        // 发送请求 调用验证法律编号唯一性的接口
        const response = await listClauses(params);
        // 如果是修改操作，需要排除当前正在编辑的记录
        const isDuplicate = response.rows.some(row =>
          this.form.id ? row.id !== this.form.id : true
        );

        if (isDuplicate && response.rows.length > 0) {
          // 如果已有重复的记录，则返回错误 如果验证失败，则调用 callback 并传入错误信息
          callback(new Error(rule.message || '法律编号不能重复'));
        } else {
          callback();
        }
      } catch (error) {
        console.error('验证法律编号唯一性失败:', error);
        callback();
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateClauses(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addClauses(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除法律法规管理编号为"' + ids + '"的数据项？').then(function() {
        return delClauses(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('advertisement/clauses/export', {
        ...this.queryParams
      }, `clauses_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
