<!-- 素材库 -->
<script setup lang="ts">
import { inject, ref } from 'vue'
import type { CanvasInst, ResourceLibType, ResourceLibTagType } from '@/packages/_type/type.ts'
import { DrConst } from '@/packages/_constant/constant.ts'

const canvasInst = inject(DrConst.CANVAS_INST) as CanvasInst
import { Search } from '@element-plus/icons-vue'

const resourceLibVisible = ref(true)
const searchName = ref('')
const selectedTag = ref<string[]>([])
const resourceLibList = ref<ResourceLibType[]>([])
const resourceLibTagList = ref<ResourceLibTagType[]>([])

for (let i = 0; i < 40; i++) {
  resourceLibList.value.push({
    name: '图片' + i,
    type: 'image',
    url: 'https://picsum.photos/200/300?random=' + i,
  })
}

/**
 * 添加组件到画布
 * @param type
 */
const addChart = (item: ResourceLibType) => {
  console.log(item)
  canvasInst.addChart('DrImage')
}

const onSelected = (item: ResourceLibTagType) => {
  console.log(item)
}

const onClose = () => {
  resourceLibVisible.value = false
}
</script>
<template>
  <el-dialog v-model="resourceLibVisible" title="素材库" width="80%">
    <div class="resource-lib-wrapper">
      <div class="search">
        <el-input v-model="searchName" :suffix-icon="Search" size="large" placeholder="搜索"></el-input>
      </div>
      <div class="tag-wrapper">
        <span :class="{ tag: true, active: selectedTag.includes(item.code) }" v-for="item in resourceLibTagList" :key="item.code" @click="onSelected(item)">{{ item.name }}</span>
      </div>
      <el-scrollbar>
        <div class="resource-card">
          <div class="card" v-for="item in resourceLibList" :key="item.name" @click="addChart(item)">
            <div class="image"><img :src="item.url" /></div>
            <div class="desc">{{ item.name }}</div>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <template #footer>
      <el-button @click="onClose">取消</el-button>
      <el-button type="primary" @click="onClose">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.resource-lib-wrapper {
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

  & .resource-card {
    display: grid;
    grid-template-columns: repeat(auto-fit, 240px);
    justify-content: center;
    gap: 16px;
    margin: 16px 0;
    height: 500px;
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
