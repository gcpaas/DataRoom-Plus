import { type Component } from 'vue'

type ComponentMap = {
  [key: string]: Component
}

type PanelComponentMap = {
  [key: string]: Component
}

type ComponentInstanceMap = {
  [key: string]: () => any
}

import {
  DrText,
  DrTextPanel,
  getDrTextInstance,
  DrTextInteractionDefine,
} from '@/packages/components/Text/install.ts'

const components: ComponentMap = {
  DrText,
}

const panelComponents: PanelComponentMap = {
  DrTextPanel,
}

const componentInstances: ComponentInstanceMap = { getDrTextInstance }

const getPanelComponent = (name: string) => {
  name = `${name}Panel`
  return panelComponents[name]
}

const interactionDefines = [DrTextInteractionDefine]

const getComponent = (name: string) => {
  return components[name]
}

const getComponentInstance = (name: string): (() => any) | undefined => {
  name = `get${name}Instance`
  return componentInstances[name]()
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
