<script setup lang="ts">
import {getComponent, getComponentInstance, getPanelComponent} from '@DrPackage/components/AutoInstall.ts'
import {type CSSProperties, nextTick, onMounted, reactive} from 'vue'
import {type Component, computed, defineAsyncComponent, ref, shallowRef, provide} from 'vue'
import {GridLayout, GridItem} from 'vue-grid-layout-v3'
import {v4 as uuidv4} from 'uuid'
import type {ChartConfig} from '../components/type/define.ts'
import {getChartById} from '@/packages/_common/_utils.ts'
import type {CanvasInst, GlobalVariable, LeftToolBar, PageBasicConfig, PageStageEntity} from '@/packages/_common/_type.ts'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {DrConst} from '@/packages/_common/_constant.ts'
import {pageApi} from "@/packages/page/api.ts";

const ContextMenu = defineAsyncComponent(() => import('@/packages/PageDesigner/ContextMenu.vue'))
const router = useRouter()
const activeChart = ref<ChartConfig<unknown>>()
const componentLibRef = ref(null)
const pageStageEntity = ref<PageStageEntity>()
const chartList = ref<ChartConfig<unknown>[]>([])
const basicConfig = ref<PageBasicConfig>({} as PageBasicConfig)
const globalVariable = ref<GlobalVariable>({} as GlobalVariable)

const addChart = (type: string) => {
  const chartInst: ChartConfig<unknown> = getComponentInstance(type)
  chartInst.i = uuidv4()
  chartInst.id = chartInst.i
  chartInst.x = 0
  chartInst.y = 0
  chartInst.w = 5
  chartInst.h = 5
  chartList.value.push(chartInst)
}
const leftToolPanelShow = ref(true)
const rightControlPanelShow = ref(true)
// 记录右侧控制面板是否为页面配置
const rightControlPanelSetting = ref(true)
const ControlPanelWrapper = defineAsyncComponent(() => import('@/packages/_components/ControlPanel.vue'))
const ControlPanel = defineAsyncComponent(() => import('@/packages/PageDesigner/ControlPanel.vue'))
const ComponentLib = defineAsyncComponent(() => import('@/packages/_components/ComponentLib.vue'))
const ComponentLayer = defineAsyncComponent(() => import('@/packages/_components/ComponentLayer.vue'))
const GlobalVariableComponent = defineAsyncComponent(() => import('@/packages/_components/GlobalVariable.vue'))
const ResourceLib = defineAsyncComponent(() => import('@/packages/_components/ResourceLib.vue'))

const leftToolBarList: Array<LeftToolBar> = reactive([
  {
    name: '图层',
    desc: '图层',
    component: shallowRef<Component>(ComponentLayer),
    componentName: 'ComponentLayer',
  },
  {
    name: '组件',
    desc: '组件库',
    component: shallowRef<Component>(ComponentLib),
    componentName: 'ComponentLib',
  },
  {
    name: '素材',
    desc: '素材库',
    component: shallowRef<Component>(ResourceLib),
    componentName: 'ResourceLib',
  },
  {
    name: '变量',
    desc: '全局变量',
    component: shallowRef<Component>(GlobalVariableComponent),
    componentName: 'GlobalVariable',
  },
])
// @ts-expect-error ignore
const activeLeftToolBar = ref<LeftToolBar>(leftToolBarList[1])
/**
 * 激活组件
 * @param id
 */
const activeChartById = (id: string) => {
  const chart: ChartConfig<unknown> = getChartById(id, chartList.value)
  activeChart.value = chart
  rightControlPanelSetting.value = false
}

/**
 * 右侧配置面板开关
 * @param open
 */
const switchRightControlPanel = (open: boolean = true) => {
  rightControlPanelShow.value = open
}
const switchPageControlPanel = () => {
  switchRightControlPanel(true)
  rightControlPanelSetting.value = true
}

/**
 * 子组件注入使用
 */
const canvasInst = reactive<CanvasInst>({
  addChart: addChart,
  chartList: chartList,
  activeChartById: activeChartById,
  switchRightControlPanel: switchRightControlPanel,
  onChartDeleteClick: (chartId: string) => {
    chartList.value = chartList.value.filter((item) => item.id != chartId)
  },
})
provide(DrConst.CANVAS_INST, canvasInst)

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
 * 左侧工具面板开关
 * @param open
 */
const switchLeftToolPanel = (open: boolean = true) => {
  leftToolPanelShow.value = open
}

const componentLibVisible = ref(false)
const resourceLibVisible = ref(false)
const globalVariableVisible = ref(false)
const contextMenuVisible = ref(false)
/**
 * 左侧工具面版激活
 * @param leftToolBar
 */
const onActiveLeftToolBar = (leftToolBar: LeftToolBar) => {
  if (leftToolBar.componentName == 'ResourceLib') {
    resourceLibVisible.value = false
    nextTick(() => {
      resourceLibVisible.value = true
    })
    return
  } else if (leftToolBar.componentName == 'GlobalVariable') {
    globalVariableVisible.value = false
    nextTick(() => {
      globalVariableVisible.value = true
    })
    return
  }
  activeLeftToolBar.value = leftToolBar
  switchLeftToolPanel(true)
}
/**
 * 计算组件坐标样式
 * @param chart
 */
const computedChartStyle = (chart: ChartConfig<unknown>): CSSProperties => {
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
  const chart: ChartConfig<unknown> = getChartById(i, chartList.value)
  chart.w = newW
  chart.h = newH
}

const onMove = (i: string, newX: number, newY: number) => {
  console.log('onMove i=' + i + ', X=' + newX + ', Y=' + newY)
}

const onMoved = (i: string, newX: number, newY: number) => {
  console.log('onMoved i=' + i + ', X=' + newX + ', Y=' + newY)
  const chart: ChartConfig<unknown> = getChartById(i, chartList.value)
  chart.x = newX
  chart.y = newY
}

/**
 * 图表点击
 * @param chart
 */
const onChartClick = (chart: ChartConfig<unknown>) => {
  console.log('onChartClick', chart)
  activeChartById(chart.id)
}

const contextMenuEvent = ref<MouseEvent>()
const onRightClick = (e: MouseEvent, chart: ChartConfig<unknown>) => {
  e.preventDefault()
  contextMenuVisible.value = false
  contextMenuEvent.value = e
  activeChartById(chart.id)
  nextTick(() => {
    contextMenuVisible.value = true
  })
}
/**
 * 右击菜单样式
 */
const computedContextMenuStyle = computed(() => {
  return {
    position: 'absolute',
    left: contextMenuEvent.value?.clientX + 'px',
    top: contextMenuEvent.value?.clientY + 'px',
  }
})

/**
 * 页面预览
 */
const onPreview = () => {
  console.log('onPreview')
  localStorage.setItem('chartList', JSON.stringify(chartList.value))
  // 跳转到 /dataRoom/pagePreviewer 路由
  const routeData = router.resolve({
    path: '/dataRoom/pagePreviewer',
    query: {code: 'test'},
  })
  console.log('预览跳转', routeData.href)
  window.open(routeData.href, '_blank')
}

const onSave = () => {
  if (!pageStageEntity.value) {
    ElMessage({
      message: '页面信息未加载',
      type: 'error',
    })
    return
  }
  pageApi.updatePageConfig({
    ...pageStageEntity.value,
    pageConfig: {
      ...pageStageEntity.value.pageConfig,
      chartList: chartList.value,
      basicConfig: basicConfig.value
    }
  }).then((res) => {
    ElMessage({
      message: '保存成功',
      type: 'success',
    })
  })
}

onMounted(() => {
  // 获取路由中code 参数
  const code: string = router.currentRoute.value.query.code as string
  // 根据编码获取页面详情
  pageApi.getPageConfig(code, "design").then((res) => {
    pageStageEntity.value = res
    console.log(res)
    chartList.value = res.pageConfig?.chartList || []
    basicConfig.value = res.pageConfig?.basicConfig || {}
    globalVariable.value = res.pageConfig?.globalVariableList || []
  })
})
</script>

<template>
  <div class="dr-page-designer">
    <div class="header" ref="titleRef">
      <div class="header-left">
        <img src="@/assets/logo-small.png" alt="logo" class="logo" @click="router.push('/dataRoom/page/index')"/>
        <div class="title">{{ pageStageEntity?.name }}</div>
      </div>
      <div style="margin-right: 8px">
        <el-button @click="switchPageControlPanel" size="small">历史</el-button>
        <el-button @click="switchPageControlPanel" size="small">设置</el-button>
        <el-button @click="onPreview" size="small">预览</el-button>
        <el-button @click="onSave" size="small" type="primary">保存</el-button>
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
              <Close/>
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
            <GridLayout v-model:layout="chartList" :col-num="12" :row-height="30" :is-draggable="true" :is-resizable="true" :vertical-compact="true" :use-css-transforms="true">
              <GridItem
                v-for="item in chartList"
                :key="item.id"
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
                @contextmenu="(e: MouseEvent) => onRightClick(e, item)"
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
        <el-scrollbar>
          <ControlPanel v-if="rightControlPanelSetting" :basicConfig="basicConfig"></ControlPanel>
          <ControlPanelWrapper v-else :chart="activeChart!">
            <component :is="getPanelComponent(activeChart?.type)" :chart="activeChart"></component>
          </ControlPanelWrapper>
        </el-scrollbar>
      </div>
      <el-icon class="right-panel-tool-anchor" @click="switchRightControlPanel(!rightControlPanelShow)" :style="computedToolAnchorStyle">
        <ArrowRight v-if="rightControlPanelShow"/>
        <ArrowLeft v-else/>
      </el-icon>
    </div>
  </div>
  <ComponentLib v-if="componentLibVisible" ref="componentLibRef"></ComponentLib>
  <ResourceLib v-if="resourceLibVisible" ref="resourceLibRef"></ResourceLib>
  <GlobalVariableComponent v-if="globalVariableVisible" ref="globalVariableRef" :globalVariable="globalVariable"></GlobalVariableComponent>
  <ContextMenu v-if="contextMenuVisible" ref="contextMenuRef" :style="computedContextMenuStyle" :chart="activeChart"></ContextMenu>
</template>

<style scoped lang="scss">
@use './assets/index.scss';
// 拖拽背景样式
:deep(.vue-grid-item.vue-grid-placeholder) {
  background: var(--dr-primary);
}

.dr-page-designer {
  display: grid;
  grid-template-rows: var(--dr-designer-header-height) auto;
  height: 100vh; // 设置容器高度为视口高度
  & .header {
    background-color: white;
    color: var(--dr-text1);
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid var(--dr-border);

    & .header-left {
      display: flex;
      align-items: center;
      margin-left: 8px;

      & .logo {
        height: 30px;
        object-fit: contain;
        cursor: pointer;
      }

      & .title {
        margin-left: 16px;
        font-size: 14px;
      }
    }
  }

  & .main {
    background-color: var(--dr-bg2);
    display: grid;
    grid-template-columns: var(--dr-designer-left-tool-bar-width) var(--dr-designer-left-tool-panel-width) auto var(--dr-designer-right-panel-width);

    & .left-tool-bar {
      background-color: #fff;
      border-right: 1px solid var(--dr-border);

      & .bar {
        font-size: 12px;
        text-align: center;
        margin: 4px auto;
        padding: 8px 0;

        &:hover {
          cursor: pointer;
          background-color: var(--dr-bg2);
        }
      }

      & .active {
        background-color: var(--dr-primary1);
        color: var(--dr-primary);
        position: relative;

        &::before {
          content: '';
          left: 0;
          top: 0;
          bottom: 0;
          width: 3px;
          position: absolute;
          background-color: var(--dr-primary);
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
        background-color: var(--dr-bg2);
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
            color: var(--dr-primary);
          }
        }
      }

      & .panel-body {
        background-color: white;
        overflow-y: hidden;
        padding: 8px 4px 16px 4px;
      }
    }

    & .canvas {
      display: grid;
      background-color: var(--dr-bg2);
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
      height: calc(100vh - var(--dr-designer-header-height));
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
      color: var(--dr-primary);
      background-color: var(--dr-primary1);
    }
  }
}
</style>
