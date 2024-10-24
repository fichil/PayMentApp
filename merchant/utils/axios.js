import axios from "axios";
import store from "../store";

// 创建axios实例
const instance = axios.create({
	baseURL: '/apr', //基础url
	changeOrigin: true,
	timeout: 50000 // 毫秒
})

const whiteList = ['/PayMentApp-merchant/merchant/login']

// 设置请求拦截器
instance.interceptors.request.use(
	config => {
		console.log('进入请求拦截器')
		// 获取当前路由路径
		const currentPath = config.url; // 假设config.url包含了完整的路径
		uni.getSystemInfo({
			success:function(res){
				console.log("deviceId",res.deviceId)
			}
		})
		
		if (!whiteList.includes(currentPath)) {
			uni.getStorage({
				key: 'token',
				success: function(res) {
					const token = res.data; // 获取到的 token
					config.headers["admin_token"] = token; // 设置请求头
				},
				fail: function(err) {
					console.log('Failed to retrieve token', err);
				}
			});
		}
		return config;
	},
	error => {
		//处理错误
		return Promise.reject(error)
	}
)

//设置相应拦截器
instance.interceptors.response.use(
	response => {
		console.log('进入响应拦截')
		if(response.data.code == -186){
			uni.showToast({
				title: 'token过期',
				icon: 'none',
				duration: 2000
			});
			setTimeout(()=>{
				uni.clearStorage();
				uni.reLaunch({
					url: "/pages/login/index"
				});
			},3000)
			return false
		}
		
		return response
	},
	error => {
		return Promise.reject(error)
	}
)

export default instance