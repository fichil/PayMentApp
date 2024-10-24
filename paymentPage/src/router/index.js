//引入路由模块
import VueRouter from "vue-router";
//引入vue模块
import Vue from "vue";
//import store from '../store/index'
//import { getToken } from '@/utils/authToken'
//使用路由模块，把路由模块挂给vue
Vue.use(VueRouter)
var routes = [
    {
        name: "CenterView",
        path: "/CenterView",
        component: () => import("../views/CenterView.vue")
    },
    {
        name: "NotFind",
        path: "/NotFind",
        component: () => import("../views/NotFind.vue")
    },
    {
        name: "SuccessPage",
        path: "/SuccessPage",
        component: () => import("../views/SuccessPage.vue")
    },

];

var router = new VueRouter({
    mode: "hash",
    routes
})
//全局路由守卫 ---》拦截所有路由
router.beforeEach((to, from, next) => {
    console.log("to", to)
    console.log("from", from)
    next();
    /**
     * 登录注册/放行，其他拦截
     */
    //白名单
    // var whiltList = ['/login','/'];
    // let index = whiltList.indexOf(to.path)
    // if(index != -1){
    //     next();
    //     return
    // } else {
    //     console.log("用户信息",store.state.user.userName)
    //     //从cookie中取出ecard_token
    //     if (getToken()) {
    //         console.log("有token")
    //         //先去判断vuex里面是否有存储admin的信息，如果有直接放行，如果没有就像后端请求信息放入vuex
    //         if(store.state.user.userName === undefined) {
    //             console.log("没有用户信息")
    //             //这里就要发起请求
    //             store.dispatch("getUserInfo").then(res => {
    //                 if(res){
    //                     console.log("用户信息请求成功")
    //                     next();//成功在跳转，再去请求菜单，不然就不请求菜单了
    //                 }
    //             })//拿着登录后传来的token去请求个人信息


    //         } else{
    //             console.log("有用户信息")
    //             next();
    //         }
    //     } else{
    //         alert("请先登录")
    //         if(from.fullPath != "/login" ){
    //            // router.push("/login?rediret" + to.path)//将目标路由携带给登录路由，登录成功之后获取redirect参数，跳转到目标路径
    //            //router.push("/login")
    //            router.push("/login")
    //         } 

    //        // router.push("/login")
    //     }

    // }



})

//抛出路由器
export default router;