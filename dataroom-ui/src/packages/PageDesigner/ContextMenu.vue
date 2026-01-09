<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import type { ChartConfig } from '@/packages/components/type/define.ts'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  chart?: ChartConfig<unknown>
}>()
// 确保拿到的chart非空
const chart = props.chart!

const emit = defineEmits<{
  switchRightControlPanel: [open: boolean]
  deleteChart: [chartId: string]
}>()

onMounted(() => {
  document.addEventListener('click', onOutsideClick)
})

onUnmounted(() => {
  document.removeEventListener('click', onOutsideClick)
})

const contextMenuRef = ref()
const contextMenuVisible = ref(true)
/**
 * 点击其他位置后，隐藏菜单
 * @param e
 */
const onOutsideClick = (e: MouseEvent) => {
  if (contextMenuRef.value) {
    if (!contextMenuRef.value.contains(e.target as Node)) {
      console.log('点击组件外部，触发组件隐藏')
      contextMenuVisible.value = false
    }
  }
}

/**
 * 点击配置
 */
const onChartConfigClick = () => {
  emit('switchRightControlPanel', true)
  contextMenuVisible.value = false
}
/**
 * 删除组件
 */
const onChartDeleteClick = () => {
  emit('switchRightControlPanel', false)
  emit('deleteChart', chart.id)
  contextMenuVisible.value = false
}

const onChartCopyClick = () => {
  ElMessage.warning('功能开发中...')
  contextMenuVisible.value = false
}

const onChartHideClick = () => {
  ElMessage.warning('功能开发中...')
  contextMenuVisible.value = false
}
</script>

<template>
  <div class="context-menu-wrapper" ref="contextMenuRef" v-if="contextMenuVisible">
    <div class="menu" @click="onChartConfigClick">配置</div>
    <div class="menu" @click="onChartDeleteClick">删除</div>
    <div class="menu" @click="onChartCopyClick">复制并粘贴</div>
    <div class="menu" @click="onChartHideClick">设为隐藏</div>
  </div>
</template>

<style scoped>
.context-menu-wrapper {
  height: 122px;
  width: 200px;
  background: #fff;
  padding: 16px 0;
  border-radius: 4px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  color: var(--dr-text);
  font-size: 14px;

  & .menu {
    padding: 8px;

    &:hover {
      color: var(--dr-primary);
      background-color: var(--dr-primary1);
      cursor: pointer;
    }
  }
}
</style>
