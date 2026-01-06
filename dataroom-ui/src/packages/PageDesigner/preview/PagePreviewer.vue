<script setup lang="ts">
import { getComponent, getComponentInstance } from '@DrPackage/components/AutoInstall.ts'
import { ref, provide, onMounted } from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout-v3'
import type { ChartConfig } from '@DrPackage/components/type/define.ts'
import {pageApi} from "@/packages/page/api.ts";
import type {GlobalVariable, PageBasicConfig, PageStageEntity} from "@/packages/_common/_type.ts";
import {useRoute} from "vue-router";

const pageStageEntity = ref<PageStageEntity>()
const chartList = ref<ChartConfig<unknown>[]>([])
const basicConfig = ref<PageBasicConfig>({} as PageBasicConfig)
const globalVariable = ref<GlobalVariable[]>([] as GlobalVariable[])
const route = useRoute()
onMounted(() => {
  // 获取路由中code 参数
  const code: string = route.params.pageCode as string
  const pageStatus: string = route.params.pageStatus as string
  // 根据编码获取页面详情
  pageApi.getPageConfig(code, pageStatus).then((res) => {
    pageStageEntity.value = res
    console.log(res)
    chartList.value = res.pageConfig?.chartList || []
    basicConfig.value = res.pageConfig?.basicConfig || {}
    globalVariable.value = res.pageConfig?.globalVariableList || []
  })
})

</script>

<template>
  <div class="dr-page-preview">
    <GridLayout
      v-model:layout="chartList"
      :col-num="48"
      :row-height="16"
      :is-draggable="false"
      :is-resizable="false"
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
      >
        <div class="chart-wrapper" :key="item.id" :id="item.id" :data-dr-id="item.id">
          <component :is="getComponent(item.type)" :chart="item"></component>
        </div>
      </GridItem>
    </GridLayout>
  </div>
</template>

<style scoped lang="scss">
.dr-page-preview {
  width: 100%;
  height: calc(100vh);
  background-color: #f5f7fa;

  & .chart-wrapper {
    background-color: white;
    height: 100%;
    width: 100%;
  }
}
</style>
