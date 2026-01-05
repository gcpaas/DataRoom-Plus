<!-- 控制面板 -->
<script setup lang="ts">
import {computed, ref, watch} from 'vue'
import type {ChartConfigInterface} from '../components/type/define.ts'
import {Search} from "@element-plus/icons-vue";
import {getComponentBehaviors, getComponentDatasetFields} from "@/packages/components/AutoInstall.ts";

const {chart} = defineProps<{
  chart: ChartConfigInterface<unknown>
}>()
const activeTab = ref('style')
const chartConfig = computed(() => chart)

const datasetFields = getComponentDatasetFields(chart.type);
console.log('datasetFields',datasetFields)

watch(() => chart.id, () => {
  const behaviors = getComponentBehaviors(chart.type)
  let datasetFields = getComponentDatasetFields(chart.type);
  console.log('datasetFields',datasetFields)
  console.log('behaviors', behaviors)
})
</script>

<template>
  <div class="control-panel">
    <el-tabs v-model="activeTab" class="control-tabs">
      <el-tab-pane label="样式" name="style">
        <div class="tab-content">
          <el-form label-width="100px" label-position="left" size="small">
            <el-form-item label="标题/图层">
              <el-input v-model="chartConfig.title"></el-input>
            </el-form-item>
          </el-form>
          <slot></slot>
        </div>
      </el-tab-pane>
      <el-tab-pane label="数据" name="data">
        <div class="tab-content">
          <el-form label-width="100px" label-position="left" size="small">
            <el-form-item label="数据集">
              <el-input v-model="chartConfig.title" placeholder="请选择数据集" :suffix-icon="Search"></el-input>
            </el-form-item>
            <el-form-item v-for="field in datasetFields"  :label="field.desc">
              <el-input v-model="chartConfig.title" placeholder="请选择数据集" :suffix-icon="Search"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane label="交互" name="interaction">
        <div class="tab-content">
          <div class="placeholder">交互配置开发中...</div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped lang="scss">
.control-panel {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;

  .control-tabs {
    flex: 1;
    display: flex;
    flex-direction: column;

    :deep(.el-tabs__header) {
      margin-bottom: 0;
      border-bottom: none;
      padding: 0 16px;
    }

    :deep(.el-tabs__nav-wrap)::after {
      display: none;
    }

    :deep(.el-tabs__content) {
      flex: 1;
      overflow: hidden;
    }

    .tab-content {
      padding: 16px;
      height: 100%;
      overflow-y: auto;

      .placeholder {
        color: var(--el-text-color-secondary);
        text-align: center;
        padding: 40px 0;
      }
    }
  }
}
</style>
