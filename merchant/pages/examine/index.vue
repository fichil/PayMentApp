<template>
	<view class="header">
		<button @click="navigateToHome" class="home-button">返回主页</button>
	</view>
	<view class="container">

		<view class="input-group">
			<view class="label">法人姓名</view>
			<input v-model="legalPersonName" placeholder="请输入法人的姓名" class="input" />
		</view>
		<view class="input-group">
			<view class="label">法人电话</view>
			<input v-model="legalPersonPhone" placeholder="请输入法人的电话" class="input" />
		</view>

		<view class="input-group">
			<view class="label">商户名</view>
			<input v-model="qrImg" placeholder="请输入商户名" class="input" />
		</view>
		<!-- #ifdef H5 -->
		<view class="upload-group">
			<view class="upload-title">营业资格证上传</view>
			<htz-image-upload :max="1" v-model="ceshiData" :formData="user" @uploadSuccess="ceshiUploadSuccess"
				action="apr/PayMentApp-merchant/merchant/uploadMerchantLicense">
			</htz-image-upload>
		</view>

		<view class="upload-group">
			<view class="upload-title">法人身份证上传</view>
			<view class="id-card-label">身份证正面</view>
			<htz-image-upload src="idCardFront" :max="1" v-model="idCardFront" :formData="user"
				@uploadSuccess="ceshiUploadSuccess" action="apr/PayMentApp-merchant/merchant/uploadMerchantIDCardFront">
			</htz-image-upload>

			<view class="id-card-label">身份证反面</view>
			<htz-image-upload :max="1" v-model="idCardBack" :formData="user" @uploadSuccess="ceshiUploadSuccess"
				action="apr/PayMentApp-merchant/merchant/uploadMerchantIDCardReverse">
			</htz-image-upload>
		</view>
		<!-- #endif -->

		<!-- #ifdef MP-WEIXIN -->

		<view class="upload-group">
			<view class="upload-title">营业资格证上传</view>
			<htz-image-upload :max="1" v-model="ceshiData" :formData="user" @uploadSuccess="ceshiUploadSuccess"
				action="http://192.168.10.207:9090/PayMentApp-merchant/merchant/uploadMerchantLicense">
			</htz-image-upload>
		</view>
		<view class="upload-group">
			<view class="upload-title">法人身份证上传</view>
			<view class="id-card-label">身份证正面</view>
			<htz-image-upload :max="1" v-model="idCardFront" :formData="user" @uploadSuccess="ceshiUploadSuccess"
				action="http://192.168.10.207:9090/PayMentApp-merchant/merchant/uploadMerchantIDCardFront">
			</htz-image-upload>

			<view class="id-card-label">身份证反面</view>
			<htz-image-upload :max="1" v-model="idCardBack" :formData="user" @uploadSuccess="ceshiUploadSuccess"
				action="http://192.168.10.207:9090/PayMentApp-merchant/merchant/uploadMerchantIDCardReverse">
			</htz-image-upload>
		</view>
		<!-- #endif -->


		<button @click="submitForm">提交</button> <!-- 添加提交按钮 -->
	</view>
</template>

<script>
	import htzImageUpload from '@/components/htz-image-upload/htz-image-upload.vue'
	import {
		http
	} from '../../js_sdk/service';
	import store from '../../store'
	import axios from '../../utils/axios.js';

	export default {
		async onLoad() {
			const token = store.getters.getToken;
			const merchantNumber = store.getters.getMerchantNumber;
			if (merchantNumber === 0) {
				// #ifdef H5

				try {
					const res2 = await axios.post('/PayMentApp-merchant/merchant/getUserInfo');
					// 假设 res2.data.data 是你的用户数据
					store.commit('setUser', res2.data.data);
					store.commit('setNickname', res2.data.data.nickname);
					store.commit('setMerchantName', res2.data.data.merchantName);
					store.commit('setWallet', res2.data.data.wallet);
					store.commit('setMerchantNumber', res2.data.data.merchantNumber);

					// 请求完成后输出
					this.afterAsync();
				} catch (error) {
					console.error('请求失败', error);
				}
				// #endif

				// #ifdef MP-WEIXIN

				try {
					const res2 = await http.post(
						'http://192.168.10.207:9090/PayMentApp-merchant/merchant/getUserInfo');
					// 假设 res2.data.data 是你的用户数据
					store.commit('setUser', res2.data.data);
					store.commit('setNickname', res2.data.data.nickname);
					store.commit('setMerchantName', res2.data.data.merchantName);
					store.commit('setWallet', res2.data.data.wallet);
					store.commit('setMerchantNumber', res2.data.data.merchantNumber);

					// 请求完成后输出
					this.afterAsync();
				} catch (error) {
					console.error('请求失败', error);
				}
				// #endif
			} else {
				this.afterAsync();
			}
		},
		data() {
			return {
				ceshiData: [],
				legalPersonName: '', // 法人姓名
				legalPersonPhone: '', // 法人电话
				qrImg: '', // 商户名
				idCardFront: [], // 法人身份证正面图片数组
				idCardBack: [], // 法人身份证反面图片数组
				user: {
					merchantNumber: 0,
					adminId: 0
				}
			}
		},
		components: {
			htzImageUpload,
		},
		methods: {
			ceshiUploadSuccess(res) { // 上传成功
				if (res.statusCode === 200) {
					console.log(res);

					let responseData;
					try {
						responseData = typeof res.data === 'string' ? JSON.parse(res.data) : res.data;
					} catch (error) {
						console.error('解析JSON失败:', error);
						uni.showToast({
							title: '解析响应数据失败',
							icon: 'none',
							duration: 2000
						});
						return;
					}

					if (responseData.code == -1) {
						uni.showToast({
							title: responseData.data,
							icon: 'none',
							duration: 2000
						});
					} else {
						uni.showToast({
							title: '上传成功！',
							icon: 'success',
							duration: 2000
						});
					}
				}
			},
			navigateToHome() {
				uni.switchTab({
					url: '/pages/person/index'
				});
			},
			afterAsync() {
				this.user.merchantNumber = store.getters.getMerchantNumber;
				this.user.adminId = store.getters.getAdminId;
			},

			submitForm() { // 提交表单的方法
				if (this.legalPersonName && this.legalPersonPhone && this.qrImg) {
					const formData = {
						legalPersonName: this.legalPersonName,
						legalPersonPhone: this.legalPersonPhone,
						qrImg: this.qrImg,
						adminId: store.getters.getAdminId
					};

					// #ifdef H5
					axios.post("PayMentApp-merchant/merchant/uploadMerchantInfo", formData)
						.then(response => {
							if (response.status === 200) {
								// 检查返回的 code
								if (response.data.code === -1) {
									console.error('错误：', response.data.message); // 输出错误信息
									uni.showToast({
										title: '上传失败：' + response.data.message,
										icon: 'none', // 显示为无图标的提示
										duration: 2000 // 提示框持续时间
									});
								} else {
									console.log('更新成功:', response.data);
									uni.showToast({
										title: '上传成功！',
										icon: 'success', // 显示成功图标
										duration: 2000 // 提示框持续时间
									});
								}
							}
						})
						.catch(error => {
							console.error('更新失败:', error);
							uni.showToast({
								title: '更新失败，请重试！',
								icon: 'none', // 显示为无图标的提示
								duration: 2000 // 提示框持续时间
							});
						});

					// #endif    

					// #ifdef MP-WEIXIN
					console.log("formdata ", formData);
					http.post("http://192.168.10.207:9090/PayMentApp-merchant/merchant/uploadMerchantInfo", formData)
						.then(response => {
							if (response.status === 200) {
								// 检查返回的 code
								if (response.data.code === -1) {
									console.error('错误：', response.data.message); // 输出错误信息
									uni.showToast({
										title: '上传失败：' + response.data.message,
										icon: 'none', // 显示为无图标的提示
										duration: 2000 // 提示框持续时间
									});
								} else {
									console.log('更新成功:', response.data);
									uni.showToast({
										title: '上传成功！',
										icon: 'success', // 显示成功图标
										duration: 2000 // 提示框持续时间
									});
								}
							}
						})
						.catch(error => {
							console.error('更新失败:', error);
							uni.showToast({
								title: '更新失败，请重试！',
								icon: 'none', // 显示为无图标的提示
								duration: 2000 // 提示框持续时间
							});
						});

					// #endif    

				} else {
					console.log('请确保所有必填字段均已填写');
					uni.showToast({
						title: '请确保所有必填字段均已填写',
						icon: 'none', // 显示为无图标的提示
						duration: 2000 // 提示框持续时间
					});
				}
			}
		},
	}
</script>

<style scoped>
	.container {
		margin: 20px;

	}

	.input-group {
		margin-bottom: 30px;

	}

	.label {
		font-size: 16px;
		color: #333;

		margin-bottom: 10px;

	}

	.input {


		max-width: 400px;

		padding: 10px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}

	.upload-group {
		margin-bottom: 40px;

	}

	.upload-title {
		font-size: 18px;
		font-weight: bold;
		color: #333;

		margin-bottom: 10px;

	}

	.id-card-label {
		font-size: 16px;
		color: #333;

		margin-top: 20px;

	}

	.home-button {
		margin: 0 auto;
		/* 让按钮在中间 */
	}
</style>