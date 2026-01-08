/**
 * 图表插件、所有图表都需要继承该类并手动注册
 */
export class ChartPlugin {
  // 组件类型、默认为组件所在的文件夹名称
  type: string
  // 组件名称、用于显示
  name: string
  // 组件信息描述、多个之间使用逗号隔开，可用于检索
  desc: string
  // 组件缩略图
  thumbnail: string
  // 所属标签、用于分类
  tags: string[]

  constructor(type: string, name: string, desc: string, thumbnail: string, tags: string[]) {
    this.type = type
    this.name = name
    this.desc = desc
    this.thumbnail = thumbnail
    this.tags = tags
  }
}

/**
 * 组件配置基础信息
 */
export interface ChartConfig<T> {
  // 唯一标识
  id: string
  // 唯一标识，仅仪表盘类型使用
  i: string
  // 组件类型
  type: string
  // 组件显示的图层名称 或 卡片名称
  title: string
  // 宽度或占用的份数
  w: number
  // 高度或占用的份数
  h: number
  // x坐标或x轴份数
  x: number
  // y坐标或y轴份数
  y: number
  // 组件层级、层级越大、越靠前显示
  z: number
  // X轴旋转角度
  rotateX: number
  // Y轴旋转角度
  rotateY: number
  // Z轴旋转角度
  rotateZ: number
  // 图表组件个性化配置
  props: T,
  // 图表交互
  behaviors?: {
    // key 为 交互名称，与Behavior中的name保持一致，value 为交互的行为定义
    [key: string]: {
      disabled: boolean
      actions: ChartAction[]
    }
  }
  // 数据集与字段绑定
  dataset: {
    // 数据集编码
    code: string
    // 字段绑定
    fields: {
      // key为图表对应的指标、维度、属性值等字段，value 为数据集字段名
      [key: string]: string
    },
    // 数据处理脚本
    script: string
    // 数据集入参、如果数据集需要入参的话
    params: {
      // key 为数据集入参名称，value 为当前入参是通过何种方式获取
      [key: string]: {
        // 来源,暂时仅支持全局变量、固定值
        from: string | 'globalVar' | 'fixed'
        // 变量名称、仅当 from = globalVar 时有效
        variableName: string
        // 默认值、或固定值
        defaultValue: string
      }
    }
  }
}


export interface ChartAction {
  // 动作描述
  name: string
  // 动作类型，暂时仅支持高代码
  type: string | 'code'
  // JS代码
  code: string
}

/**
 * 图表行为交互参数定义
 */
export interface BehaviorEventParam {
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
 * 行为交互定义
 */
export interface Behavior {
  // 事件名称、用于显示
  name: string
  // 事件描述、用于说明
  desc: string
  // 事件触发时指定的方法名称、用于调用
  method: string
  // 参数列表
  paramsList: Array<BehaviorEventParam>
}

/**
 * 图表数据集字段定义
 */
export interface ChartDatasetField {
  // 字段名称
  name: string
  // 字段说明
  desc: string
  // 是否必填
  required: boolean
  // 是否多选
  multiple: boolean
}

/**
 * 创建 ChartConfig 基础配置的工厂函数
 * @param type 组件类型
 * @param props 组件个性化配置
 * @param overrides 需要覆盖的配置项
 * @returns 完整的 ChartConfig 对象
 */
export function createChartConfig<T>(
  type: string,
  props: T,
  overrides?: Partial<Omit<ChartConfig<T>, 'type' | 'props'>>
): ChartConfig<T> {
  return {
    id: Math.random().toString(),
    i: Math.random().toString(),
    type: type,
    title: '未命名组件',
    w: 150,
    h: 100,
    x: 100,
    y: 100,
    z: 100,
    rotateX: 0,
    rotateY: 0,
    rotateZ: 0,
    props: props,
    behaviors: {},
    dataset: {
      code: '',
      fields: {},
      script: '',
      params: {}
    },
    ...overrides,
  }
}


/**
 * 组件必须实现的方法接口
 */
export interface IComponentLifecycle {
  /**
   * 组件初始化方法
   * 用于组件加载时的初始化逻辑
   */
  init: () => void | Promise<void>
  /**
   * 更新数据
   */
  autoRefreshData: () => void | Promise<void>
  /**
   * 修改数据
   * @param data
   */
  changeData: (data: any) => void | Promise<void>
  /**
   * 触发组件行为方法
   * @param action
   */
  triggerAction: (action: ChartAction) => void | Promise<void>
  /**
   * 组件销毁前的清理方法
   * 用于清理定时器、取消订阅等
   */
  destroy?: () => void
}
