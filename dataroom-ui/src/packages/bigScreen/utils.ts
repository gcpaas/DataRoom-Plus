import type { BasicConfig } from '@/packages/components/type/define.ts'
import { type ElementType } from 'selecto/src/types.ts'

/**
 * 根据图表HTML对象获取对应的图表配置
 * @param e
 * @param chartList
 */
export const getChartById = (
  e: ElementType,
  chartList: BasicConfig<unknown>[],
): BasicConfig<unknown> => {
  const dataDrId: string | null = e.getAttribute('data-dr-id')
  const chart = chartList.find((item) => item.id === dataDrId)
  if (!chart) {
    console.error(`未找到data-dr-id = ${dataDrId} 组件`,e)
    throw new Error(`未找到data-dr-id = ${dataDrId} 组件`)
  }
  return chart
}
/**
 * 从transform中提取坐标信息
 * @param transform 格式：translate(xpx, ypx)
 */
export const extractPositionFromTransform = (transform: string): { x: number; y: number } => {
  const match = transform.match(/translate\((.*), (.*)\)/)
  if (!match) {
    console.error(`未能够从${transform}中提取到坐标信息`)
    return { x: 0, y: 0 }
  }
  // @ts-expect-error ignore
  const x = parseInt(match[1].replace('px', ''))
  // @ts-expect-error ignore
  const y = parseInt(match[2].replace('px', ''))
  return { x, y }
}
