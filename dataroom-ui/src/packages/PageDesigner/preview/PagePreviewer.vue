<script setup lang="ts">
import { getComponent, getComponentInstance } from '@DrPackage/components/install.ts'
import { ref, provide, onMounted } from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout-v3'
import type { BasicConfig } from '@DrPackage/components/type/define.ts'

let chartList = ref<BasicConfig<unknown>[]>([])

const chartIndex = ref(100)

// 模拟加载
onMounted(() => {
  const chartListStr = localStorage.getItem('chartList')
  if (chartListStr) {
    const _chartList = JSON.parse(chartListStr)
    chartList = ref<BasicConfig<unknown>[]>(_chartList)
  } else {
    const layout = [
      { x: 0, y: 0, w: 2, h: 2, i: '0' },
      { x: 2, y: 0, w: 2, h: 4, i: '1' },
      { x: 4, y: 0, w: 2, h: 5, i: '2' },
    ]
    layout.forEach((item) => {
      const inst: BasicConfig<unknown> = getComponentInstance('DrText')
      inst.id = item.i
      inst.i = item.i
      inst.x = item.x
      inst.y = item.y
      inst.w = item.w
      inst.h = item.h
      chartList.value.push(inst)
    })
  }
})

const addChart = (type: string) => {
  const chartInst: BasicConfig<unknown> = getComponentInstance(type)
  chartInst.w = 3
  chartInst.h = 3
  chartInst.x = 0
  chartInst.y = 0
  chartInst.i = chartIndex.value++ + ''
  chartInst.id = chartInst.i
  console.log('新增组件', chartInst)
  chartList.value.push(chartInst)
}
/**
 * 子组件注入使用
 */
provide('canvasInst', {
  addChart: addChart,
})
</script>

<template>
  <div class="dr-page-preview">
    <GridLayout
      v-model:layout="chartList"
      :col-num="12"
      :row-height="30"
      :is-draggable="false"
      :is-resizable="false"
      :vertical-compact="true"
      :use-css-transforms="true"
    >
      <GridItem
        v-for="(item, index) in chartList"
        :key="index"
        :static="false"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.id"
      >
        <div class="chart-wrapper" :key="item.id" :id="item.id" :data-dr-id="item.id">
          <component :is="getComponent(item.type)" :chart="item"></component>
        </div>
      </GridItem>
    </GridLayout>
  </div>
</template>

<style scoped lang="scss">
.dr-page-preview {
  width: 100%;
  height: calc(100vh);
  background-color: #f5f7fa;

  & .chart-wrapper {
    background-color: white;
    height: 100%;
    width: 100%;
  }
}
</style>
