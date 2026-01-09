<script lang="ts">
import {defineComponent} from 'vue'
import {DrConst} from '@/packages/_common/_constant.ts'

export default defineComponent({
  name: DrConst.THIS_PLUGIN_TYPE,
})
</script>
<script setup lang="ts">
import type {DrTextConfig} from './install.ts'
import {onMounted, onBeforeUnmount, ref, inject, getCurrentInstance} from "vue";
import type {ChartAction, IComponentLifecycle} from '@/packages/components/type/define.ts'
import {datasetApi} from "@/packages/dataset/api.ts";
import type {CanvasInst, GlobalVariable} from '@/packages/_common/_type.ts'

const {chart} = defineProps<{
  chart: DrTextConfig
}>()


const canvasInst = inject(DrConst.CANVAS_INST) as CanvasInst

// 实现必须的方法
const init = () => {
  console.log("初始化文本组件")
  registerCurrentInstance()
  // 组件初始化逻辑
  autoRefreshData()
}

const autoRefreshData = () => {
  console.log("加载文本组件", chart.dataset?.code.trim().length)
  if (!chart.dataset?.code) {
    console.log("组件 %s 未配置数据集", chart.id)
    return
  }
  // 生成参数 - 使用通用工具函数
  const paramMap = canvasInst.fillDatasetParams(chart)
  console.log("参数", paramMap)
  datasetApi.run4Chart({
    datasetCode: chart.dataset.code,
    paramMap: paramMap,
    returnSingleValue: true
  }).then((res) => {
    changeData(res.data)
  })
}

const changeData = (datasetValue: any) => {
  const textFieldName = chart.dataset?.fields?.text ?? 'text'
  const text = datasetValue?.[textFieldName] ?? ''
  textValue.value = text
}

const destroy = () => {
  console.log("清理文本组件")
  // 清理逻辑（可选）
}

const triggerAction = (action: ChartAction, data: any) => {
  console.log("触发文本组件行为", action)
  // 行为逻辑（可选）
  if (action.name == 'autoRefreshData') {
    autoRefreshData()
    return
  }
  if (action.name == 'changeData') {
    changeData(data)
    return
  }
}

const currentInstance = getCurrentInstance()
const registerCurrentInstance = () => {
  console.log("注册文本组件实例")
  canvasInst.registerChartInstance(chart.id, currentInstance)
}
// 使用 onMounted 调用初始化
onMounted(() => {
  init()
})

onBeforeUnmount(() => {
  destroy()
})

// 暴露必须实现的方法，使用接口类型约束
defineExpose<IComponentLifecycle>({
  init,
  autoRefreshData,
  changeData,
  triggerAction,
  registerCurrentInstance:
  destroy
})

const textValue = ref('')

const onTextClick = () => {
  canvasInst.triggerChartBehavior(chart.id, 'click', {text: textValue.value})
}

</script>

<template>
  <div class="dr-text" :id="chart.id" @click="onTextClick">{{ textValue || chart.props.text }}</div>
</template>

<style scoped>
.dr-text {
  width: 100%;
  height: 100%;
}
</style>
