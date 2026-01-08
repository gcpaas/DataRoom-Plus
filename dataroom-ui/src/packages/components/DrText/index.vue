<script lang="ts">
import {defineComponent} from 'vue'
import {DrConst} from '@/packages/_common/_constant.ts'

export default defineComponent({
  name: DrConst.THIS_PLUGIN_TYPE,
})
</script>
<script setup lang="ts">
import type {DrTextConfig} from './install.ts'
import {onMounted, onBeforeUnmount} from "vue";
import type {ChartAction, IComponentLifecycle} from '@/packages/components/type/define.ts'

const {chart} = defineProps<{
  chart: DrTextConfig
}>()

// 实现必须的方法
const init = () => {
  console.log("初始化文本组件")
  // 组件初始化逻辑
}

const refreshData = () => {
  console.log("刷新文本组件")
  // 组件刷新逻辑
}

const destroy = () => {
  console.log("清理文本组件")
  // 清理逻辑（可选）
}

const triggerAction = (action: ChartAction) => {
  console.log("触发文本组件行为", action)
  // 行为逻辑（可选）
}

// 使用 onMounted 调用初始化
onMounted(() => {
  init()
})

onBeforeUnmount(() => {
  destroy()
})

// 暴露必须实现的方法，使用接口类型约束
defineExpose<IComponentLifecycle>({
  init,
  refreshData,
  triggerAction,
  destroy
})
</script>

<template>
  <div class="dr-text">{{ chart.props.text }}</div>
</template>

<style scoped>
.dr-text {
  width: 100%;
  height: 100%;
}
</style>
