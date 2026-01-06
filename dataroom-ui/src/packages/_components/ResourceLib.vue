<!-- 素材库 -->
<script setup lang="ts">
import { inject, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { CanvasInst } from '@/packages/_common/_type.ts'
import { DrConst } from '@/packages/_common/_constant.ts'
import ResourceManage from '@/packages/resource/index.vue'
import type { ResourceEntity } from '@/packages/resource/api'
import type {DrImageConfig, DrImagePropsInterface} from "@/packages/components/DrImage/install.ts";

const canvasInst = inject(DrConst.CANVAS_INST) as CanvasInst

const resourceLibVisible = ref(true)
const selectedResources = ref<ResourceEntity[]>([])

/**
 * 处理选中资源的更新
 * @param resources
 */
const handleSelectedResourcesUpdate = (resources: ResourceEntity[]) => {
  selectedResources.value = resources
}

/**
 * 取消
 */
const onClose = () => {
  resourceLibVisible.value = false
}

/**
 * 确定添加素材到画布
 */
const onConfirm = () => {
  if (selectedResources.value.length === 0) {
    return
  }
  // 遍历选中的素材,添加到画布
  selectedResources.value.forEach(item => {
    const chartInst = canvasInst.addChart('DrImage') as DrImageConfig
    chartInst.props.url = item.url!
  })
  resourceLibVisible.value = false
}

/**
 * 复制素材地址到剪贴板
 */
const onCopyUrl = async () => {
  if (selectedResources.value.length === 0) {
    ElMessage.warning('请先选择素材')
    return
  }
  const url = selectedResources.value[0].url
  if (!url) {
    ElMessage.error('该素材没有地址信息')
    return
  }
  try {
    // 使用Clipboard API复制到剪贴板
    await navigator.clipboard.writeText(url)
    ElMessage.success('地址已复制到剪贴板')
    resourceLibVisible.value = false
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败,请手动复制')
  }
}
</script>
<template>
  <el-dialog v-model="resourceLibVisible" title="素材库" width="80%">
    <div class="resource-lib-wrapper">
      <ResourceManage :selectable="true" :single-select="true" @update:selectedResources="handleSelectedResourcesUpdate" />
    </div>
    <template #footer>
      <el-button @click="onClose">取消</el-button>
      <el-button type="success" @click="onCopyUrl">复制地址</el-button>
      <el-button type="primary" @click="onConfirm">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.resource-lib-wrapper {
  height: 600px;
  overflow: hidden;

  // 调整资源组件在对话框中的样式
  :deep(.dr-resource) {
    height: 100%;
    overflow: hidden;
  }

  :deep(.resource-header) {
    flex-shrink: 0;
  }

  :deep(.resource-content) {
    flex: 1;
    height: 0;
    overflow: hidden;
  }

  :deep(.el-scrollbar) {
    height: 100%;
  }

  :deep(.el-scrollbar__view) {
    height: 100%;
  }
}
</style>
