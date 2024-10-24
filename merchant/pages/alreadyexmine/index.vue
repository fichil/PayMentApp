<template>
	
	<view class="header">
		<button @click="navigateToHome" class="home-button">返回主页</button>
	</view>
	<view class="exminecontainer">
		<view class="input-group">
			<view class="label">法人姓名</view>
			<input v-model="legalPersonName" placeholder="法人姓名" class="input" disabled />
		</view>
		<view class="input-group">
			<view class="label">法人电话</view>
			<input v-model="legalPersonPhone" placeholder="法人电话" class="input" disabled />
		</view>

		<view class="input-group">
			<view class="label">商户名</view>
			<input v-model="merchantName" placeholder="商户名" class="input" disabled />
		</view>

		<view class="upload-group">
			<view class="upload-title">营业资格证</view>
			<view class="uploaded-images">
				<image v-if="ceshiData" :src="ceshiData" class="uploaded-image" />
			</view>
		</view>

		<view class="upload-group">
			<view class="upload-title">法人身份证</view>
			<view class="id-card-label">身份证正面</view>
			<view class="uploaded-images">
				<image v-if="idCardFront" :src="idCardFront" class="uploaded-image" />
			</view>

			<view class="id-card-label">身份证反面</view>
			<view class="uploaded-images">
				<image v-if="idCardBack" :src="idCardBack" class="uploaded-image" />
			</view>
		</view>
	</view>
</template>

<script>
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
				ceshiData: '',
				legalPersonName: '', // 法人姓名
				legalPersonPhone: '', // 法人电话
				merchantName: '', // 商户名
				adminId: '',
				idCardFront: '',
				idCardBack: '',
			}
		},

		methods: {
			navigateToHome() {
				uni.switchTab({
					url: '/pages/person/index'
				});
			},
			afterAsync() {
				this.fetchData();
			},
			fetchData() {
				// #ifdef H5
				axios.get('/PayMentApp-merchant/checkout/selectCheckOut', {
						params: {
							adminId: store.getters.getAdminId
						}
					})
					.then((res) => {
						console.log("列表数据", res.data.data.leglePerson);
						this.legalPersonName = res.data.data.leglePerson; 
						this.legalPersonPhone = res.data.data.tele; 
						this.merchantName = res.data.data.qrImg; 
						this.idCardFront = res.data.data.identityFront; 
						this.idCardBack = res.data.data.identityBack; 
						this.ceshiData = res.data.data.certificate;

					})
					.catch(error => {
						console.error('获取数据失败:', error);
					});
				// #endif

				// #ifdef MP-WEIXIN

				http.get('http://192.168.10.207:9090/PayMentApp-merchant/checkout/selectCheckOut', {
						params: {
							adminId: store.getters.getAdminId
						}
					})
					.then((res) => {
						console.log("列表数据", res.data.data.leglePerson);
						this.legalPersonName = res.data.data.leglePerson; // 法人姓名
						this.legalPersonPhone = res.data.data.tele; // 法人电话
						this.merchantName = res.data.data.qrImg; // 商户名
						this.idCardFront = res.data.data.identityFront; // 法人身份证正面图片
						this.idCardBack = res.data.data.identityBack; // 法人身份证反面图片
						this.ceshiData = res.data.data.certificate;

						console.log("身份证正面图片路径:", this.idCardFront);
						console.log("身份证反面图片路径:", this.idCardBack);
						console.log("营业资格证图片路径:", this.ceshiData);
					})
					.catch(error => {
						console.error('获取数据失败:', error);
					});
				// #endif


			}
		}
	}
</script>

<style scoped>
	.exminecontainer {
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