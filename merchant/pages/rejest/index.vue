<template>
	<view>
		<uni-table :data="tableData">
			<uni-th align="center">id</uni-th>
			<uni-th align="center">拒绝原因</uni-th>			
			<uni-tr v-for="(item, i) in tableData" :key="item.id" @click="goToDetail(item)">
				<uni-td align="center">{{ item.id }}</uni-td>
				<uni-td align="center">{{ item.reason }}</uni-td>
			</uni-tr>
		</uni-table>

		<uni-pagination :total="total" :page-size="pageSize" :current-page.sync="currentPage" @change="fetchData" />
	</view>
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
				tableData: [],
				currentPage: 1,
				pageSize: 10,
				total: 0,
				adminId: store.getters.getAdminId, 
			};
		},
		created() {
			this.fetchData(); // 在组件创建时获取数据
		},
		methods: {
			fetchData() {
				const getBillByIdVo = {
					adminId: store.getters.getAdminId, // 使用 adminId
					currentPage: this.currentPage,
					pageSize: this.pageSize
				};

				// #ifdef H5	
				axios.post('/PayMentApp-merchant/checkout/getCheckOutRejectByAdminId', getBillByIdVo)
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
				http.post('http://192.168.10.207:9090/PayMentApp-merchant/checkout/getCheckOutRejectByAdminId', getBillByIdVo)
					.then((res) => {
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