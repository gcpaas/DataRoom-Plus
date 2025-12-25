/**
 * 交互定义
 */
export class ChartDefine {
  // 组件显示名称
  name: string
  // 组件信息描述
  desc?: string
  // 组件缩略图
  thumbnail: string
  // 所属标签、用于分类
  tags: string[]

  constructor(name: string, desc: string, thumbnail: string, tags: string[]) {
    this.name = name
    this.desc = desc
    this.thumbnail = thumbnail
    this.tags = tags
  }
}

/**
 * 组件配置基础类型定义
 */
export interface BasicConfig<T> {
  // 唯一表示
  id: string
  i: string
  // 组件类型
  type: string
  // 组件显示的名称
  title: string
  // 宽度
  w: number
  // 高度
  h: number
  // x坐标
  x: number
  // y坐标
  y: number
  // 组件层级、层级越大、越靠前显示
  z: number
  // X轴旋转角度
  rotateX: number
  // Y轴旋转角度
  rotateY: number
  // Z轴旋转角度
  rotateZ: number
  // 自定义配置
  props: T
}

/**
 * 交互参数定义
 */
export interface BehaviorParam {
  // 参数名称
  name: string
  // 参数描述
  desc: string
  // 参数类型
  type: string
  // 是否必填
  required: boolean
  // 默认值
  defaultValue: string
}

/**
 * 交互定义
 */
export interface Behavior {
  // 事件名称
  name: string
  // 事件描述
  desc: string
  // 事件触发时指定的方法名称
  method: string
  paramsList: Array<BehaviorParam>
}
