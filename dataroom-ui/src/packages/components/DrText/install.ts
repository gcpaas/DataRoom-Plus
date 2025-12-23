import { defineAsyncComponent } from 'vue'
import type { BasicConfig, Behavior } from '../type/define'
// 注册组件
const component = defineAsyncComponent(() => import('./index.vue'))
// 注册组件配置面板
const controlPanel = defineAsyncComponent(() => import('./panel/index.vue'))

interface DrTextProps {
  // 文本
  text: string
  // 字体大小
  fontSize: number
}

/**
 * 定义组件配置类型
 */
export type DrTextConfig = BasicConfig<DrTextProps>

/**
 * 定义获取该组件实例的方法，返回本组件新实例对象
 * @constructor
 */
const getInstance = (): DrTextConfig => {
  const config: DrTextConfig = {
    id: Math.random().toString(),
    type: 'DrText',
    w: 150,
    h: 100,
    x: 100,
    y: 100,
    z: 999,
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
const behaviors: Array<Behavior> = [
  {
    name: '点击',
    desc: '鼠标点击文本时触发',
    method: 'triggerClickEvent',
    paramsList: [],
  },
]

export { component, controlPanel, getInstance, behaviors }
