<script lang="ts">
import { defineComponent } from 'vue'
import * as echarts from 'echarts'

export default defineComponent({
  name: 'DrText',
})
</script>
<script setup lang="ts">
import type { DrEchartsBarConfig } from './install.ts'
import { onMounted, onUnmounted, ref } from 'vue'

const { chart } = defineProps<{
  chart: DrEchartsBarConfig
}>()

const chartRef = ref<HTMLElement | null>(null)
const myChart = ref<echarts.ECharts | null>(null)
// 定义响应式数据
const chartData = ref([120, 200, 150, 80, 70, 110, 130])

// 封装图表更新函数
const updateChart = (data: number[]) => {
  if (!myChart.value) return
  const option = {
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    },
    yAxis: {
      type: 'value',
    },
    series: [{ data, type: 'bar' }],
  }
  myChart.value.setOption(option, { notMerge: true })
}

onMounted(() => {
  if (!chartRef.value) return
  myChart.value = echarts.init(chartRef.value)
  updateChart(chartData.value) // 初始化渲染

  // 窗口自适应
  const resizeHandler = () => {
    console.log("窗口改变")
    myChart.value?.resize()
  }
  window.addEventListener('resize', resizeHandler)
  onUnmounted(() => window.removeEventListener('resize', resizeHandler))
})
</script>

<template>
  <div class="dr-echarts-bar" ref="chartRef">{{ chart.props.text }}{{ chart.id }}</div>
</template>

<style scoped>
.dr-echarts-bar {
  color: var(--dr-primary);
  width: 100%;
  height: 100%;
}
</style>
