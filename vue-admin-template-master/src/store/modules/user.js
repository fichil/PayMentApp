import { login, logout, getInfo, updatePassword } from "@/api/user"; // 导入用户相关的 API
import { getToken, setToken, removeToken } from "@/utils/auth"; // 导入 token 操作的工具函数
import { resetRouter } from "@/router"; // 导入重置路由的函数

// 定义默认状态
const getDefaultState = () => {
  return {
    token: getToken(), // 从 cookie 中获取 token
    name: "", // 用户名
    avatar: "", // 用户头像
    roleId: [], // 用户角色
    menus: [], // 用户菜单
  };
};

const state = getDefaultState(); // 初始化状态

const mutations = {
  // 重置状态
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState()); // 将状态重置为默认值
  },
  // 设置 token
  SET_TOKEN: (state, token) => {
    state.token = token; // 将 token 存入状态
  },
  // 设置用户名
  SET_NAME: (state, name) => {
    state.name = name; // 将用户名存入状态
  },
  // 设置用户头像
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar; // 将头像存入状态
  },
  // 设置用户角色
  SET_ROLE: (state, roleId) => {
    state.roleId = roleId; // 将角色存入状态
  },
  // 设置用户菜单
  SET_MENU: (state, menus) => {
    state.menus = menus; // 将用户菜单存入状态
  },
  //设置用户id
  SET_ID: (state, id) => {
    state.id = id; // 将用户id存入状态
  },
};

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
   
    
    const {username, password, code } = userInfo; // 解构用户名和密码
    return new Promise((resolve, reject) => {
      // 调用登录 API
      login({ username: username.trim(), password: password, code: code })
        .then((response) => {
          const { data } = response;          
          commit("SET_TOKEN", data); // vuex 提交 token
          setToken(data); // 将 token 存入 cookie
          resolve(); // 成功时调用 resolve
        })
        .catch((error) => {
          reject(error); // 失败时调用 reject
        });
    });
  },

  // 获取用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
    
      // 调用获取用户信息的 API
      getInfo(state.token)
        .then((response) => {
          console.log(response);
          
          
          // const { data } = response.data;

          if (!response.data) {
            return reject("验证失败，请重新登录"); // 如果没有数据，拒绝并提示
            //刷新验证码
            
          }          

          const { nickname,  roleId } = response.data.adminInfo.data; // 解构用户信息
          commit("SET_NAME", nickname); // 提交用户名
          commit("SET_AVATAR", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"); // 提交头像          
          commit("SET_ROLE", roleId); // 提交角色
          commit("SET_ID", response.data.adminInfo.data.id)
          commit("SET_MENU", response.data.menuList.data); // 提交菜单
          
          resolve(response.data); // 成功时调用 resolve
        })
        .catch((error) => {          
          reject(error); // 失败时调用 reject
        });
    });
  },
  //获取验证码
  refreshCaptcha() {
    // return new Promise((resolve, reject) => {
    //   // refreshCaptcha()
    //   //   .then((response) => {
    //   //     const { data } = response;
    //   //     resolve(data);
    //   //   })
    //   //   .catch((error) => {
    //   //     reject(error);
    //   //   });
    //   resolve("api/PayMentApp-platform/admin/code" + "?" + new Date().getTime())
    // });
    return "api/PayMentApp-platform/admin/code" + "?" + new Date().getTime();
  },


  // 用户登出
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      removeToken(); // 首先移除 token
      resetRouter(); // 重置路由
      commit("RESET_STATE"); // 重置状态
      resolve(); // 成功时调用 resolve
    });
  },

  // 移除 token
  resetToken({ commit }) {
    return new Promise((resolve) => {
      removeToken(); // 首先移除 token
      commit("RESET_STATE"); // 重置状态
      resolve(); // 成功时调用 resolve
    });
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
