<!-- 控制面板 -->
<script setup lang="ts">
import { ref, reactive } from 'vue'
import type {PageBasicConfig} from "@/packages/_common/_type.ts";
import { resourceApi } from '@/packages/resource/api'
import { ElMessage } from 'element-plus'
import { getCookie, getCookieName } from '@/packages/_common/_cookie'
import { Picture } from '@element-plus/icons-vue'

// 默认激活配置tab
const activeTab = ref('config')

// 页面配置
const pageConfig = reactive<PageBasicConfig>({
  bgFill: 'color',
  bgColor: '#ffffff',
  bgUrl: '',
  bgRepeat: 'no-repeat'
})

// 缩略图
const thumbnail = ref('')

// 背景图透明度（0-100）
const bgOpacity = ref(100)

// 上传相关
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL
const resourceBaseUrl = import.meta.env.VITE_RESOURCE_BASE_URL
const uploadUrl = `${apiBaseUrl}/dataRoom/resource/upload`
const cookieName = getCookieName()
const cookieValue = getCookie(cookieName)
const uploadHeaders = reactive({
  [cookieName]: cookieValue
})

// 缩略图上传成功
const handleThumbnailUploadSuccess = (response: any) => {
  if (response && response.data) {
    thumbnail.value = response.data.url || ''
    ElMessage.success('缩略图上传成功')
  }
}

// 背景图上传成功
const handleBgUploadSuccess = (response: any) => {
  if (response && response.data) {
    pageConfig.bgUrl = response.data.url || ''
    ElMessage.success('背景图上传成功')
  }
}

// 上传失败
const handleUploadError = () => {
  ElMessage.error('上传失败，请重试')
}

// 获取完整的资源URL
const getResourceUrl = (url?: string) => {
  console.log('url = '+url)
  if (!url) return ''
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  return `${resourceBaseUrl}${url}`
}
</script>

<template>
  <div class="control-panel">
    <el-tabs v-model="activeTab" class="control-tabs">
      <el-tab-pane label="配置" name="config">
        <el-scrollbar class="tab-scrollbar">
          <div class="tab-content">
            <el-form label-width="100px" label-position="left" size="small">
              <!-- 页面缩略图 -->
              <el-form-item label="页面缩略图">
                <div class="thumbnail-upload-section">
                  <el-upload
                    :action="uploadUrl"
                    :headers="uploadHeaders"
                    :on-success="handleThumbnailUploadSuccess"
                    :on-error="handleUploadError"
                    :show-file-list="false"
                    accept="image/*"
                    class="thumbnail-uploader"
                  >
                    <div class="thumbnail-preview-box">
                      <el-image
                        v-if="thumbnail"
                        :src="getResourceUrl(thumbnail)"
                        fit="cover"
                        class="thumbnail-image"
                        lazy
                      >
                        <template #error>
                          <div class="thumbnail-placeholder">
                            <el-icon><Picture /></el-icon>
                            <span>加载失败</span>
                          </div>
                        </template>
                      </el-image>
                      <div v-else class="thumbnail-placeholder">
                        <el-icon><Picture /></el-icon>
                        <span>点击上传缩略图</span>
                      </div>
                    </div>
                  </el-upload>
                </div>
              </el-form-item>

              <!-- 背景填充方式 -->
              <el-form-item label="背景填充">
                <el-radio-group v-model="pageConfig.bgFill">
                  <el-radio value="color">颜色</el-radio>
                  <el-radio value="image">图片</el-radio>
                </el-radio-group>
              </el-form-item>

              <!-- 背景颜色 -->
              <el-form-item label="背景颜色" v-if="pageConfig.bgFill === 'color'">
                <el-color-picker v-model="pageConfig.bgColor" show-alpha></el-color-picker>
              </el-form-item>

              <!-- 背景图配置 -->
              <template v-if="pageConfig.bgFill === 'image'">
                <el-form-item label="背景图">
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
                          v-if="pageConfig.bgUrl"
                          :src="getResourceUrl(pageConfig.bgUrl)"
                          fit="cover"
                          class="bg-image"
                          lazy
                        >
                          <template #error>
                            <div class="bg-placeholder">
                              <el-icon><Picture /></el-icon>
                              <span>加载失败</span>
                            </div>
                          </template>
                        </el-image>
                        <div v-else class="bg-placeholder">
                          <el-icon><Picture /></el-icon>
                          <span>点击上传背景图</span>
                        </div>
                      </div>
                    </el-upload>
                  </div>
                </el-form-item>

                <el-form-item label="透明度">
                  <el-input-number v-model="bgOpacity" :min="0" :max="100" :step="1" controls-position="right" />
                </el-form-item>

                <el-form-item label="填充方式">
                  <el-select v-model="pageConfig.bgRepeat" placeholder="请选择填充方式">
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
            <div class="placeholder">交互配置开发中...</div>
            <!-- 这里放置交互相关的配置项 -->
            <slot name="interaction"></slot>
          </div>
        </el-scrollbar>
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

      .thumbnail-upload-section {
        width: 100%;

        .thumbnail-uploader {
          :deep(.el-upload) {
            width: 100%;
            display: block;
            cursor: pointer;
          }
        }

        .thumbnail-preview-box {
          width: 100%;
          height: 160px;
          border: 1px dashed var(--el-border-color);
          border-radius: 4px;
          overflow: hidden;
          position: relative;
          transition: all 0.3s;

          &:hover {
            border-color: var(--el-color-primary);
          }

          .thumbnail-image {
            width: 100%;
            height: 100%;
            display: block;

            :deep(img) {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }
          }

          .thumbnail-placeholder {
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

          &:hover {
            border-color: var(--el-color-primary);
          }

          .bg-image {
            width: 100%;
            height: 100%;
            display: block;

            :deep(img) {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }
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
