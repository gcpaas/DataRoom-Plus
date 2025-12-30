export enum DrConst {
  /**
   * 画布实例
   */
  CANVAS_INST = 'canvasInst',
  /**
   * 用于标识当前组件类型，vite插件会处理进行替换
   */
  THIS_PLUGIN_TYPE = 'THIS_PLUGIN_TYPE',
}

export  enum ComponentLibTagTypeConst {
  /**
   * 折线图、面积图
   */
  LINE = 'line',
  /**
   * 柱状图
   */
  BAR = 'bar',
  /**
   * 饼图
   */
  PIE = 'pie',
  /**
   * 散点图
   */
  SCATTER = 'scatter',
  /**
   * 雷达图
   */
  RADAR = 'radar',
  /**
   * 漏斗图
   */
  FUNNEL = 'funnel',
  /**
   * 仪表盘
   */
  GAUGE = 'gauge',
  /**
   * 地图
   */
  MAP = 'map',
  /**
   * 文本
   */
  TEXT = 'text',
  /**
   * 多媒体、音视频
   */
  MEDIA = 'media',
  /**
   * 表单
   */
  FORM = 'form',
}
/**
 * 页面类型枚举
 */
export const PageType = {
  DIRECTORY: 'directory',
  VISUAL_SCREEN: 'visualScreen',
  PAGE: 'page'
} as const;


/**
 * 页面状态枚举
 */
export const PageStatus = {
  DESIGN: 'design',
  PUBLISHED: 'published',
  HISTORY: 'history',
  PREVIEW: 'preview',
  SNAPSHOT: 'snapshot'
} as const;

/**
 * 资源类型枚举
 */
export const ResourceType = {
  DIRECTORY: 'directory',
  IMAGE: 'image',
  VIDEO: 'video'
} as const;

/**
 * 数据源类型枚举
 */
export const DataSourceType = {
  MYSQL: 'mysql',
  POSTGRESQL: 'postgresql',
  ORACLE: 'oracle'
} as const;
