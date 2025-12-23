<script setup lang="ts">
import { getComponent, getComponentInstance } from '@DrPackage/components/install.ts'
import { computed, reactive, ref } from 'vue'
import type { BasicConfig } from '../components/type/define.ts'
import { menuList } from './componentMenuInstall.ts'

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
      gridTemplateColumns: '40px 200px auto 200px',
    }
  } else if (!leftToolPanelShow.value && !rightControlPanelShow.value) {
    return {
      gridTemplateColumns: '40px  auto ',
    }
  } else if (!leftToolPanelShow.value && rightControlPanelShow.value) {
    return {
      gridTemplateColumns: '40px auto 200px',
    }
  } else {
    return {
      gridTemplateColumns: '40px 200px auto',
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
</script>

<template>
  <div class="dr-bs-editor">
    <div class="header">标题 <el-button @click="rightControlPanelButton">配置</el-button></div>
    <div class="main" :style="mainStyle">
      <div class="left-tool-bar"><el-button @click="leftToolPanelButton">左侧</el-button></div>
      <div class="left-tool-panel" :style="leftToolPanelStyle"></div>
      <div class="canvas"></div>
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
    background-color: #3478f6;
    color: white;
  }

  & .main {
    background-color: aliceblue;
    display: grid;
    grid-template-columns: 40px 200px auto 200px;

    & .left-tool-bar {
      background-color: #fcfcfc;
    }

    & .left-tool-panel {
      background-color: white;
    }

    & .canvas {
      background-color: #e6e6e6;
    }

    & .right-panel {
      background-color: white;
    }
  }
}
</style>
