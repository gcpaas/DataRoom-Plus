<script setup lang="ts">
import { getComponent, getComponentInstance } from '@DrPackage/components/install.ts'
import { reactive } from 'vue'
import type { BasicConfig } from '../components/type/define.ts'
import { menuList } from './componentMenuInstall.ts'

console.log(menuList)
const chartList: BasicConfig<unknown>[] = reactive([])
// 根据组件类型获取配置
const chartInst1: BasicConfig<unknown> = getComponentInstance('DrText')
const chartInst2: BasicConfig<unknown> = getComponentInstance('Remote')
// 添加到画布
chartList.push(chartInst1)
chartList.push(chartInst2)
</script>

<template>
  <!-- 顶部导航栏 -->

  <div>
    {{ chartList }}
    <template v-for="chart in chartList" :key="chart.type">
      <component :is="getComponent(chart.type)" :chart="chart"></component>
      <component :is="getComponent(chart.type)" :chart="chart"></component>
    </template>
  </div>
</template>

<style scoped></style>
