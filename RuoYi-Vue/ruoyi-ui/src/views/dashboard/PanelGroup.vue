<template>
  <div>

    <!-- 初审人员登录显示 -->
    <el-row v-if="roles == 'check' || roles == 'admin'" :gutter="40" class="panel-group">
      <div>
        初审人员代办事宜
      </div>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="number" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              今日增加
            </div>
            <count-to :start-val="0" :end-val="addToday1Count" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="message" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              待查看
            </div>
            <count-to :start-val="0" :end-val="checkCount" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="search" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              待初审
            </div>
            <count-to :start-val="0" :end-val="approve1Count" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shopping">
            <svg-icon icon-class="time" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              48小时未处理
            </div>
            <count-to :start-val="0" :end-val="longTermUntreated1Count" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
    </el-row>
    <!-- 复审人员登录显示 -->
    <el-row v-if="roles == 'reexamine' || roles == 'admin'" :gutter="40" class="panel-group">
      <div>
        复审人员代办事宜
      </div>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="number" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              今日增加
            </div>
            <count-to :start-val="0" :end-val="addToday2Count" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="search" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              待复审
            </div>
            <count-to :start-val="0" :end-val="approve2Count" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="question" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              可疑广告
            </div>
            <count-to :start-val="0" :end-val="suspiciousCount" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shopping">
            <svg-icon icon-class="time" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              48小时未处理
            </div>
            <count-to :start-val="0" :end-val="longTermUntreated2Count" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
    </el-row>
    <!-- 处理人员登录显示 -->
    <el-row v-if="roles == 'handle' || roles == 'admin'" :gutter="40" class="panel-group">
      <div>
        执法人员代办事宜
      </div>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="number" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              今日增加
            </div>
            <count-to :start-val="0" :end-val="addToday3Count" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="people" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              待处理
            </div>
            <count-to :start-val="0" :end-val="approve3Count" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shopping">
            <svg-icon icon-class="time" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              48小时未处理
            </div>
            <count-to :start-val="0" :end-val="longTermUntreated3Count" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import {count} from "@/api/advertisement/advertisement"
import {getInfo} from "@/api/login";

export default {
  data() {
    return {
      checkCount: 0,
      approve1Count: 0,
      addToday1Count: 0,
      longTermUntreated1Count: 0,
      addToday2Count: 0,
      approve2Count: 0,
      suspiciousCount: 0,
      longTermUntreated2Count: 0,
      addToday3Count: 0,
      approve3Count: 0,
      longTermUntreated3Count: 0,
      roles : ""
    }
  },
  components: {
    CountTo
  },
  created() {
    this.countDate()
    this.getInfoDate()
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    getInfoDate() {
      getInfo().then(response => {
        console.log(response, 777)
        this.roles = response.roles[0]
      })
    },
    countDate() {
      count().then(response => {
        this.checkCount = response.data.checkCount
        this.approve1Count = response.data.approve1Count
        this.addToday1Count = response.data.addToday1Count
        this.longTermUntreated1Count = response.data.longTermUntreated1Count
        this.addToday2Count = response.data.addToday2Count
        this.approve2Count = response.data.approve2Count
        this.suspiciousCount = response.data.suspiciousCount
        this.longTermUntreated2Count = response.data.longTermUntreated2Count
        this.addToday3Count = response.data.addToday3Count
        this.approve3Count = response.data.approve3Count
        this.longTermUntreated3Count = response.data.longTermUntreated3Count
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
