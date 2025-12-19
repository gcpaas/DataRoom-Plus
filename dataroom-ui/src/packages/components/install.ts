import { defineAsyncComponent } from 'vue'

const DrText = defineAsyncComponent(
  () => /* @vite-ignore */ /* chunkName: "DrText" */ import('@/packages/components/Text/index.vue'),
)
const components = {
  DrText,
}

export default components
