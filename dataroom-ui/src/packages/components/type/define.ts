/**
 * 图表插件、所有图表都需要继承该类并手动注册
 */
export class ChartPlugin {
  /**
   * 组件类型
   */
  type: string
  /**
   * 组件名称、用于显示
   */
  name: string
  /**
   * 组件信息描述、多个之间使用逗号隔开，可用于检索
   */
  desc: string
  /**
   * 组件缩略图
   */
  thumbnail: string
  /**
   * 所属标签、用于分类
   */
  tags: string[]

  constructor(type: string, name: string, desc: string, thumbnail: string, tags: string[]) {
    this.type = type
    this.name = name
    this.desc = desc
    this.thumbnail = thumbnail
    this.tags = tags
  }
}

export interface ChartDatasetInterface {
  /**
   * 数据集编码
   */
  code: string
  xFields: string[]
}
/**
 * 组件配置基础信息
 */
export interface ChartConfigInterface<T> {
  /**
   * 唯一标识
   */
  id: string
  /**
   * 唯一标识，仅仪表盘类型使用
   */
  i: string
  /**
   * 组件类型
   */
  type: string
  /**
   * 组件显示的图层名称
   */
  title: string
  /**
   * 宽度或占用的份数
   */
  w: number
  /**
   * 高度或占用的份数
   */
  h: number
  /**
   * x坐标或x轴份数
   */
  x: number
  /**
   * y坐标或y轴份数
   */
  y: number
  /**
   * 组件层级、层级越大、越靠前显示
   */
  z: number
  /**
   * X轴旋转角度
   */
  rotateX: number
  /**
   * Y轴旋转角度
   */
  rotateY: number
  /**
   * Z轴旋转角度
   */
  rotateZ: number
  /**
   * 数据集编码
   */
  dataset?:string
  /**
   * 自定义配置
   */
  props: T
}

/**
 * 图表行为交互参数定义
 */
export interface BehaviorParamInterface {
  /**
   * 参数名称
   */
  name: string
  /**
   * 参数描述
   */
  desc: string
  /**
   * 参数类型
   */
  type: string
  /**
   * 是否必填
   */
  required: boolean
  /**
   * 默认值
   */
  defaultValue: string
}

/**
 * 行为交互定义
 */
export interface BehaviorInterface {
  /**
   * 事件名称、用于显示
   */
  name: string
  /**
   * 事件描述、用于说明
   */
  desc: string
  /**
   * 事件触发时指定的方法名称、用于调用
   */
  method: string
  /**
   * 参数列表
   */
  paramsList: Array<BehaviorParamInterface>
}

/**
 * 图表数据集字段定义
 */
export interface ChartDatasetFieldInterface {
  /**
   * 字段名称
   */
  name: string
  /**
   * 字段说明
   */
  desc: string
  /**
   * 是否必填
   */
  required: boolean
}
