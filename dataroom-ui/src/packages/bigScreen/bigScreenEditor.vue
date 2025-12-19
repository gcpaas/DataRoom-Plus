<script setup lang="ts">
import {
  getComponent,
  getPanelComponent,
  getComponentInstance,
} from '@/packages/components/install.ts'
import { reactive } from 'vue'

const chartList = reactive([
  {
    name: 'DrText',
  },
])

chartList.forEach((chart) => {
  const inst = getComponentInstance(chart.name)
  console.log(inst)
})
</script>

<template>
  x
  <div>
    画布
    <template v-for="chart in chartList" :key="chart.name">
      <div>{{ chart.name }}</div>
      <component :is="getComponent(chart.name)"></component>
      <component :is="getPanelComponent(chart.name)"></component>
    </template>
  </div>
</template>

<style scoped></style>
