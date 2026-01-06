import {type Component, type Ref} from 'vue'
import type {ChartAction, ChartConfig} from '@DrPackage/components/type/define.ts'

interface ComponentLibTagInterface {
  // 类型名称
  name: string
  // 标签名称
  tag: string
}

/**
 * 画布实例
 */
interface CanvasInst {
  addChart: (type: string) => void
  chartList: Ref<Array<ChartConfig<unknown>>>
  activeChartById: (id: string) => void
  switchRightControlPanel: (open: boolean) => void
  onChartDeleteClick: (chartId: string) => void
}

/**
 * 左侧工具bar定义
 */
interface LeftToolBar {
  // tab显示名称
  name: string
  // 激活工具面板显示名称
  desc: string
  // 激活时工具面板组件
  component: Component
  // 组件名
  componentName: string
}

/**
 * 页面实体定义
 */
interface PageStageEntity {
  // ID
  id?: string
  // 页面名称
  name?: string
  // 页面编码
  pageCode: string
  // 备注描述
  remark: string
  // 页面类型
  pageType: string | 'directory' | 'visualScreen' | 'page'
  // 页面状态
  pageStatus: string | 'design' | 'published' | 'history' | 'preview' | 'snapshot'
  // 基础配置
  pageConfig: PageConfig | VisualScreenPageConfig
}

/**
 * 页面基础配置
 */
interface PageBasicConfig {
  // 页面背景设置
  background: {
    // 背景填充方式
    fill: string | 'image' | 'color'
    // 背景色
    color: string
    // 背景图url
    url: string
    // 背景图透明度
    opacity: number
    // 背景图填充方式
    repeat: 'no-repeat' | 'repeat' | 'repeat-x' | 'repeat-y'
  }
  // 定时器配置列表
  timers?: {
    // 定时器ID
    id: string
    // 定时器名称
    name: string
    // 是否启用
    enabled: boolean
    // 定时器间隔，单位毫秒
    interval: number
    // 执行的动作
    actions: ChartAction[]
  }[]
}

/**
 * 页面配置
 */
interface PageConfig {
  // 页面类型
  pageType: string
  // 页面基础配置
  basicConfig: PageBasicConfig
  // 全局变量
  globalVariableList: GlobalVariable[]
  // 图表组件配置
  chartList: ChartConfig<unknown>[]
}

/**
 * 大屏配置
 */
interface VisualScreenPageConfig {
  // 页面类型
  pageType: string
  //页面基础配置
  basicConfig: VisualScreenPageBasicConfig
  // 全局变量
  globalVariableList: GlobalVariable[]
  // 图表组件配置
  chartList: ChartConfig<unknown>[]
}

/**
 * 大屏页面基础配置
 */
interface VisualScreenPageBasicConfig {
  background: {
    // 背景填充方式
    fill: string | 'image' | 'color'
    // 背景色
    color: string
    // 背景图url
    url: string
    // 背景图透明度
    opacity: number
    // 背景图填充方式
    repeat: 'no-repeat' | 'repeat' | 'repeat-x' | 'repeat-y'
  }
}

/**
 * 全局变量
 */
interface GlobalVariable {
  // 唯一标识
  id: string
  // 来源
  from: 'static' | 'url'
  // URL 参数名称
  urlName?: string
  // 变量名称
  name: string
  // 描述
  remark: string
  // 默认值
  defaultValue: string
  // JS脚本
  script?: string
}

export type {CanvasInst, LeftToolBar, ComponentLibTagInterface, GlobalVariable, PageBasicConfig, PageStageEntity}
