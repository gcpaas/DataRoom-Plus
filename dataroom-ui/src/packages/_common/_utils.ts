import type {ChartConfig} from '@/packages/components/type/define.ts'
import type {CanvasInst, PageBasicConfig, PageTimer} from '@/packages/_common/_type.ts'
import {type Reactive, reactive, type Ref} from 'vue'
import {ElMessage} from 'element-plus'

/**
 * 根据图表HTML对象获取对应的图表配置
 * @param e
 * @param chartList
 */
export const getChartByElement = (e: HTMLElement | SVGElement, chartList: ChartConfig<unknown>[]): ChartConfig<unknown> => {
  const dataDrId: string | null = e.getAttribute('data-dr-id')
  const chart = chartList.find((item) => item.id === dataDrId)
  if (!chart) {
    console.error(`未找到data-dr-id = ${dataDrId} 组件`, e)
    throw new Error(`未找到data-dr-id = ${dataDrId} 组件`)
  }
  return chart
}
/**
 * 根据id获取对应的图表配置
 * @param id
 * @param chartList
 */
export const getChartById = (id: string, chartList: ChartConfig<unknown>[]): ChartConfig<unknown> => {
  const chart = chartList.find((item) => item.id === id)
  if (!chart) {
    console.error(`未找到id = ${id} 组件`)
    throw new Error(`未找到id = ${id} 组件`)
  }
  return chart
}

/**
 * 从transform中提取坐标、旋转信息
 * @param transform 格式：translate(60px, 60px) rotateZ(60deg) rotateX(60deg) rotateY(60deg)
 */
export const extractPositionFromTransform = (
  transformStr: string,
): {
  x: number
  y: number
  rotateX: number
  rotateY: number
  rotateZ: number
} => {
  const result = {
    x: 0,
    y: 0,
    rotateX: 0,
    rotateY: 0,
    rotateZ: 0,
  }
  const translateReg = /translate\s*\(\s*([-+]?\d+(\.\d+)?)(?:px)?\s*,\s*([-+]?\d+(\.\d+)?)(?:px)?\s*\)/i
  const translateMatch = transformStr.match(translateReg)
  if (translateMatch) {
    // @ts-expect-error ignore
    result.x = parseInt(translateMatch[1])
    // @ts-expect-error ignore
    result.y = parseInt(translateMatch[3])
  }
  const rotateXReg = /rotateX\s*\(\s*([-+]?\d+(\.\d+)?)(?:deg)?\s*\)/i
  const rotateXMatch = transformStr.match(rotateXReg)
  if (rotateXMatch) {
    // @ts-expect-error ignore
    result.rotateX = parseInt(rotateXMatch[1])
  }
  const rotateYReg = /rotateY\s*\(\s*([-+]?\d+(\.\d+)?)(?:deg)?\s*\)/i
  const rotateYMatch = transformStr.match(rotateYReg)
  if (rotateYMatch) {
    // @ts-expect-error ignore
    result.rotateY = parseInt(rotateYMatch[1])
  }
  const rotateReg = /rotate\s*\(\s*([-+]?\d+(\.\d+)?)(?:deg)?\s*\)/i
  const rotateMatch = transformStr.match(rotateReg)
  if (rotateMatch) {
    // @ts-expect-error ignore
    result.rotateZ = parseInt(rotateMatch[1])
  } else {
    const rotateZReg = /rotateZ\s*\(\s*([-+]?\d+(\.\d+)?)(?:deg)?\s*\)/i
    const rotateZMatch = transformStr.match(rotateZReg)
    if (rotateZMatch) {
      // @ts-expect-error ignore
      result.rotateZ = parseInt(rotateZMatch[1])
    }
  }
  return result
}

/**
 * 根据文件夹路径获取父文件夹名称
 * @param filePath
 */
export const getParentFolderName = (filePath: string): string => {
  const pathParts = new URL(filePath).pathname.split('/')
  // @ts-expect-error ignore
  return pathParts[pathParts.length - 2]
}

/**
 * 解析字符串中的参数
 * 匹配 #{paramName} 格式的参数，并返回去重后的参数名数组
 * @param text 需要解析的字符串
 * @returns 参数名数组，如果没有识别出参数则返回空数组
 */
export const parseParams = (text: string): string[] => {
  if (!text) {
    return []
  }

  // 使用正则匹配 #{paramName} 格式的参数
  const regex = /#\{([^}]+)\}/g
  const matches = [...text.matchAll(regex)]

  if (matches.length === 0) {
    return []
  }

  // @ts-expect-error 提取参数名、去除空格并去重
  const paramNames = [...new Set(matches.map(match => match[1].trim()))]

  return paramNames
}

/**
 * 解析多个字符串中的参数
 * 匹配 #{paramName} 格式的参数，并返回去重后的参数名数组
 * @param texts 需要解析的字符串数组
 * @returns 参数名数组，如果没有识别出参数则返回空数组
 */
export const parseParamsFromMultiple = (texts: string[]): string[] => {
  if (!texts || texts.length === 0) {
    return []
  }

  const allParams = new Set<string>()

  texts.forEach(text => {
    const params = parseParams(text)
    params.forEach(param => allParams.add(param))
  })

  return Array.from(allParams)
}


/**
 *  拼接素材地址
 */
export const getResourceUrl = (url: string): string => {
  if (!url) {
    return ''
  }
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  const resourceBaseUrl = import.meta.env.VITE_RESOURCE_BASE_URL
  return resourceBaseUrl + (resourceBaseUrl.endsWith('/') ? '' : '/') + (url.startsWith('/') ? url.substring(1) : url)
}

/**
 * 定时器管理类
 * 用于管理页面中的定时器，包括启动、停止、执行等操作
 */
export class TimerManager {
  // 定时器实例映射表
  private timerIntervalMap: Map<string, number> = new Map()
  // 画布实例引用
  private canvasInst: Reactive<CanvasInst>
  // 基础配置引用
  private basicConfig: Ref<PageBasicConfig>

  constructor(
    canvasInst: Reactive<CanvasInst>,
    basicConfig: Ref<PageBasicConfig>
  ) {
    this.canvasInst = canvasInst
    this.basicConfig = basicConfig
  }

  /**
   * 执行定时器动作
   * @param timer 定时器配置
   */
  private executeTimerActions(timer: PageTimer): void {
    if (!timer.actions || timer.actions.length === 0) {
      return
    }
    console.log(`执行定时器 [${timer.name}] 的动作，共 ${timer.actions.length} 个动作`)
    // 依次执行所有动作
    timer.actions.forEach((action: any, index: number) => {
      try {
        if (action.type === 'code' && action.code) {
          this.canvasInst.triggerChartAction('', action)
        }
      } catch (error) {
        console.error(`定时器动作 [${action.name}] 执行失败:`, error)
        ElMessage.error(`定时器动作 [${action.name}] 执行失败: ${error}`)
      }
    })
  }

  /**
   * 启动定时器
   * @param timer 定时器配置
   */
  startTimer(timer: PageTimer): void {
    if (!timer.enabled || !timer.id) {
      return
    }
    // 如果已存在，先清除
    this.stopTimer(timer.id)
    console.log(`启动定时器 [${timer.name}]，间隔: ${timer.interval}ms`)
    // 创建新的定时器
    const intervalId = window.setInterval(() => {
      this.executeTimerActions(timer)
    }, timer.interval)
    this.timerIntervalMap.set(timer.id, intervalId)
  }

  /**
   * 停止定时器
   * @param timerId 定时器ID
   */
  stopTimer(timerId: string): void {
    const intervalId = this.timerIntervalMap.get(timerId)
    if (intervalId) {
      window.clearInterval(intervalId)
      this.timerIntervalMap.delete(timerId)
      console.log(`停止定时器 [${timerId}]`)
    }
  }

  /**
   * 重新加载所有定时器
   */
  reloadAllTimers(): void {
    console.log('重新加载所有定时器')

    // 清除所有现有定时器
    this.clearAllTimers()

    // 启动所有启用的定时器
    if (this.basicConfig.value.timers) {
      this.basicConfig.value.timers.forEach((timer) => {
        if (timer.enabled) {
          this.startTimer(timer)
        }
      })
    }
  }

  /**
   * 清除所有定时器
   */
  clearAllTimers(): void {
    console.log('清除所有定时器')
    this.timerIntervalMap.forEach((intervalId) => {
      window.clearInterval(intervalId)
    })
    this.timerIntervalMap.clear()
  }
}
