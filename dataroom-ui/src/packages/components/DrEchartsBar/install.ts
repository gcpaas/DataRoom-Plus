import { defineAsyncComponent } from 'vue'
import type { BasicConfig, Behavior } from '../type/define'
import DrEchartsBarPlugin from '@/packages/components/DrEchartsBar/define.ts'
// 注册组件
const component = defineAsyncComponent(() => import('./index.vue'))
// 注册组件配置面板
const controlPanel = defineAsyncComponent(() => import('./panel/index.vue'))

interface DrEchartsBarProps {
  // 文本
  text: string
  // 字体大小
  fontSize: number
}

/**
 * 定义组件配置类型
 */
export type DrEchartsBarConfig = BasicConfig<DrEchartsBarProps>

/**
 * 定义获取该组件实例的方法，返回本组件新实例对象
 * @constructor
 */
const getInstance = ():DrEchartsBarConfig => {
  const config: DrEchartsBarConfig = {
    id: Math.random().toString(),
    i: Math.random().toString(),
    type: 'DrEchartsBar',
    title: '柱状图',
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
const behaviors: Array<Behavior> = [
  {
    name: '点击',
    desc: '鼠标点击文本时触发',
    method: 'triggerClickEvent',
    paramsList: [],
  },
]

export { component, controlPanel, getInstance, behaviors }
