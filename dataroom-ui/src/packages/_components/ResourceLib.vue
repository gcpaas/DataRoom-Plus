<!-- 素材库 -->
<script setup lang="ts">
import { inject, ref } from 'vue'
import type { CanvasInstInterface } from '@/packages/_common/_type.ts'
import { DrConst } from '@/packages/_common/_constant.ts'
import ResourceManage from '@/packages/resource/index.vue'
import type { ResourceEntity } from '@/packages/resource/api'

const canvasInst = inject(DrConst.CANVAS_INST) as CanvasInstInterface

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
    console.log('添加素材到画布:', item)
    canvasInst.addChart('DrImage')
  })
  resourceLibVisible.value = false
}
</script>
<template>
  <el-dialog v-model="resourceLibVisible" title="素材库" width="80%">
    <div class="resource-lib-wrapper">
      <ResourceManage :selectable="true" @update:selectedResources="handleSelectedResourcesUpdate" />
    </div>
    <template #footer>
      <el-button @click="onClose">取消</el-button>
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
