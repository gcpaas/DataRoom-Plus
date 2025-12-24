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
      name: 'home',
      component: () => import('@/packages/dashBoard/PageEditor.vue'),
    },
    {
      path: '/bigScreenEditor',
      name: 'bigScreenEditor',
      component: () => import('@/packages/bigScreen/PageEditor.vue'),
    },
    {
      path: '/dashBoardEditor',
      name: 'dashBoardEditor',
      component: () => import('@/packages/dashBoard/PageEditor.vue'),
    },
  ],
})

export default router
