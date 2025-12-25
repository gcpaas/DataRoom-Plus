<!-- 组件库 -->
<script setup lang="ts">
import DrTextPlugin from '@DrPackage/components/DrText/define.ts'
import { inject } from 'vue'
import type { CanvasInst } from '@/packages/_components/type.ts'

const canvasInst = inject('canvasInst') as CanvasInst
const plugin = new DrTextPlugin()
const addChartToCanvas = (type: string) => {
  console.log(type)
  canvasInst.addChart(type)
}
</script>

<template>
  <div class="text" @click="addChartToCanvas('DrText')">
    文本组件
    <img :src="plugin.thumbnail" style="width: 60%; height: 60%" />
  </div>
</template>

<style scoped lang="scss">
.text {
  background-color: white;
  height: 50px;
  margin: 16px;

  &:hover {
    color: var(--dr-prmary);
    cursor: pointer;
  }
}
</style>
