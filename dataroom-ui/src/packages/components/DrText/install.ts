import {defineAsyncComponent} from 'vue'
import type {ChartConfig, Behavior, ChartDatasetField} from '../type/define'
import {createChartConfig} from '../type/define'
import {DrConst} from '@/packages/_common/_constant.ts'
// 注册组件
const component = defineAsyncComponent(() => import('./index.vue'))
// 注册组件配置面板
const controlPanel = defineAsyncComponent(() => import('./panel/index.vue'))

interface DrTextPropsInterface {
  // 文本
  text: string
  // 字体大小
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
const getInstance = (): DrTextConfig => {
  return createChartConfig<DrTextPropsInterface>(
    DrConst.THIS_PLUGIN_TYPE,
    {
      text: '文本占位符',
      fontSize: 14,
    },
    {
      title: '文本'
    }
  )
}
/**
 * 定义组件交互定义
 */
const behaviors: Behavior[] = [
  {
    name: '单击',
    desc: '鼠标点击文本时触发',
    method: 'click',
    paramsList: [{
      name: 'text',
      desc: '点击的文本值',
      type: 'string',
      required: false,
      defaultValue: ''
    }],
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
