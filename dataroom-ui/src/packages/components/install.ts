import { type Component } from 'vue'
import type { BasicConfig } from '@DrPackage/components/type/define.ts'

type ComponentMap = {
  [key: string]: Component
}

type PanelComponentMap = {
  [key: string]: Component
}

type ComponentInstanceMap = {
  [key: string]: () => BasicConfig<unknown>
}

// 导入 DrText 组件
import {
  DrText,
  DrTextPanel,
  getDrTextInstance,
  DrTextInteractionDefine,
} from '@DrPackage/components/DrText/install.ts'

import {
  RemoteComponent,
  RemoteComponentPanel,
  getRemoteComponentInstance,
  RemoteComponentInteractionDefine,
} from '@DrPackage/components/Remote/install.ts'

const components: ComponentMap = {
  DrText,
  RemoteComponent,
}

const panelComponents: PanelComponentMap = {
  DrTextPanel,
  RemoteComponentPanel,
}

const componentInstances: ComponentInstanceMap = {
  getDrTextInstance,
  getRemoteComponentInstance,
}

const interactionDefines = [DrTextInteractionDefine, RemoteComponentInteractionDefine]

const getPanelComponent = (name: string) => {
  name = `${name}Panel`
  return panelComponents[name]
}

const getComponent = (name: string) => {
  return components[name]
}

const getComponentInstance = (name: string): BasicConfig<unknown> => {
  const fnName = `get${name}Instance`
  const instanceFn = componentInstances[fnName]
  if (instanceFn) {
    return instanceFn()
  }
  console.error(`未找到组件 ${name} 对应的 ${fnName} 方法`)
  return {} as BasicConfig<unknown>
}

export {
  components,
  panelComponents,
  componentInstances,
  interactionDefines,
  getComponent,
  getPanelComponent,
  getComponentInstance,
}
