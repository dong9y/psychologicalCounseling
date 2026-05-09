<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    chartData: {
      type: Object,
      default: () => ({
        legendData: ['正常', '关注', '中风险', '高风险'],
        seriesData: [
          { value: 70, name: '正常' },
          { value: 20, name: '关注' },
          { value: 8, name: '中风险' },
          { value: 2, name: '高风险' }
        ]
      })
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions({ legendData, seriesData } = {}) {
      const colorMap = {
        '正常': '#67C23A',
        '关注': '#E6A23C',
        '中风险': '#F56C6C',
        '高风险': '#F56C6C'
      }

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center',
          data: legendData || ['正常', '关注', '中风险', '高风险'],
          textStyle: {
            color: '#606266'
          }
        },
        series: [
          {
            name: '风险分布',
            type: 'pie',
            roseType: 'radius',
            radius: ['40%', '70%'],
            center: ['35%', '50%'],
            data: seriesData || [
              { value: 70, name: '正常' },
              { value: 20, name: '关注' },
              { value: 8, name: '中风险' },
              { value: 2, name: '高风险' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
            itemStyle: {
              normal: {
                color: function(params) {
                  return colorMap[params.name] || '#409EFF'
                }
              }
            },
            label: {
              show: false
            }
          }
        ]
      })
    }
  }
}
</script>
