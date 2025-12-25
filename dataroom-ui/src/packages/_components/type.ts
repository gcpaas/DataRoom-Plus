import { type Component, type Ref } from 'vue'
import type { BasicConfig } from '@/packages/components/type/define.ts'

interface CanvasInst {
  addChart: (type: string) => void
  chartList: Ref<Array<BasicConfig<unknown>>>
  activeChartById: (id: string) => void
}

/**
 * 左侧工具bar定义
 */
interface LeftToolBar {
  // tab显示名称
  name: string
  // 激活工具面板显示名称
  desc: string
  // 激活时工具面板组件
  component: Component
  // 组件名
  componentName: string
}

export type { CanvasInst, LeftToolBar }
