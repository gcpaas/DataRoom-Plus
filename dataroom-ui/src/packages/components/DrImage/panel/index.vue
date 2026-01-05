<script lang="ts">
import { defineComponent } from 'vue'
import { DrConst } from '@/packages/_common/_constant.ts'

export default defineComponent({
  name: DrConst.THIS_PLUGIN_TYPE + 'ControlPanel',
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
      <el-form-item label="URL">
        <el-input v-model="chartConfig.props.url" />
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped></style>
