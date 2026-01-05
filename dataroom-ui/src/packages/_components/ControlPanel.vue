<!-- 控制面板 -->
<script setup lang="ts">
import {computed, ref, watch, defineAsyncComponent, onMounted} from 'vue'
import type {ChartConfigInterface} from '../components/type/define.ts'
import {Search} from "@element-plus/icons-vue";
import {getComponentBehaviors, getComponentDatasetFields} from "@/packages/components/AutoInstall.ts";
import type {DatasetEntity} from '@/packages/dataset/api'
import {datasetApi} from '@/packages/dataset/api'

// 懒加载数据集管理页面
const DatasetManage = defineAsyncComponent(() => import('@/packages/dataset/index.vue'))

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
  // 加载数据集名称
  loadDatasetName()
})

// 数据集选择对话框
const datasetDialogVisible = ref(false)
const selectedDataset = ref<DatasetEntity | null>(null)
const datasetName = ref('')

// 加载数据集名称
const loadDatasetName = async () => {
  if (chartConfig.value.dataset) {
    try {
      const detail = await datasetApi.detail(chartConfig.value.dataset)
      datasetName.value = detail.name
    } catch (error) {
      console.error('加载数据集名称失败:', error)
      datasetName.value = ''
    }
  } else {
    datasetName.value = ''
  }
}

// 打开数据集选择对话框
const openDatasetDialog = () => {
  datasetDialogVisible.value = true
}

// 处理数据集选择
const handleDatasetSelect = (dataset: DatasetEntity) => {
  selectedDataset.value = dataset
}

// 取消选择
const handleCancelDataset = () => {
  datasetDialogVisible.value = false
  selectedDataset.value = null
}

// 确认选择数据集
const handleConfirmDataset = () => {
  if (selectedDataset.value) {
    // 更新数据集名称到输入框
    datasetName.value = selectedDataset.value.name
    // 保存数据集编码到chartConfig
    chartConfig.value.dataset = selectedDataset.value.code
    datasetDialogVisible.value = false
  }
}

// 组件挂载时加载数据集名称
onMounted(() => {
  loadDatasetName()
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
              <el-input
                v-model="datasetName"
                placeholder="请选择数据集"
                :suffix-icon="Search"
                readonly
                @click="openDatasetDialog"
                style="cursor: pointer;"
              ></el-input>
            </el-form-item>
            <el-form-item v-for="field in datasetFields" :key="field.name" :label="field.desc">
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

    <!-- 数据集选择对话框 -->
    <el-dialog
      v-model="datasetDialogVisible"
      title="选择数据集"
      width="80%"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div class="dataset-dialog-wrapper">
        <DatasetManage :selectable="true" @update:selectedDataset="handleDatasetSelect" />
      </div>
      <template #footer>
        <el-button @click="handleCancelDataset">取消</el-button>
        <el-button type="primary" @click="handleConfirmDataset">确定</el-button>
      </template>
    </el-dialog>
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

// 数据集选择对话框样式
.dataset-dialog-wrapper {
  max-height: calc(70vh - 120px);
  overflow: hidden;

  // 调整数据集组件在对话框中的样式
  :deep(.dr-dataset) {
    height: 100%;
    overflow: hidden;
  }

  :deep(.dataset-left) {
    flex-shrink: 0;
  }

  :deep(.dataset-right) {
    flex: 1;
    min-width: 0;
    overflow: hidden;
  }

  :deep(.el-scrollbar) {
    height: 100%;
  }

  :deep(.el-scrollbar__view) {
    height: 100%;
  }

  :deep(.el-scrollbar__bar) {
    z-index: 10;
  }
}
</style>
