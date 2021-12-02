import Main from '@/components/main'
import tasktime from '@/view/tasktime/tasktime-index'
import { hasMenu } from '@/libs/util'
import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 * }
 */
import Vue from 'vue'
import VueRouter from 'vue-router'


//核心代码
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

export default [
    {
        path: '/segaoa/login',
        name: 'login',
        meta: {
            title: 'Login - 登录',
            hideInMenu: true
        },
        component: () => import('@/view/login/login.vue')
    },
    {
        path: '/segaoa',
        name: '_home',
        redirect: '/segaoa/home',
        component: Main,
        meta: {
            hideInMenu: true,
            notCache: true
        },
        children: [
            {
                path: '/segaoa/home',
                name: 'home',
                meta: {
                    hideInMenu: true,
                    title: '首页',
                    notCache: true,
                    icon: 'md-home'
                },
                component: () => import('@/view/single-page/home')
            }
        ]
    },{
        path: '/segaoa/project',
        name: '项目管理',
        meta: {
            icon: 'ios-folder-outline',
            title: '项目管理',
            notCache: true,
            hideInMenu: hasMenu('0100000000')
        },
        component: Main,
        children: [{
            path: 'projectmanagement',
            name: '项目信息',
            meta: {
                icon: 'md-funnel',
                title: '导入EXCEL',
                notCache: true,
                hideInMenu: hasMenu('0101000000')
            },
            component: () => import('@/view/project/promanagement/project-index.vue')
        },{
            path: 'dailypaper',
            name: '日报',
            meta: {
                icon: 'ios-list-box',
                title: '导入EXCEL',
                notCache: true,
                hideInMenu: hasMenu('0102000000')
            },
            component: () => import('@/view/project/dailypaper/daily-index.vue')
        },{
            path: 'tasktime',
            name: '工时管理',
            meta: {
                icon: 'md-person',
                title: '工时管理',
                notCache: true,
                hideInMenu: hasMenu('0104000000')
            },
            component: () => import('@/view/tasktime/tasktime-index.vue')
        }]
    }/*,{
      path:'/fixed-assets',
      name:'固定资产',
      meta:{
          icon:'logo-usd',
          title:'固定资产',
          hideInMenu: hasMenu('0200000000')
      },
      component:Main,
      children:[{
          path:'administration',
          name:'行政管理',
          meta:{
              icon:'ios-ionitron-outline',
              title:'行政管理',
              hideInMenu: hasMenu('0201000000')
          },
          component:() => import('@/view/fixed-assets/administration/administration-index.vue')
        }]
    }*/,{
        path:'/segaoa/fixed-assets',
        name:'人事管理',
        meta:{
            icon:'ios-people',
            title:'人事管理',
            notCache: true,
            hideInMenu: hasMenu('0300000000')
        },
        component:Main,
        children:[{
            path:'attendance',
            name:'考勤',
            meta:{
                icon:'ios-rainy-outline',
                title:'考勤',
                notCache: true,
                hideInMenu: hasMenu('0301000000')
            },
            component:() => import('@/view/human-engineering/attendance/attendance-index.vue')
        },{
            path:'wechat',
            name:'微信',
            meta:{
                icon:'ios-chatbubbles',
                title:'微信',
                notCache: true,
                hideInMenu: hasMenu('0302000000')
            },
            component:() => import('@/view/human-engineering/wechat/wechat-index.vue')
        }]
    },
    {
        path: '/segaoa/statistics',
        name: '统计管理',
        meta: {
            icon: 'md-settings',
            title: '统计管理',
            notCache: true,
            hideInMenu: hasMenu('0500000000')
        },
        component: Main,
        children: [
            {
                path: 'statistics',
                name: '工时统计台账',
                meta: {
                    icon: 'md-person',
                    title: '导入EXCEL',
                    notCache: true,
                    hideInMenu: hasMenu('0501000000')
                },
                component: () => import('@/view/statistics/workinghours-index.vue')
            }
        ]
    },
    {
        path: '/segaoa/system',
        name: '系统管理',
        meta: {
            icon: 'md-settings',
            title: '系统管理',
            notCache: true,
            hideInMenu: hasMenu('0400000000')
        },
        component: Main,
        children: [
            {
                path: 'user',
                name: '用户管理',
                meta: {
                    icon: 'md-person',
                    title: '导入EXCEL',
                    notCache: true,
                    hideInMenu: hasMenu('0401000000')
                },
                component: () => import('@/view/system/user/user-index.vue')
            },
            {
                path: 'department',
                name: '部门管理',
                meta: {
                    icon: 'md-download',
                    title: '部门管理',
                    notCache: true,
                    hideInMenu: hasMenu('0402000000')
                },
                component: () => import('@/view/system/department/department-index.vue')
            },
            {
                path: 'roleModel',
                name: '角色权限管理',
                meta: {
                    icon: 'md-download',
                    title: '管理',
                    notCache: true,
                    hideInMenu: hasMenu('0403000000')
                },
                component: () => import('@/view/system/role-model/role-model-index.vue')
            }
        ]
    }
    ,
    {
        path: '/segaoa/argu',
        name: 'argu',
        meta: {
            hideInMenu: true
        },
        component: Main,
        children: [
            {
                path: 'params/:id',
                name: 'params',
                meta: {
                    icon: 'md-flower',
                    title: route => `{{ params }}-${route.params.id}`,
                    notCache: true,
                    beforeCloseName: 'before_close_normal'
                },
                component: () => import('@/view/argu-page/params.vue')
            },
            {
                path: 'query',
                name: 'query',
                meta: {
                    icon: 'md-flower',
                    title: route => `{{ query }}-${route.query.id}`,
                    notCache: true
                },
                component: () => import('@/view/argu-page/query.vue')
            }
        ]
    },
    {
        path: '/segaoa/401',
        name: 'error_401',
        meta: {
            hideInMenu: true
        },
        component: () => import('@/view/error-page/401.vue')
    },
    {
        path: '/segaoa/500',
        name: 'error_500',
        meta: {
            hideInMenu: true
        },
        component: () => import('@/view/error-page/500.vue')
    },
    {
        path: '/segaoa/*',
        name: 'error_404',
        meta: {
            hideInMenu: true
        },
        component: () => import('@/view/error-page/404.vue')
    }
]
