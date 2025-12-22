import { type Component } from 'vue'
import type { BasicConfig, Interaction } from '@DrPackage/components/type/define.ts'

type ComponentMap = {
  [key: string]: Component
}

type PanelComponentMap = {
  [key: string]: Component
}

type ComponentInstanceMap = {
  [key: string]: () => BasicConfig<unknown>
}

// 使用 Vite 的 import.meta.glob 自动导入所有组件目录下的 install.ts
const installModules = import.meta.glob<{
  [key: string]: Component | (() => BasicConfig<unknown>) | Array<Interaction>
}>('./**/install.ts', { eager: true })

// 存储组件、面板组件、实例方法和交互定义
const components: ComponentMap = {}
const panelComponents: PanelComponentMap = {}
const componentInstances: ComponentInstanceMap = {}
const interactionDefines: Array<Interaction>[] = []

// 遍历所有导入的模块进行注册
Object.entries(installModules).forEach(([path, module]) => {
  // 从路径中提取组件名称，例如：./DrText/install.ts -> DrText
  const match = path.match(/\.\/([^/]+)\/install\.ts$/)
  if (!match) return
  const componentName = match[1]
  // 注册主组件
  // @ts-ignore
  if (module['component']) {
    // @ts-ignore
    components[componentName] = module['component'] as Component
  }
  // 注册面板组件
  const panelName = `${componentName}Panel`
  if (module['controlPanel']) {
    panelComponents[panelName] = module['controlPanel'] as Component
  }

  // 注册实例方法
  const instanceMethodName = `get${componentName}Instance`
  if (module[instanceMethodName]) {
    componentInstances[instanceMethodName] = module[
      instanceMethodName
    ] as () => BasicConfig<unknown>
  }

  // 注册交互定义
  const interactionDefineName = `${componentName}InteractionDefine`
  if (module[interactionDefineName]) {
    interactionDefines.push(module[interactionDefineName] as Array<Interaction>)
  }
})

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
