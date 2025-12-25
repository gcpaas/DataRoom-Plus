import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/pagePreviewer'
    },
    {
      path: '/visualScreenDesigner',
      name: 'visualScreenDesigner',
      component: () => import('@/packages/VisualScreenDesigner/VisualScreenDesigner.vue'),
    },
    {
      path: '/pageDesigner',
      name: 'pageDesigner',
      component: () => import('@/packages/PageDesigner/PageDesigner.vue'),
    },
    {
      path: '/pagePreviewer',
      name: 'pagePreviewer',
      component: () => import('@/packages/PageDesigner/preview/PagePreviewer.vue'),
    },
  ],
})

export default router
