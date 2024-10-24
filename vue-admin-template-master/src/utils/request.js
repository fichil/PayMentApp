import axios from "axios";
import { Message } from "element-ui";
import store from "@/store";
import { getToken } from "@/utils/auth";

// 创建一个 axios 实例
const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_API, // url = 基础 url + 请求 url
  baseURL: "/api",
  // 代理地址
  changeOrigin: true,

  //携带Cooke
  withCredentials: true,

  timeout: 50000, // 请求超时
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在请求发送之前做某些处理

    if (store.getters.token) {
      // 让每个请求携带 token
      // ['X-Token'] 是自定义的 header 键
      // 请根据实际情况进行修改
      config.headers["admin_token"] = getToken();
    }
    return config;
  },
  (error) => {
    // 处理请求错误
    console.log(error); // 调试用
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  /**
   * 如果你想获取 http 信息，比如 headers 或状态
   * 请返回 response => response
   */

  /**
   * 根据自定义代码判断请求状态
   * 这里只是一个示例
   * 你也可以通过 HTTP 状态码判断状态
   */
  (response) => {
    const res = response.data;

    // 如果自定义代码不是 200，则认为是错误
    if (res.code !== 200) {
      if (res.code === -300) {
        // 验证码错误
        Message({
          message: "验证码错误",
          type: "error",
          duration: 5 * 1000,
        });

        setTimeout(() => {
          window.location.reload();
        }, 2000);
        return;
      } else if (res.code === -200) {
        Message({
          message: "密码错误",
          type: "error",
          duration: 5 * 1000,
        });
        setTimeout(() => {
          window.location.reload();
        }, 2000);
        return;
      }
      // Message({
      //   message: res.message || "错误",
      //   type: "error",
      //   duration: 5 * 1000,
      // });

      // 50008: 非法 token; 50012: 其他客户端已登录; 50014: Token 过期;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // 重新登录
        MessageBox.confirm(
          "您已退出登录，可以取消留在此页面，或重新登录",
          "确认退出",
          {
            confirmButtonText: "重新登录",
            cancelButtonText: "取消",
            type: "warning",
          }
        ).then(() => {
          store.dispatch("user/resetToken").then(() => {
            location.reload();
          });
        });
      }
      return Promise.reject(new Error(res.message || "错误"));
    } else {
      return res;
    }
  },
  (error) => {
    console.log("错误: " + error); // 调试用
    Message({
      message: error.message,
      type: "error",
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  }
);

export default service;
