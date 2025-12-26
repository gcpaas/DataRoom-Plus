<script lang="ts">
import { defineComponent } from 'vue'
import { DrConst } from '@/packages/_common/_constant.ts'

export default defineComponent({
  name: DrConst.THIS_PLUGIN_TYPE,
})
</script>
<script setup lang="ts">
import type { DrImageConfig } from './install.ts'

const { chart } = defineProps<{
  chart: DrImageConfig
}>()
</script>

<template>
  <div class="dr-image">
    <img src="https://picsum.photos/200/300?random"/>
  </div>
</template>

<style scoped>
.dr-image {
  width: 100%;
  height: 100%;
  & img {
    width: 100%;
    height: 100%;
  }
}
</style>
