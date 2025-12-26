import type { ChartConfigInterface } from '@/packages/components/type/define.ts'

/**
 * 根据图表HTML对象获取对应的图表配置
 * @param e
 * @param chartList
 */
export const getChartByElement = (e: HTMLElement | SVGElement, chartList: ChartConfigInterface<unknown>[]): ChartConfigInterface<unknown> => {
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
export const getChartById = (id: string, chartList: ChartConfigInterface<unknown>[]): ChartConfigInterface<unknown> => {
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
