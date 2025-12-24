<script setup lang="ts">
import {
  getComponent,
  getComponentInstance,
  getPanelComponent,
} from '@DrPackage/components/install.ts'
import { type CSSProperties } from 'vue'
import {
  type Component,
  computed,
  defineAsyncComponent,
  reactive,
  ref,
  shallowRef,
  provide,
} from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout-v3'
import type { BasicConfig } from '../components/type/define.ts'
import { getChartById } from '@/packages/dashBoard/utils.ts'

const activeChart = ref<BasicConfig<unknown>>()
const chartList: BasicConfig<unknown>[] = reactive([])
const layout = [
  { x: 0, y: 0, w: 2, h: 2, i: '0' },
  { x: 2, y: 0, w: 2, h: 4, i: '1' },
  { x: 4, y: 0, w: 2, h: 5, i: '2' },
]
const chartIndex = ref(3)
layout.forEach((item) => {
  const inst: BasicConfig<unknown> = getComponentInstance('DrText')
  inst.id = item.i
  inst.i = item.i
  inst.x = item.x
  inst.y = item.y
  inst.w = item.w
  inst.h = item.h
  chartList.push(inst)
})
const addChart = (type: string) => {
  const chartInst: BasicConfig<unknown> = getComponentInstance(type)
  chartInst.w = 3
  chartInst.h = 3
  chartInst.x = 0
  chartInst.y = 0
  chartInst.i = (chartIndex.value++)+''
  chartInst.id = chartInst.i
  console.log('新增组件', chartInst)
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
 * 计算组件坐标样式
 * @param chart
 */
const computedChartStyle = (chart: BasicConfig<unknown>): CSSProperties => {
  return {}
}

const onResize = (i: string, newH: string, newW: string, newHPx: string, newWPx: string) => {
  console.log(
    'onResize i=' + i + ', H=' + newH + ', W=' + newW + ', H(px)=' + newHPx + ', W(px)=' + newWPx,
  )
}
const onResized = (i: string, newH: number, newW: number, newHPx: string, newWPx: string) => {
  console.log(
    'onResized i=' + i + ', H=' + newH + ', W=' + newW + ', H(px)=' + newHPx + ', W(px)=' + newWPx,
  )
  const chart: BasicConfig<unknown> = getChartById(i, chartList)
  chart.w = newW
  chart.h = newH
}

const onMove = (i: string, newX: number, newY: number) => {
  console.log('onMove i=' + i + ', X=' + newX + ', Y=' + newY)
}

const onMoved = (i: string, newX: number, newY: number) => {
  console.log('onMoved i=' + i + ', X=' + newX + ', Y=' + newY)
  const chart: BasicConfig<unknown> = getChartById(i, chartList)
  chart.x = newX
  chart.y = newY
}

const onContainerResized = (newH: string, newW: string, newHPx: string, newWPx: string) => {
  console.log(
    'onContainerResized H=' + newH + ', W=' + newW + ', H(px)=' + newHPx + ', W(px)=' + newWPx,
  )
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
          {{chartList}}
          <GridLayout
            v-model:layout="chartList"
            :col-num="12"
            :row-height="30"
            :is-draggable="true"
            :is-resizable="true"
            :vertical-compact="true"
            :use-css-transforms="true"
          >
            <GridItem
              v-for="(item, index) in chartList"
              :key="index"
              :static="false"
              :x="item.x"
              :y="item.y"
              :w="item.w"
              :h="item.h"
              :i="item.id"
              @resize="onResize"
              @move="onMove"
              @resized="onResized"
              @container-resized="onContainerResized"
              @moved="onMoved"
            >
              <div
                class="chart-wrapper"
                :key="item.id"
                :id="item.id"
                :data-dr-id="item.id"
                :style="computedChartStyle(item)"
              >
                <component :is="getComponent(item.type)" :chart="item"></component>
              </div>
            </GridItem>
          </GridLayout>
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
      background-color: #f5f7fa;
      grid-template-rows: auto 40px;

      & .canvas-main {
        & .chart-wrapper {
          background-color: white;
          height: 100%;
          width: 100%;
        }
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
