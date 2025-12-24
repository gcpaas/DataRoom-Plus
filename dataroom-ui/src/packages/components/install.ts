import { type Component } from 'vue'
import type { BasicConfig, Behavior } from '@DrPackage/components/type/define.ts'

type ComponentMap = {
  [key: string]: Component
}

type PanelComponentMap = {
  [key: string]: Component
}

type ComponentInstanceMap = {
  [key: string]: () => BasicConfig<unknown>
}

type BehaviorMap = {
  [key: string]: Behavior
}

// 使用 Vite 的 import.meta.glob 自动导入所有组件目录下的 install.ts
const installModules = import.meta.glob<{
  [key: string]: Component | (() => BasicConfig<unknown>)
}>('./**/install.ts', { eager: true })

// 存储组件、控制面板组件、实例方法和交互定义
const components: ComponentMap = {}
const panelComponents: PanelComponentMap = {}
const componentInstances: ComponentInstanceMap = {}
const behaviors: BehaviorMap = {}

// 组件自动注册
Object.entries(installModules).forEach(([path, module]) => {
  // 从路径中提取组件名称，例如：./DrText/install.ts -> DrText
  const match = path.match(/\.\/([^/]+)\/install\.ts$/)
  if (!match) return
  const componentName = match[1]
  if (!componentName) {
    console.error('组件路径%s未能够解析出组件名', path)
    return
  }
  // 注册主组件
  if (module['component']) {
    components[componentName] = module['component'] as Component
  }
  // 注册控制面板组件
  if (module['controlPanel']) {
    panelComponents[componentName] = module['controlPanel'] as Component
  }
  // 注册实例方法
  const instanceMethodName = `getInstance`
  if (module[instanceMethodName]) {
    componentInstances[componentName] = module[instanceMethodName] as () => BasicConfig<unknown>
  }

  // 注册交互定义
  const behaviorDefineName = `behaviors`
  if (module[behaviorDefineName]) {
    behaviors[componentName] = module[behaviorDefineName] as Behavior
  }
})

const getPanelComponent = (name: string|undefined) => {
  console.log("获取配置面板",name,panelComponents)
  if (name) {
    return panelComponents[name]
  }
  return null
}

const getComponent = (name: string) => {
  return components[name]
}

const getComponentInstance = (name: string): BasicConfig<unknown> => {
  const instanceFn = componentInstances[name]
  if (instanceFn) {
    return instanceFn()
  }
  console.error(`未找到组件 ${name} 对应的实例化方法`)
  return {} as BasicConfig<unknown>
}

export {
  components,
  panelComponents,
  componentInstances,
  behaviors,
  getComponent,
  getPanelComponent,
  getComponentInstance,
}
