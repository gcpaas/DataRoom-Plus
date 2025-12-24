import DrTextPlugin from '@DrPackage/components/DrText/define.ts'

const menuList = [
  {
    code: 'chart',
    name: '图表',
    icon: 'el-icon-s-data',
    children: [
      {
        code: 'bar',
        name: '柱状图',
        icon: 'bar-chart-fill',
        children: [],
      },
      {
        code: 'strip',
        name: '条形图',
        icon: 'bar-chart-horizontal-fill',
        children: [],
      },
      {
        code: 'line',
        name: '折线图',
        icon: 'line-chart-fill',
      },
      {
        code: 'quyutu',
        name: '区域图',
        icon: 'building-3-fill',
      },
      {
        code: 'binhuantu',
        name: '饼环图',
        icon: 'pie-chart-fill',
      },
      {
        code: 'jindutu',
        name: '进度图',
        icon: 'donut-chart-line',
      },
      {
        code: 'sandiantu',
        name: '散点图',
        icon: 'bubble-chart-fill',
      },
      {
        code: 'leidatu',
        name: '雷达图',
        icon: 'radar-fill',
      },
      {
        code: 'guanxitu',
        name: '关系图',
        icon: 'node-tree',
      },
      {
        code: 'qita',
        name: '其他',
        icon: 'mark-pen-fill',
      },
    ],
  },
  {
    code: 'ditu',
    name: '地图',
    icon: 'el-icon-tickets',
  },
  {
    code: 'info',
    name: '信息',
    icon: 'el-icon-tickets',
    children: [new DrTextPlugin()],
  },
  {
    code: 'biaoge',
    name: '列表',
    icon: 'el-icon-tickets',
  },
  {
    code: 'control',
    name: '控件',
    icon: 'el-icon-s-data',
    children: [
      {
        code: 'anniulei',
        name: '按钮类',
        icon: 'btns',
        children: [],
      },
      {
        code: 'xuanzelei',
        name: '选择类',
        icon: 'selects',
        children: [],
      },
      {
        code: 'shurulei',
        name: '输入类',
        icon: 'inputs',
        children: [],
      },
      {
        code: 'shujulei',
        name: '数据类',
        icon: 'numbers',
        children: [],
      },
      {
        code: 'daohanglei',
        name: '导航类',
        icon: 'navs',
        children: [],
      },
    ],
  },
  {
    code: 'container',
    name: '容器',
    icon: 'el-icon-postcard',
    children: [],
  },
  {
    code: 'meiti',
    name: '媒体',
    icon: 'el-icon-tickets',
    children: [],
  },
  {
    code: 'qita',
    name: '其他',
    icon: 'el-icon-qita',
    children: [],
  },
  {
    code: 'wode',
    name: '模板',
    icon: 'el-icon-tickets',
    children: [],
  },
]
export { menuList }
