<script lang="ts">
import {defineComponent} from 'vue'
import {DrConst} from '@/packages/_common/_constant.ts'

export default defineComponent({
  name: DrConst.THIS_PLUGIN_TYPE,
})
</script>
<script setup lang="ts">
import type {DrTextConfig} from './install.ts'

const {chart} = defineProps<{
  chart: DrTextConfig
}>()

</script>

<template>
  <div class="dr-text">{{ chart.props.text }}</div>
</template>

<style scoped>
.dr-text {
  width: 100%;
  height: 100%;
}
</style>
