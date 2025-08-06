<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <!-- <el-form-item label="广告Id" prop="adId">
        <el-input
          v-model="queryParams.adId"
          placeholder="请输入广告Id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="营利类型" prop="adProfitabilityType">
        <el-select
          v-model="queryParams.adProfitabilityType"
          placeholder="请选择营利类型"
          clearable
        >
          <el-option
            v-for="dict in dict.type.ad_profitability_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="行业分类" prop="adIndustryTypes">
        <el-select
          v-model="queryParams.adIndustryTypes"
          placeholder="请选择行业分类"
          clearable
          multiple
          filterable
        >
          <el-option
            v-for="dict in industryList"
            :key="dict"
            :label="dict"
            :value="dict"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="媒体类型" prop="adMediumTypes">
        <el-select
          v-model="queryParams.adMediumTypes"
          placeholder="请选择媒体类型"
          clearable
          multiple
          filterable
        >
          <el-option
            v-for="dict in mediumList"
            :key="dict"
            :label="dict"
            :value="dict"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="省 " prop="province">
        <el-select
          v-model="queryParams.province"
          placeholder="请选择省"
          clearable
          filterable
          @change="getCityByQuery"
        >
          <el-option
            v-for="dict in provinceList"
            :key="dict.name"
            :label="dict.name"
            :value="dict.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="市 " prop="city">
        <el-select
          v-model="queryParams.city"
          placeholder="请选择市"
          clearable
          filterable
          @change="getDistrictByQuery"
        >
          <el-option
            v-for="dict in cityList"
            :key="dict.name"
            :label="dict.name"
            :value="dict.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="区/县" prop="district">
        <el-select
          v-model="queryParams.district"
          placeholder="请选择区/县"
          clearable
          filterable
          @change="getStreetByQuery"
        >
          <el-option
            v-for="dict in districtList"
            :key="dict.name"
            :label="dict.name"
            :value="dict.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="街道" prop="street">
        <el-select
          v-model="queryParams.street"
          placeholder="请选择街道"
          clearable
          filterable
        >
          <el-option
            v-for="dict in streetList"
            :key="dict.name"
            :label="dict.name"
            :value="dict.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="处理结果" prop="handleResult">
        <el-select
          v-model="queryParams.handleResult"
          placeholder="请选择处理结果"
          clearable
        >
          <el-option
            v-for="dict in dict.type.ad_handle_result"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="处理时间" prop="handleTime">
        <el-date-picker
          clearable
          v-model="queryParams.handleTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择处理时间"
        >
        </el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="处理者" prop="handler">
        <el-input
          v-model="queryParams.handler"
          placeholder="请输入处理者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['advertisement:enforcement:add']"
          >新增</el-button
        >
      </el-col> 
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['advertisement:enforcement:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['advertisement:enforcement:remove']"
          >删除</el-button
        >
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['advertisement:enforcement:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="enforcementList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="营利类型"
        align="center"
        prop="adProfitabilityType"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.ad_profitability_type"
            :value="scope.row.adProfitabilityType"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="行业分类"
        align="center"
        prop="adIndustryTypes"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        label="媒体类型"
        align="center"
        prop="adMediumTypes"
        show-overflow-tooltip
      >
      </el-table-column>
      <!--      <el-table-column label="违法行为描述" align="center" prop="adDescription" show-overflow-tooltip/>-->
      <el-table-column
        label="广告图片"
        align="center"
        prop="adImages"
        width="100"
      >
        <template slot-scope="scope">
          <image-preview :src="scope.row.adImages" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column
        label="市-县-街道 "
        align="center"
        prop="city"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{
            scope.row.city + scope.row.district + scope.row.street
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理Id" align="center" prop="id" v-if="false" />
      <el-table-column label="处理单据" align="center" prop="adCode" />
      <el-table-column label="广告Id" align="center" prop="adId" v-if="false" />
      <el-table-column label="处理结果" align="center" prop="handleResult">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.ad_handle_result"
            :value="scope.row.handleResult"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="处理后照片"
        align="center"
        prop="postHandleImage"
        width="100"
      >
        <template slot-scope="scope">
          <image-preview
            :src="scope.row.postHandleImage"
            :width="50"
            :height="50"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="处理时间"
        align="center"
        prop="handleTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.handleTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="处理者"
        align="center"
        prop="handler"
        v-if="false"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['advertisement:enforcement:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['advertisement:enforcement:remove']"
            >删除</el-button
          > -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlePreview(scope.row)"
            v-hasPermi="['advertisement:enforcement:remove']"
            >预览报告</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改执行结果记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="广告Id" prop="adId">
          <el-input v-model="form.adId" placeholder="请输入广告Id" />
        </el-form-item>
        <el-form-item label="处理结果" prop="handleResult">
          <el-select v-model="form.handleResult" placeholder="请选择处理结果">
            <el-option
              v-for="dict in dict.type.ad_handle_result"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理后照片" prop="postHandleImage">
          <image-upload v-model="form.postHandleImage" />
        </el-form-item>
        <el-form-item label="处理时间" prop="handleTime">
          <el-date-picker
            clearable
            v-model="form.handleTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择处理时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="处理者" prop="handler">
          <el-input v-model="form.handler" placeholder="请输入处理者" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <template>
      <el-dialog
        :visible.sync="previewOpen"
        title="报告预览"
        width="80%"
        @closed="handleDialogClosed"
      >
        <!-- 自定义标题部分 -->
        <template #title>
          <div class="dialog-title">
            <span>报告预览</span>
            <el-button
              type="primary"
              icon="el-icon-download"
              size="mini"
              @click="downloadReport"
              style="float: right; margin-right: 20px"
            >
              下载报告
            </el-button>
          </div>
        </template>

        <!-- PDF预览容器 -->
        <div v-if="pdfUrl" class="pdf-container">
          <iframe
            :src="pdfUrl + '#toolbar=0&view=FitH'"
            type="application/pdf"
            style="width: 100%; height: 600px"
            frameborder="0"
          ></iframe>
        </div>
        <!-- 加载提示 -->
        <div v-else style="text-align: center; padding: 20px">
          <i class="el-icon-loading"></i> 正在加载 PDF 文件...
        </div>
      </el-dialog>
    </template>
  </div>
</template>
  </div>
</template>

<script>
import {
  listEnforcement,
  getEnforcement,
  delEnforcement,
  addEnforcement,
  updateEnforcement,
  selectReport,
} from "@/api/advertisement/enforcement";
import {
  getIndustry,
  getMedium,
  allRegional,
} from "@/api/advertisement/advertisement";
import pdf from "vue-pdf";
export default {
  components: {
    pdf,
  },
  name: "Enforcement",
  dicts: [
    "ad_profitability_type",
    "ad_industry_type",
    "ad_audit_status",
    "ad_check_status",
    "ad_industry_type",
    "ad_violation_type",
    "ad_medium_type",
    "ad_handle_result",
  ],
  data() {
    return {
      mediumList: [], //媒体列表
      industryList: [], //行业列表
      provinceList: [], //省
      cityList: [], //市
      districtList: [], //县
      streetList: [], //街道
      // 预览PDF
      previewOpen: false,
      pdfUrl: "",
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
      // 执行结果记录表格数据
      enforcementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        //广告数据
        adProfitabilityType: null,
        adIndustryType: null,
        adMediumType: null,
        adIndustryTypes: [],
        adMediumTypes: [],
        province: null,
        city: null,
        district: null,
        street: null,

        pageNum: 1,
        pageSize: 10,
        adId: null,
        handleResult: null,
        postHandleImage: null,
        handleTime: null,
        handler: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
    //获取省级行政:默认展示河北省
    allRegional({ parent: "0" }).then((response) => {
      //console.log("省级行政:",response);
      this.provinceList = response.rows;
    });
    //获取行业列表
    getIndustry().then((response) => {
      //console.log("行业列表:",response);
      this.industryList = response.data;
    });
    //获取媒体列表
    getMedium().then((response) => {
      //console.log("行业列表:",response);
      this.mediumList = response.data;
    });
  },
  methods: {
    //搜索条件：获取市级行政
    getCityByQuery() {
      //console.log("getCity:",this.form.province)
      //清空市县街道
      this.cityList = [];
      this.districtList = [];
      this.streetList = [];
      this.queryParams.city = "";
      this.queryParams.district = "";
      this.queryParams.street = "";
      //获取省级行政:默认展示河北省
      if (this.queryParams.province == "") {
        console.log("没有数据");
      } else {
        let parent = 0;
        this.provinceList.forEach((element) => {
          if (element.name == this.queryParams.province) {
            parent = element.code;
          }
        });
        //console.log("获取市:",parent)
        allRegional({ parent: parent }).then((response) => {
          this.cityList = response.rows;
        });
      }
    },
    //搜索条件：获取区县行政
    getDistrictByQuery() {
      //console.log("getCity:",this.form.province)
      //清空县街道
      this.districtList = [];
      this.streetList = [];
      this.queryParams.district = "";
      this.queryParams.street = "";
      //获取省级行政:默认展示河北省
      if (this.form.city == "") {
        console.log("没有数据");
      } else {
        let parent = 0;
        this.cityList.forEach((element) => {
          if (element.name == this.queryParams.city) {
            parent = element.code;
          }
        });
        //console.log("获取区县:",parent,this.queryParams.city)
        allRegional({ parent: parent }).then((response) => {
          this.districtList = response.rows;
        });
      }
    },
    //搜索条件：获取街道行政
    getStreetByQuery() {
      //console.log("getCity:",this.form.province)
      //清空街道
      this.streetList = [];
      this.queryParams.street = "";
      //获取省级行政:默认展示河北省
      if (this.queryParams.district == "") {
        console.log("没有数据");
      } else {
        let parent = 0;
        this.districtList.forEach((element) => {
          if (element.name == this.queryParams.district) {
            parent = element.code;
          }
        });
        console.log("获取街道:", parent, this.queryParams.district);
        allRegional({ parent: parent }).then((response) => {
          this.streetList = response.rows;
        });
      }
    },

    /** 查询执行结果记录列表 */
    getList() {
      this.loading = true;
      listEnforcement(this.queryParams).then((response) => {
        this.enforcementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        adId: null,
        handleResult: null,
        postHandleImage: null,
        handleTime: null,
        handler: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加执行结果记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getEnforcement(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改执行结果记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateEnforcement(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEnforcement(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除执行结果记录编号为"' + ids + '"的数据项？')
        .then(function () {
          return delEnforcement(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "advertisement/enforcement/export",
        {
          ...this.queryParams,
        },
        `enforcement_${new Date().getTime()}.xlsx`
      );
    },
    // 预览pdf
    handlePreview(row) {
      selectReport(row.adId)
        .then((response) => {
          if (response.code === 200) {
            // 处理文件路径（根据实际部署情况可能需要拼接域名）
            const filePath = response.msg;
            this.pdfUrl = process.env.VUE_APP_BASE_API + filePath;
            console.log("PDF URL:", this.pdfUrl); // 检查 pdfUrl 的实际值
            this.previewOpen = true;
          } else {
            this.$message.error("获取报告失败");
          }
        })
        .catch((error) => {
          this.$message.error("预览失败：" + error.message);
        });
    },
    // 下载报告
    downloadReport() {
      if (!this.pdfUrl) {
        this.$message.error("文件地址无效");
        return;
      }

      // 创建临时下载链接
      const link = document.createElement("a");
      link.href = this.pdfUrl;
      link.download = this.getFileNameFromPath(this.pdfUrl); // 获取文件名
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    // 从路径中提取文件名
    getFileNameFromPath(path) {
      return path.substring(path.lastIndexOf("/") + 1);
    },

    // 对话框关闭时重置
    handleDialogClosed() {
      this.pdfUrl = ""; // 重置PDF链接
    },
  },
};
</script>
<style scoped>
.dialog-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.pdf-container {
  position: relative;
  height: 600px;
  overflow-y: auto;
}

.el-button[type="primary"][icon="el-icon-download"] {
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>
