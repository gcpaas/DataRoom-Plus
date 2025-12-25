<script setup lang="ts">
import { getComponent, getComponentInstance, getPanelComponent } from '@DrPackage/components/install.ts'
import { type CSSProperties, reactive } from 'vue'
import { type Component, computed, defineAsyncComponent, ref, shallowRef, provide } from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout-v3'
import type { BasicConfig } from '../components/type/define.ts'
import { getChartById } from '@/packages/PageDesigner/utils.ts'
import type { CanvasInst, LeftToolBar } from '@/packages/_components/type.ts'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { DrConst } from '@/packages/_components/constant.ts'

const router = useRouter()
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
  // 循环添加
  for (let i = 0; i < 20; i++) {
    const instCopy: BasicConfig<unknown> = JSON.parse(JSON.stringify(inst))
    instCopy.id = (chartIndex.value++).toString()
    instCopy.i = (chartIndex.value++).toString()
    chartList.value.push(instCopy)
  }
})
const addChart = (type: string) => {
  const chartInst: BasicConfig<unknown> = getComponentInstance(type)
  chartInst.w = 3
  chartInst.h = 3
  chartInst.x = 0
  chartInst.y = 0
  chartInst.i = chartIndex.value++ + ''
  chartInst.id = chartInst.i
  chartList.value.push(chartInst)
}
/**
 * 子组件注入使用
 */
const canvasInst = reactive<CanvasInst>({
  addChart: addChart,
  chartList: chartList,
  activeChartById: (id: string) => {
    const chart: BasicConfig<unknown> = getChartById(id, chartList.value)
    activeChart.value = chart
  },
})
provide(DrConst.CANVAS_INST, canvasInst)

const leftToolPanelShow = ref(true)
const rightControlPanelShow = ref(true)
const ControlPanel = defineAsyncComponent(() => import('@/packages/_components/ControlPanel.vue'))
const ComponentLib = defineAsyncComponent(() => import('@/packages/_components/ComponentLib.vue'))
const ComponentLayer = defineAsyncComponent(() => import('@/packages/_components/ComponentLayer.vue'))
const GlobalVariable = defineAsyncComponent(() => import('@/packages/_components/GlobalVariable.vue'))
const ResourceLib = defineAsyncComponent(() => import('@/packages/_components/ResourceLib.vue'))

const leftToolBarList: Array<LeftToolBar> = reactive([
  {
    name: '图层',
    desc: '图层',
    component: shallowRef<Component>(ComponentLayer),
    componentName: 'ComponentLayer',
  },
  {
    name: '组件库',
    desc: '组件库',
    component: shallowRef<Component>(ComponentLib),
    componentName: 'ComponentLib',
  },
  {
    name: '素材库',
    desc: '素材库',
    component: shallowRef<Component>(ResourceLib),
    componentName: 'ResourceLib',
  },
  {
    name: '全局变量',
    desc: '全局变量',
    component: shallowRef<Component>(GlobalVariable),
    componentName: 'GlobalVariable',
  },
])
// @ts-expect-error ignore
const activeLeftToolBar = ref<LeftToolBar>(leftToolBarList[1])

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
/**
 * 右侧配置面板开关
 * @param open
 */
const switchRightControlPanel = (open: boolean = true) => {
  rightControlPanelShow.value = open
}
/**
 * 左侧工具面板开关
 * @param open
 */
const switchLeftToolPanel = (open: boolean = true) => {
  leftToolPanelShow.value = open
}
/**
 * 左侧工具面版激活
 * @param leftToolBar
 */
const onActiveLeftToolBar = (leftToolBar: LeftToolBar) => {
  activeLeftToolBar.value = leftToolBar
  switchLeftToolPanel(true)
}
/**
 * 计算组件坐标样式
 * @param chart
 */
const computedChartStyle = (chart: BasicConfig<unknown>): CSSProperties => {
  // 暂时无用
  if (chart) {
    return {}
  }
  return {}
}
const computedToolAnchorStyle = computed(() => {
  if (rightControlPanelShow.value) {
    return {
      position: 'fixed',
      right: '330px',
    }
  }
  return {
    position: 'fixed',
    right: 0,
  }
})

const onResize = (i: string, newH: string, newW: string, newHPx: string, newWPx: string) => {
  console.log('onResize i=' + i + ', H=' + newH + ', W=' + newW + ', H(px)=' + newHPx + ', W(px)=' + newWPx)
}
const onResized = (i: string, newH: number, newW: number, newHPx: string, newWPx: string) => {
  console.log('onResized i=' + i + ', H=' + newH + ', W=' + newW + ', H(px)=' + newHPx + ', W(px)=' + newWPx)
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

/**
 * 图表点击
 * @param chart
 */
const onChartClick = (chart: BasicConfig<unknown>) => {
  console.log('onChartClick', chart)
  activeChart.value = chart
}
/**
 * 页面预览
 */
const onPreview = () => {
  console.log('onPreview')
  localStorage.setItem('chartList', JSON.stringify(chartList.value))
  // 跳转到 /dataRoom/pagePreviewer 路由
  const routeData = router.resolve({
    path: '/dataRoom/pagePreviewer',
    query: { code: 'test' },
  })
  console.log('预览跳转', routeData.href)
  window.open(routeData.href, '_blank')
}

const onSave = () => {
  console.log('onSave')
  localStorage.setItem('chartList', JSON.stringify(chartList.value))
  ElMessage({
    message: '已保存到缓存',
    type: 'success',
  })
}
</script>

<template>
  <div class="dr-page-designer">
    <div class="header" ref="titleRef">
      <div class="title">标题</div>
      <div style="margin-right: 8px">
        <el-button @click="switchRightControlPanel(!rightControlPanelShow)" size="small">设置</el-button>
        <el-button @click="onPreview" size="small">预览</el-button>
        <el-button @click="onSave" size="small">保存</el-button>
      </div>
    </div>
    <div class="main" :style="mainStyle">
      <div class="left-tool-bar">
        <div
          v-for="item in leftToolBarList"
          :key="item.name"
          :class="{ bar: true, active: activeLeftToolBar.componentName === item.componentName }"
          @click="onActiveLeftToolBar(item)"
        >
          {{ item.name }}
        </div>
      </div>
      <div class="left-tool-panel" :style="leftToolPanelStyle">
        <div class="panel-header">
          <div style="position: relative">
            <span class="title">{{ activeLeftToolBar.desc }}</span>
            <el-icon class="close" @click="switchLeftToolPanel(false)">
              <Close />
            </el-icon>
          </div>
        </div>
        <div class="panel-body">
          <el-scrollbar>
            <component :is="activeLeftToolBar.component"></component>
          </el-scrollbar>
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
                @moved="onMoved"
                @click="onChartClick(item)"
              >
                <div class="chart-wrapper" :key="item.id" :id="item.id" :data-dr-id="item.id" :style="computedChartStyle(item)">
                  <component :is="getComponent(item.type)" :chart="item"></component>
                </div>
              </GridItem>
            </GridLayout>
          </el-scrollbar>
        </div>
      </div>
      <div class="right-panel" :style="rightControlPanelStyle">
        <component :is="getPanelComponent(activeChart?.type)" :chart="activeChart"></component>
      </div>
      <el-icon class="right-panel-tool-anchor" @click="switchRightControlPanel(!rightControlPanelShow)" :style="computedToolAnchorStyle">
        <ArrowRight v-if="rightControlPanelShow" />
        <ArrowLeft v-else />
      </el-icon>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use './assets/index.scss';
// 拖拽背景样式
:deep(.vue-grid-item.vue-grid-placeholder) {
  background: var(--dr-prmary);
}

.dr-page-designer {
  display: grid;
  grid-template-rows: var(--dr-designer-header-height) auto;
  height: 100vh; // 设置容器高度为视口高度
  & .header {
    background-color: var(--dr-prmary);
    color: white;
    display: flex;
    align-items: center;
    justify-content: space-between;

    & .title {
      margin-left: 8px;
      font-size: 14px;
    }
  }

  & .main {
    background-color: aliceblue;
    display: grid;
    grid-template-columns: var(--dr-designer-left-tool-bar-width) var(--dr-designer-left-tool-panel-width) auto var(--dr-designer-right-panel-width);

    & .left-tool-bar {
      background-color: #fcfcfc;
      border-right: 1px solid var(--dr-border);

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
      height: calc(100vh - var(--dr-designer-left-tool-panel-header-height));
      border-right: 1px solid var(--dr-border);
      & .panel-header {
        background-color: #fcfcfc;
        box-sizing: border-box;
        border-bottom: 1px solid var(--dr-border);
        font-size: 12px;
        line-height: 40px;
        height: 40px;
        align-self: center;
        padding-left: 8px;
        // 关闭按钮
        & .close {
          position: absolute;
          height: 40px;
          line-height: 40px;
          right: 16px;
          top: 0px;

          & :hover {
            cursor: pointer;
            color: var(--dr-prmary);
          }
        }
      }

      & .panel-body {
        background-color: #f8f8f8;
        overflow-y: auto;
      }
    }

    & .canvas {
      display: grid;
      background-color: #f5f7fa;
      grid-template-rows: auto;

      & .canvas-main {
        // 减去header 高度
        height: calc(100vh - var(--dr-designer-header-height));
        // 使用了el-scroll
        overflow: hidden;

        & .chart-wrapper {
          background-color: white;
          height: 100%;
          width: 100%;
        }
      }
    }

    & .right-panel {
      background-color: white;
      border-left: 1px solid var(--dr-border);
    }
  }

  .right-panel-tool-anchor {
    position: fixed;
    box-sizing: border-box;
    right: 329px;
    top: 50%;
    transform: translateY(-50%);
    width: 16px;
    height: 50px;
    background-color: white;
    color: var(--dr-text);
    border-radius: 5px 0 0 5px;
    border-top: 1px solid var(--dr-border);
    border-left: 1px solid var(--dr-border);
    border-bottom: 1px solid var(--dr-border);

    &:hover {
      cursor: pointer;
      color: var(--dr-prmary);
      background-color: var(--dr-prmary1);
    }
  }
}
</style>
