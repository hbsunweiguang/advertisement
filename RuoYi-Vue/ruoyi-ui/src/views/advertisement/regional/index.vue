<template>
  <div class="app-container">
    <el-row :gutter="20">
      <splitpanes :horizontal="this.$store.getters.device === 'mobile'" class="default-theme">
        <!--行政区域数据-->
        <pane size="16">
          <el-col>
            <div class="head-container">
              <el-input v-model="areaName" placeholder="请输入区域名称" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px" />
            </div>
            <div class="head-container">
              <el-tree
                :data="areaOptions"
                :props="defaultProps"
                :expand-on-click-node="false"
                :filter-node-method="filterNode"
                ref="tree"
                node-key="code"
                highlight-current
                @node-click="handleNodeClick"
              />
            </div>
          </el-col>
        </pane>
        <!--用户数据-->
        <pane size="84">
          <!-- 原有用户列表代码保持不变 -->
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
            <el-form-item label="地区名称" prop="name">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入地区名称"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="省名称" prop="provinceName">
              <el-input
                v-model="queryParams.provinceName"
                placeholder="请输入省名称"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="市名称" prop="cityName">
              <el-input
                v-model="queryParams.cityName"
                placeholder="请输入市名称"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="区县名称" prop="countyName">
              <el-input
                v-model="queryParams.countyName"
                placeholder="请输入区县名称"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="首字母" prop="sepll">
              <el-input
                v-model="queryParams.sepll"
                placeholder="请输入首字母"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="行政区域类型" prop="type">
              <el-select v-model="queryParams.type" placeholder="请选择行政区域类型" clearable>
                <el-option
                  v-for="dict in dict.type.ad_regional_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
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
                v-hasPermi="['advertisement:regional:add']"
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
                v-hasPermi="['advertisement:regional:edit']"
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
                v-hasPermi="['advertisement:regional:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleExport"
                v-hasPermi="['advertisement:regional:export']"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="regionalList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="地址编码" align="center" prop="code" />
            <el-table-column label="地区名称" align="center" prop="name" />
            <el-table-column label="地区全称" align="center" prop="sname" show-overflow-tooltip/>
            <el-table-column label="省" align="center" prop="provinceName" />
            <el-table-column label="市" align="center" prop="cityName" />
            <el-table-column label="区县" align="center" prop="countyName" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['advertisement:regional:edit']"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['advertisement:regional:remove']"
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
        </pane>
      </splitpanes>
    </el-row>
    <!-- 添加或修改行政区域管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row>
        <el-col :span="12">
            <el-form-item label="地址编码" prop="name" v-if="form.code==null">
              <el-input v-model="form.code" placeholder="请输入地址编码" />
            </el-form-item>
            <el-form-item label="地区名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入地区名称" />
            </el-form-item>
            <el-form-item label="地区全称" prop="sname">
              <el-input v-model="form.sname" type="textarea" placeholder="请输入内容" />
            </el-form-item>
            <el-form-item label="父编码" prop="parent">
              <el-input v-model="form.parent" placeholder="请输入父编码" />
            </el-form-item>
            <el-form-item label="默认值(预留)" prop="initialization">
              <el-input v-model="form.initialization" placeholder="请输入默认值(预留)" />
            </el-form-item>
            <el-form-item label="首字母" prop="sepll">
              <el-input v-model="form.sepll" placeholder="请输入首字母" />
            </el-form-item>
            <el-form-item label="行政区域类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择行政区域类型" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.ad_regional_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          <el-form-item label="同级下排序" prop="order">
            <el-input v-model="form.order" placeholder="请输入同级下排序" />
          </el-form-item>
          <el-form-item label="行政区域等级" prop="level">
            <el-select v-model="form.level" placeholder="请选择行政区域等级" style="width: 100%">
              <el-option
                v-for="dict in dict.type.ad_regional_level"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">

          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注" />
          </el-form-item>
          <el-form-item label="所属国家名" prop="nationName">
            <el-input v-model="form.nationName" placeholder="请输入所属国家名" />
          </el-form-item>
          <el-form-item label="所属省名称" prop="provinceName">
            <el-input v-model="form.provinceName" placeholder="请输入所属省名称" />
          </el-form-item>
          <el-form-item label="所属市名称" prop="cityName">
            <el-input v-model="form.cityName" placeholder="请输入所属市名称" />
          </el-form-item>
          <el-form-item label="所属区县名称" prop="countyName">
            <el-input v-model="form.countyName" placeholder="请输入所属区县名称" />
          </el-form-item>
          <el-form-item label="所属街道名称" prop="townName">
            <el-input v-model="form.townName" placeholder="请输入所属街道名称" />
          </el-form-item>
          <el-form-item label="经度" prop="lng">
            <el-input v-model="form.lng" placeholder="请输入经度" />
          </el-form-item>
          <el-form-item label="纬度" prop="lat">
            <el-input v-model="form.lat" placeholder="请输入纬度" />
          </el-form-item>
        </el-col>
      </el-row>



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRegional, getRegional, delRegional, addRegional, updateRegional, listArea } from "@/api/advertisement/regional"
import { getToken } from "@/utils/auth"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import { Splitpanes, Pane } from "splitpanes"
import "splitpanes/dist/splitpanes.css"

export default {
  name: "Regional",
  dicts: ['ad_regional_type', 'ad_regional_level'],
  components: { Treeselect, Splitpanes, Pane },
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
      // 行政区域管理表格数据
      regionalList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        sname: null,
        parent: null,
        initialization: null,
        sepll: null,
        type: null,
        order: null,
        level: null,
        villageType: null,
        nationName: null,
        provinceName: null,
        cityName: null,
        countyName: null,
        townName: null,
        lng: null,
        lat: null
      },
      // 新增区域相关数据
      areaName: undefined, // 区域搜索名称
      areaOptions: [],     // 行政区域树数据
      // 修改默认props配置
      defaultProps: {
        children: "children",
        label: "label" // 改为使用areaName作为显示字段
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "地址编码不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "地区名称不能为空", trigger: "blur" }
        ],
        parent: [
          { required: true, message: "父编码不能为空", trigger: "blur" }
        ],
        sepll: [
          { required: true, message: "首字母不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "行政区域类型不能为空", trigger: "change" }
        ],
        order: [
          { required: true, message: "同级下排序不能为空", trigger: "blur" }
        ],
        level: [
          { required: true, message: "行政区域等级不能为空", trigger: "change" }
        ],
      }
    }
  },
  watch: {
    // 修改为根据名称筛选区域树
    areaName(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getList()
    this.getAreaTree() // 改为获取行政区域树
  },
  methods: {
    /** 查询行政区域树结构 */
    getAreaTree() {
      listArea().then(response => {
        this.areaOptions = this.buildTree(response.data)
      })
    },
    /** 构建树形结构 */
    buildTree(data, parent = 0) {
      const tree = []
      data.forEach(item => {
        if (item.parent == parent) {
          const children = this.buildTree(data, item.code)
          if (children.length > 0) {
            item.children = children
          }
          tree.push({
            code: item.code,
            label: item.name,
            children: children
          })
        }
      })
      return tree
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 节点单击事件 - 修改为根据区域筛选用户
    handleNodeClick(data) {
      console.log(data)
      // this.queryParams.sname = data.label // 假设后端支持根据areaId查询
      this.queryParams.parent = data.code
      this.handleQuery()
    },
    /** 查询行政区域管理列表 */
    getList() {
      this.loading = true
      listRegional(this.queryParams).then(response => {
        this.regionalList = response.rows
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
        code: null,
        name: null,
        sname: null,
        parent: null,
        initialization: null,
        sepll: null,
        type: null,
        order: null,
        level: null,
        remark: null,
        villageType: null,
        nationName: null,
        provinceName: null,
        cityName: null,
        countyName: null,
        townName: null,
        lng: null,
        lat: null
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
      this.ids = selection.map(item => item.code)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加行政区域管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const code = row.code || this.ids
      getRegional(code).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改行政区域管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.code != null) {
            updateRegional(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRegional(this.form).then(response => {
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
      const codes = row.code || this.ids
      this.$modal.confirm('是否确认删除行政区域管理编号为"' + codes + '"的数据项？').then(function() {
        return delRegional(codes)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('advertisement/regional/export', {
        ...this.queryParams
      }, `regional_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
<style scoped>
.app-container {
  padding: 20px;
  height: calc(100vh - 90px);
}

.splitpanes.default-theme .splitpanes__pane {
  background: #fff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.splitpanes.default-theme .splitpanes__splitter {
  background-color: #f0f0f0;
  border-left: 1px solid #e6e6e6;
  border-right: 1px solid #e6e6e6;
  width: 6px;
}

.head-container {
  padding: 10px;
  background: #f8f8f8;
  border-radius: 4px;
  margin-bottom: 15px;
}

.el-tree {
  min-height: 300px;
  max-height: calc(100vh - 250px);
  overflow-y: auto;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
}

.el-table {
  margin-top: 15px;
}

.mb8 {
  margin-bottom: 15px;
}

.el-form-item {
  margin-bottom: 18px;
}

.el-input, .el-select, .el-textarea {
  width: 100%;
}

.el-dialog__body {
  padding: 20px;
}

/* 树节点高亮样式 */
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background-color: #f0f7ff;
  color: #409EFF;
  font-weight: bold;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .app-container {
    height: auto;
    padding: 10px;
  }

  .el-col {
    margin-bottom: 15px;
  }

  .el-tree {
    max-height: 200px;
  }

  .el-form--inline .el-form-item {
    margin-right: 10px;
  }
}

/* 表格操作按钮间距调整 */
.el-button--mini {
  margin: 0 5px;
}

/* 表单对话框布局优化 */
.el-row {
  margin-bottom: 0;
}

.el-col {
  padding: 0 10px;
}
</style>
