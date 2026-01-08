<script lang="ts">
import {defineComponent} from 'vue'
import {DrConst} from '@/packages/_common/_constant.ts'

export default defineComponent({
  name: DrConst.THIS_PLUGIN_TYPE,
})
</script>
<script setup lang="ts">
import type {DrTextConfig} from './install.ts'
import {onMounted, onBeforeUnmount, computed, ref} from "vue";
import type {ChartAction, IComponentLifecycle} from '@/packages/components/type/define.ts'
import {datasetApi} from "@/packages/dataset/api.ts";

const {chart} = defineProps<{
  chart: DrTextConfig
}>()

// 实现必须的方法
const init = () => {
  console.log("初始化文本组件")
  // 组件初始化逻辑
  autoRefreshData()
}

const fillDatasetParams = () => {
  // 根据当前chart配置的数据集字段的来源，生成参数


}

const autoRefreshData = () => {
  console.log("加载文本组件",chart.dataset?.code.trim().length)
  if (!chart.dataset?.code) {
    console.log("组件 %s 未配置数据集", chart.id)
    return
  }
  // 生成参数
  datasetApi.run4Chart({
    datasetCode: chart.dataset.code,
    paramMap: {},
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

const triggerAction = (action: ChartAction) => {
  console.log("触发文本组件行为", action)
  // 行为逻辑（可选）
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
  destroy
})

const textValue = ref('')
</script>

<template>
  <div class="dr-text">{{ textValue || chart.props.text }}</div>
</template>

<style scoped>
.dr-text {
  width: 100%;
  height: 100%;
}
</style>
