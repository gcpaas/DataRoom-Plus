import {defineAsyncComponent} from 'vue'
import type {ChartConfig, Behavior, ChartDatasetField} from '../type/define'
import {DrConst} from '@/packages/_common/_constant.ts'
// 注册组件
const component = defineAsyncComponent(() => import('./index.vue'))
// 注册组件配置面板
const controlPanel = defineAsyncComponent(() => import('./panel/index.vue'))

interface DrTextPropsInterface {
  /**
   * 文本
   */
  text: string
  /**
   * 字体大小
   */
  fontSize: number
}

/**
 * 定义组件配置类型
 */
export type DrTextConfig = ChartConfig<DrTextPropsInterface>

/**
 * 定义获取该组件实例的方法，返回本组件新实例对象
 * @constructor
 */
const getInstance = (pageType: string | 'page' | 'visualScreen'): DrTextConfig => {
  const config: DrTextConfig = {
    id: Math.random().toString(),
    i: Math.random().toString(),
    type: DrConst.THIS_PLUGIN_TYPE,
    title: '文本',
    w: pageType === 'visualScreen' ? 150 : 5,
    h: pageType == 'visualScreen' ? 100 : 5,
    x: pageType === 'visualScreen' ? 100 : 0,
    y: pageType === 'visualScreen' ? 0 : 0,
    z: 100,
    rotateX: 0,
    rotateY: 0,
    rotateZ: 0,
    props: {
      text: '我是文本',
      fontSize: 14,
    },
  }
  return config
}
/**
 * 定义组件交互定义
 */
const behaviors: Behavior[] = [
  {
    name: '点击',
    desc: '鼠标点击文本时触发',
    method: 'triggerClickEvent',
    paramsList: [],
  },
]
/**
 * 定义维度、指标字段信息
 */
const datasetFields: ChartDatasetField[] = [
  {
    name: 'xField',
    desc: '文本值',
    required: true
  }
]

export {component, controlPanel, getInstance, behaviors, datasetFields}
