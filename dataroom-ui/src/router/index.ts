import {createRouter, createWebHashHistory} from 'vue-router'
import config from '../../package.json'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/dataRoom/page/index'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/packages/Login.vue'),
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
      path: '/dataRoom/visualScreenPreview',
      name: 'visualScreenPreview',
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

console.log(
  '%cDataRoom-Plus%cv%s%c 请给我一个Star %s',
  `font-size:24px;color:#3478f6;vertical-align: bottom;background:#ecf2fd;padding:0 10px;border-radius:8px;`,
  `font-size:18px;color:#666;vertical-align: bottom;margin-left:12px;`,
  config.version,
  `font-size:18px;color:#999;vertical-align: bottom;margin-left:15px;text-decoration: none;`,
  `https://gitee.com/gcpaas/DataRoom-Plus`
);
export default router
