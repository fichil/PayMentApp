import router, { constantRoutes } from "@/router";
import store from "..";
import Layout from "@/layout/index.vue";
import { subMenuList} from "@/utils/auth";

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some((role) => route.meta.roles.includes(role));
  } else {
    return true;
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = [];

  routes.forEach((route) => {
    const tmp = { ...route };
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles);
      }
      res.push(tmp);
    }
  });

  return res;
}

const state = {
  routes: [],
  addRoutes: [],
};

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes;
    // 将静态路由和动态路由合并
    state.routes = constantRoutes.concat(routes);
  },
};

const actions = {
  generateRoutes({ commit }, roleId) {
    return new Promise((resolve) => {
      let accessedRoutes;
      let asyncRoutes = [];
      // 获得异步路由
      const menus = store.getters.menus;

      // 处理 JSON 菜单数据
      if (menus && menus.length > 0) {
        const menuList = [];
        for (let i = 0; i < menus.length; i++) {
          const obj = {};
          obj.path = menus[i].path;
          obj.name = menus[i].name;
          obj.redirect = menus[i].redirect;
          obj.meta = JSON.parse(menus[i].meta);
          // 单独处理
          if(menus[i].id === 2){
              obj.alwaysShow = true;
          }
          
          if (menus[i].component === "layout") {
            obj.component = Layout
          }
          //子菜单
          obj.children=subMenuList(menus[i].children)
          menuList.push(obj);
        }
        console.log(menuList);
        
        asyncRoutes=menuList
      }
      // if (roleId===0) {
      //   console.log("roleId===0");
      //   accessedRoutes = asyncRoutes || [];
      // } else {
      //   accessedRoutes = filterAsyncRoutes(asyncRoutes, roleId);
      // }
      commit("SET_ROUTES", asyncRoutes);      
      resolve(asyncRoutes);
    });
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
