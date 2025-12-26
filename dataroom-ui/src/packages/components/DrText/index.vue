<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'DrText',
})
</script>
<script setup lang="ts">
import type { DrTextConfig } from './install.ts'

const { chart } = defineProps<{
  chart: DrTextConfig
}>()

</script>

<template>
  <div class="dr-text">{{ chart.props.text }}{{chart.id}}</div>
</template>

<style scoped>
.dr-text {
  color: var(--dr-primary);
  width: 100%;
  height: 100%;
}
</style>
