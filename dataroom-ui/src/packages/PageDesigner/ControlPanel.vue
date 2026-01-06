<!-- 控制面板 -->
<script setup lang="ts">
import {computed, defineAsyncComponent, reactive, ref} from 'vue'
import type {PageBasicConfig} from "@/packages/_common/_type.ts";
import {resourceApi} from '@/packages/resource/api'
import {ElMessage, ElMessageBox} from 'element-plus'
import {getCookie, getCookieName} from '@/packages/_common/_cookie'
import {Delete, Picture, Plus, Setting} from '@element-plus/icons-vue'

/**
 * 懒加载定时器配置对话框
 */
const TimerConfigDialog = defineAsyncComponent(() => import('./TimerConfigDialog.vue'))

// Props
const { basicConfig }  = defineProps<{
  basicConfig: PageBasicConfig
}>()

// 定时器接口定义
interface Timer {
  id: string
  name: string
  enabled: boolean
  interval: number // 间隔时间（毫秒）
  actions: TimerAction[]
}

interface TimerAction {
  name: string
  type: 'code'
  code: string
}

// 定时器列表
const timers = computed(() => {
  if (!basicConfig.timers) {
    basicConfig.timers = []
  }
  return basicConfig.timers
})

// 定时器配置对话框
const timerConfigDialogVisible = ref(false)
const currentTimer = ref<Timer | null>(null)
const currentTimerIndex = ref<number>(-1)

// 默认激活配置tab
const activeTab = ref('config')

// 上传相关
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL
const resourceBaseUrl = import.meta.env.VITE_RESOURCE_BASE_URL
const uploadUrl = `${apiBaseUrl}/dataRoom/resource/upload`
const cookieName = getCookieName()
const cookieValue = getCookie(cookieName)
const uploadHeaders = reactive({
  [cookieName]: cookieValue
})

// 背景图上传成功
const handleBgUploadSuccess = (response: any) => {
  if (response && response.data) {
    basicConfig.background.url = response.data.url || ''
    ElMessage.success('背景图上传成功')
  }
}

// 上传失败
const handleUploadError = () => {
  ElMessage.error('上传失败，请重试')
}

// 获取完整的资源URL
const getResourceUrl = (url?: string) => {
  console.log('url = ' + url)
  if (!url) return ''
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  return `${resourceBaseUrl}${url}`
}

// 添加新定时器
const addTimer = () => {
  const timerCount = timers.value.length + 1
  const newTimer: Timer = {
    id: `timer_${Date.now()}`,
    name: `定时器${timerCount}`,
    enabled: false,
    interval: 5000,
    actions: []
  }
  timers.value.push(newTimer)
  ElMessage.success('已添加新的定时器')
}

// 切换定时器启用状态
const toggleTimer = (timer: Timer, enabled: boolean) => {
  timer.enabled = enabled
  ElMessage.success(enabled ? '定时器已启用' : '定时器已禁用')
}

// 打开定时器配置对话框
const openTimerConfig = (timer: Timer, index: number) => {
  currentTimer.value = timer
  currentTimerIndex.value = index
  timerConfigDialogVisible.value = true
}

// 删除定时器
const deleteTimer = (index: number) => {
  ElMessageBox.confirm(
    '确定要删除这个定时器吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      timers.value.splice(index, 1)
      ElMessage.success('定时器已删除')
    })
    .catch(() => {
      // 用户取消删除
    })
}
</script>

<template>
  <div class="control-panel">
    <el-tabs v-model="activeTab" class="control-tabs">
      <el-tab-pane label="配置" name="config">
        <el-scrollbar class="tab-scrollbar">
          <div class="tab-content">
            <el-form label-width="80px" label-position="left" size="small" v-if="basicConfig.background">
              <!-- 背景填充方式 -->
              <el-form-item label="背景填充">
                <el-radio-group v-model="basicConfig.background.fill">
                  <el-radio value="color">颜色</el-radio>
                  <el-radio value="image">图片</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="背景颜色" v-if="basicConfig.background.fill === 'color'">
                <el-color-picker v-model="basicConfig.background.color" show-alpha></el-color-picker>
              </el-form-item>
              <template v-if="basicConfig.background.fill === 'image'">
                <el-form-item label="背景图片">
                  <div class="bg-upload-section">
                    <el-upload
                      :action="uploadUrl"
                      :headers="uploadHeaders"
                      :on-success="handleBgUploadSuccess"
                      :on-error="handleUploadError"
                      :show-file-list="false"
                      accept="image/*"
                      class="bg-uploader"
                    >
                      <div class="bg-preview-box">
                        <el-image
                          v-if="basicConfig.background.url"
                          :src="getResourceUrl(basicConfig.background.url)"
                          fit="contain"
                          class="bg-image"
                          lazy
                        >
                          <template #error>
                            <div class="bg-placeholder">
                              <el-icon>
                                <Picture/>
                              </el-icon>
                              <span>加载失败</span>
                            </div>
                          </template>
                        </el-image>
                        <div v-else class="bg-placeholder">
                          <el-icon>
                            <Picture/>
                          </el-icon>
                          <span>点击上传背景图</span>
                        </div>
                      </div>
                    </el-upload>
                  </div>
                </el-form-item>

                <el-form-item label="透明度">
                  <el-input-number v-model="basicConfig.background.opacity" :min="0" :max="100" :step="1" controls-position="right" style="width: 100%"/>
                </el-form-item>

                <el-form-item label="填充方式">
                  <el-select v-model="basicConfig.background.repeat" placeholder="请选择填充方式">
                    <el-option label="不重复" value="no-repeat"></el-option>
                    <el-option label="重复" value="repeat"></el-option>
                    <el-option label="水平重复" value="repeat-x"></el-option>
                    <el-option label="垂直重复" value="repeat-y"></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-form>
          </div>
        </el-scrollbar>
      </el-tab-pane>
      <el-tab-pane label="交互" name="interaction">
        <el-scrollbar class="tab-scrollbar">
          <div class="tab-content">
            <div class="timer-header">
              <span class="timer-title">定时器列表</span>
              <el-button type="primary" size="small" @click="addTimer">
                <el-icon><Plus /></el-icon>
                添加定时器
              </el-button>
            </div>
            <div class="timer-list">
              <div class="timer-item" v-for="(timer, index) in timers" :key="timer.id">
                <div class="timer-info">
                  <div class="timer-name">{{ timer.name }}</div>
                  <div class="timer-desc">间隔：{{ timer.interval }}ms</div>
                </div>
                <div class="timer-controls">
                  <el-switch
                    v-model="timer.enabled"
                    size="small"
                    @change="(val: boolean) => toggleTimer(timer, val)"
                  />
                  <el-icon class="setting-icon" @click="openTimerConfig(timer, index)">
                    <Setting />
                  </el-icon>
                  <el-icon class="delete-icon" @click="deleteTimer(index)">
                    <Delete />
                  </el-icon>
                </div>
              </div>
              <div v-if="timers.length === 0" class="empty-timer">
                <el-empty description="暂无定时器，请点击上方按钮添加" :image-size="80" />
              </div>
            </div>
          </div>
        </el-scrollbar>
      </el-tab-pane>
    </el-tabs>

    <!-- 定时器配置对话框 -->
    <TimerConfigDialog
      v-if="timerConfigDialogVisible && currentTimer"
      v-model="timerConfigDialogVisible"
      :timer="currentTimer"
      :timer-index="currentTimerIndex"
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
      background-color: var(--el-bg-color);
    }

    :deep(.el-tabs__nav-wrap) {
      &::after {
        display: none;
      }

      .el-tabs__nav-prev,
      .el-tabs__nav-next {
        display: none !important;
      }
    }

    :deep(.el-tabs__nav) {
      position: relative;
      width: 100%;
      background-color: var(--el-fill-color-lighter);
      border-radius: 8px;
      padding: 4px;
      display: flex;
      gap: 4px;
    }

    :deep(.el-tabs__item) {
      flex: 1;
      height: 32px;
      line-height: 32px;
      text-align: center;
      border-radius: 6px;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      font-size: 13px;
      color: var(--el-text-color-regular);
      padding: 0 12px;
      z-index: 1;

      &:hover {
        color: var(--el-color-primary);
      }

      &.is-active {
        color: var(--el-color-primary);
        background-color: var(--el-bg-color);
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        font-weight: 500;
      }
    }

    :deep(.el-tabs__active-bar) {
      display: none;
    }

    :deep(.el-tabs__content) {
      flex: 1;
      overflow: hidden;
    }

    .tab-scrollbar {
      height: 100%;

      :deep(.el-scrollbar__wrap) {
        overflow-x: hidden;
      }

      :deep(.el-scrollbar__bar) {
        z-index: 10 !important;
      }
    }

    .tab-content {
      padding: 16px;

      .placeholder {
        color: var(--el-text-color-secondary);
        text-align: center;
        padding: 40px 0;
      }

      .timer-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 16px;
        padding-bottom: 12px;
        border-bottom: 1px solid var(--el-border-color-lighter);

        .timer-title {
          font-size: 16px;
          font-weight: 500;
          color: var(--el-text-color-primary);
        }
      }

      .timer-list {
        .timer-item {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding: 12px;
          margin-bottom: 12px;
          background: var(--el-fill-color-light);
          border-radius: 6px;
          border: 1px solid var(--el-border-color-lighter);
          transition: all 0.3s;

          &:hover {
            background: var(--el-fill-color);
            border-color: var(--el-color-primary-light-7);
          }

          .timer-info {
            flex: 1;
            min-width: 0;
            margin-right: 12px;

            .timer-name {
              font-size: 14px;
              font-weight: 500;
              color: var(--el-text-color-primary);
              margin-bottom: 4px;
            }

            .timer-desc {
              font-size: 12px;
              color: var(--el-text-color-secondary);
            }
          }

          .timer-controls {
            display: flex;
            align-items: center;
            gap: 12px;

            .setting-icon,
            .delete-icon {
              font-size: 18px;
              cursor: pointer;
              transition: all 0.3s;
            }

            .setting-icon {
              color: var(--el-text-color-regular);

              &:hover {
                color: var(--el-color-primary);
                transform: rotate(90deg);
              }
            }

            .delete-icon {
              color: var(--el-color-danger);

              &:hover {
                transform: scale(1.2);
              }
            }
          }
        }

        .empty-timer {
          padding: 40px 0;
        }
      }

      .upload-section {
        width: 100%;

        .preview-image {
          margin-top: 12px;
          width: 100%;
          max-width: 200px;
          border: 1px solid var(--el-border-color);
          border-radius: 4px;
          overflow: hidden;

          img {
            width: 100%;
            height: auto;
            display: block;
          }
        }
      }
      .bg-upload-section {
        width: 100%;

        .bg-uploader {
          :deep(.el-upload) {
            width: 100%;
            display: block;
            cursor: pointer;
          }
        }

        .bg-preview-box {
          width: 100%;
          height: 160px;
          border: 1px dashed var(--el-border-color);
          border-radius: 4px;
          overflow: hidden;
          position: relative;
          transition: all 0.3s;
          padding: 16px;
          box-sizing: border-box;

          &:hover {
            border-color: var(--el-color-primary);
          }

          .bg-image {
            width: 100%;
            height: 100%;
            display: block;
          }

          .bg-placeholder {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            background-color: var(--el-fill-color-light);
            color: var(--el-text-color-secondary);
            font-size: 14px;

            .el-icon {
              font-size: 48px;
              margin-bottom: 8px;
              color: var(--el-text-color-placeholder);
            }
          }
        }
      }
    }
  }
}
</style>
