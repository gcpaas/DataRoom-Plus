import { type Component, type ComponentInternalInstance } from 'vue'

interface CanvasInst {
  addChart: (type: string) => void,
  chartInstList: Array<ComponentInternalInstance>
}

interface LeftToolBar{
  // tab显示名称
  name: string,
  // 激活工具面板显示名称
  desc: string,
  // 激活时工具面板组件
  component: Component,
  // 组件名
  componentName: string
}

export type { CanvasInst,LeftToolBar }
