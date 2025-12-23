<script setup lang="ts">
import { getComponent, getComponentInstance } from '@DrPackage/components/install.ts'
import { type Component, computed, defineAsyncComponent, reactive, ref } from 'vue'
import type { BasicConfig } from '../components/type/define.ts'
import { menuList } from './componentMenuInstall.ts'

const ComponentLib = defineAsyncComponent(() => import('./ComponentLib.vue'))
const ComponentLayer = defineAsyncComponent(() => import('./ComponentLayer.vue'))
const GlobalVariable = defineAsyncComponent(() => import('./GlobalVariable.vue'))
const ResourceLib = defineAsyncComponent(() => import('./ResourceLib.vue'))
const leftToolBarComponent: Record<string, Component> = {
  ComponentLib,
  ComponentLayer,
  GlobalVariable,
  ResourceLib,
}
const activeLeftToolBarComponent = ref(ComponentLib)
console.log(menuList)
const chartList: BasicConfig<unknown>[] = reactive([])
// 根据组件类型获取配置
const chartInst1: BasicConfig<unknown> = getComponentInstance('DrText')
const chartInst2: BasicConfig<unknown> = getComponentInstance('Remote')
// 添加到画布
chartList.push(chartInst1)
chartList.push(chartInst2)
const leftToolPanelShow = ref(true)
const rightControlPanelShow = ref(true)
// 核心：使用计算属性生成main区域的样式对象
const mainStyle = computed(() => {
  if (leftToolPanelShow.value && rightControlPanelShow.value) {
    return {
      gridTemplateColumns: '60px 200px auto 330px',
    }
  } else if (!leftToolPanelShow.value && !rightControlPanelShow.value) {
    return {
      gridTemplateColumns: '60px  auto ',
    }
  } else if (!leftToolPanelShow.value && rightControlPanelShow.value) {
    return {
      gridTemplateColumns: '60px auto 330px',
    }
  } else {
    return {
      gridTemplateColumns: '60px 200px auto',
    }
  }
})

const leftToolPanelStyle = computed(() => {
  if (!leftToolPanelShow.value) {
    return {
      display: 'none',
    }
  }
  return {}
})

const rightControlPanelStyle = computed(() => {
  if (!rightControlPanelShow.value) {
    return {
      display: 'none',
    }
  }
  return {}
})

const leftToolPanelButton = () => {
  leftToolPanelShow.value = !leftToolPanelShow.value
}

const rightControlPanelButton = () => {
  rightControlPanelShow.value = !rightControlPanelShow.value
}

const activeLeftToolBarFun = (name: string) => {
  const component: Component | undefined = leftToolBarComponent[name]
  if (component) {
    activeLeftToolBarComponent.value = component
  }
}
</script>

<template>
  <div class="dr-bs-editor">
    <div class="header">
      标题
      <el-button @click="rightControlPanelButton">配置</el-button>
      <el-button @click="leftToolPanelButton">左侧</el-button>
    </div>
    <div class="main" :style="mainStyle">
      <div class="left-tool-bar">
        <div class="bar" @click="activeLeftToolBarFun('ComponentLayer')">图层</div>
        <div class="bar active" @click="activeLeftToolBarFun('ComponentLib')">组件库</div>
        <div class="bar" @click="activeLeftToolBarFun('ResourceLib')">素材库</div>
        <div class="bar" @click="activeLeftToolBarFun('GlobalVariable')">全局变量</div>
      </div>
      <div class="left-tool-panel" :style="leftToolPanelStyle">
        <div class="panel-header">图册名称</div>
        <div class="panel-body">
          <component :is="activeLeftToolBarComponent"></component>
        </div>
      </div>
      <div class="canvas">
        <div class="canvas-main">

        </div>
        <div class="footer">底部工具</div>
      </div>
      <div class="right-panel" :style="rightControlPanelStyle"></div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.dr-bs-editor {
  display: grid;
  grid-template-rows: 60px auto;
  height: 100vh; // 设置容器高度为视口高度
  & .header {
    background-color: var(--dr-prmary);
    color: white;
  }

  & .main {
    background-color: aliceblue;
    display: grid;
    grid-template-columns: 60px 200px auto 200px;

    & .left-tool-bar {
      background-color: #fcfcfc;
      border-right: 1px solid #e8e8e8;

      & .bar {
        font-size: 12px;
        text-align: center;
        margin: 4px auto;
        padding: 8px 0;

        &:hover {
          cursor: pointer;
          background-color: #efefef;
        }
      }

      & .active {
        background-color: #3478f620;
        color: var(--dr-prmary);
      }
    }

    & .left-tool-panel {
      background-color: white;
      display: grid;
      grid-template-rows: 40px auto;

      & .panel-header {
        background-color: #fcfcfc;
        border-bottom: 1px solid #e8e8e8;
        font-size: 12px;
        line-height: 40px;
        align-self: center;
        padding-left: 16px;
      }

      & .panel-body {
        background-color: #f8f8f8;
        overflow-y: auto;
      }
    }

    & .canvas {
      display: grid;
      background-color: #e6e6e6;
      grid-template-rows: auto 40px;

      & .footer {
        background-color: #f5f5f5;
      }
    }

    & .right-panel {
      background-color: white;
    }
  }
}
</style>
