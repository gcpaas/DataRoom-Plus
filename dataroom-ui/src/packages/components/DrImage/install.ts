import { defineAsyncComponent } from 'vue'
import type { ChartConfigInterface, BehaviorInterface } from '../type/define'
// 注册组件
const component = defineAsyncComponent(() => import('./index.vue'))
// 注册组件配置面板
const controlPanel = defineAsyncComponent(() => import('./panel/index.vue'))

interface DrImagePropsInterface {
  // 文本
  text: string
  // 字体大小
  fontSize: number
}

/**
 * 定义组件配置类型
 */
export type DrImageConfig = ChartConfigInterface<DrImagePropsInterface>

/**
 * 定义获取该组件实例的方法，返回本组件新实例对象
 * @constructor
 */
const getInstance = (): DrImageConfig => {
  const config: DrImageConfig = {
    id: Math.random().toString(),
    i: Math.random().toString(),
    type: 'DrImage',
    title: '文本',
    w: 150,
    h: 100,
    x: Math.random() * 100 + 100,
    y: 100,
    z: 999,
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
const behaviors: BehaviorInterface[] = [
  {
    name: '点击',
    desc: '鼠标点击文本时触发',
    method: 'triggerClickEvent',
    paramsList: [],
  },
]

export { component, controlPanel, getInstance, behaviors }
