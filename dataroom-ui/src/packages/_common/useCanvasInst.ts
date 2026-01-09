import {reactive, type Ref} from 'vue'
import type {CanvasInst, GlobalVariable} from '@/packages/_common/_type.ts'
import type {ChartAction, ChartConfig} from '@DrPackage/components/type/define.ts'
import {fillDatasetParams, getChartById, getResourceUrl} from '@/packages/_common/_utils.ts'
import {type ComponentInternalInstance} from '@vue/runtime-core'
import {ElMessage} from "element-plus";

type ChartInstanceMap = Record<string, ComponentInternalInstance>

interface UseCanvasInstOptions {
  chartList: Ref<ChartConfig<unknown>[]>
  globalVariable: Ref<GlobalVariable[]>
  addChart?: (type: string) => ChartConfig<unknown>
  activeChartById?: (id: string) => void
}

/**
 * 创建画布实例的组合式函数
 * 用于在设计器和预览器中共享组件实例管理逻辑
 */
export function useCanvasInst(options: UseCanvasInstOptions) {
  const {chartList, globalVariable, addChart, activeChartById} = options

  const chartInstanceMap: ChartInstanceMap = {}

  const canvasInst = reactive<CanvasInst>({
    addChart: addChart || (() => {
      throw new Error('addChart 方法未实现')
    }),
    chartList: chartList,
    activeChartById: activeChartById || (() => {
      throw new Error('activeChartById 方法未实现')
    }),
    fillDatasetParams: (chart: ChartConfig<unknown>) => {
      return fillDatasetParams(chart, globalVariable.value)
    },
    registerChartInstance: (charId: string, chartInstance: ComponentInternalInstance | null) => {
      if (!chartInstance) {
        console.error(`注册组件 ${charId} 的实例失败，实例为空`)
        return
      }
      chartInstanceMap[charId] = chartInstance
    },
    getChartInstanceById: (charId: string) => {
      const chartInstance = chartInstanceMap[charId]
      if (!chartInstance) {
        console.error(`获取组件 ${charId} 的实例失败，实例为空`)
        throw new Error(`组件 ${charId} 的实例为空`)
      }
      return chartInstance
    },
    triggerChartAction: (charId: string = 'unknown', action: ChartAction) => {
      if (action.type === 'code') {
        try {
          const context = {
            getChartById: getChartById
          }
          const func = new Function('context', `with(context) { ${action.code} }`)
          func(context)
        } catch (error) {
          console.error(`定时器动作 [${action.name}] 执行失败:`, error)
          ElMessage.error(`定时器动作 [${action.name}] 执行失败: ${error}`)
        }
        return
      }
      const chartInstance = canvasInst.getChartInstanceById(charId)
      chartInstance.exposed?.triggerAction(action)
      return
    },
    triggerChartBehavior: (charId: string, behaviorName: string, triggerData: any) => {
      const chart = getChartById(charId, chartList.value)
      if (!chart) {
        return
      }
      if (!chart.behaviors) {
        return;
      }
      for (const key of Object.keys(chart.behaviors)) {
        if (key === 'click') {
          const behavior = chart.behaviors[key]
          if (!behavior) {
            continue
          }
          if (behavior.disabled) {
            continue
          }
          if (!behavior.actions || behavior.actions.length == 0) {
            continue
          }
          for (let i = 0; i < behavior.actions.length; i++) {
            const action = behavior.actions[i]
            if (!action) {
              continue
            }
            canvasInst.triggerChartAction(chart.id, action)
          }
        }
      }
    },
  })

  return {
    canvasInst,
    chartInstanceMap
  }
}
