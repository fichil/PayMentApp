<template>
	<view>
		<view class="example-body">
			<uni-datetime-picker v-model="range" type="daterange" @change="fetchData" />
			<uni-input v-model="merchantNumber" placeholder="请输入商户编号" @input="fetchData" />
		</view>

		<uni-table :data="tableData">
			<uni-th align="center">订单号</uni-th>
			<uni-th align="center">金额</uni-th>
			<uni-th align="center">时间</uni-th>
			<uni-tr v-for="(item, i) in tableData" :key="item.id" @click="goToDetail(item)">
				<uni-td align="center">{{ item.orderNumber }}</uni-td>
				<uni-td align="center">{{ item.price }}</uni-td>
				<uni-td align="center">{{ formatDate(item.time) }}</uni-td>
			</uni-tr>
		</uni-table>

		<uni-pagination :total="total" :page-size="pageSize" :current-page.sync="currentPage" @change="fetchData" />
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
				tableData: [],
				range: ['2024-09-01', '2099-12-31'],
				merchantNumber: 'db123456', // 新增商户编号
				currentPage: 1,
				pageSize: 10,
				total: 0,
			};
		},
		created() {
			console.log(store.getters.getMerchantNumber)
		},
		mounted() {

		},
		methods: {
			afterAsync() {
				this.fetchData();
			},

			fetchData() {
				const getBillByIdVo = {
					startDateStr: this.range[0],
					endDateStr: this.range[1],
					merchantNumber: store.getters.getMerchantNumber, // 添加商户编号到请求参数
					currentPage: this.currentPage,
					pageSize: this.pageSize
				};
				// #ifdef H5	
				axios.post('/PayMentApp-merchant/order/getBillById', getBillByIdVo)
					.then((res) => {
						console.log("列表数据", res.data.data.list);
						this.tableData = res.data.data.list;
						this.total = res.data.data.total;
					})
					.catch(error => {
						console.error('获取数据失败:', error);
					});

				// #endif

				// #ifdef MP-WEIXIN
				console.log("yonhushuj ", getBillByIdVo);
				http.post('http://192.168.10.207:9090/PayMentApp-merchant/order/getBillById',getBillByIdVo)
					.then((res) => {
						console.log("yonhushuj ", getBillByIdVo);
						console.log("列表数据", res.data.data.list);
						this.tableData = res.data.data.list;
						this.total = res.data.data.total;
					})
					.catch(error => {
						console.error('获取数据失败:', error);
					});

				// #endif

			},
			formatDate(dateString) {
				const options = {
					year: 'numeric',
					month: '2-digit',
					day: '2-digit',
					hour: '2-digit',
					minute: '2-digit',
					second: '2-digit'
				};
				return new Date(dateString).toLocaleString('zh-CN', options); // 格式化日期
			},
		}
	}
</script>

<style scoped>
	.example-body {
		margin-bottom: 20px;
	}
</style>