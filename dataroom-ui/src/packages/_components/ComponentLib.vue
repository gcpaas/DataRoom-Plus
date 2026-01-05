<!-- 组件库 -->
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
  <div class="component-lib-wrapper">
    <div class="search">
      <el-input v-model="searchName" :suffix-icon="Search" placeholder="搜索"></el-input>
    </div>
    <div class="component-card">
      <div class="card" v-for="plugin in filterPluginList" :key="plugin.name" @click="addChart(plugin.type)">
        <div class="image">
          <el-image :src="plugin.thumbnail" lazy fit="contain" />
        </div>
        <div class="desc">{{ plugin.desc }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.component-lib-wrapper {
  padding: 16px;
  box-sizing: border-box;
  overflow-x: hidden;

  & .search {
    width: 100%;
    margin-bottom: 16px;
  }

  & .component-card {
    display: flex;
    flex-direction: column;
    gap: 16px;

    & .card {
      background-color: white;
      border: 1px solid var(--dr-border);
      overflow: hidden;

      &:hover {
        cursor: pointer;
        border: 1px solid var(--dr-primary);
      }

      & .image {
        width: 100%;
        height: 70px;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 8px;
        box-sizing: border-box;

        & .el-image {
          width: 100%;
          height: 100%;
        }
      }

      & .desc {
        padding-left: 16px;
        background-color: white;
        height: 30px;
        line-height: 30px;
        font-size: 12px;
        color: var(--dr-text1);
      }
    }
  }
}
</style>
