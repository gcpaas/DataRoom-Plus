<script setup lang="ts">
import {
  getComponent,
  getComponentInstance,
  getPanelComponent,
} from '@DrPackage/components/install.ts'
import { type ComputedRef, type CSSProperties } from 'vue'
import { debounce } from 'lodash'
import Moveable, {
  type OnClick,
  type OnDrag,
  type OnDragEnd,
  type OnRotate,
  type OnRotateEnd,
  type OnResize,
  type OnResizeEnd,
  type OnDragStart,
  type OnEvent,
} from 'vue3-moveable'
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
import VanillaSelecto from 'selecto'

const canvasContainer = document.getElementById('canvas-main')
const activeChart = ref<BasicConfig<unknown>>()
const chartList: BasicConfig<unknown>[] = reactive([])
/**
 * 被框选中的组件、可以进行拖拽、旋转、缩放
 */
const moveableTargets: ComputedRef<(HTMLElement | null)[]> = computed(() => {
  if (!activeChart.value) {
    return []
  }
  const dom = document.getElementById(activeChart.value.id)
  return [dom]
})

const addChart = (type: string) => {
  const chartInst: BasicConfig<unknown> = getComponentInstance(type)
  chartList.push(chartInst)
}
/**
 * 子组件注入使用
 */
provide('canvasInst', {
  addChart: addChart,
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
/**
 * 左侧工具面版样式
 */
const leftToolPanelStyle = computed(() => {
  if (!leftToolPanelShow.value) {
    return {
      display: 'none',
    }
  }
  return {}
})
/**
 * 右侧配置面版样式
 */
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

const onActiveLeftToolBar = (name: string) => {
  activeLeftToolBarComponentName.value = name
  const component = leftToolBarComponent[name]
  if (component) {
    activeLeftToolBarComponent.value = component
  }
}
/**
 * 单击组件
 * @param e
 */
const onChartClick = (e: OnClick) => {
  console.log('onChartClick', e)
}
/**
 * 拖拽组件开始
 * @param e
 */
const onDragStart = (e: OnDragStart) => {
  console.log('onDragStart ', e)
}

/**
 * 拖拽组件中
 * @param e
 */
const onDrag = (e: OnDrag) => {
  // console.log('onDrag', e)
  e.target.style.transform = e.transform
  updateTransform(e, e.transform, e.width, e.height)
}
/**
 * 拖拽组件结束
 * @param e
 */
const onDragEnd = (e: OnDragEnd) => {
  console.log('onDragEnd', e)
}

const _updateTransform = (e: OnEvent, transform: string, width: number, height: number) => {
  console.log('updateTransform', width)
  const chart: BasicConfig<unknown> = getChartById(e.target, chartList)
  const { x, y, rotateX, rotateY, rotateZ } = extractPositionFromTransform(transform)
  chart.x = x
  chart.y = y
  chart.w = width
  chart.h = height
  chart.rotateX = rotateX
  chart.rotateY = rotateY
  chart.rotateZ = rotateZ
}
const updateTransform = debounce((e: OnEvent, transform: string, width: number, height: number) => {
  _updateTransform(e, transform, width, height)
}, 100)
/**
 * 缩放组件中
 * @param e
 */
const onResize = (e: OnResize) => {
  e.target.style.width = `${e.width}px`
  e.target.style.height = `${e.height}px`
  e.target.style.transform = e.drag.transform
  updateTransform(e, e.drag.transform, e.width, e.height)
}
/**
 * 缩放组件结束
 * @param e
 */
const onResizeEnd = (e: OnResizeEnd) => {
  console.log('onResizeEnd', e)
  return null
}
/**
 * 旋转组件中
 * @param e
 */
const onRotate = (e: OnRotate) => {
  console.log('onRotate', e.drag.transform)
  e.target.style.transform = e.drag.transform
}

/**
 * 旋转组件结束
 * @param e
 */
const onRotateEnd = (e: OnRotateEnd) => {
  console.log('onRotateEnd', e)
  const width: number = parseInt(e.target.style.width.replace('px', ''))
  const height: number = parseInt(e.target.style.height.replace('px', ''))
  updateTransform(e, e.target.style.transform, width, height)
}
/**
 * 框选开始
 * @param e
 */
const onSelectDragStart = (e: import('selecto').OnDragStart<VanillaSelecto>) => {
  console.log('onSelectorDragStart ', e)
}
/**
 * 框选结束
 * @param e
 */
const onSelectEnd = (e: import('selecto').OnSelectEnd<VanillaSelecto>) => {
  console.log('onSelectEnd', e)
  if (e.selected.length <= 0) {
    activeChart.value = undefined
    return
  }
  const target = e.selected[0]
  if (target) {
    const active = getChartById(target, chartList)
    activeChart.value = active
  }
}
/**
 * 计算组件坐标样式
 * @param chart
 */
const computedChartStyle = (chart: BasicConfig<unknown>): CSSProperties => {
  let transform = `translate(${chart.x}px,${chart.y}px)`
  if (chart.rotateX) {
    transform += ` rotateX(${chart.rotateX}deg)`
  }
  if (chart.rotateY) {
    transform += ` rotateY(${chart.rotateY}deg)`
  }
  if (chart.rotateZ) {
    transform += ` rotateZ(${chart.rotateZ}deg)`
  }
  return {
    position: 'absolute',
    transform: transform,
    width: `${chart.w}px`,
    height: `${chart.h}px`,
  }
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
          @click="onActiveLeftToolBar('ComponentLayer')"
        >
          图层
        </div>
        <div
          :class="{ bar: true, active: activeLeftToolBarComponentName === 'ComponentLib' }"
          @click="onActiveLeftToolBar('ComponentLib')"
        >
          组件库
        </div>
        <div
          :class="{ bar: true, active: activeLeftToolBarComponentName === 'ResourceLib' }"
          @click="onActiveLeftToolBar('ResourceLib')"
        >
          素材库
        </div>
        <div
          :class="{ bar: true, active: activeLeftToolBarComponentName === 'GlobalVariable' }"
          @click="onActiveLeftToolBar('GlobalVariable')"
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
            :target="moveableTargets"
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
            :selectFromInside="false"
            :continueSelect="false"
            :toggleContinueSelect="'shift'"
            :hitRate="100"
            :ratio="0"
            @dragStart="onSelectDragStart"
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
