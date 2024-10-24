import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

/* Layout */
import Layout from "@/layout";
import { title } from "@/settings";

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */
/**
 * 动态路由
 */

// export const asyncRoutes = [
// ]

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: "/login",
    component: () => import("@/views/login/index"),
    hidden: true,
  },

  {
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true,
  },

  {
    path: "/", // 路由的路径，表示根路径
    component: Layout, // 使用的布局组件
    redirect: "/dashboard", // 当访问根路径时，重定向到 /dashboard
    children: [
      {
        path: "dashboard", // 子路由的路径，表示为 /dashboard
        name: "Dashboard", // 子路由的名称，用于标识该路由
        component: () => import("@/views/dashboard/index"), // 异步加载子路由对应的组件
        meta: {
          title: "首页", // 在侧边栏和面包屑中显示的名称
          icon: "dashboard", // 在侧边栏中显示的图标
        },
      },
    ],
  },

  {
    path: "/support",
    component: Layout,
    redirect: "/support/MainView/ListView", // 重定向到子路由
    alwaysShow: true,
    meta:{
      title:"客服服务",
      icon: "dashboard",
    },
    children: [
      {
        path: "MainView", // 子路由的路径
        name: "MainView",
        component: () => import("@/views/support/MainView"), // 异步加载子路由对应的组件
        meta: {
          title: "客服", // 在侧边栏和面包屑中显示的名称
          icon: "dashboard", // 在侧边栏中显示的图标
        },
        children:[
          // {
          //   path: "index",
          //   name: "index",
          //   component: () => import("@/views/support/MainView"),
          //   hidden: true,
          // },
          // {
          //   path: "nullView",
          //   name: "nullView",
          //   component: () => import("@/views/support/nullView"),
          //   hidden: true,
          //   meta:{
          //     title:"客服Null"
          //   }
          // },
          {
            path: "ListView",
            name: "ListView",
            component: () => import("@/views/support/ListView"),
            hidden: true,
            meta:{
              title:"聊天列表"
            }
          },
        ]
      }
    ],
  },

  // 404 page must be placed at the end !!!
  // { path: '*', redirect: '/404', hidden: true }
];

const createRouter = () =>
  new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes,
  });

const router = createRouter();

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
