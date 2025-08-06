<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="广告ID" prop="adId">
        <el-input
          v-model="queryParams.adId"
          placeholder="请输入广告ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable>
          <el-option
            v-for="dict in dict.type.ad_audit_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="当审核状态是违法、存疑状态，可以选择法条数据,可以多条逗号隔开" prop="clauseCode">
        <el-input
          v-model="queryParams.clauseCode"
          placeholder="请输入当审核状态是违法、存疑状态，可以选择法条数据,可以多条逗号隔开"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="审核人" prop="auditor">
        <el-input
          v-model="queryParams.auditor"
          placeholder="请输入审核人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核时间" prop="auditTime">
        <el-date-picker clearable
                        v-model="queryParams.auditTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择审核时间">
        </el-date-picker>
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
          v-hasPermi="['advertisement:records:add']"
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
          v-hasPermi="['advertisement:records:edit']"
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
          v-hasPermi="['advertisement:records:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['advertisement:records:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="广告ID" align="center" prop="adId" />
      <el-table-column label="审核状态" align="center" prop="auditStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_audit_status" :value="scope.row.auditStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="法条编码" align="center" prop="clauseCode" class="myButton">
        <template slot-scope="scope">
          <el-tooltip v-for="law in scope.row.lawClausesList" :key="law.id"
                      class="item" effect="dark" :content="law.content" placement="top-start">
            <el-button type="text">{{law.lawName}}:{{law.clauseNumber}}</el-button>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="审核意见" align="center" prop="auditComments" />
      <el-table-column label="审核人" align="center" prop="auditorName" />
      <el-table-column label="审核时间" align="center" prop="auditTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.auditTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['advertisement:records:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['advertisement:records:remove']"
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

    <!-- 添加或修改审核记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="广告ID" prop="adId">
          <el-input v-model="form.adId" placeholder="请输入广告ID" />
        </el-form-item>
        <el-form-item label="审核状态" prop="auditStatus">
          <el-select v-model="form.auditStatus" placeholder="请选择审核状态">
            <el-option
              v-for="dict in dict.type.ad_audit_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="法条编码" prop="clauseCode">
          <el-input v-model="form.clauseCode" placeholder="请输入法条编码" />
        </el-form-item>
        <el-form-item label="审核意见" prop="auditComments">
          <el-input v-model="form.auditComments" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="审核人" prop="auditor">
          <el-input v-model="form.auditor" placeholder="请输入审核人" />
        </el-form-item>
        <el-form-item label="审核时间" prop="auditTime">
          <el-date-picker clearable
                          v-model="form.auditTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择审核时间">
          </el-date-picker>
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
import { listRecords, getRecords, delRecords, addRecords, updateRecords } from "@/api/advertisement/records"

export default {
  name: "Records",
  dicts: ['ad_audit_status'],
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
      // 审核记录表格数据
      recordsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        adId: null,
        auditStatus: null,
        clauseCode: null,
        auditComments: null,
        auditor: null,
        auditTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询审核记录列表 */
    getList() {
      this.loading = true
      listRecords(this.queryParams).then(response => {
        this.recordsList = response.rows
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
        adId: null,
        auditStatus: null,
        clauseCode: null,
        auditComments: null,
        auditor: null,
        auditTime: null,
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
      this.title = "添加审核记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRecords(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改审核记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRecords(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRecords(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除审核记录编号为"' + ids + '"的数据项？').then(function() {
        return delRecords(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('advertisement/records/export', {
        ...this.queryParams
      }, `records_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
<style scoped>
.el-button + .el-button{
  margin-left: 0px !important;
}
</style>
