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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      default: () => ({
        xData: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        expectedData: [85, 78, 90, 82, 88, 92, 87],
        actualData: [80, 75, 88, 78, 85, 90, 84]
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
    setOptions({ xData, expectedData, actualData } = {}) {
      this.chart.setOption({
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        xAxis: {
          data: xData || ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
          boundaryGap: false,
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#909399'
          },
          axisLine: {
            lineStyle: {
              color: '#E4E7ED'
            }
          }
        },
        yAxis: {
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#909399'
          },
          axisLine: {
            lineStyle: {
              color: '#E4E7ED'
            }
          },
          splitLine: {
            lineStyle: {
              color: '#F0F2F5'
            }
          }
        },
        legend: {
          data: ['情绪指数', '上周同期'],
          textStyle: {
            color: '#606266'
          },
          top: 0
        },
        series: [{
          name: '情绪指数',
          itemStyle: {
            normal: {
              color: '#409EFF',
              lineStyle: {
                color: '#409EFF',
                width: 2
              },
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0, y: 0, x2: 0, y2: 1,
                  colorStops: [
                    { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                    { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
                  ]
                }
              }
            }
          },
          smooth: true,
          type: 'line',
          data: expectedData,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: '上周同期',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#67C23A',
              lineStyle: {
                color: '#67C23A',
                width: 2,
                type: 'dashed'
              }
            }
          },
          data: actualData,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
}
</script>
