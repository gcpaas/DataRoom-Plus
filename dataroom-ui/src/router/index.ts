import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/',
      name: 'bigScreenEditor',
      component: () => import('@/packages/bigScreen/bigScreenEditor.vue'),
    },
  ],
})

export default router
