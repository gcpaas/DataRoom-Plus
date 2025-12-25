<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'DrImageControlPanel',
})
</script>
<script setup lang="ts">
import type { DrImageConfig } from '../install.ts'
import { computed } from 'vue'

const { chart } = defineProps<{
  chart: DrImageConfig
}>()
const chartConfig = computed(() => chart)
</script>
<template>
  <div>
    <el-form :model="chartConfig" label-width="auto">
      <el-form-item label="图层ID">
        <el-input v-model="chartConfig.id" />
      </el-form-item>
      <el-form-item label="图层名称">
        <el-input v-model="chartConfig.title" />
      </el-form-item>
      <el-form-item label="文本内容">
        <el-input v-model="chartConfig.props.text" />
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped></style>
