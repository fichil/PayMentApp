import Vue  from "vue";
import Vuex from 'vuex'
import http from "@/utils/request";
import { getToken } from "@/utils/authToken";

Vue.use(Vuex)

//创建一个vuex状态管理器
var store = new Vuex.Store({
    state:{
        user:{},//存储登录后的管理员的信息
        userId:"",
        userName:"",
        userAge:"",
        userPhone:"",
        userCardId:'',
        userBalance:''
    },
    getters:{
        getUserName(state) {
            return state.userName
        }
    },
    mutations:{
        setUser(state,value){
            state.user = value
        },
        setUserId(state,value){
            state.userId = value
        }
        ,
        setUserName(state,value){
            state.userName = value
        },

    },
    actions:{
        setUser(store,value){
            store.commit("setUser",value)
        },
       async getUserInfo(){
       return await http.get(
                "/user/getUserInfo",
                {
                    params:{
                        token:getToken()
                    }
                }
            ).then(res => {
                if(res){
                    console.log("用户信息是",res)
                    store.commit("setUser",res.data);
                    
                    return true;  
                } else {
                    return false;
                }
                
                
            })
        }
    }
})

export default store