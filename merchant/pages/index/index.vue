<template>
	<view class="container">
		<uni-card title="今日交易额">
			<text class="uni-body">￥<text>{{todayPrice}}</text></text>
		</uni-card>
		<uni-card title="今日交易数">
			<text class="uni-body"><text>{{todayCount}}</text></text>
		</uni-card>
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
				todayPrice: 0,
				todayCount: 0
			}
		},
		computed: {
			todayPrice() {
				return this.todayPrice
			},
			todayCount() {
				return this.todayCount
			}
		},
		mounted() {

		},
		methods: {
			afterAsync() {
				this.getInfo();
			},

			getInfo() {
				// #ifdef H5

				axios.get('/PayMentApp-merchant/order/getInfo', {
					params: {
						merchantNumber: store.getters.getMerchantNumber
					}
				}).then((res) => {
					console.log(res)
					this.todayCount = res.data.data.todayCount;
					this.todayPrice = res.data.data.todayPrice;
				})

				// #endif

				// #ifdef MP-WEIXIN

				http.get('http://192.168.10.207:9090/PayMentApp-merchant/order/getInfo', {
					params: {
						merchantNumber: store.getters.getMerchantNumber
					}
				}).then((res) => {
					console.log(res)
					this.todayCount = res.data.data.todayCount;
					this.todayPrice = res.data.data.todayPrice;
					console.log(this.todayCount, this.todayPrice)
				})

				// #endif
			}
		}
	}
</script>

<style>
	.container {
		padding: 20px;
		font-size: 14px;
		line-height: 24px;
	}
</style>