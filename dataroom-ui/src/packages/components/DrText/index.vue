<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'DrText',
})
</script>
<script setup lang="ts">
import type { DrTextConfig } from './install.ts'
import { computed, type CSSProperties, inject, getCurrentInstance, onMounted } from 'vue'
import type { CanvasInst } from '@/packages/bigScreen/type.ts'

const { chart } = defineProps<{
  chart: DrTextConfig
}>()
onMounted(()=>{
  const canvasInst = inject('canvasInst') as CanvasInst
  let currentInstance = getCurrentInstance()
  console.log(currentInstance)
  canvasInst.chartInstList.push(currentInstance)
})

const customStyle = computed<CSSProperties>(() => {
  const { x = 0, y = 0, w = 0, h = 0 } = chart
  return {
    position: 'absolute',
    left: `${x}px`,
    top: `${y}px`,
    width: `${w}px`,
    height: `${h}px`,
  }
})
</script>

<template>
  <div class="dr-text" :style="customStyle">{{ chart.props.text }}</div>
</template>

<style scoped>
.dr-text {
  color: var(--dr-prmary);
}
</style>
