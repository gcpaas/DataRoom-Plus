/**
 * 组件配置基础类型定义
 */
export interface BasicConfig<T> {
  // 唯一表示
  id: string
  // 组件类型
  type: string
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
