<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'DrText',
})
</script>
<script setup lang="ts">
import type { DrTextConfig } from './install.ts'
import { reactive } from 'vue'

const { chart } = defineProps<{
  chart: DrTextConfig
}>()
// 避免直接修改props数据，编译报错
const drChart = reactive(chart)
</script>

<template>
  <div class="dr-text">{{ drChart.props.text }}-字体大小: {{ drChart.props.fontSize }}</div>
</template>

<style scoped>
.dr-text {
  color: red;
}
</style>
