<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Search, Plus, MoreFilled} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {pageApi, type PageEntity} from './api'

const router = useRouter()
const searchName = ref('')
const pageList = ref<PageEntity[]>([])
const loading = ref(false)

/**
 * 查询
 */
const getPageList = () => {
  loading.value = true
  try {
    const params: { name?: string } = {}
    if (searchName.value) {
      params.name = searchName.value
    }
    pageApi.list(params).then((res) => {
      pageList.value = res || []
    })
  } catch (error) {
    console.error('查询失败:', error)
  } finally {
    loading.value = false
  }
}

// 新增页面
const handleAdd = () => {
  ElMessageBox.prompt('请输入页面名称', '新增页面', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /\S+/,
    inputErrorMessage: '页面名称不能为空'
  }).then(async ({value}) => {
    try {
      pageApi.insert({
        name: value,
        code: 'page_' + Date.now(),
        pageType: 'visualScreen'
      }).then((res) => {
        ElMessage.success('新增成功')
        getPageList()
      })
    } catch (error) {
      console.error('新增失败:', error)
    }
  }).catch(() => {
  })
}

/**
 * 编辑页面
 * @param item
 */
const handleEdit = (item: PageEntity) => {
  router.push({
    path: '/dataRoom/pageDesigner',
    query: {code: item.code}
  })
}

/**
 * 发布
 * @param item
 */
const handlePublish = async (item: PageEntity) => {
  try {
    await pageApi.publish({
      pageCode: item.code,
      remark: '发布'
    })
    ElMessage.success('发布成功')
    getPageList()
  } catch (error) {
    console.error('发布失败:', error)
  }
}

/**
 * 取消发布
 * @param item
 */
const handleOffline = async (item: PageEntity) => {
  try {
    await pageApi.offline({
      code: item.code,
      remark: '取消发布'
    })
    ElMessage.success('取消发布成功')
    getPageList()
  } catch (error) {
    console.error('取消发布失败:', error)
  }
}

/**
 * 删除
 * @param page
 */
const handleDelete = (page: PageEntity) => {
  ElMessageBox.confirm(`确定要删除${page.name}吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      pageApi.delete(page.code).then((res) => {
        ElMessage.success('删除成功')
        getPageList()
      })
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {
  })
}

/**
 * 预览
 * @param page
 */
const handlePreview = (page: PageEntity) => {
  let path = '/dataRoom/pagePreviewer'
  if (page.pageType = 'visualScreen') {
    path = '/dataRoom/visualScreenPreview'
  }
  router.push({
    path: path,
    query: {code: page.code}
  })
}

// 页面加载时获取列表
onMounted(() => {
  getPageList()
})
</script>

<template>
  <div class="dr-page">
    <div class="page-header">
      <div class="search-box">
        <el-input
          v-model="searchName"
          placeholder="请输入页面名称"
          :prefix-icon="Search"
          clearable
          @keyup.enter="getPageList"
        />
      </div>
      <div class="button-group">
        <el-button :icon="Search" @click="getPageList">查询</el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
      </div>
    </div>

    <div class="page-content" v-loading="loading">
      <div class="card-list">
        <div class="page-card" v-for="item in pageList" :key="item.id">
          <div class="card-thumbnail">
            <!-- 缩略图占位 -->
            <div class="thumbnail-placeholder">
              <span>{{ item.name }}</span>
            </div>
          </div>
          <div class="card-footer">
            <div class="card-name" :title="item.name">{{ item.name }}</div>
            <el-dropdown trigger="click" @command="(command:string) => {
              if (command === 'edit') handleEdit(item)
              else if (command === 'publish') handlePublish(item)
              else if (command === 'offline') handleOffline(item)
              else if (command === 'delete') handleDelete(item)
              else if (command === 'preview') handlePreview(item)
            }">
              <el-icon class="more-icon">
                <MoreFilled/>
              </el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="edit">编辑</el-dropdown-item>
                  <el-dropdown-item command="publish" v-if="item.pageStatus !== 'PUBLISHED'">发布</el-dropdown-item>
                  <el-dropdown-item command="offline" v-if="item.pageStatus === 'PUBLISHED'">取消发布</el-dropdown-item>
                  <el-dropdown-item command="preview">预览</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
      <el-empty :image-size="200" v-if="!loading && pageList.length === 0" description="暂无页面"/>
    </div>
  </div>
</template>

<style scoped lang="scss">
.dr-page {
  height: calc(100vh - 60px);
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .page-header {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    gap: 16px;

    .search-box {
      width: 300px;
    }

    .button-group {
      display: flex;
      gap: 8px;
    }
  }

  .page-content {
    flex: 1;
    overflow-y: auto;

    .card-list {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      padding: 0 20px;

      .page-card {
        background: #fff;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        overflow: hidden;
        transition: all 0.3s;
        cursor: pointer;

        &:hover {
          box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        }

        .card-thumbnail {
          width: 100%;
          height: 180px;
          background: #f5f7fa;
          display: flex;
          align-items: center;
          justify-content: center;

          .thumbnail-placeholder {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #909399;
            font-size: 14px;
          }
        }

        .card-footer {
          padding: 12px 16px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          border-top: 1px solid #e4e7ed;

          .card-name {
            flex: 1;
            font-size: 14px;
            color: #303133;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-right: 8px;
          }

          .more-icon {
            font-size: 18px;
            color: #606266;
            cursor: pointer;
            transition: color 0.3s;

            &:hover {
              color: #409eff;
            }
          }
        }
      }
    }
  }
}
</style>
