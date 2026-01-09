import {reactive, type Ref} from 'vue'
import type {CanvasInst, GlobalVariable} from '@/packages/_common/_type.ts'
import type {ChartAction, ChartConfig} from '@DrPackage/components/type/define.ts'
import {getChartById} from '@/packages/_common/_utils.ts'
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
      const paramMap: Record<string, any> = {}
      const datasetParams = chart.dataset.params
      if (!datasetParams) {
        console.error(`组件 ${chart.id} 数据集参数未配置`)
        return paramMap
      }
      for (const paramName of Object.keys(datasetParams)) {
        const paramConfig = datasetParams[paramName]
        if (!paramConfig) {
          console.error(`组件 ${chart.id} 数据集参数 ${paramName} 未正确配置`)
          continue
        }
        // 默认使用默认值
        let paramValue = paramConfig.defaultValue
        paramMap[paramName] = paramValue
        if (paramConfig.from === 'fixed') {
          // 固定值：使用默认值
          paramValue = paramConfig.defaultValue
          paramMap[paramName] = paramValue
          continue
        }
        if (paramConfig.from === 'globalVar') {
          if (!paramConfig.variableName) {
            console.error(`组件 ${chart.id} 数据集参数 ${paramName} 的全局变量名称未配置`)
            continue
          }
          paramValue = canvasInst.getGlobalVariableValue(paramConfig.variableName)
          paramMap[paramName] = paramValue
        }
      }
      return paramMap
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
        return
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
    getGlobalVariableValue: (globalVariableName: string) => {
      let paramValue = ''
      for (let i = 0; i < globalVariable.value.length; i++) {
        const globalVar = globalVariable.value[i]
        if (!globalVar) {
          continue
        }
        if (globalVar.name !== globalVariableName) {
          continue
        }
        if (globalVar.from === 'static') {
          // 静态变量：直接使用默认值
          paramValue = globalVar.defaultValue
        } else if (globalVar.from === 'url') {
          if (!globalVar.urlName) {
            console.error(`全局变量 ${globalVar.name} 的URL参数名称未配置`)
            return ''
          }
          const urlParams = new URLSearchParams(window.location.search)
          paramValue = urlParams.get(globalVar.urlName) || globalVar.defaultValue
        }
        // 如果配置了脚本，执行脚本处理
        if (globalVar.script && globalVar.script.trim()) {
          let scriptFunc = null
          try {
            // 使用 Function 构造器创建并执行脚本
            scriptFunc = new Function(globalVar.script)
            let returnValue = scriptFunc()
            if (!returnValue) {
              console.error(`全局变量: ${globalVar.name} 脚本执行后未返回值，将使用默认值  ${paramValue}, 脚本: ${globalVar.script}`)
              returnValue = paramValue
            }
            paramValue = returnValue
          } catch (error) {
            console.error(`全局变量  ${globalVar.name} 的脚本失败，脚本: ${globalVar.script} , 异常: `, error)
          } finally {
            if (scriptFunc != null) {
              scriptFunc = null
            }
          }
        }
      }
      return paramValue;
    },
    updateGlobalVariableValue: (globalVariableName: string, value: any) => {
      for (let i = 0; i < globalVariable.value.length; i++) {
        const globalVar = globalVariable.value[i]
        if (!globalVar) {
          continue
        }
        if (globalVar.name !== globalVariableName) {
          continue
        }
        globalVar.defaultValue = value
        if (!value) {
          console.warn(`全局变量 ${globalVar.name} 的默认值被设置为了空`)
        }
      }
    }
  })

  return {
    canvasInst,
    chartInstanceMap
  }
}
