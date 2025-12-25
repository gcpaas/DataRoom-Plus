<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'DrImage',
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
