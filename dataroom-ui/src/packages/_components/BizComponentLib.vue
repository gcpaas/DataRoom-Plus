<!-- 业务组件库、暂时不用 -->
<script setup lang="ts">
import { computed, inject, ref } from 'vue'
import type { CanvasInstInterface, ComponentLibTagInterface } from '@/packages/_common/_type.ts'
import { DrConst } from '@/packages/_common/_constant.ts'

const canvasInst = inject(DrConst.CANVAS_INST) as CanvasInstInterface
import { pluginList, componentLibTagList } from './PluginRegister.ts'
import { Search } from '@element-plus/icons-vue'

const componentLibVisible = ref(true)
const searchName = ref('')
const selectedTag = ref<string[]>([])
/**
 * 添加组件到画布
 * @param type
 */
const addChart = (type: string) => {
  console.log(type)
  canvasInst.addChart(type)
}
/**
 * 选中标签
 * @param item
 */
const onSelected = (item: ComponentLibTagInterface) => {
  if (selectedTag.value.includes(item.tag)) {
    // 从数组中删除
    selectedTag.value = selectedTag.value.filter((code) => code !== item.tag)
    return
  }
  selectedTag.value.push(item.tag)
}
/**
 * 动态筛选
 */
const filterPluginList = computed(() => {
  if (selectedTag.value.length == 0) {
    return pluginList
  }
  return pluginList.filter((item) => {
    for (let i = 0; i < item.tags.length; i++) {
      const tag = item.tags[i] as string
      if (selectedTag.value.includes(tag)) {
        return true
      }
    }
    return false
  })
})

const onClose = () => {
  componentLibVisible.value = false
}
</script>
<template>
  <el-dialog v-model="componentLibVisible" title="组件库" width="60%">
    <div class="component-lib-wrapper">
      <div class="search">
        <el-input v-model="searchName" :suffix-icon="Search" size="large" placeholder="搜索"></el-input>
      </div>
      <div class="tag-wrapper">
        <span :class="{ tag: true, active: selectedTag.includes(item.tag) }" v-for="item in componentLibTagList" :key="item.tag" @click="onSelected(item)">{{ item.name }}</span>
      </div>
      <div class="component-card">
        <div class="card" v-for="plugin in filterPluginList" :key="plugin.name" @click="addChart(plugin.type)">
          <div class="image">
            <el-image :src="plugin.thumbnail" lazy />
          </div>
          <div class="desc">{{ plugin.desc }}</div>
        </div>
      </div>
    </div>
    <template #footer>
      <el-button @click="onClose">取消</el-button>
      <el-button type="primary" @click="onClose">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.component-lib-wrapper {
  & .search {
    width: 50%;
    margin: 0 auto 16px auto;
  }

  & .tag-wrapper {
    text-align: center;

    & .tag {
      margin: 0 8px;
      padding: 4px 4px;
      border-radius: 4px;
      cursor: pointer;
      height: 20px;
      line-height: 20px;
      display: inline-block;

      &:hover {
        color: var(--dr-primary);
        background-color: var(--dr-primary1);
      }
    }

    & .active {
      color: var(--dr-primary);
      background-color: var(--dr-primary1);
    }
  }

  & .component-card {
    display: grid;
    grid-template-columns: repeat(auto-fit, 240px);
    justify-content: center;
    gap: 16px;
    margin: 16px 0;

    & .card {
      background-color: var(--dr-bg2);
      height: 200px;
      border: 1px solid var(--dr-border);

      &:hover {
        box-shadow: 0 0 8px var(--dr-border);
        cursor: pointer;
        color: var(--dr-primary);
      }

      & .image {
        width: 100%;
        height: 154px;
        margin: 0 auto;
        padding: 8px;

        & .el-image {
          width: 95%;
          height: 95%;
          object-fit: contain;
        }
      }

      & .desc {
        padding-left: 16px;
        background-color: white;
        height: 30px;
        line-height: 30px;
      }
    }
  }
}
</style>
