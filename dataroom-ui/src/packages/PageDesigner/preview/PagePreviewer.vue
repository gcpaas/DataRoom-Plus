<script setup lang="ts">
import { getComponent, getComponentInstance } from '@DrPackage/components/AutoInstall.ts'
import {ref, provide, onMounted, onUnmounted, computed, type CSSProperties, nextTick, watch} from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout-v3'
import type { ChartConfig } from '@DrPackage/components/type/define.ts'
import {pageApi} from "@/packages/page/api.ts";
import type {GlobalVariable, PageBasicConfig, PageStageEntity} from "@/packages/_common/_type.ts";
import {useRoute} from "vue-router";
import {getResourceUrl, TimerManager} from "@/packages/_common/_utils.ts";
import {useCanvasInst} from '@/packages/_common/useCanvasInst.ts'
import {DrConst} from '@/packages/_common/_constant.ts'

const pageStageEntity = ref<PageStageEntity>()
const chartList = ref<ChartConfig<unknown>[]>([])
const basicConfig = ref<PageBasicConfig>({} as PageBasicConfig)
const globalVariable = ref<GlobalVariable[]>([] as GlobalVariable[])
const route = useRoute()

// 定时器管理器
let timerManager: TimerManager | null = null

/**
 * 创建画布实例供子组件使用
 */
const { canvasInst } = useCanvasInst({
  chartList,
  globalVariable
})
provide(DrConst.CANVAS_INST, canvasInst)
// 提供全局变量列表给子组件
provide('globalVariableList', globalVariable)

/**
 * 初始化定时器管理器
 */
const initTimerManager = () => {
  if (!timerManager) {
    timerManager = new TimerManager(chartList, globalVariable, basicConfig)
  }
  return timerManager
}

/**
 * 监听定时器配置变化
 */
watch(
  () => basicConfig.value.timers,
  (newTimers) => {
    console.log('[预览模式] 定时器配置发生变化', newTimers)
    
    if (!timerManager) {
      return
    }
    
    if (!newTimers) {
      // 如果定时器配置被清空，停止所有定时器
      timerManager.clearAllTimers()
      return
    }
    
    // 重新加载所有定时器
    timerManager.reloadAllTimers()
  },
  { deep: true }
)

onMounted(() => {
  // 获取路由中code 参数
  const code: string = route.params.pageCode as string
  const pageStatus: string = route.params.pageStatus as string
  // 根据编码获取页面详情
  pageApi.getPageConfig(code, pageStatus).then((res) => {
    pageStageEntity.value = res
    console.log(res)
    chartList.value = res.pageConfig?.chartList || []
    basicConfig.value = res.pageConfig?.basicConfig || {}
    globalVariable.value = res.pageConfig?.globalVariableList || []
    
    // 页面加载完成后，初始化并启动所有启用的定时器
    nextTick(() => {
      const manager = initTimerManager()
      manager.reloadAllTimers()
    })
  })
})

/**
 * 组件卸载时清理所有定时器
 */
onUnmounted(() => {
  if (timerManager) {
    timerManager.clearAllTimers()
    timerManager = null
  }
})


const computedCanvasMainContainerStyle  = computed(() => {
  const background = basicConfig.value.background
  if (!background) {
    return {}
  }

  const styles: CSSProperties = {}
  console.log(background)
  if (background.fill === 'color' && background.color) {
    // 纯色背景
    styles.backgroundColor = background.color
  } else if (background.fill === 'image' && background.url) {
    // 图片背景
    styles.backgroundImage = `url(${getResourceUrl(background.url)})`
    styles.backgroundRepeat = background.repeat || 'no-repeat'
    styles.backgroundSize = 'cover'
    styles.backgroundPosition = 'center'
    // 设置透明度
    if (background.opacity !== undefined && background.opacity !== 1) {
      styles.opacity = background.opacity
    }
    // 如果有背景色，作为图片的底色
    if (background.color) {
      styles.backgroundColor = background.color
    }
  }

  return styles
})

</script>

<template>
  <div class="dr-page-preview" :style="computedCanvasMainContainerStyle">
    <GridLayout
      v-model:layout="chartList"
      :col-num="48"
      :row-height="16"
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
