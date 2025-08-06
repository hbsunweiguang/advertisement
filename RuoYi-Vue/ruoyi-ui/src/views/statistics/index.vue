
<template>
  <dv-full-screen-container class="main-box">
    <div class="container">
      <!-- 顶部区域：包含标题装饰和时间显示 -->
      <div class="top-area">
        <div class="title-box">
          <!-- 左侧装饰 -->
          <dv-decoration-8 style="width:45%;height:50px;" />
          <!-- 中间标题 -->
          <dv-decoration-11 style="width:420px;height:60px;color:#fff;font-weight: bolder;font-size: 26px;">石家庄户外广告指数</dv-decoration-11>
          <!-- 右侧装饰 -->
          <dv-decoration-8 style="width:45%;height:50px;" :reverse="true"/>
        </div>
        <!-- 当前时间显示 -->
        <div class="time-box">
          {{ time }}
        </div>
      </div>

      <!-- 主要内容区：分为左、中、右三个区域 -->
      <div class="content-area">
        <!-- 左侧图表区：显示各区违法广告数量和违法行业数量 -->
        <div class="left-area">
          <dv-border-box-13 class="chart-card">
            <div class="chart-title">各区违法广告数量</div>
            <bar-chart class="chart-content" :chart-data="barChartData" />
          </dv-border-box-13>
          <dv-border-box-13 class="chart-card">
            <div class="chart-title">违法行业数量</div>
            <bar-chart class="chart-content" :chart-data="industryBarChartData" />
          </dv-border-box-13>
        </div>

        <!-- 中间地图区：显示石家庄各区县广告数量 -->
        <div class="middle-area">
          <dv-border-box-10 class="map-card">
            <div class="chart-title">石家庄各区县广告数量</div>
            <map-chart class="map-content" :chart-data="mapChartData" />
          </dv-border-box-10>
        </div>

        <!-- 右侧数据区：显示违规广告媒体类型、广告类型月度统计和最新违法信息 -->
        <div class="right-area">
          <dv-border-box-12 class="chart-card">
            <div class="chart-title">违规广告媒体类型</div>
            <bar-chart class="chart-content" :chart-data="mediaTypeBarChartData" style="width:100%;height:100%;"/>
          </dv-border-box-12>
          <dv-border-box-12 class="chart-card">
            <div class="chart-title">广告类型月度统计</div>
            <line-chart class="chart-content" :chart-data="profitabilityChartData" style="width:100%;height:100%;"/>
          </dv-border-box-12>
          <dv-border-box-8 class="chart-card">
            <!-- <div class="chart-title">广告类型月度统计</div> -->
            <!-- 滚动显示最新违法信息 -->
            <dv-scroll-board :config="scrollBoardConfig" style="width:100%;height:100%;" />
          </dv-border-box-8>
        </div>
      </div>

      <!-- 底部图表区：显示各区违法广告状态数量 -->
      <div class="bottom-area">
        <dv-border-box-1 class="chart-card">
          <div class="chart-title">各区违法广告状态数量</div>
          <audit-status-bar-chart class="chart-content" :chart-data="auditStatusBarData" />
        </dv-border-box-1>
      </div>
    </div>
  </dv-full-screen-container>
</template>

<script>
// 导入混入和组件
import resize from '@/views/statistics/mixins/resize';
import BarChart from './charts/BarChart.vue';
import LineChart from './charts/LineChart.vue';
import MapChart from './charts/MapChart.vue';
import DistrictChart from './charts/DistrictChart.vue';
import AuditStatusPieChart from './charts/AuditStatusPieChart.vue'; // 导入审计状态饼图组件
import AuditStatusBarChart from './charts/AuditStatusBarChart.vue'; // 添加导入审计状态柱状图组件
// 导入API方法
import {listStatistics}  from "@/api/advertisement/statistics"

export default {
  name: 'Statistics',
  // 注册组件
  components: {
    BarChart,
    LineChart,
    MapChart,
    DistrictChart,
    AuditStatusPieChart, // 注册审计状态饼图组件
    AuditStatusBarChart // 添加注册审计状态柱状图组件
  },
  // 使用混入
  mixins: [resize],
  
  // 组件数据
  data() {
    return {
      // 当前时间
      time: new Date().toLocaleString('zh-CN', { hour12: false, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' }),
      // 时间更新定时器
      timer: null,
      // 总数据
      totalData: {},
      // 滚动板配置
      scrollBoardConfig: {
        header: ['违法编号', '违法内容'],
        headerHeight: 23,
        data: [],
        index: true,
        columnWidth: [34, 100],
        align: ['left'],
        rowNum: 4,
        headerBGC: '#00BAFF',
        oddRowBGC: '#0f375f',
        evenRowBGC: '#1f4b73'
      },
      // 图表数据
      barChartData: [], // 各区违法广告数量
      mediaTypeBarChartData: [], // 违规广告媒体类型
      industryBarChartData: [], // 违法行业数量
      mapChartData: [], // 石家庄各区县广告数量
      districtChartData: [], // 各区违法广告状态数量
      profitabilityChartData: [], // 广告类型月度统计
      auditStatusPieData: [], // 添加审计状态饼图数据属性
      auditStatusBarData: [] // 添加审计状态柱状图数据属性
    }
  },
  
  // 组件创建时获取数据
  created(){
    this.getData()
  },
  
  // 组件挂载后启动时间更新定时器
  mounted() {
    this.timer = setInterval(() => {
      this.time = new Date().toLocaleString('zh-CN', { hour12: false, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' });
    }, 1000);
  },
  
  // 组件销毁前清除定时器
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
  },
  
  // 组件方法
  methods: {
    // 调用接口获取统计数据
    getData(){
      listStatistics().then(res=>{
        if(res.code == 200){
          this.totalData = res.data
          this.processAndSetData(this.totalData);
        }
      })
    },
    
    // 处理并设置图表数据
    processAndSetData(data) {
      // 设置违规广告媒体类型数据
      this.mediaTypeBarChartData = data.mediumCountMap.map(item => ({
        name: item.medium_type,
        value: item.advertisement_count
      }));
      
      // 设置违法行业数量数据
      this.industryBarChartData = data.industryCountMap.map(item => {
        return {
          name: item.industry_type,
          value: item.advertisement_count
        };
      });
      
      // 设置各区违法广告数量数据
      this.barChartData = data.districtCountMap.map(item => ({
        name: item.district,
        value: item.count
      }));
      
      // 设置区域分布图数据
      const districtChartData = [];
      const areaTypes = ['商业区', '工业区', '住宅区', '文教区', '交通区'];
      
      data.districtIllegalCountMap.forEach(item => {
        areaTypes.forEach((areaType, index) => {
          // 将数量按区域类型平均分配
          districtChartData.push({
            district: item.district,
            area_type: areaType,
            count: Math.floor(item.count / areaTypes.length) + (index < item.count % areaTypes.length ? 1 : 0)
          });
        });
      });
      
      this.districtChartData = districtChartData;
      
      // 设置广告类型月度统计数据
      if (data.profitabilityCountMap && data.profitabilityCountMap.length > 0) {
        // 合并所有盈利类型的数据，按月份分组
        const monthData = {};
        
        data.profitabilityCountMap.forEach(profitability => {
          profitability.time.forEach(item => {
            const month = `${item.month}月`;
            if (!monthData[month]) {
              monthData[month] = {};
            }
            monthData[month][profitability.profitability_type] = item.count;
          });
        });
        
        // 转换为图表所需格式
        this.profitabilityChartData = Object.keys(monthData).map(month => {
          return {
            month: month,
            publicWelfare: monthData[month]['1'] || 0,
            commercial: monthData[month]['2'] || 0
          };
        });
      } else {
        // 如果没有数据，设置默认数据
        this.profitabilityChartData = [
          { month: '1月', publicWelfare: 10, commercial: 20 },
          { month: '2月', publicWelfare: 15, commercial: 25 },
          { month: '3月', publicWelfare: 20, commercial: 30 }
        ];
      }
      
      // 更新滚动表格数据
      if (data.latestIllegalAdvertisementList && data.latestIllegalAdvertisementList.length > 0) {
        this.scrollBoardConfig = {
          ...this.scrollBoardConfig,
          data: data.latestIllegalAdvertisementList.slice(0, 7).map(item => [
            `W${item.id}`,
            item.adDescription || '无描述'
          ])
        };
      }
      
      // 设置地图数据
      this.mapChartData = data.districtIllegalCountMap.map(item => ({
        district: item.district,
        count: item.count
      }));
      
      // 设置审计状态饼图数据 - 为每个区县创建单独的数据
      this.auditStatusPieData = (data.auditStatusCountMap || []).map(item => ({
        district: item.district,
        status: item.status || []
      }));
      
      // 设置审计状态柱状图数据
      this.auditStatusBarData = data.auditStatusCountMap || [];
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
/* 主容器背景 */
.main-box {
  background: url("https://i.urusai.cc/t2s1I.jpg") no-repeat;
  background-size: cover;
}

/* 内容容器 */
.container {
  width: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
}

/* 顶部区域 */
.top-area {
  height: 10%;
  margin-bottom: 20px;
}

/* 标题盒子 */
.title-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70%;
}

/* 时间显示盒子 */
.time-box {
  text-align: center;
  color: white;
  font-size: 20px;
  height: 30%;
  line-height: 30px;
}

/* 内容区域 */
.content-area {
  display: flex;
  height: 57%;
  gap: 20px;
  // margin-bottom: 10px;
}

/* 左侧区域 */
.left-area {
  width: 25%;
  display: flex;
  flex-direction: column;
  gap: 20px;
  // background-color: saddlebrown;

}

/* 中间区域 */
.middle-area {
  width: 50%;
}

/* 右侧区域 */
.right-area {
  width: 25%;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  gap: 20px;
  // background-color: saddlebrown;
}

/* 底部区域 */
.bottom-area {
  margin-top: 24px;
  height: 25%;
}

/* 图表卡片 */
.chart-card {
  width: 100%;
  height: 100%;
  padding: 10px;
  box-sizing: border-box;
}

/* 地图卡片 */
.map-card {
  width: 100%;
  height: 100%;
  padding: 10px;
  box-sizing: border-box;
}

/* 图表标题 */
.chart-title {
  color: white;
  font-size: 16px;
  text-align: center;
}

/* 图表内容 */
.chart-content {
  width: 100%;
  height: 100%;
}

/* 地图内容 */
.map-content {
  width: 100%;
  height: 100%;
}

/* 数据汇总 */
.data-summary {
  display: flex;
  justify-content: space-around;
  color: white;
  margin-top: 10px;
}

/* 数据项 */
.data-item {
  text-align: center;
}

/* 数据项数值 */
.data-item span {
  color: #3398DB;
  font-weight: bold;
}

/* 工作成果 */
.work-result {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  height: calc(100% - 30px);
}

/* 成果项 */
.result-item {
  background-color: rgba(51, 152, 219, 0.2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 5px;
}

/* 区域图表容器 */
.district-chart-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}

.district-pie-chart {
  width: 33.33%; /* 根据需要调整，确保每行显示3个饼图 */
  height: 50%; /* 根据需要调整，确保能显示两行 */
}
</style>
