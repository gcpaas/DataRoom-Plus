import { defineAsyncComponent } from 'vue'
import type { BasicConfig, Interaction } from '../type/define'
// 注册组件
const DrText = defineAsyncComponent(() => import('@/packages/components/Text/DrText.vue'))
// 注册组件配置面板
const DrTextPanel = defineAsyncComponent(() => import('@/packages/components/Text/DrTextPanel.vue'))

/**
 * 定义组件配置类型
 */
export interface DrTextConfig {
  // 名称
  name: string
  // 年龄
  age: number
}

/**
 * 定义获取该组件实例的方法，返回本组件新实例对象
 * @constructor
 */
const getDrTextInstance = () => {
  const config: BasicConfig<DrTextConfig> = {
    id: Math.random().toString(),
    type: 'DrText',
    w: 100,
    h: 100,
    x: 100,
    y: 100,
    z: 999,
    props: {
      name: 'xx',
      age: 18,
    },
  }
  return config
}
/**
 * 定义组件交互定义
 */
const DrTextInteractionDefine: Array<Interaction> = [
  {
    name: '点击',
    desc: '鼠标点击文本时触发',
    method: 'triggerClickEvent',
    paramsList: [],
  },
]

export { DrText, DrTextPanel, getDrTextInstance, DrTextInteractionDefine }
