import router from "./router";
import store from "./store";
import { Message } from "element-ui";
import NProgress from "nprogress"; // 进度条
import "nprogress/nprogress.css"; // 进度条样式
import { getToken } from "@/utils/auth"; // 从 cookie 中获取 token
import getPageTitle from "@/utils/get-page-title";

NProgress.configure({ showSpinner: false }); // NProgress 配置

const whiteList = ["/login", "/register", ""]; // 无需重定向的白名单

router.beforeEach(async (to, from, next) => {
  // 开始进度条
  NProgress.start();

  // 设置页面标题
  document.title = getPageTitle(to.meta.title);

  // 判断用户是否已登录
  const hasToken = getToken();

  if (hasToken) {
    if (to.path === "/login") {
      // 如果已登录，重定向到首页
      next({ path: "/" });
      NProgress.done(); //关闭加载图标
    } else {
      const hasGetUserInfo = store.getters.name;
      if (hasGetUserInfo) {
        next();
      } else {
        try {
          // 获取用户信息
          await store.dispatch("user/getInfo");

          
          //获取当前用户的角色
          const roleId = store.getters.roleId;          
          

          // 通过角色获取路由
          const accessedRoutes = await store.dispatch(
            "permission/generateRoutes",
            roleId
          );

          //添加到路由
          router.addRoutes(accessedRoutes);

          
          // 在这动态添加最后的通配路由，确保先有动态路由、再有通配路由，解决动态路由刷新会跳转到404问题
          let lastRou = [{ path: '*', redirect: '/404' }]
          router.addRoutes(lastRou)


          next({ ...to, replace: true })          
        } catch (error) {
          console.log(error);
          // 移除 token 并跳转到登录页面重新登录
          await store.dispatch("user/resetToken");
          Message.error(error || "发生错误");
          next(`/login?redirect=${to.path}`);
          NProgress.done();
        }
      }
    }
  } else {
    /* 没有 token */

    if (whiteList.indexOf(to.path) !== -1) {
      // 在免费登录白名单中，直接进入
      next();
    } else {
      // 其他没有访问权限的页面重定向到登录页面
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // 完成进度条
  NProgress.done();
});
