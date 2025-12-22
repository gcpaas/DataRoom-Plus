import { defineAsyncComponent } from 'vue'
import type { BasicConfig, Interaction } from '../type/define'
// 注册组件
const RemoteComponent = defineAsyncComponent(() => import('./RemoteComponent.vue'))
// 注册组件配置面板
const RemoteComponentPanel = defineAsyncComponent(() => import('./RemoteComponentPanel.vue'))

interface RemoteComponentProps {
  // 文本
  text: string
  // 字体大小
  fontSize: number
}

/**
 * 定义组件配置类型
 */
export type RemoteComponentConfig = BasicConfig<RemoteComponentProps>

/**
 * 定义获取该组件实例的方法，返回本组件新实例对象
 * @constructor
 */
const getRemoteComponentInstance = (): RemoteComponentConfig => {
  const config: RemoteComponentConfig = {
    id: Math.random().toString(),
    type: 'RemoteComponent',
    w: 100,
    h: 100,
    x: 100,
    y: 100,
    z: 999,
    props: {
      text: '我是远程组件啊',
      fontSize: 14,
    },
  }
  return config
}
/**
 * 定义组件交互定义
 */
const RemoteComponentInteractionDefine: Array<Interaction> = [
  {
    name: '点击',
    desc: '鼠标点击文本时触发',
    method: 'triggerClickEvent',
    paramsList: [],
  },
]

export { RemoteComponent, RemoteComponentPanel, getRemoteComponentInstance, RemoteComponentInteractionDefine }
