<template>
  <div ref="chart" class="chart-container"></div>
</template>

<script>
import * as echarts from 'echarts';
import resize from '../mixins/resize';

export default {
  name: 'RankChart',
  mixins: [resize],
  props: {
    chartData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      chart: null
    };
  },
  mounted() {
    this.initChart();
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
      this.chart = null;
    }
  },
  watch: {
    chartData: {
      handler() {
        this.initChart();
      },
      deep: true
    }
  },
  methods: {
    initChart() {
      if (!this.$refs.chart) return;
      
      if (this.chart) {
        this.chart.dispose();
      }
      
      this.chart = echarts.init(this.$refs.chart);
      
      // 处理数据
      let yAxisData = [];
      let seriesData = [];
      
      if (this.chartData && this.chartData.length > 0) {
        // 按数量排序，显示前6名
        const sortedData = this.chartData
          .sort((a, b) => (b.count || b.value || b.advertisement_count) - (a.count || a.value || a.advertisement_count))
          .slice(0, 6);
          
        yAxisData = sortedData.map(item => item.industry_type || item.name);
        seriesData = sortedData.map(item => item.count || item.value || item.advertisement_count);
      } else {
        // 默认数据
        yAxisData = ['房地产', '金融', '教育培训', '医疗健康', '汽车', '餐饮'];
        seriesData = [350, 280, 220, 180, 150, 120];
      }
      
      const option = {
        // 坐标轴配置(横向柱状图)
        xAxis: { type: 'value' }, // 数值轴
        yAxis: { 
          type: 'category',      // 类目轴
          data: yAxisData        // Y轴数据(显示在左侧)
        },
        
        // 数据系列配置
        series: [
          { 
            name: '违规数量', 
            type: 'bar',        // 图表类型: bar(柱状图)
            data: seriesData    // 数据数组
          }
        ]
      };
      
      this.chart.setOption(option);
    }
  }
};
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 100%;
}
</style>