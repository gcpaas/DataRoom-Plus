import { type Component, type ComponentInternalInstance } from 'vue'

interface CanvasInst {
  addChart: (type: string) => void,
  chartInstList: Array<ComponentInternalInstance>
}

interface LeftToolBar{
  name: string,
  component: Component,
  componentName: string
}

export type { CanvasInst,LeftToolBar }
