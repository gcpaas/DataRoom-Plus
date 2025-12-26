import DrTextPlugin from '@DrPackage/components/DrText/define.ts'
import DrEchartsBarPlugin from '@DrPackage/components/DrEchartsBar/define.ts'
import DrImagePlugin from '@DrPackage/components/DrImage/define.ts'
import type { ComponentLibTagInterface } from '@/packages/_common/_type.ts'

enum TypeCode {
  /**
   * 折线图、面积图
   */
  line = 'line',
  /**
   * 柱状图
   */
  bar = 'bar',
  /**
   * 饼图
   */
  pie = 'pie',
  /**
   * 散点图
   */
  scatter = 'scatter',
  /**
   * 雷达图
   */ radar = 'radar',
  /**
   * 漏斗图
   */
  funnel = 'funnel',
  /**
   * 仪表盘
   */
  gauge = 'gauge',
  /**
   * 地图
   */
  map = 'map',
  /**
   * 文本
   */
  text = 'text',
  /**
   * 多媒体、音视频
   */
  media = 'media',
  /**
   * 表单
   */
  form = 'form',
}

const componentLibTypeList: ComponentLibTagInterface[] = [
  {
    name: '折线图',
    code: TypeCode.line,
  },
  {
    name: '柱状图',
    code: TypeCode.bar,
  },
  {
    name: '饼图',
    code: TypeCode.pie,
  },
  {
    name: '散点图',
    code: TypeCode.scatter,
  },
  {
    name: '雷达图',
    code: TypeCode.radar,
  },
  {
    name: '漏斗图',
    code: TypeCode.funnel,
  },
  {
    name: '仪表盘',
    code: TypeCode.gauge,
  },
  {
    name: '柱状图',
    code: TypeCode.bar,
  },
  {
    name: '文本',
    code: TypeCode.text,
  },
  {
    name: '地图',
    code: TypeCode.map,
  },
  {
    name: '音视频',
    code: TypeCode.media,
  },
  {
    name: '表单',
    code: TypeCode.form,
  },
]
/**
 * 组件列表
 */
const componentLibList = [
  new DrTextPlugin([TypeCode.text]),
  new DrEchartsBarPlugin([TypeCode.bar]),
  new DrImagePlugin([TypeCode.media])
]
export { componentLibList, componentLibTypeList }
