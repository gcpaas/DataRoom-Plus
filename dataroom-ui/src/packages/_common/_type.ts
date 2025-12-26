import { type Component, type Ref } from 'vue'
import type { BasicConfig } from '@DrPackage/components/type/define.ts'

interface ComponentLibTagType {
  // 类型名称
  name: string
  // 类型编码
  code: string
}

/**
 * 画布实例
 */
interface CanvasInst {
  addChart: (type: string) => void
  chartList: Ref<Array<BasicConfig<unknown>>>
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

interface ResourceLibType {
  name: string
  type: string
  url: string
}

interface ResourceLibTagType {
  // 类型名称
  name: string
  // 类型编码
  code: string
}

/**
 * 页面实体定义
 */
interface PageEntity {
  id: string
  // 页面名称
  name: string
  pageCode: string
  parentPageCode: string
  pageType: string
  // 封面图片地址
  coverPicture: string
  // 备注描述
  remark: string
  // 基础配置
  pageConfig: PageConfig
  // 全局变量
  globalVariableList: GlobalVariable[]
  // 图表组件配置
  chartList: Array<BasicConfig<unknown>>
}

/**
 * 页面基础配置
 */
interface PageConfig {
  // 背景填充方式、可选值 image、color
  bgFill: 'image' | 'color'
  // 背景色
  bgColor: string
  // 背景图url
  bgUrl: string
  // 背景图填充方式
  bgRepeat: 'no-repeat' | 'repeat' | 'repeat-x' | 'repeat-y'
}

/**
 * 全局变量
 */
interface GlobalVariable {
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
  // 脚本
  script: string
}

export type { CanvasInst, LeftToolBar, ComponentLibTagType, ResourceLibType, ResourceLibTagType, GlobalVariable }
