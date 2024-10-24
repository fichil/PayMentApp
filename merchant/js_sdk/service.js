import Request from './luch-request/luch-request/index.js'
import store from '@/store/index.js';

const http = new Request()

const whiteList = ['http://192.168.10.207:9090/PayMentApp-merchant/merchant/login']

http.setConfig((config) => {
	/* 设置全局配置 */
	config.baseUrl = "http://192.168.10.207:9090" /* 根域名不同 */
	config.header = {
		...config.header
	},
	config.timeout = 50000
	return config
})

/**
 * 自定义验证器，如果返回true 则进入响应拦截器的响应成功函数(resolve)，否则进入响应拦截器的响应错误函数(reject)
 * @param { Number } statusCode - 请求响应体statusCode（只读）
 * @return { Boolean } 如果为true,则 resolve, 否则 reject
 */
// 有默认，非必写
http.validateStatus = (statusCode) => {
	return statusCode === 200
}



http.interceptors.request.use((config) => {
	/* 请求之前拦截器 */
	console.log('Request进入请求拦截器')
	// 获取当前路由路径
	const currentPath = config.url; // 假设config.url包含了完整的路径
	console.log("路径",currentPath);
	uni.getSystemInfo({
		success:function(res){
			console.log("deviceId",res.deviceId)
		}
	})
	
	if (!whiteList.includes(currentPath)) {
		console.log("要加token")
		const token = uni.getStorageSync("token");
		console.log(token);
		config.header = {
		    ...config.header,
		    admin_token : token 
		}
		console.log(config.header)
		
	}else{
		console.log("不加token")
		config.header = {
			...config.header,
			'trueCode': "1234"
		}
	}

	return config
})

// 必须使用异步函数，注意
http.interceptors.response.use(async (response) => {
	/* 请求之后拦截器 */
	// if (response.data.code !== 200) { // 服务端返回的状态码不等于200，则reject()
	//   return Promise.reject(response)
	// }
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
}, (response) => {
	// 请求错误做点什么
	console.log("请求错误做点什么", response);
	if (response) {
		let data = response.data
		const token = uni.getStorageSync("token")
		console.log("------异常响应------", token)
		console.log("------异常响应------", data.status)
	}
	return response
})

export {
	http
}