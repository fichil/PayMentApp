<template>
	<view class="mainContain">
		<view class="image-placeholder">
			<image src="../../static/image/logo.png" id="logo" />
			<h1>聚合支付-商户平台</h1>
		</view>

		<view class="form-placeholder">
			<view class="example">
				<!-- 基础表单校验 -->
				<uni-forms ref="valiForm" :rules="rules" :modelValue="formData">
					<uni-forms-item label="账号" required name="account" class="word">
						<uni-easyinput v-model="formData.account" placeholder="请输入账号" prefixIcon="info" />
					</uni-forms-item>
					<uni-forms-item label="密码" required name="password" class="word">
						<uni-easyinput v-model="formData.password" placeholder="请输入密码" type="password"
							prefixIcon="info" />
					</uni-forms-item>
					<!-- #ifdef H5 -->
					<uni-forms-item label="验证码" required name="code" class="word">
						<view class="code-container">
							<uni-easyinput v-model="formData.code" placeholder="请输入验证码" maxlength="4"
								style="width: 40%;" />
							<image :src="imgUrl" style="height: 35px; width: 40%; margin-left: 10px;"
								@click="refreshCode"></image>
						</view>
					</uni-forms-item>
					<!-- #endif -->
				</uni-forms>
				<button type="primary" @click="submit('valiForm')">提交</button>
				<!-- <button type="default" @click="register()" style="margin-top: 10px;">注册</button> -->
			</view>
		</view>

	</view>
</template>

<script>
	import axios from '@/utils/axios.js'
	import store from '../../store';
	import urlTobase64 from '@/utils/imageBase64.js'
	import {
		http
	} from '@/js_sdk/service.js'

	export default {
		onLoad() {

		},
		data() {
			return {
				imgUrl: "/apr/PayMentApp-merchant/merchant/code?id=" + new Date().getTime(),
				// 自定义表单数据
				formData: {
					account: 'merchant01',
					password: '123456',
					// #ifdef H5
					code: '',
					// #endif
					// #ifdef MP-WEIXIN
					code: '1234',
					// #endif
					
				},
				// 自定义表单校验规则
				rules: {
					account: {
						rules: [{
							required: true,
							errorMessage: '账号不能为空'
						}]
					},
					password: {
						rules: [{
							required: true,
							errorMessage: '密码不能为空'
						}]
					},
					code: {
						rules: [{
							required: true,
							errorMessage: '验证码不能为空'
						}, {
							pattern: /^.{4}$/,
							errorMessage: '验证码必须为4位'
						}]
					},
				},
			};
		},
		methods: {
			// 在这里添加你的方法
			async submit(ref) {
				await this.$refs[ref].validate().then(res => {
					// #ifdef H5
					axios.post("/PayMentApp-merchant/merchant/login", res).then((res) => {
						console.log("成功收到", res);
						if (res.data.code != 200) {
							uni.showToast({
									icon: 'error',
									title: res.data.msg
								}),
								this.refreshCode();
							this.formData.code = '';
							this.formData.password = '';
							return
						}

						//成功登陆
						uni.showToast({
							title: `登陆成功,正在获取用户数据`,
						});
						store.commit('setToken', res.data.data)
						//存进storage中
						uni.setStorage({
							key: "token",
							data: res.data.data
						})

						axios.post('/PayMentApp-merchant/merchant/getUserInfo').then((res2) => {
							console.log(res2)
							store.commit('setUser', res2.data.data)
							store.commit('setNickname', res2.data.data.nickname)
							store.commit('setMerchantName', res2.data.data.merchantName)
							store.commit('setWallet', res2.data.data.wallet)
							store.commit('setMerchantNumber', res2.data.data.merchantNumber)
						})

						//获取成功
						setTimeout(() => {
							uni.showToast({
								title: `获取成功,稍后自动跳转`,
							});
						}, 1000)
						// 跳转
						setTimeout(() => {
							uni.switchTab({
								url: "../../pages/index/index"
							})
						}, 3000)

					})
					// #endif

					// #ifdef MP-WEIXIN
					http.post("http://192.168.10.207:9090/PayMentApp-merchant/merchant/login", res).then((
						res) => {
							console.log("成功发送", res);
							if (res.data.code != 200) {
								uni.showToast({
										icon: 'error',
										title: res.data.msg
									}),
									this.refreshCode();
								this.formData.code = '';
								this.formData.password = '';
								return
							}

							//成功登陆
							uni.showToast({
								title: `登陆成功,正在获取用户数据`,
							});
							store.commit('setToken', res.data.data)
							//存进storage中
							uni.setStorage({
								key: "token",
								data: res.data.data
							})

							http.post(
									'http://192.168.10.207:9090/PayMentApp-merchant/merchant/getUserInfo')
								.then((res2) => {
									console.log(res2)
									store.commit('setUser', res2.data.data)
									store.commit('setNickname', res2.data.data.nickname)
									store.commit('setMerchantName', res2.data.data.merchantName)
									store.commit('setWallet', res2.data.data.wallet)
									store.commit('setMerchantNumber', res2.data.data.merchantNumber)
								})

							//获取成功
							setTimeout(() => {
								uni.showToast({
									title: `获取成功,稍后自动跳转`,
								});
							}, 1000)
							// 跳转
							setTimeout(() => {
								uni.switchTab({
									url: "../../pages/index/index"
								})
							}, 3000)

						})
					// #endif

				}).catch(err => {
					console.log('err', err);
					uni.showToast({
						icon: 'error',
						title: err[0].errorMessage
					});
					this.refreshCode();		
					this.formData.password = '';
					// #ifdef H5
					this.formData.code = '';
					// #endif
				})
			},

			refreshCode() {
				this.imgUrl = "/apr/PayMentApp-merchant/merchant/code?id=" + new Date().getTime();
			},

			//跳转注册界面
			register() {
				uni.navigateTo({
					url: "/pages/register/index"
				})
			}

		}
	}
</script>

<style>
	.mainContain {
		/* 背景 */
		height: 94vh;
		background-image: url("../../static/image/bg.jpeg");
		background-size: cover;
		background-position: center;
	}

	.image-placeholder {
		display: flex;
		justify-content: center;
		padding-top: 3vh;
		flex-direction: column;
		align-items: center;
	}

	#logo {
		padding-bottom: 5vh;
		width: 200px;
		height: 200px;
	}

	.form-placeholder {
		padding-top: 5vh;
		display: flex;
		justify-content: center;
		flex-direction: column;
		align-items: center;
	}

	.example {
		max-width: 400px;
		width: 80%;
	}

	.word {
		font-size: 16px;
		/* 设置字体大小 */
		color: #330000;
		/* 设置字体颜色 */
		font-weight: bold;
		/* 设置字体粗细 */
	}

	#checkCode {
		width: 20%;
		height: 150px;
		display: inline;
	}

	.code-container {
		display: flex;
		width: 100%;
		/* 确保容器占满整个宽度 */
	}
</style>