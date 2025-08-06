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
      type: Object,
      default: () => ({})
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
      
      // 审核状态映射
      const statusMap = {
        '0': '未审核',
        '1': '初审合法',
        '2': '初审存疑',
        '3': '初审违法',
        '4': '复审合法',
        '5': '驳回初审',
        '6': '复审违法',
        '7': '自动复审合法',
        '8': '自动复审违法'
      };
      
      // 处理数据
      let seriesData = [];
      
      // 处理单个区县的数据
      if (this.chartData && this.chartData.status && Array.isArray(this.chartData.status)) {
        seriesData = this.chartData.status.map(statusItem => {
          const status = statusItem.audit_status;
          const count = statusItem.count || 0;
          return {
            name: statusMap[status] || `状态${status}`,
            value: count
          };
        });
      } else {
        // 默认数据
        seriesData = [
          { name: '未审核', value: 30 },
          { name: '初审合法', value: 20 },
          { name: '初审存疑', value: 15 },
          { name: '初审违法', value: 10 },
          { name: '复审合法', value: 25 }
        ];
      }
      
      const option = {
        title: {
          text: this.chartData.district || '未知区县',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%))'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 10,
          top: 20,
          bottom: 20,
          textStyle: {
            color: '#fff'
          },
          data: seriesData.map(item => item.name)
        },
        series: [
          {
            name: '广告状态',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: seriesData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            }
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