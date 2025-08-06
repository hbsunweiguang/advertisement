<template>
  <div ref="chart" class="chart-container"></div>
</template>

<script>
import * as echarts from 'echarts';
import resize from '../mixins/resize';

export default {
  name: 'LineChart',
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
      let xAxisData = [];
      let publicWelfareData = [];
      let commercialData = [];
      
      if (this.chartData && this.chartData.length > 0) {
        xAxisData = this.chartData.map(item => item.month || item.name);
        publicWelfareData = this.chartData.map(item => item.publicWelfare || 0);
        commercialData = this.chartData.map(item => item.commercial || 0);
      } else {
        // 默认数据
        xAxisData = ['1月', '2月', '3月', '4月', '5月', '6月'];
        publicWelfareData = [50, 70, 60, 80, 90, 100];
        commercialData = [120, 190, 150, 180, 200, 250];
      }
      
      const option = {
        // 颜色配置 - 可添加多个颜色对应不同折线
        color: ['#3398DB', '#E74C3C'],
        
        // 图例配置
        legend: {
          data: ['公益广告', '商业广告'], // 图例项
          top: 0,                      // 位置(top/bottom/left/right)
          textStyle: { color: '#fff' }  // 文字样式
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            lineStyle: {
              color: '#fff'
            }
          },
          backgroundColor: 'rgba(255, 255, 255)', // 设置tooltip背景色为半透明黑色
          textStyle: {
            color: '#666666'  // 文字颜色
          }
        },
        
        // X轴配置
        xAxis: { 
          type: 'category',  // 类目轴
          boundaryGap: false, // 坐标轴两端留白策略
          data: xAxisData     // X轴数据
        },
        
        // Y轴配置
        yAxis: {
          type: 'value',     // 数值轴
          // 可以根据需要添加更多配置，例如：
          min: 0,         // 最小值
          // axisLabel: {    // 坐标轴刻度标签
          //   color: '#fff'  // 标签颜色
          // }
        },
        grid: {
          top: '30',
          left: '10',
          right: '20',
          bottom: '0',
          containLabel: true  // 包含坐标轴标签在内的网格区域
        },
        
        // 数据系列配置(多条折线)
        series: [
          {
            name: '公益广告', 
            type: 'line',     // 图表类型: line(折线图)
            // stack: '总量',    // 堆叠标识(相同值会堆叠)
            data: publicWelfareData // 数据数组
          },
          {
            name: '商业广告', 
            type: 'line', 
            // stack: '总量', 
            data: commercialData
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
  /* min-height: 200px; */
}
</style>