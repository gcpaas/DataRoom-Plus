<!-- 控制面板 -->
<script setup lang="ts">
import {computed, ref, watch, defineAsyncComponent} from 'vue'
import type {Behavior, ChartConfig} from '../components/type/define.ts'
import {Pointer, Setting} from "@element-plus/icons-vue";
import {getComponentBehaviors, getComponentDatasetFields} from "@/packages/components/AutoInstall.ts";
import type {DatasetEntity} from '@/packages/dataset/api'
import {datasetApi} from '@/packages/dataset/api'

/**
 * 懒加载数据集管理页面
 */
const DatasetManage = defineAsyncComponent(() => import('@/packages/dataset/index.vue'))
/**
 * 懒加载交互配置对话框
 */
const BehaviorConfigDialog = defineAsyncComponent(() => import('./BehaviorConfigDialog.vue'))

const {chart} = defineProps<{
  chart: ChartConfig<unknown>
}>()

// 默认激活样式tab
const activeTab = ref('style')
const chartConfig = computed(() => chart)

// 数据集选择对话框
const datasetDialogVisible = ref(false)
const selectedDataset = ref<DatasetEntity | null>(null)
const datasetName = ref('')
const datasetFields = ref<any[]>([])
const behaviors = ref<Behavior[]>([])
const behaviorConfigDialogVisible = ref(false)
const currentBehavior = ref<Behavior | null>(null)

/**
 * 初始化组件相关数据
 * 根据组件类型获取对应的数据集字段、行为等属性
 */
const initComponentData = () => {
  datasetFields.value = getComponentDatasetFields(chart.type)
  behaviors.value = getComponentBehaviors(chart.type)

  console.log('datasetFields', datasetFields.value)
  console.log('behaviors', behaviors)

  // 加载数据集名称
  loadDatasetName()
}

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
    console.log(selectedDataset.value)
    // 更新数据集名称到输入框
    datasetName.value = selectedDataset.value.name
    // 保存数据集编码到chartConfig
    chartConfig.value.dataset = selectedDataset.value.code
    datasetDialogVisible.value = false
  }
}

/**
 * 检查交互行为是否启用
 * @param behavior
 */
const isBehaviorEnabled = (behavior: Behavior): boolean => {
  if (!chartConfig.value.behaviors) {
    return false
  }
  const behaviorConfig = chartConfig.value.behaviors[behavior.method]
  return behaviorConfig && !behaviorConfig.disabled
}

/**
 * 切换交互行为开关
 * @param behavior
 * @param enabled
 */
const toggleBehavior = (behavior: Behavior, enabled: boolean) => {
  if (!chartConfig.value.behaviors) {
    chartConfig.value.behaviors = {}
  }
  if (!chartConfig.value.behaviors[behavior.method]) {
    chartConfig.value.behaviors[behavior.method] = {
      disabled: !enabled,
      actions: []
    }
  } else {
    chartConfig.value.behaviors[behavior.method].disabled = !enabled
  }
}

/**
 * 打开交互行为配置对话框
 * @param behavior
 */
const openBehaviorConfig = (behavior: Behavior) => {
  currentBehavior.value = behavior
  behaviorConfigDialogVisible.value = true
}

/**
 * 监听组件实例变化
 * 当chart.id改变时，说明切换了不同的组件实例，需要重新初始化
 * immediate: true 确保首次进入时也会执行
 */
watch(
  () => chart.id,
  () => {
    initComponentData()
  },
  {immediate: true}
)

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
                :suffix-icon="Pointer"
                readonly
                @click="openDatasetDialog"
                style="cursor: pointer;"
              ></el-input>
            </el-form-item>
            <el-form-item v-for="field in datasetFields" :key="field.name" :label="field.desc">
              <el-input v-model="chartConfig.title" placeholder="请选择数据集" :suffix-icon="Pointer"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane label="交互" name="interaction">
        <div class="tab-content">
          <div class="behavior-list">
            <div class="behavior-item" v-for="behavior in behaviors" :key="behavior.name">
              <div class="behavior-info">
                <div class="behavior-name">{{ behavior.name }}</div>
                <div class="behavior-desc">{{ behavior.desc }}</div>
              </div>
              <div class="behavior-controls">
                <el-switch
                  :model-value="isBehaviorEnabled(behavior)"
                  @change="(val: boolean) => toggleBehavior(behavior, val)"
                />
                <el-icon class="setting-icon" @click="openBehaviorConfig(behavior)">
                  <Setting />
                </el-icon>
              </div>
            </div>
          </div>
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
        <DatasetManage :selectable="true" @update:selectedDataset="handleDatasetSelect"/>
      </div>
      <template #footer>
        <el-button @click="handleCancelDataset">取消</el-button>
        <el-button type="primary" @click="handleConfirmDataset">确定</el-button>
      </template>
    </el-dialog>

    <!-- 交互配置对话框 -->
    <BehaviorConfigDialog
      v-if="behaviorConfigDialogVisible && currentBehavior"
      v-model="behaviorConfigDialogVisible"
      :behavior="currentBehavior"
      :chart="chartConfig"
    />
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

    .behavior-list {
      .behavior-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 12px;
        margin-bottom: 16px;
        background: var(--dr-bg2);
        border-radius: 4px;
        border: 1px solid var(--dr-border);

        .behavior-info {
          flex: 1;
          min-width: 0;
          margin-right: 12px;

          .behavior-name {
            font-size: 14px;
            font-weight: 500;
            color: var(--dr-text);
            margin-bottom: 4px;
          }

          .behavior-desc {
            font-size: 12px;
            color: var(--el-text-color-secondary);
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }

        .behavior-controls {
          display: flex;
          align-items: center;
          gap: 12px;

          .setting-icon {
            font-size: 18px;
            color: var(--el-text-color-regular);
            cursor: pointer;
            transition: color 0.3s;

            &:hover {
              color: var(--dr-primary);
            }
          }
        }
      }
    }
  }
}

// 数据集选择对话框样式
.dataset-dialog-wrapper {
  height: calc(70vh - 120px);
  overflow: hidden;

  // 调整数据集组件在对话框中的样式
  :deep(.dr-dataset) {
    height: 100%;
    overflow: hidden;
  }

  :deep(.dataset-left) {
    flex-shrink: 0;
    height: 100%;
  }

  :deep(.dataset-left .tree-content) {
    flex: 1;
    overflow: hidden;
  }

  :deep(.dataset-left .tree-content .el-scrollbar__wrap) {
    overflow-x: hidden;
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
    z-index: 10 !important;
  }
}
</style>
