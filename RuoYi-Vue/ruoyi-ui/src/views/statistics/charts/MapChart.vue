<template>
  <div ref="chart" class="chart-container"></div>
</template>

<script>
import * as echarts from 'echarts';
import resize from '../mixins/resize';
import shijiazhuangMapData from '../data/shijiazhuang.json';

export default {
  name: 'MapChart',
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
      // 注册地图数据
      echarts.registerMap('shijiazhuang', shijiazhuangMapData);
      
      if (!this.$refs.chart) return;
      
      if (this.chart) {
        this.chart.dispose();
      }
      
      this.chart = echarts.init(this.$refs.chart);
      
      // 处理数据
      let seriesData = [];
      
      if (this.chartData && this.chartData.length > 0) {
        seriesData = this.chartData.map(item => ({
          name: item.district || item.name,
          value: item.count || item.value
        }));
      } else {
        // 默认数据
        seriesData = [
          { name: '长安区', value: 20 },
          { name: '桥西区', value: 30 },
          { name: '新华区', value: 100 },
          { name: '裕华区', value: 70 },
          { name: '藁城区', value: 40 },
          { name: '鹿泉区', value: 80 },
          { name: '栾城区', value: 10 },
          { name: '井陉县', value: 50 },
          { name: '井陉矿区', value: 50 },
          { name: '正定县', value: 80 },
          { name: '行唐县', value: 50 },
          { name: '灵寿县', value: 50 },
          { name: '高邑县', value: 40 },
          { name: '深泽县', value: 35 },
          { name: '赞皇县', value: 30 },
          { name: '无极县', value: 55 },
          { name: '平山县', value: 65 },
          { name: '元氏县', value: 50 },
          { name: '赵县', value: 45 },
          { name: '辛集市', value: 80 },
          { name: '晋州市', value: 70 },
          { name: '新乐市', value: 75 }
        ];
      }
      
      const option = {
        // 背景色
        // backgroundColor: '#031525',
        // grid:{
        //   top: 0,
        //   bottom: 0,
        //   left: 0,
        //   right: 0,
        //   containLabel: true
        // },
        // 提示框配置
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            // params.data.value 可能是 undefined 或 null
            var value = params.data && params.data.value !== undefined && params.data.value !== null ? params.data.value : 0;
            return params.name + '<br/>广告数量：' + value;
          }
        },
        
        // 视觉映射配置
        visualMap: { 
          type: 'continuous',
          min: 0,
          max: 120,
          calculable: true,
          left: 30,
          bottom: 60,
          textStyle: {
            color: '#ffffff'
          },
          inRange: {
            // color: ['#0066cc', '#0099cc', '#00ccff'] // 科技蓝风格
            color: ['#00ccff', '#0066cc', '#003366']
          }
        },
        
        // 地图系列配置
        series: [{
          name: '广告数量',
          type: 'map',
          mapType: 'shijiazhuang',
          roam: false,
          // 添加边框和科技风格
          itemStyle: {
            areaColor: '#0c3b70', // 修改为更亮的深蓝色背景
            borderColor: '#2cf5ff', // 区县边框颜色
            borderWidth: 1.5, // 边框宽度
            shadowColor: '#2cf5ff', // 边框阴影颜色
            shadowBlur: 10 // 边框阴影模糊度
          },
          // 高亮时的样式
          emphasis: {
            itemStyle: {
              areaColor: '#0066cc', // 高亮时的区域颜色
              borderColor: '#fff', // 高亮时的边框颜色
              borderWidth: 2, // 高亮时的边框宽度
              shadowColor: '#fff', // 高亮时的阴影颜色
              shadowBlur: 20 // 高亮时的阴影模糊度
            },
            label: {
              show: true,
              color: '#fff' // 高亮时标签颜色
            }
          },
          label: { 
            show: true,
            color: '#fff' // 标签颜色
          },
          data: seriesData
        }]
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