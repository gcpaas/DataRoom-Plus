<script lang="ts">
import { defineComponent } from 'vue'
import { DrConst } from '@/packages/_common/_constant.ts'

export default defineComponent({
  name: DrConst.THIS_PLUGIN_TYPE,
})
</script>
<script setup lang="ts">
import type { DrImageConfig } from './install.ts'
import {getResourceUrl} from "@/packages/_common/_utils.ts";

const { chart } = defineProps<{
  chart: DrImageConfig
}>()

</script>

<template>
  <div class="dr-image">
    <el-image :src="getResourceUrl(chart.props.url)" lazy>
      <template #error>
        <div class="image-error-slot">
           图片加载失败
        </div>
      </template>
    </el-image>
  </div>
</template>

<style scoped>
.dr-image {
  width: 100%;
  height: 100%;

  & .el-image {
    width: 100%;
    height: 100%;
  }

  & .image-error-slot {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: var(--dr-text);
  }
}
</style>
