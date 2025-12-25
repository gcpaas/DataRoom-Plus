<!-- 组件库 -->
<script setup lang="ts">
import { computed, inject, ref } from 'vue'
import type { CanvasInst, ComponentLibTagType } from '@/packages/_type/type.ts'
import { DrConst } from '@/packages/_constant/constant.ts'

const canvasInst = inject(DrConst.CANVAS_INST) as CanvasInst
import { componentLibList, componentLibTypeList } from './componentLibInstall.ts'
import { Search } from '@element-plus/icons-vue'

const componentLibVisible = ref(true)
const searchName = ref('')
const selectedTag = ref<string[]>([])
/**
 * 添加组件到画布
 * @param type
 */
const addChart = (type: string) => {
  canvasInst.addChart(type)
}
/**
 * 选中标签
 * @param item
 */
const onSelected = (item: ComponentLibTagType) => {
  if (selectedTag.value.includes(item.code)) {
    // 从数组中删除
    selectedTag.value = selectedTag.value.filter((code) => code !== item.code)
    return
  }
  selectedTag.value.push(item.code)
}
/**
 * 动态筛选
 */
const filterComponentLibList = computed(() => {
  if (selectedTag.value.length == 0) {
    return componentLibList
  }
  return componentLibList.filter((item) => {
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
        <span :class="{ tag: true, active: selectedTag.includes(item.code) }" v-for="item in componentLibTypeList" :key="item.code" @click="onSelected(item)">{{ item.name }}</span>
      </div>
      <div class="component-card">
        <div class="card" v-for="plugin in filterComponentLibList" :key="plugin.name" @click="addChart(plugin.name)">
          <div class="image"><img :src="plugin.thumbnail" /></div>
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
        color: var(--dr-prmary);
        background-color: var(--dr-prmary1);
      }
    }

    & .active {
      color: var(--dr-prmary);
      background-color: var(--dr-prmary1);
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
        color: var(--dr-prmary);
      }

      & .image {
        width: 100%;
        height: 154px;
        margin: 0 auto;
        padding: 8px;
      }

      & img {
        width: 95%;
        height: 95%;
        object-fit: contain;
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
