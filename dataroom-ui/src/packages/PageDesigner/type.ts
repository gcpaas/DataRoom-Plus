import { type ComponentInternalInstance } from 'vue'

interface CanvasInst {
  addChart: (type: string) => void,
  chartInstList: Array<ComponentInternalInstance>
}

export type { CanvasInst }
