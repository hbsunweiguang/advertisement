<template>
  <div ref="chart" class="chart-container"></div>
</template>

<script>
import * as echarts from 'echarts';
import resize from '@/views/statistics/mixins/resize';

export default {
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
      
      // 定义区域类型
      const areaTypes = ['商业区', '工业区', '住宅区', '文教区', '交通区'];
      
      // 处理数据
      let xAxisData = [];
      let seriesData = [];
      
      if (this.chartData && this.chartData.length > 0) {
        // 提取所有不同的区域名称
        const districtSet = new Set();
        this.chartData.forEach(item => {
          districtSet.add(item.district);
        });
        xAxisData = Array.from(districtSet);
        
        // 为每个区域类型创建数据系列
        seriesData = areaTypes.map(areaType => {
          return {
            name: areaType,
            type: 'bar',
            stack: 'total',
            emphasis: { focus: 'series' },
            data: xAxisData.map(district => {
              const item = this.chartData.find(d => d.district === district && d.area_type === areaType);
              return item ? item.count || item.value : 0;
            })
          };
        });
      } else {
        // 默认数据
        xAxisData = ['长安区', '桥西区', '新华区', '裕华区', '井陉矿区', '藁城区', '鹿泉区', '栾城区'];
        seriesData = [
          { name: '商业区', type: 'bar', stack: 'total', emphasis: { focus: 'series' }, data: [1200, 1500, 1000, 1300, 500, 800, 700, 900] },
          { name: '工业区', type: 'bar', stack: 'total', emphasis: { focus: 'series' }, data: [800, 600, 900, 500, 700, 600, 500, 400] },
          { name: '住宅区', type: 'bar', stack: 'total', emphasis: { focus: 'series' }, data: [1500, 1800, 1300, 1600, 900, 1100, 1000, 1200] },
          { name: '文教区', type: 'bar', stack: 'total', emphasis: { focus: 'series' }, data: [900, 1100, 800, 1000, 600, 700, 800, 900] },
          { name: '交通区', type: 'bar', stack: 'total', emphasis: { focus: 'series' }, data: [500, 700, 600, 800, 400, 500, 600, 700] }
        ];
      }
      
      const option = {
        color: ['#3398DB', '#2ECC71', '#F39C12', '#E74C3C', '#9B59B6'],
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: areaTypes, textStyle: { color: '#fff' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: xAxisData, axisLabel: { color: '#fff' } },
        yAxis: { type: 'value', axisLabel: { color: '#fff' } },
        series: seriesData
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