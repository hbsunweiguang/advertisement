// 引入 ECharts 和适配器
import * as echarts from '../../ec-canvas/echarts';

Page({
  data: {
    ec: {
      lazyLoad: true // 启用懒加载
    }
  },

  onReady() {
    // 初始化图表
    this.initChart();
  },

  initChart() {
    // 获取组件实例
    this.ecComponent = this.selectComponent('#mychart');
    
    this.ecComponent.init((canvas, width, height) => {
      // 初始化图表
      const chart = echarts.init(canvas, null, {
        width: width,
        height: height
      });
      
      // 示例配置
      const option = {
        title: {
          text: '扫码统计图表'
        },
        tooltip: {},
        xAxis: {
          data: ['周一', '周二', '周三', '周四', '周五']
        },
        yAxis: {},
        series: [{
          name: '扫码量',
          type: 'bar',
          data: [5, 20, 36, 10, 15]
        }]
      };
      
      chart.setOption(option);
      return chart;
    });
  },

});