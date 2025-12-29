import {createRouter, createWebHashHistory} from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/dataRoom/page/index'
    },
    {
      path: '/dataRoom',
      name: 'dataRoom',
      component: () => import('@/packages/layout/UpDownLayout.vue'),
      children: [{
        path: 'page/index',
        name: 'page',
        component: () => import('@/packages/page/index.vue'),
      }, {
        path: 'resource/index',
        name: 'resource',
        component: () => import('@/packages/resource/index.vue'),
      }, {
        path: 'dataSource/index',
        name: 'dataSource',
        component: () => import('@/packages/dataSource/index.vue'),
      }, {
        path: 'dataset/index',
        name: 'dataset',
        component: () => import('@/packages/dataset/index.vue'),
      }]
    },
    {
      path: '/dataRoom/visualScreenDesigner',
      name: 'visualScreenDesigner',
      component: () => import('@/packages/VisualScreenDesigner/VisualScreenDesigner.vue'),
    },
    {
      path: '/dataRoom/pageDesigner',
      name: 'pageDesigner',
      component: () => import('@/packages/PageDesigner/PageDesigner.vue'),
    },
    {
      path: '/dataRoom/pagePreviewer',
      name: 'pagePreviewer',
      component: () => import('@/packages/PageDesigner/preview/PagePreviewer.vue'),
    },
  ],
})

export default router
