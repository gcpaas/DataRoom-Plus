<script setup lang="ts">
import {
  getComponent,
  getComponentInstance,
  getPanelComponent,
} from '@DrPackage/components/install.ts'
import { type CSSProperties, reactive } from 'vue'
import { type Component, computed, defineAsyncComponent, ref, shallowRef, provide } from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout-v3'
import type { BasicConfig } from '../components/type/define.ts'
import { getChartById } from '@/packages/PageDesigner/utils.ts'
import type { LeftToolBar } from '@/packages/VisualScreenDesigner/type.ts'

const activeChart = ref<BasicConfig<unknown>>()
const chartList = ref<BasicConfig<unknown>[]>([])
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
  chartList.value.push(inst)
})
const addChart = (type: string) => {
  const chartInst: BasicConfig<unknown> = getComponentInstance(type)
  chartInst.w = 3
  chartInst.h = 3
  chartInst.x = 0
  chartInst.y = 0
  chartInst.i = chartIndex.value++ + ''
  chartInst.id = chartInst.i
  console.log('新增组件', chartInst)
  chartList.value.push(chartInst)
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

const leftToolBarList: Array<LeftToolBar> = reactive([
  {
    name: '图层',
    component: ComponentLayer,
    componentName: 'ComponentLayer',
  },
  {
    name: '组件库',
    component: ComponentLib,
    componentName: 'ComponentLib',
  },
  {
    name: '素材库',
    component: ResourceLib,
    componentName: 'ResourceLib',
  },
  {
    name: '全局变量',
    component: GlobalVariable,
    componentName: 'GlobalVariable',
  },
])

// 当前激活的左侧组件
const activeLeftToolBarComponent = shallowRef<Component>(ComponentLib)
const activeLeftToolBarComponentName = ref('ComponentLib')
const leftToolPanelShow = ref(true)
const rightControlPanelShow = ref(true)
const activeLeftToolBar = ref<LeftToolBar>(null)

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

const onActiveLeftToolBar = (leftToolBar: LeftToolBar) => {
  activeLeftToolBar.value = leftToolBar
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
  const chart: BasicConfig<unknown> = getChartById(i, chartList.value)
  chart.w = newW
  chart.h = newH
}

const onMove = (i: string, newX: number, newY: number) => {
  console.log('onMove i=' + i + ', X=' + newX + ', Y=' + newY)
}

const onMoved = (i: string, newX: number, newY: number) => {
  console.log('onMoved i=' + i + ', X=' + newX + ', Y=' + newY)
  const chart: BasicConfig<unknown> = getChartById(i, chartList.value)
  chart.x = newX
  chart.y = newY
}

const onContainerResized = (newH: string, newW: string, newHPx: string, newWPx: string) => {
  console.log(
    'onContainerResized H=' + newH + ', W=' + newW + ', H(px)=' + newHPx + ', W(px)=' + newWPx,
  )
}

const onChartClick = (chart: BasicConfig<unknown>) => {
  console.log('onChartClick', chart)
  activeChart.value = chart
}
</script>

<template>
  <div class="dr-page-designer">
    <div class="header" ref="titleRef">
      标题
      <el-button @click="rightControlPanelButton">配置</el-button>
      <el-button @click="leftToolPanelButton">左侧</el-button>
    </div>
    <div class="main" :style="mainStyle">
      <div class="left-tool-bar">
        <div
          v-for="item in leftToolBarList"
          :key="item.name"
          :class="{ bar: true, active: activeLeftToolBar?.name === item.componentName }"
          @click="onActiveLeftToolBar(item)"
        >
          {{ item.name }}
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
          <el-scrollbar>
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
                @click="onChartClick(item)"
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
          </el-scrollbar>
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
.dr-page-designer {
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
        background-color: var(--dr-prmary1);
        color: var(--dr-prmary);
        position: relative;

        &::before {
          content: '';
          left: 0;
          top: 0;
          bottom: 0;
          width: 3px;
          position: absolute;
          background-color: var(--dr-prmary);
        }
      }
    }

    & .left-tool-panel {
      background-color: white;
      display: grid;
      grid-template-rows: 40px auto;

      & .panel-header {
        background-color: #fcfcfc;
        box-sizing: border-box;
        border-bottom: 1px solid #e8e8e8;
        font-size: 12px;
        line-height: 40px;
        height: 40px;
        align-self: center;
        padding-left: 8px;
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
        // 减去header、footer 高度
        height: calc(100vh - 40px - 60px);
        // 使用了el-scroll
        overflow: hidden;

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
