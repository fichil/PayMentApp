import axios from 'axios';
import { getToken } from './authToken';
import router from '@/router';
//import { config } from 'vue/types/umd';

//创建一个axios
var http = axios.create({
     baseURL: "/api",
    timeout: 5000//设置请求超时时间
})

//设置请求拦截器，统一设置一些请求头
http.interceptors.request.use(config => {
    if (getToken()){
        config.headers["ecard_token"] = getToken();//给后端拦截器判断用的
    }
    return config;
})

//创建响应拦截器，统一的错误处理
http.interceptors.response.use(
    res => {
        if (res.data.code == 401) {//前端的请求中没有携带token
            alert(res.data.message)
            //?怎么判断当前是不是登录页
            if (router.history.current.path != "/login") {
                router.push("/login")
            }

            return false;
        }
        return res.data;
    },
    err => {
        
        console.log("111",err);
        if (err.request.status == 404) {
            alert("您请求的资源不存在")
            return
        }
        if (err.request.status == 500) {
            alert("服务器出错，请稍后重试")
            return
        }

    }
)

//导出
export default http;