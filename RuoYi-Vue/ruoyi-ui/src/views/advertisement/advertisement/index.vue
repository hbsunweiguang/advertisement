<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="营利类型" prop="adProfitabilityType">
        <el-select v-model="queryParams.adProfitabilityType" placeholder="请选择营利类型" clearable>
          <el-option
            v-for="dict in dict.type.ad_profitability_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="行业分类" prop="adIndustryTypes">
        <el-select v-model="queryParams.adIndustryTypes" placeholder="请选择行业分类" clearable multiple filterable >
          <el-option
            v-for="dict in industryList"
            :key="dict"
            :label="dict"
            :value="dict"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="媒体类型" prop="adMediumTypes">
        <el-select v-model="queryParams.adMediumTypes" placeholder="请选择媒体类型" clearable multiple filterable >
          <el-option
            v-for="dict in mediumList"
            :key="dict"
            :label="dict"
            :value="dict"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="采集时间" prop="surveyTime">
        <el-date-picker clearable
                        v-model="queryParams.beginSurveyTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择采集开始时间">
        </el-date-picker>
        -
        <el-date-picker clearable
                        v-model="queryParams.endSurveyTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择采集结束时间">
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item label="违规类别" prop="violationType">
        <el-select v-model="queryParams.violationType" placeholder="请选择违规类别" clearable>
          <el-option
            v-for="dict in dict.type.ad_violation_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>-->

        <el-form-item label="省 " prop="province">
          <el-select v-model="queryParams.province" placeholder="请选择省" clearable filterable @change="getCityByQuery">
            <el-option
              v-for="dict in provinceList"
              :key="dict.name"
              :label="dict.name"
              :value="dict.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="市 " prop="city">
          <el-select v-model="queryParams.city" placeholder="请选择市" clearable filterable @change="getDistrictByQuery">
            <el-option
              v-for="dict in cityList"
              :key="dict.name"
              :label="dict.name"
              :value="dict.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="区/县" prop="district">
          <el-select v-model="queryParams.district" placeholder="请选择区/县" clearable filterable @change="getStreetByQuery">
            <el-option
              v-for="dict in districtList"
              :key="dict.name"
              :label="dict.name"
              :value="dict.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="街道" prop="street">
          <el-select v-model="queryParams.street" placeholder="请选择街道" clearable filterable >
            <el-option
              v-for="dict in streetList"
              :key="dict.name"
              :label="dict.name"
              :value="dict.name"
            />
          </el-select>
        </el-form-item>
      <el-form-item label="广告主" prop="advertiser">
        <el-input
          v-model="queryParams.advertiser"
          placeholder="请输入广告主"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

<!--      <el-form-item label="监测人" prop="surveyor">
        <el-select v-model="queryParams.surveyor" placeholder="请选择监测人" clearable filterable >
          <el-option
            v-for="dict in userList"
            :key="dict.nickName"
            :label="dict.nickName"
            :value="dict.nickName"
          />
        </el-select>
      </el-form-item>-->
      <el-form-item label="查看状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="请选择查看状态" clearable>
          <el-option
            v-for="dict in dict.type.ad_check_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="查看时间" prop="checkTime">
        <el-date-picker clearable
                        v-model="queryParams.checkTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择查看时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="处理状态 " prop="handleStatus">
        <el-select v-model="queryParams.handleStatus" placeholder="请选择处理状态 " clearable>
          <el-option
            v-for="dict in dict.type.ad_handle_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['advertisement:advertisement:add']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['advertisement:advertisement:edit']"
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
          v-hasPermi="['advertisement:advertisement:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['advertisement:advertisement:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="advertisementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column type="index"  label="序号"      width="50">
      </el-table-column>-->
      <el-table-column label="营利类型" align="center" prop="adProfitabilityType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_profitability_type" :value="scope.row.adProfitabilityType"/>
        </template>
      </el-table-column>
      <el-table-column label="行业分类" align="center" prop="adIndustryType" show-overflow-tooltip>
      </el-table-column>
      <el-table-column label="媒体类型" align="center" prop="adMediumType" show-overflow-tooltip>
      </el-table-column>
<!--      <el-table-column label="违法行为描述" align="center" prop="adDescription" show-overflow-tooltip/>-->
      <el-table-column label="广告图片" align="center" prop="adImages" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.adImages" :width="50" :height="50"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="违规类别" align="center" prop="violationType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_violation_type" :value="scope.row.violationType"/>
        </template>
      </el-table-column>-->
<!--      <el-table-column label="省 " align="center" prop="province" />-->
      <el-table-column label="市-县-街道 " align="center" prop="city" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.city+scope.row.district+scope.row.street }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="区/县" align="center" prop="district" />
      <el-table-column label="" align="center" prop="street" />-->
<!--      <el-table-column label="详细地址" align="center" prop="address" show-overflow-tooltip/>-->
<!--      <el-table-column label="经度" align="center" prop="latitude" />
      <el-table-column label="维度" align="center" prop="longitude" />-->
      <el-table-column label="广告主" align="center" prop="advertiser" />
      <el-table-column label="采集时间" align="center" prop="surveyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.surveyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="监测人" align="center" prop="surveyor" />-->
      <el-table-column label="查看状态" align="center" prop="checkStatus">
        <template slot-scope="scope">
          <el-button  size="mini" type="primary" @click="showDetail(scope.row)" v-if="scope.row.checkStatus==0">未查看</el-button>
          <el-button size="mini" type="success" @click="showDetail(scope.row)" v-if="scope.row.checkStatus==1">已查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="查看时间" align="center" prop="checkTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理状态 " align="center" prop="handleStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_handle_status" :value="scope.row.handleStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="auditStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_audit_status" :value="scope.row.auditStatus"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="报告地址" align="center" prop="report" show-overflow-tooltip/>-->
<!--      <el-table-column label="逻辑删除标志" align="center" prop="isDeleted" />-->
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['advertisement:advertisement:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['advertisement:advertisement:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleApprove(scope.row.id)"
            v-hasPermi="['advertisement:advertisement:edit']"
          >初审</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleHistory(scope.row.id)"
            v-hasPermi="['advertisement:advertisement:edit']"
          >历史追溯</el-button>
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

    <!-- 广告审批 -->
    <el-dialog title="广告初步审核" :visible.sync="showApprove" width="75%" append-to-body @close="cancelByApprove">
      <el-form ref="formByApprove" :model="records" :rules="rulesByApprove" label-width="120px"  class="my-dialog">
        <el-descriptions class="margin-top" :column="2" size="medium" border >
          <el-descriptions-item label="审核状态" >
              <el-form-item label="" prop="auditStatus">
                <el-select v-model="records.auditStatus" placeholder="请选择审核状态"
                           clearable style="width: 100%" @change="changeAuditStatus">
                  <el-option label="初审合法" value="1" />
                  <el-option label="初审存疑" value="2" />
                  <el-option label="初审违法" value="3" />
                </el-select>
              </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="违规类别" >
            <el-form-item label="" prop="violationType">
              <el-select v-model="records.violationType" clearable placeholder="请选择违规类别" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.ad_violation_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="审核意见" >
            <el-form-item label="" prop="auditComments">
              <el-input v-model="records.auditComments"  placeholder="请输入审核意见" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="违法行为描述" >
            <el-form-item label="" prop="adDescription">
              <el-input v-model="records.adDescription"  placeholder="请输入违法行为内容" />
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>
      </el-form>
      <el-descriptions class="margin-top" :column="1" size="medium" border v-if="showClauses">
        <el-descriptions-item label="请选择违法法条" >
          <el-button type="primary" icon="el-icon-search" size="mini" @click="showClauses2=true">点击选择法条</el-button>
          <el-table :fit="true" :data="clausesList"  >
<!--            <el-table-column type="selection" width="55" align="center" />-->
            <!--      <el-table-column label="序号" align="center" type="index"  />-->
            <el-table-column label="法律编号" align="center" prop="clauseCode" width="100"/>
            <el-table-column label="法律名称" align="center" prop="lawName" width="150" show-overflow-tooltip/>
            <el-table-column label="条文序号" align="center" prop="clauseNumber" width="100" />
            <el-table-column label="条文内容" align="center" prop="content"  show-overflow-tooltip/>
            <el-table-column label="操作" align="center" prop="content"  width="100">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="removeClauses(scope.row)"
                  v-hasPermi="['advertisement:advertisement:remove']"
                >移除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormByApprove">确 定</el-button>
        <el-button @click="cancelByApprove">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 显示法条列表 -->
    <el-dialog title="广告初步审核" :visible.sync="showClauses2" width="60%" append-to-body>
      <Clauses @child-data="handleChildData" :showClauses2="showClauses2"></Clauses>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="showClauses2=false">确 定</el-button>
        <el-button @click="showClauses2=false">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 查看广告详情 -->
    <el-dialog title="查看广告详情" :visible.sync="showInfo" width="60%" append-to-body>
      <el-descriptions class="margin-top" :column="2" size="medium" border >
        <el-descriptions-item label="营利类型" >
          <dict-tag :options="dict.type.ad_profitability_type" :value="form.adProfitabilityType"/>
        </el-descriptions-item>
        <el-descriptions-item label="行业分类" >
          <dict-tag :options="dict.type.ad_industry_type" :value="form.adIndustryType"/>
        </el-descriptions-item>
        <el-descriptions-item label="媒体类型" >
          <dict-tag :options="dict.type.ad_medium_type" :value="form.adMediumType"/>
        </el-descriptions-item>
        <el-descriptions-item label="违规类别" >
          <dict-tag :options="dict.type.ad_violation_type" :value="form.violationType"/>
        </el-descriptions-item>

        <el-descriptions-item label="查看状态" >
          <el-button  size="mini" type="primary" v-if="form.checkStatus==0">未查看</el-button>
          <el-button size="mini" type="success"  v-if="form.checkStatus==1">已查看</el-button>
        </el-descriptions-item>
        <el-descriptions-item label="查看时间" >
          {{ parseTime(form.checkTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item label="审核状态" >
          <dict-tag :options="dict.type.ad_audit_status" :value="form.auditStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审批" >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleApprove(form.id)"
            v-hasPermi="['advertisement:advertisement:edit']"
          >去初审</el-button>
        </el-descriptions-item>
        <el-descriptions-item label="广告主人" >{{ form.advertiser }}</el-descriptions-item>
        <el-descriptions-item label="处理状态" >
          <dict-tag :options="dict.type.ad_handle_status" :value="form.handleStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="违法行为描述" >{{ form.adDescription }}</el-descriptions-item>
      </el-descriptions>

      <el-descriptions class="margin-top" :column="2" size="medium" border >
        <el-descriptions-item label="监测人" >{{form.surveyor}}</el-descriptions-item>
        <el-descriptions-item label="采集时间" >{{ parseTime(form.surveyTime, '{y}-{m}-{d}') }}</el-descriptions-item>
<!--        <el-descriptions-item label="是否删除" >
          <el-button  size="mini" type="primary" v-if="form.isDeleted==0">未删除</el-button>
          <el-button size="mini" type="danger" v-if="form.isDeleted==1">已删除</el-button>
        </el-descriptions-item>-->
        <el-descriptions-item label="创建者" >{{form.createName}}</el-descriptions-item>
        <el-descriptions-item label="创建时间" >{{form.createTime}}</el-descriptions-item>
        <el-descriptions-item label="更新者" >{{form.updateName}}</el-descriptions-item>
        <el-descriptions-item label="更新时间" >{{form.updateTime}}</el-descriptions-item>
        <el-descriptions-item label="备注" >{{ form.remark }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions class="margin-top" :column="2" size="medium" border >
        <el-descriptions-item label="图片" :label-width="100" >
          <image-preview v-for="img in form.imgs" :key="img" :src="img" :width="100" :height="100" style="margin-left: 10px"/>
        </el-descriptions-item>

      </el-descriptions>
      <el-descriptions class="margin-top" :column="2" size="medium" border label-width="150px">
        <el-descriptions-item label="省" :label-width="100">{{ form.province }}</el-descriptions-item>
        <el-descriptions-item label="市" >{{ form.city }}</el-descriptions-item>
        <el-descriptions-item label="区/县" >{{ form.district }}</el-descriptions-item>
        <el-descriptions-item label="街道" >{{ form.street }}</el-descriptions-item>
        <el-descriptions-item label="详细地址" >{{ form.address }}</el-descriptions-item>
      </el-descriptions>
        <tlbs-map
          ref="mapRef"
          api-key="VCNBZ-OC5WM-YY76R-6MNEQ-7HAK7-YWBVU"
          :center="center"
          :zoom="zoom"
          :control="control"
          @click="onClick"
          @map_inited="onMapInited"
        >
          <tlbs-multi-marker
            ref="markerRef"
            :geometries="geometries"
            :styles="styles"
            :options="options"
          />
          <!--             <div class="control-container">
                          <button @click.stop="getLayerInstance">
                            打印点标记实例
                          </button>
                        </div>-->
        </tlbs-map>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel" type="primary">关  闭</el-button>
      </div>
    </el-dialog>


    <!-- 添加或修改广告列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body class="my-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-descriptions class="margin-top" :column="2" size="medium" border >
          <el-descriptions-item label="营利类型" >
            <el-form-item label="" prop="adProfitabilityType">
              <el-select v-model="form.adProfitabilityType" placeholder="请选择营利类型" style="width: 100%;border: none">
                <el-option
                  v-for="dict in dict.type.ad_profitability_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="行业分类" >
              <el-form-item label="" prop="adIndustryTypes">
                <el-select v-model="form.adIndustryTypes" placeholder="请选择行业分类" clearable multiple filterable >
                  <el-option
                    v-for="dict in industryList"
                    :key="dict"
                    :label="dict"
                    :value="dict"
                  />
                </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="媒体类型" >
            <el-form-item label="" prop="adMediumTypes">
              <el-select v-model="form.adMediumTypes" placeholder="请选择行业分类" clearable multiple filterable >
                <el-option
                  v-for="dict in mediumList"
                  :key="dict"
                  :label="dict"
                  :value="dict"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>

          <el-descriptions-item label="广告主" >
            <el-form-item label="" prop="advertiser">
              <el-input v-model="form.advertiser" placeholder="请输入广告主" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="违规类别" >
            <el-form-item label="" prop="violationType">
              <el-select v-model="form.violationType" clearable placeholder="请选择违规类别" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.ad_violation_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="违法行为描述" >
            <el-form-item label="" prop="adDescription">
              <el-input v-model="form.adDescription"  placeholder="请输入违法行为" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="监测人" >
            <el-form-item label="" prop="surveyor">
              <el-select v-model="form.surveyor" placeholder="请选择监测人" clearable filterable >
                <el-option
                  v-for="dict in userList"
                  :key="dict.nickName"
                  :label="dict.nickName"
                  :value="dict.nickName"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="采集时间" >
            <el-form-item label="" prop="surveyTime">
              <el-date-picker clearable
                              v-model="form.surveyTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              style="width: 100%"
                              placeholder="请选择采集时间">
              </el-date-picker>
            </el-form-item>
          </el-descriptions-item>

          <el-descriptions-item label="报告地址" >
            <el-form-item label="" prop="report">
              <el-input v-model="form.report"  placeholder="请输入报告地址" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="备注" >
            <el-form-item label="" prop="remark">
              <el-input v-model="form.remark"  placeholder="请输入备注" />
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions class="margin-top" :column="2" size="medium" border >
          <el-descriptions-item label="广告图片" >
            <el-form-item label="" prop="adImages">
              <image-upload v-model="form.adImages"/>
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>

        <el-descriptions class="margin-top" :column="2" size="medium" border >
          <el-descriptions-item label="省">
            <el-form-item label="" prop="province">
              <el-select v-model="form.province" placeholder="请选择省" clearable filterable @change="getCity">
                <el-option
                  v-for="dict in provinceList"
                  :key="dict.name"
                  :label="dict.name"
                  :value="dict.name"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="市">
            <el-form-item label="" prop="city">
              <el-select v-model="form.city" placeholder="请选择市" clearable filterable @change="getDistrict">
                <el-option
                  v-for="dict in cityList"
                  :key="dict.name"
                  :label="dict.name"
                  :value="dict.name"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="区/县">
            <el-form-item label="" prop="district">
              <el-select v-model="form.district" placeholder="请选择区/县" clearable filterable @change="getStreet">
                <el-option
                  v-for="dict in districtList"
                  :key="dict.name"
                  :label="dict.name"
                  :value="dict.name"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="街道">
            <el-form-item label="" prop="street">
              <el-select v-model="form.street" placeholder="请选择街道" clearable filterable @change="changeStreet">
                <el-option
                  v-for="dict in streetList"
                  :key="dict.name"
                  :label="dict.name"
                  :value="dict.name"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="经度">
            <el-form-item label="" prop="latitude" >
              <el-input v-model="form.latitude" placeholder="请输入经度" readonly/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="维度">
            <el-form-item label="" prop="longitude" >
              <el-input v-model="form.longitude" placeholder="请输入维度" readonly/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="详细地址">
            <el-form-item label="" prop="address">
              <el-input v-model="form.address" placeholder="请输入详细地址" />
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>
        <el-row>
          <el-col :span="24">
            <tlbs-map
              ref="mapRef"
              api-key="VCNBZ-OC5WM-YY76R-6MNEQ-7HAK7-YWBVU"
              :center="center"
              :zoom="zoom"
              :control="control"
              @click="onClick"
              @map_inited="onMapInited"
            >
              <tlbs-multi-marker
                ref="markerRef"
                :geometries="geometries"
                :styles="styles"
                :options="options"
              />
            </tlbs-map>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 历史追朔 -->
    <el-dialog title="查看历史追朔" :visible.sync="showRecords" width="65%" append-to-body >
      <el-table v-loading="loading" :data="recordsList"  class="myRecords">
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column label="审核状态" align="center" prop="auditStatus" width="100">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.ad_audit_status" :value="scope.row.auditStatus"/>
          </template>
        </el-table-column>
        <el-table-column label="法条编码" align="center" prop="clauseCode" class="myButton" width="190">
          <template slot-scope="scope">
            <el-tooltip v-for="law in scope.row.lawClausesList" :key="law.id"
                        class="item" effect="dark" :content="law.content" placement="top-start">
              <el-button type="text">{{law.lawName}}:{{law.clauseNumber}}</el-button><br>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="审核意见" align="center" prop="auditComments" show-overflow-tooltip/>
        <el-table-column label="审核人" align="center" prop="auditorName" width="100"/>
        <el-table-column label="审核时间" align="center" prop="auditTime" width="150">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.auditTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancelByHistory">关  闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listAdvertisement, getAdvertisement, delAdvertisement, addAdvertisement,
  updateAdvertisement,allRegional,getUserList,approveAdvertisement,getIndustry,
  getMedium} from "@/api/advertisement/advertisement"
import Clauses from '@/components/clauses'
import {parseTime} from "../../../utils/ruoyi";
import { listRecords } from "@/api/advertisement/records"

export default {
  name: "Advertisement",
  dicts: ['ad_violation_type', 'ad_handle_status', 'ad_industry_type', 'ad_medium_type', 'ad_check_status', 'ad_audit_status', 'ad_profitability_type'],
  data() {
    return {
      // 审核记录表格数据
      recordsList: [],
      showRecords:false,

      mediumList:[],//媒体列表
      industryList:[], //行业列表
      showClauses:false, //控制法条组件的显示和隐藏
      showClauses2:false, //控制法条组件的显示和隐藏
      clausesList:[], //法条列表

      //审核
      showApprove:false,//控制审批对话框
      records:{},//审批记录
      rulesByApprove: {
        auditStatus: [
          { required: true, message: "审批类型不能为空", trigger: "change" }
        ]
      },
      clauseCodes: [], //审批记录


      //地图
      mapRef : null,
      markerRef : null,
      center : { lat: 38.037057, lng: 114.468664 },//地图这些位置
      zoom : 10,
      control: {
        scale: {},
        zoom: {
          position: 'topRight',
        },
      },
      geometries: [
        { styleId: 'marker',  position: { lat: 38.037057, lng: 114.468664} }, //marker标记点
      ],
      styles: {
        marker: {
          width: 20,
          height: 30,
          anchor: { x: 10, y: 30 },
        },
      },
      options: {
        minZoom: 5,
        maxZoom: 15,
      },

      provinceList:[],//省
      cityList:[],//市
      districtList:[],//县
      streetList:[],//街道
      userList:[], //检测人
      showInfo:false, //是否是查看信息

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
      // 广告列表表格数据
      advertisementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        beginSurveyTime:null,
        endSurveyTime:null,
        pageNum: 1,
        pageSize: 10,
        adProfitabilityType: null,
        adIndustryType: null,
        adMediumType: null,
        adDescription: null,
        adImages: null,
        violationType: null,
        province: null,
        city: null,
        district: null,
        street: null,
        address: null,
        advertiser: null,
        surveyTime: null,
        surveyor: null,
        checkStatus: null,
        checkTime: null,
        handleStatus: null,
        auditStatus: null,
        report: null,
        isDeleted: null,
        adIndustryTypes:[],
        adMediumTypes:[]
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        adProfitabilityType: [
          { required: true, message: "营利类型不能为空", trigger: "change" }
        ],
        adIndustryTypes: [
          { required: true, message: "行业分类不能为空", trigger: "change" }
        ],
        adMediumTypes: [
          { required: true, message: "媒体类型不能为空", trigger: "change" }
        ],
        adImages: [
          { required: true, message: "广告图片不能为空", trigger: "blur" }
        ],
        violationType: [
          { required: true, message: "违规类别不能为空", trigger: "change" }
        ],
        latitude: [
          { required: true, message: "经度不能为空", trigger: "blur" }
        ],
        longitude: [
          { required: true, message: "维度不能为空", trigger: "blur" }
        ],
        advertiser: [
          { required: true, message: "广告主不能为空", trigger: "blur" }
        ],
        surveyTime: [
          { required: true, message: "采集时间不能为空", trigger: "blur" }
        ],
        surveyor: [
          { required: true, message: "监测人不能为空", trigger: "blur" }
        ],
      }
    }
  },
  components:{
    Clauses
  },
  created() {
    this.getList()
    //获取省级行政:默认展示河北省
    allRegional({parent:"0"}).then(response => {
      //console.log("省级行政:",response);
      this.provinceList = response.rows
    })
    //获取监测人信息
    getUserList({"deptId":103}).then(response => {
      //console.log("监测人信息:",response);
      this.userList = response.rows
    })
    //获取行业列表
    getIndustry().then(response => {
      //console.log("行业列表:",response);
      this.industryList = response.data
    })
    //获取媒体列表
    getMedium().then(response => {
      //console.log("行业列表:",response);
      this.mediumList = response.data
    })
  },
  methods: {
    parseTime,
    //移除选中的法条
    removeClauses(row){
      for (let i = 0; i <this.clausesList.length; i++) {
        if (this.clausesList[i].id==row.id){
          this.clausesList.splice(i,1)
        }
      }
    },
    //关闭历史追溯弹框
    cancelByHistory(){
      this.showRecords = false
      this.recordsList = []
    },
    //打开历史追溯弹框
    handleHistory(id){
      this.showRecords = true
      listRecords({"adId":id}).then(response => {
        this.recordsList = response.rows
      })
    },
    //审批时候，审批状态发生改变
    changeAuditStatus(){
      if (this.records.auditStatus == "1") {
        this.showClauses = false
      }else{
        this.showClauses = true
      }
    },
    //接收子组件中的数据
    handleChildData(data) {
      // 处理子组件传递过来的数据
      this.clausesList.push(...data) ;
      //数组去重复元素
      const ids = new Set();
      let newArr = []
      for (const item of this.clausesList) {
        if (!ids.has(item.id)) {
          ids.add(item.id);
          newArr.push(item);
        }
      }
      this.clausesList = newArr
      console.log(777,this.clausesList)
    },
    //显示初审对话框
    handleApprove(id){
      //设置广告id
      this.records.adId =id
      this.showApprove = true
    },
    // 取消按钮
    cancelByApprove() {
      this.showApprove = false
      this.showClauses = false
      this.resetByApprove()
    },
    // 表单重置
    resetByApprove() {
      this.records = {
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
        updateTime: null,
        violationType:null,
        adDescription:null,
        adIndustryTypes:[],
        adMediumTypes:[]
      }
      this.resetForm("form")
    },
    /** 审批提交按钮 */
    submitFormByApprove() {

      this.$refs["formByApprove"].validate(valid => {
        if (valid) {
          let submit = true
          //验证法条
          if (this.records.auditStatus!=1 && this.clausesList.length<=0) {
            let msg = this.records.auditStatus==2?'初审存疑':'初审违法'
            this.$message.error(msg+"时，请选则违法法条！")
            submit = false
          }
          //提交表单
          if (submit){
            //console.log(this.clauseCodes.toString())
            this.records.clauseCode = this.clausesList.map(item => item.id).toString()
            console.log("888",this.records)

            //提交审批:approveAdvertisement
            approveAdvertisement(this.records).then((res) => {
              this.$message.success("初审成功")
              this.showApprove = false //隐藏
              this.getList() //初始化数据
              this.resetByApprove() //重置表单
              this.showInfo = false //关闭查看详情对话框
              this.showClauses = false //关闭法条组件
              this.clausesList = [] //清空法条
            })
          }
        }
      })
    },

    /** 查看操作 {y}-{m}-{d} {h}:{i}:{s} */
    showDetail(row) {
      //更新查看状态
      if(row.checkStatus==0){
        updateAdvertisement({id:row.id,checkStatus:1,checkTime:parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}')})
          .then(response => {
          console.log(response)
            this.getList()//刷新数据
        })
      }
      //查看数据
      this.reset()
      const id = row.id || this.ids
      getAdvertisement(id).then(response => {
        this.form = response.data
        if (this.form.adImages.indexOf(',') !== -1){
          this.form.imgs = this.form.adImages.split(',')
        }else {
          this.form.imgs = [this.form.adImages]
        }
        //更新地图数据
        //更新标记
        this.zoom = 10
        this.center ={"lat":this.form.latitude,"lng":this.form.longitude}
        this.geometries = [{ styleId: 'marker',  position: {"lat":this.form.latitude,"lng":this.form.longitude} }]
        //显示弹框
        this.showInfo = true
      })
    },
    //地图单击
    onClick(e)  {
      //修改表单中坐标
      this.form.latitude = e.latLng.lat
      this.form.longitude = e.latLng.lng
      //更新标记
      this.geometries = [{ styleId: 'marker',  position: {"lat":e.latLng.lat,"lng":e.latLng.lng} }]

      console.log(this.geometries);

    },
    onMapInited()  {
      // 地图加载完成后，可以获取地图实例、点标记实例，调用地图实例、点标记实例方法
      console.log(this.$refs.mapRef);
      console.log(this.$refs.markerRef);
    },
    getLayerInstance()  {
      // 可以获取点标记实例，调用点标记实例方法
      console.log(this.$refs.markerRef);
    },

    //新增修改：街道
    changeStreet(){
      console.log("changeStreet:")
    },
    //新增修改：获取街道行政
    getStreet(){
      //console.log("getCity:",this.form.province)
      //清空街道
      this.streetList = []
      this.form.street=''
      //获取省级行政:默认展示河北省
      if (this.form.district==''){
        console.log("没有数据")
      }else{
        let parent = 0;
        this.districtList.forEach(element => {
          if (element.name == this.form.district) {
            parent = element.code
            //更新地图标记
            this.center ={"lat":element.lat,"lng":element.lng}
            this.geometries = [{ styleId: 'marker',  position: {"lat":element.lat,"lng":element.lng} }]
          }
        })
        console.log("获取街道:",parent,this.form.district)
        allRegional({"parent":parent}).then(response => {
          this.streetList = response.rows
        })
      }
    },
    //新增修改：获取区县行政
    getDistrict(){
      //console.log("getCity:",this.form.province)
      //清空县街道
      this.districtList = []
      this.streetList = []
      this.form.district=''
      this.form.street=''
      //获取省级行政:默认展示河北省
      if (this.form.city==''){
        console.log("没有数据")
      }else{
        let parent = 0;
        this.cityList.forEach(element => {
          if (element.name == this.form.city) {
            parent = element.code
            //更新地图标记
            this.center ={"lat":element.lat,"lng":element.lng}
            this.geometries = [{ styleId: 'marker',  position: {"lat":element.lat,"lng":element.lng} }]
          }
        })
        console.log("获取区县:",parent,this.form.city)
        allRegional({"parent":parent}).then(response => {
          this.districtList = response.rows
        })
      }
    },
    //新增修改：获取市级行政
    getCity(){
      //console.log("getCity:",this.form.province)
      //清空市县街道
      this.cityList = []
      this.districtList = []
      this.streetList = []
      this.form.city=''
      this.form.district=''
      this.form.street=''
      //获取省级行政:默认展示河北省
      if (this.form.province==''){
        console.log("没有数据")
      }else{
        let parent = 0;
        this.provinceList.forEach(element => {
          if (element.name == this.form.province) {
            parent = element.code
            //更新地图标记
            this.center ={"lat":element.lat,"lng":element.lng}
            this.geometries = [{ styleId: 'marker',  position: {"lat":element.lat,"lng":element.lng} }]
          }
        })
        //console.log("获取市:",parent)
        allRegional({"parent":parent}).then(response => {
          this.cityList = response.rows
        })
      }
    },

    //搜索条件：获取街道行政
    getStreetByQuery(){
      //console.log("getCity:",this.form.province)
      //清空街道
      this.streetList = []
      this.queryParams.street=''
      //获取省级行政:默认展示河北省
      if (this.queryParams.district==''){
        console.log("没有数据")
      }else{
        let parent = 0;
        this.districtList.forEach(element => {
          if (element.name == this.queryParams.district) {
            parent = element.code
          }
        })
        console.log("获取街道:",parent,this.queryParams.district)
        allRegional({"parent":parent}).then(response => {
          this.streetList = response.rows
        })
      }
    },
    //搜索条件：获取区县行政
    getDistrictByQuery(){
      //console.log("getCity:",this.form.province)
      //清空县街道
      this.districtList = []
      this.streetList = []
      this.queryParams.district=''
      this.queryParams.street=''
      //获取省级行政:默认展示河北省
      if (this.form.city==''){
        console.log("没有数据")
      }else{
        let parent = 0;
        this.cityList.forEach(element => {
          if (element.name == this.queryParams.city) {
            parent = element.code
          }
        })
        //console.log("获取区县:",parent,this.queryParams.city)
        allRegional({"parent":parent}).then(response => {
          this.districtList = response.rows
        })
      }
    },
    //搜索条件：获取市级行政
    getCityByQuery(){
      //console.log("getCity:",this.form.province)
      //清空市县街道
      this.cityList = []
      this.districtList = []
      this.streetList = []
      this.queryParams.city=''
      this.queryParams.district=''
      this.queryParams.street=''
      //获取省级行政:默认展示河北省
      if (this.queryParams.province==''){
        console.log("没有数据")
      }else{
        let parent = 0;
        this.provinceList.forEach(element => {
          if (element.name == this.queryParams.province) {
            parent = element.code
          }
        })
        //console.log("获取市:",parent)
        allRegional({"parent":parent}).then(response => {
          this.cityList = response.rows
        })
      }
    },
    /** 查询广告列表列表 */
    getList() {
      this.loading = true
      //审核状态：广告初审页面： 0未审核    5驳回初审
      this.queryParams.auditStatuses=[0,5]
      listAdvertisement(this.queryParams).then(response => {
        this.advertisementList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.showInfo = false

      this.reset()

    },
    // 表单重置
    reset() {
      //地图恢复到初始位置
      this.zoom = 10
      this.center ={ lat: 38.037057, lng: 114.468664 }
      this.geometries = [{ styleId: 'marker',  position: { lat: 38.037057, lng: 114.468664 } }]

      this.form = {
        id: null,
        adProfitabilityType: null,
        adIndustryType: null,
        adMediumType: null,
        adDescription: null,
        adImages: null,
        violationType: null,
        province: null,
        city: null,
        district: null,
        street: null,
        address: null,
        latitude: null,
        longitude: null,
        advertiser: null,
        surveyTime: null,
        surveyor: null,
        checkStatus: null,
        checkTime: null,
        handleStatus: null,
        auditStatus: null,
        report: null,
        isDeleted: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加广告"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log('查看')
      this.reset()
      const id = row.id || this.ids
      getAdvertisement(id).then(response => {
        this.form = response.data
        //将行业、媒体转换成数组
        this.form.adIndustryTypes = this.form.adIndustryType.split(",")
        this.form.adMediumTypes = this.form.adMediumType.split(",")

        this.open = true
        this.title = "修改广告"

      })
    },
    /** 提交按钮 */
    submitForm() {

      this.$refs["form"].validate(valid => {
        if (valid) {
          //将行业、媒体转换成字符串
          this.form.adIndustryType = this.form.adIndustryTypes.join(",")
          this.form.adMediumType = this.form.adMediumTypes.join(",")
          //console.log(this.form.adIndustryType,this.form.adMediumType)

          if (this.form.id != null) {
            updateAdvertisement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addAdvertisement(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除广告列表编号为"' + ids + '"的数据项？').then(function() {
        return delAdvertisement(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('advertisement/advertisement/export', {
        ...this.queryParams
      }, `advertisement_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
<style >
.borrow-info-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.info-item {
  margin-bottom: 15px;
}
.label {
  font-weight: bold;
  margin-right: 5px;
}
.value {
  color: #666;
}

.el-descriptions--medium.is-bordered .el-descriptions-item__cell{
  width: 100px;
}


.my-dialog  .el-input__inner{
  border: none !important;
  width: 100% !important;

}
.my-dialog  .el-form-item__content{
  margin-left: 0px !important;
}
.my-dialog  .el-form-item__error{
  left: 10px !important;
  top: 25px  !important;
}
.my-dialog  .el-form-item{
  margin-bottom: 0px !important;
}
.my-dialog .el-descriptions--medium.is-bordered .el-descriptions-item__cell{
  padding: 5px !important;
}


.my-label {
  background: #E1F3D8;
}
</style>
