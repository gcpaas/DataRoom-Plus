import type { BasicConfig } from '@/packages/components/type/define.ts'

export const getChartById = (
  id: string,
  chartList: BasicConfig<unknown>[],
): BasicConfig<unknown> => {
  const chart = chartList.find((item) => item.id === id)
  if (!chart) {
    console.error(`未找到id = ${id} 组件`)
    throw new Error(`未找到id = ${id} 组件`)
  }
  return chart
}
