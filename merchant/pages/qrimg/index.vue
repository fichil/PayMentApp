<template>
	<div class="imgcontainer" v-if="isImageVisible" @click="closeImage">
		<img :src="imgurl" class="centered-image" @click.stop />
	</div>
</template>

<script>
	import {
		http
	} from '../../js_sdk/service';
	import store from '../../store';
	import axios from '../../utils/axios.js';

	export default {
		data() {
			return {
				imgurl: '',
				isImageVisible: false, // 控制图片是否可见
			};
		},
		async onLoad() {
			const token = store.getters.getToken;
			const merchantNumber = store.getters.getMerchantNumber;

			if (merchantNumber === 0) {
				// #ifdef H5
				try {
					const res2 = await axios.post('/PayMentApp-merchant/merchant/getUserInfo');
					this.setUserData(res2.data.data);
					this.afterAsync();
				} catch (error) {
					console.error('请求失败', error);
				}
				// #endif

				// #ifdef MP-WEIXIN
				try {
					const res2 = await http.post(
						'http://192.168.10.207:9090/PayMentApp-merchant/merchant/getUserInfo');
					this.setUserData(res2.data.data);
					this.afterAsync();
				} catch (error) {
					console.error('请求失败', error);
				}
				// #endif
			} else {
				this.afterAsync();
			}
		},
		methods: {
			setUserData(data) {
				store.commit('setUser', data);
				store.commit('setNickname', data.nickname);
				store.commit('setMerchantName', data.merchantName);
				store.commit('setWallet', data.wallet);
				store.commit('setMerchantNumber', data.merchantNumber);
			},
			afterAsync() {
				this.imgurl = store.getters.getQrImg;
				this.isImageVisible = true;
			},
			closeImage() {
				this.isImageVisible = false;
				this.navigateToHome();
			},
			navigateToHome() {
				uni.switchTab({
					url: '/pages/person/index'
				});
			},
		},
	};
</script>

<style>
	.imgcontainer {
		display: flex;
		justify-content: center;
		/* 横向居中 */
		align-items: center;
		/* 纵向居中 */
		position: fixed;
		/* 固定位置 */
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.5);
		/* 半透明背景 */
		z-index: 1000;
		/* 确保在最上层 */
	}

	.centered-image {
		max-width: 80%;
		/* 最大宽度 */
		max-height: 80%;
		/* 最大高度 */
		cursor: pointer;
		/* 鼠标指针样式 */
	}
</style>