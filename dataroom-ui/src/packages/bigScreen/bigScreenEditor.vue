<script setup lang="ts">
import {
  getComponent,
  getComponentInstance,
  getPanelComponent,
} from '@DrPackage/components/install.ts'
import { type ComponentInternalInstance, type CSSProperties, onMounted } from 'vue'
import { debounce } from 'lodash'
import Moveable from 'vue3-moveable'
import { VueSelecto } from 'vue3-selecto'
import {
  type Component,
  computed,
  defineAsyncComponent,
  reactive,
  ref,
  shallowRef,
  provide,
} from 'vue'
import type { BasicConfig } from '../components/type/define.ts'
import { extractPositionFromTransform, getChartById } from '@/packages/bigScreen/utils.ts'
import { OnResize } from 'react-moveable/src/types.ts'

const chartList: BasicConfig<unknown>[] = reactive([])

const addChart = (type: string) => {
  const chartInst: BasicConfig<unknown> = getComponentInstance(type)
  chartList.push(chartInst)
}

const chartInstList: ComponentInternalInstance = []
provide('canvasInst', {
  addChart: addChart,
  chartInstList: chartInstList,
})

const ComponentLib = defineAsyncComponent(() => import('./ComponentLib.vue'))
const ComponentLayer = defineAsyncComponent(() => import('./ComponentLayer.vue'))
const GlobalVariable = defineAsyncComponent(() => import('./GlobalVariable.vue'))
const ResourceLib = defineAsyncComponent(() => import('./ResourceLib.vue'))
// 映射左侧组件
const leftToolBarComponent: Record<string, unknown> = {
  ComponentLib: ComponentLib,
  ComponentLayer: ComponentLayer,
  GlobalVariable: GlobalVariable,
  ResourceLib: ResourceLib,
}
// 当前激活的左侧组件
const activeLeftToolBarComponent = shallowRef<Component>(ComponentLib)
const activeLeftToolBarComponentName = ref('ComponentLib')

// 添加到画布
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
  activeLeftToolBarComponentName.value = name
  const component = leftToolBarComponent[name]
  if (component) {
    activeLeftToolBarComponent.value = component
  }
}

const onChartClick = (e: any) => {
  console.log('onChartClick', e)
}
const onDrag = (e: any) => {
  // console.log('onDrag', e)
  e.target.style.transform = e.transform
  updateTransform(e, e.transform, e.width, e.height)
}

const onRotate = (e: any) => {
  console.log('onRotate', e.drag.transform)
}

const onDragStart = (e: any) => {
  console.log('onDragStart ', e)
}

const onDragEnd = (e: any) => {
  console.log('onDragEnd', e)
  // 获取target中el的id
  // const chart: BasicConfig<unknown> = getChartById(e.target, chartList)
  // const transform: string = e.target.style.transform
  // const { x, y } = extractPositionFromTransform(transform)
  // chart.x = x
  // chart.y = y
}

const _updateTransform = (e: OnResize, transform: string, width: number, height: number) => {
  console.log(e)
  const chart: BasicConfig<unknown> = getChartById(e.target, chartList)
  const { x, y } = extractPositionFromTransform(transform)
  chart.x = x
  chart.y = y
  chart.w = width
  chart.h = height
}
const updateTransform = debounce(
  (e: OnResize, transform: string, width: number, height: number) => {
    _updateTransform(e, transform, width, height)
  },
  500,
)

const onResize = (e: OnResize) => {
  e.target.style.width = `${e.width}px`
  e.target.style.height = `${e.height}px`
  e.target.style.transform = e.drag.transform
  updateTransform(e, e.drag.transform, e.width, e.height)
}

const onResizeEnd = (e: any) => {
  console.log('onResizeEnd', e)
}
const onRotateEnd = (e: any) => {
  console.log('onRotateEnd', e)
}

const target: [] = computed(() => {
  if (!activeChart.value) {
    return []
  }
  let dom = document.getElementById(activeChart.value.id)
  return [dom]
})

const computedChartStyle = (chart: BasicConfig<unknown>): CSSProperties => {
  return {
    position: 'absolute',
    transform: `translate(${chart.x}px,${chart.y}px)`,
    width: `${chart.w}px`,
    height: `${chart.h}px`,
  }
}

const canvasContainer = document.getElementById('canvas-main')
const activeChart = ref<BasicConfig<unknown>>()
const onSelectEnd = (e: any) => {
  console.log('选中', e)
  //   let elementsByClassName = document.getElementsByClassName('chart-wrapper')
  //   target.value = elementsByClassName
  if (e.selected.length <= 0) {
    return
  }
  const target = e.selected[0]
  const active = getChartById(target, chartList)
  activeChart.value = active
  console.log(activeChart)
}
</script>

<template>
  <div class="dr-bs-editor">
    <div class="header" ref="titleRef">
      标题
      <el-button @click="rightControlPanelButton">配置</el-button>
      <el-button @click="leftToolPanelButton">左侧</el-button>
    </div>
    <div class="main" :style="mainStyle">
      <div class="left-tool-bar">
        <div
          :class="{ bar: true, active: activeLeftToolBarComponentName === 'ComponentLayer' }"
          @click="activeLeftToolBarFun('ComponentLayer')"
        >
          图层
        </div>
        <div
          :class="{ bar: true, active: activeLeftToolBarComponentName === 'ComponentLib' }"
          @click="activeLeftToolBarFun('ComponentLib')"
        >
          组件库
        </div>
        <div
          :class="{ bar: true, active: activeLeftToolBarComponentName === 'ResourceLib' }"
          @click="activeLeftToolBarFun('ResourceLib')"
        >
          素材库
        </div>
        <div
          :class="{ bar: true, active: activeLeftToolBarComponentName === 'GlobalVariable' }"
          @click="activeLeftToolBarFun('GlobalVariable')"
        >
          全局变量
        </div>
      </div>
      <div class="left-tool-panel" :style="leftToolPanelStyle">
        <div class="panel-header">图册名称</div>
        <div class="panel-body">
          <component :is="activeLeftToolBarComponent"></component>
        </div>
      </div>
      <div class="canvas">
        <div class="canvas-main" id="canvas-main">
          <div
            class="chart-wrapper"
            v-for="item in chartList"
            :key="item.id"
            :id="item.id"
            :data-dr-id="item.id"
            :style="computedChartStyle(item)"
          >
            <component :is="getComponent(item.type)" :chart="item"></component>
          </div>
          <Moveable
            ref="moveableRef"
            :draggable="true"
            :rotatable="true"
            :resizable="true"
            :target="target"
            :snappable="true"
            :bounds="{ left: 0, top: 0, right: 0, bottom: 0, position: 'css' }"
            :snap-directions="{
              top: true,
              left: true,
              bottom: true,
              right: true,
              center: true,
              middle: true,
            }"
            :element-snap-directions="{
              top: true,
              left: true,
              bottom: true,
              right: true,
              center: true,
              middle: true,
            }"
            :max-snap-element-guideline-distance="70"
            :render-directions="['nw', 'ne', 'sw', 'se', 'n', 's', 'e', 'w']"
            @click="onChartClick"
            @drag="onDrag"
            @resize="onResize"
            @rotate="onRotate"
            @dragStart="onDragStart"
            @dragEnd="onDragEnd"
            @resizeEnd="onResizeEnd"
            @rotateEnd="onRotateEnd"
          />
          <VueSelecto
            :container="canvasContainer"
            :selectableTargets="['.chart-wrapper']"
            :selectByClick="true"
            :selectFromInside="true"
            :continueSelect="false"
            :toggleContinueSelect="'shift'"
            :hitRate="100"
            @dragStart="onDragStart"
            @selectEnd="onSelectEnd"
          />
        </div>
        <div class="footer">底部工具</div>
      </div>
      <div class="right-panel" :style="rightControlPanelStyle">
        <component :is="getPanelComponent(activeChart?.type)" :chart="activeChart"></component>
      </div>
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

      & .canvas-main {
        position: relative;
      }

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
