<!-- 控制面板 -->
<script setup lang="ts">
import {ref} from 'vue'

const activeTab = ref('style')
</script>

<template>
  <div class="control-panel">
    <el-tabs v-model="activeTab" class="control-tabs">
      <el-tab-pane label="样式" name="style">
        <div class="tab-content">
          <slot></slot>
        </div>
      </el-tab-pane>
      <el-tab-pane label="数据" name="data">
        <div class="tab-content">
          <div class="placeholder">数据配置开发中...</div>
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
