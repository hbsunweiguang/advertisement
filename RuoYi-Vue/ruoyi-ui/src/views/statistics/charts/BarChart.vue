<template>
  <div ref="chart" class="chart-container"></div>
</template>

<script>
import * as echarts from 'echarts';
import resize from '@/views/statistics/mixins/resize';

export default {
  name: 'BarChart',
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
    this.$nextTick(() => {
      this.initChart();
    });
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
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
      let seriesData = [];
      
      if (this.chartData && this.chartData.length > 0) {
        xAxisData = this.chartData.map(item => item.name || item.type);
        seriesData = this.chartData.map(item => item.value || item.count || item.advertisement_count);
      } else {
        // 默认数据
        xAxisData = ['商业区', '工业区', '住宅区', '文教区', '交通区'];
        seriesData = [20, 30, 40, 50, 60];
      }
      
      // ECharts图表配置
      const option = {
        // 设置柱状图的颜色
        color: ['#3398DB'],
        
        // 鼠标悬浮提示框配置
        tooltip: { 
          // 触发类型，axis表示坐标轴触发
          trigger: 'axis', 
          // 坐标轴指示器配置项
          axisPointer: { 
            // 指示器类型，shadow表示阴影指示器
            type: 'shadow' 
          } 
        },
        
        // 直角坐标系内绘图网格配置
        grid: { 
          // 网格距离容器左侧的距离
          left: '3%', 
          // 网格距离容器右侧的距离
          right: '4%', 
          top: '3%',
          // 网格距离容器底部的距离，给X轴标签留出空间
          bottom: '5%', 
          // 是否包含坐标轴标签在内的网格区域
          containLabel: true 
        },
        
        // X轴配置
        xAxis: { 
          // 坐标轴类型，category表示类目轴
          type: 'category', 
          // X轴显示的数据
          data: xAxisData,
          // 坐标轴刻度标签的相关设置
          axisLabel: {
            // // X轴标签旋转角度，防止标签重叠
            // rotate: 45,
            // // 刻度标签的显示间隔，0表示全部显示
            // interval: 0,
            // // 刻度标签与轴线之间的距离
            // margin: 10
          }
        },
        
        // Y轴配置
        yAxis: { 
          // 坐标轴类型，value表示数值轴
          type: 'value',
          // Y轴最小值
          min: 0,
          // 移除固定的max: 10
          // 恢复动态计算最大值的函数
          max: function(value) {
            // 将最大值设置为数据最大值的1.2倍，确保柱状图不会顶到顶部
            return Math.ceil(value.max * 1.2);
          }
        },
        
        // 系列列表，每个系列通过type决定自己的图表类型
        series: [
          { 
            // 系列名称，用于tooltip的显示
            name: '数量', 
            // 图表类型，bar表示柱状图
            type: 'bar', 
            // 系列中的数据内容
            data: seriesData 
          }
        ]
      };
      
      this.chart.setOption(option);
    },
    resize() {
      if (this.chart) {
        this.chart.resize();
      }
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