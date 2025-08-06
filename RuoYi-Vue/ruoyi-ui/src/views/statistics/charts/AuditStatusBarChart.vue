<template>
  <div ref="chart" class="chart-container"></div>
</template>

<script>
import * as echarts from 'echarts';
import resize from '@/views/statistics/mixins/resize';

export default {
  name: 'AuditStatusBarChart',
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
      let xAxisData = []; // 区县名称
      let seriesData = []; // 不同状态的数据
      
      // 提取所有可能的状态
      const allStatus = [];
      if (this.chartData && this.chartData.length > 0) {
        this.chartData.forEach(item => {
          if (item.status && Array.isArray(item.status)) {
            item.status.forEach(statusItem => {
              if (!allStatus.includes(statusItem.audit_status)) {
                allStatus.push(statusItem.audit_status);
              }
            });
          }
        });
      }
      
      // 初始化X轴数据（区县名称）
      xAxisData = this.chartData.map(item => item.district || '未知区县');
      
      // 为每个状态创建一个系列
      seriesData = allStatus.map(status => {
        return {
          name: statusMap[status] || `状态${status}`,
          type: 'bar',
          stack: '总量', // 设置堆叠
          data: this.chartData.map(item => {
            if (item.status && Array.isArray(item.status)) {
              const statusItem = item.status.find(s => s.audit_status === status);
              return statusItem ? statusItem.count : 0;
            }
            return 0;
          })
        };
      });
      
      // ECharts图表配置
      const option = {
        // 设置柱状图的颜色
        color: ['#3398DB', '#FF4D4D', '#FFB366', '#99CC99', '#CCCCFF', '#FF99CC', '#66CCCC', '#FF6666', '#669933'],
        
        // 鼠标悬浮提示框配置
        tooltip: { 
          // 触发类型，axis表示坐标轴触发
          trigger: 'axis', 
          // 坐标轴指示器配置项
          axisPointer: { 
            // 指示器类型，shadow表示阴影指示器
            type: 'shadow' 
          },
          formatter: function(params) {
            if (!params || params.length === 0) return '';
            let tooltipText = params[0].name + '<br/>';
            params.forEach(param => {
              tooltipText += param.marker + param.seriesName + ': ' + param.value + '<br/>';
            });
            // 计算总量
            const total = params.reduce((sum, param) => sum + param.value, 0);
            tooltipText += '总量: ' + total;
            return tooltipText;
          }
        },
        
        // 图例配置
        legend: {
          data: seriesData.map(item => item.name),
          textStyle: {
            color: '#fff'
          }
        },
        
        // 直角坐标系内绘图网格配置
        grid: { 
          // 网格距离容器左侧的距离
          left: '3%', 
          // 网格距离容器右侧的距离
          right: '4%', 
          top: '10%',
          // 网格距离容器底部的距离，给X轴标签留出空间
          bottom: '15%', 
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
            // X轴标签旋转角度，防止标签重叠
            // rotate: 45,
            // 刻度标签的显示间隔，0表示全部显示
            interval: 0,
            // 刻度标签与轴线之间的距离
            margin: 10,
            color: '#fff'
          }
        },
        
        // Y轴配置
        yAxis: { 
          // 坐标轴类型，value表示数值轴
          type: 'value',
          // Y轴最小值
          min: 0,
          axisLabel: {
            color: '#fff'
          }
        },
        
        // 系列列表
        series: seriesData
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