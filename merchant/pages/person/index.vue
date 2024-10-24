<template>
	<view class="center">
		<view class="center_top">
			<view class="mask"></view>
		</view>
		<view class="center_box_bg">
			<view class="profily">
				<view class="base">
					<view class="profily_header"></view>
					<text>{{ nickname1 }}</text>
					<text class="store-name">{{ merchantName2 }}</text>
					<text class="store-name">余额：{{ balance3 }} 元</text>
					<image src="../../static/fumou-center-template/setting.png" mode=""></image>
				</view>
				<view class="order_status">
					<!-- 审核按钮 -->
					<view class="status" @click="state === 0 ? getexamine() : null"
						:class="{ 'disabled': state !== 0 }">
						<image class="icon" src="../../static/fumou-center-template/one.png" mode="aspectFill"></image>
						<text>审核</text>
					</view>
					<!-- 已审核按钮 -->
					<view class="status" @click="state === 1 ? getalreadyexmine() : null"
						:class="{ 'disabled': state !== 1 }">
						<image class="icon" src="../../static/fumou-center-template/2.png" mode="aspectFill"></image>
						<text>已审核</text>
					</view>
					<view class="status" @click="state === 1 ?downlaod(): null" :class="{ 'disabled': state !== 1 }">
						<image class="icon" src="../../static/fumou-center-template/2.png" mode="aspectFill"></image>
						<text>获取二维码</text>
					</view>
				</view>
			</view>
			<view class="baiban"></view>
			<view class="center_menu">
				<view class="menu_item" @click="inputDialogToggle">
					<image src="../../static/fumou-center-template/5.png" mode="aspectFill"></image>
					<text class="button-text" type="primary">修改资料</text>
				</view>
				<view class="menu_item" @click="withdrawDialogToggle">
					<image src="../../static/fumou-center-template/6.png" mode="aspectFill"></image>
					<text>提现</text>
				</view>
				<view class="menu_item" @click="navigateToWithdrawOrder">
					<image src="../../static/fumou-center-template/7.png" mode="aspectFill"></image>
					<text>提现订单查询</text>
				</view>
				<view class="menu_item" @click="navigateToRejust">
					<image src="../../static/fumou-center-template/8.png" mode="aspectFill"></image>
					<text>拒绝审核查询</text>
				</view>
				<view class="menu_item" @click="navigateToSupport">
					<image src="../../static/fumou-center-template/10.png" mode="aspectFill"></image>
					<text>客服</text>
				</view>
				<view class="menu_item" @click="logout">
					<image src="../../static/fumou-center-template/9.png" mode="aspectFill"></image>
					<text>退出登录</text>
				</view>

			</view>

			<!-- 个人资料修改弹窗 -->
			<view>
				<uni-popup ref="inputDialog" type="dialog">
					<uni-popup-dialog ref="inputClose" mode="input" title="输入内容" :value="formData.name"
						placeholder="请输入姓名" @confirm="dialogInputConfirm"></uni-popup-dialog>
				</uni-popup>
			</view>
			<!-- 提现弹窗 -->
			<view>
				<uni-popup ref="withdrawDialog" type="dialog">
					<uni-popup-dialog ref="withdrawClose" mode="input" title="提现金额" :value="withdrawAmount"
						placeholder="请输入提现金额" @confirm="withdrawConfirm"></uni-popup-dialog>
				</uni-popup>
			</view>
		</view>
	</view>

</template>

<script>
	import axios from '../../utils/axios';
	import store from '../../store';
	import {
		http
	} from '../../js_sdk/service';

	export default {
		async onLoad() {
			// #ifdef H5
			const token = store.getters.getToken;
			const merchantNumber = store.getters.getMerchantNumber;
			if (merchantNumber === 0) {
				try {
					const res2 = await axios.post('/PayMentApp-merchant/merchant/getUserInfo');
					console.log(res2);
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
			} else {
				this.afterAsync();
			}
			// #endif

			// #ifdef MP-WEIXIN
			const token = store.getters.getToken;
			const merchantNumber = store.getters.getMerchantNumber;
			if (merchantNumber === 0) {
				try {
					const res2 = await http.post('/PayMentApp-merchant/merchant/getUserInfo');
					console.log(res2);
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
			} else {
				this.afterAsync();
			}

			// #endif
		},

		data() {
			return {
				withdrawAmount: '',
				balance: 0,
				state: 0,
				merchantNumber: store.getters.getMerchantNumber,
				nickname: store.getters.getNickname,
				merchantName: store.getters.getMerchantName,
				showProfileEdit: false,
				formData: {
					name: '',
				},
				rules: {
					name: {
						required: true,
						message: '姓名不能为空'
					},
					withdrawAmount: {
						required: true,
						message: '提现金额不能为空'
					},
				}
			};
		},
		computed: {
			nickname1() {
				return this.nickname
			},
			merchantName2() {
				return this.merchantName
			},
			balance3() {
				return this.balance

			}


		},
		methods: {

			afterAsync() {
				this.nickname = store.getters.getNickname,
					this.merchantName = store.getters.getMerchantName,
					this.merchantNumber = store.getters.getMerchantNumber,
					this.balance = store.getters.getBalance,
					this.state = store.getters.getState
			},
			downlaod() {
				// 检查当前状态
				if (this.state === 1) {

					uni.navigateTo({
						url: "/pages/qrimg/index"
					});
				} else {

					uni.showToast({
						title: '您还未审核，请先进行审核。',
						icon: 'none'
					});
				}
			},
			navigateToRejust() {
				uni.navigateTo({
					url: "/pages/rejest/index"
				});
			},
			
			navigateToSupport(){
				uni.navigateTo({
					url:"/pages/support/index"
				})
			},

			getalreadyexmine() {
				// 检查当前状态
				if (this.state === 1) {
					uni.navigateTo({
						url: "/pages/alreadyexmine/index"
					});
				} else {
					uni.showToast({
						title: '您还未审核，请先进行审核。',
						icon: 'none'
					});
				}
			},
			getexamine() {
				if (this.state === 0) {
					uni.reLaunch({
						url: "/pages/examine/index"
					});
				} else {
					uni.showToast({
						title: '审核功能不可用，已经审核过了。',
						icon: 'none'
					});
				}
			},
			logout() {
				uni.clearStorage();
				uni.reLaunch({
					url: "/pages/login/index"
				});

			},
			navigateToWithdrawOrder() {
				uni.reLaunch({
					url: '/pages/withdrawal/index'
				})
			},
			inputDialogToggle() {
				this.$refs.inputDialog.open();
			},
			dialogInputConfirm(value) {

				// #ifdef H5
				if (value) {
					this.formData.name = value; // 更新姓名
					axios.post("/PayMentApp-merchant/merchant/updateAdmin", {
						nickname: this.formData.name,
						id: store.getters.getAdminId,
					}).then(response => {
						if (response.status === 200) {
							this.nickname = this.formData.name
							console.log('更新成功:', response.data);
							uni.showToast({
								title: '修改成功！',
								icon: 'success',
								duration: 2000
							});
							this.$refs.inputDialog.close(); // 关闭弹窗
						}
					});
				} else {
					console.log('姓名不能为空');

				}
				// #endif

				// #ifdef MP-WEIXIN
				if (value) {
					this.formData.name = value; // 更新姓名
					http.post("http://192.168.10.207:9090/PayMentApp-merchant/merchant/updateAdmin", {
						nickname: this.formData.name,
						id: store.getters.getAdminId,
					}).then(response => {
						if (response.status === 200) {
							this.nickname = this.formData.name
							console.log('更新成功:', response.data);
							uni.showToast({
								title: '修改成功！',
								icon: 'success',
								duration: 2000
							});
							this.$refs.inputDialog.close(); // 关闭弹窗
						}
					});
				} else {
					console.log('姓名不能为空');

				}
				// #endif

			},
			withdrawDialogToggle() {
				this.$refs.withdrawDialog.open();
			},
			refreshPage() {

				window.location.reload(); // 刷新当前页面
			},


			withdrawConfirm(value) {
				// #ifdef H5
				if (value) {
					const merchantNumber = this.getMerchantNumber;
					this.withdrawAmount = value; // 更新提现金额
					console.log('提现金额:', this.withdrawAmount); // 确保输出正确的提现金额

					// 检查余额是否足够
					if (this.withdrawAmount > this.balance) {
						console.log('余额不足，无法提现'); // 打印日志
						uni.showToast({
							title: '余额不足，无法提现',
							icon: 'none',
							duration: 2000
						});
						return; // 提前返回，不执行 API 调用
					}

					// 这里调用提现的 API
					axios.post("/PayMentApp-merchant/merchantInfo/MerchantAddWithdrawOrder", {
						amount: this.withdrawAmount,
						merchantNumber: store.getters.getMerchantNumber,
					}).then(response => {
						if (response.status === 200) {
							this.balance = this.balance - this.withdrawAmount; // 从余额中扣除提现金额
							console.log('提现成功:', response.data);

							// 弹出提示
							uni.showToast({
								title: '提现成功！',
								icon: 'success',
								duration: 2000
							});

							this.$refs.withdrawDialog.close(); // 关闭弹窗

							// 强制刷新页面
							this.refreshPage(); // 调用刷新方法
						}
					}).catch(error => {
						console.error('提现失败:', error); // 错误处理
						uni.showToast({
							title: '提现失败，请稍后重试',
							icon: 'none',
							duration: 2000
						});
					});
				} else {
					console.log('提现金额不能为空');
				}
				// #endif

				// #ifdef MP-WEIXIN
				if (value) {
					const merchantNumber = this.getMerchantNumber;
					this.withdrawAmount = value; // 更新提现金额
					console.log('提现金额:', this.withdrawAmount); // 确保输出正确的提现金额

					// 检查余额是否足够
					if (this.withdrawAmount > this.balance) {
						console.log('余额不足，无法提现'); // 打印日志
						uni.showToast({
							title: '余额不足，无法提现',
							icon: 'none',
							duration: 2000
						});
						return; // 提前返回，不执行 API 调用
					}

					// 这里调用提现的 API
					http.post("http://192.168.10.207:9090/PayMentApp-merchant/merchantInfo/MerchantAddWithdrawOrder", {
						amount: this.withdrawAmount,
						merchantNumber: store.getters.getMerchantNumber,
					}).then(response => {
						if (response.status === 200) {
							this.balance = this.balance - this.withdrawAmount; // 从余额中扣除提现金额
							console.log('提现成功:', response.data);

							// 弹出提示
							uni.showToast({
								title: '提现成功！',
								icon: 'success',
								duration: 2000
							});

							this.$refs.withdrawDialog.close(); // 关闭弹窗

							// 强制刷新页面
							this.refreshPage(); // 调用刷新方法
						}
					}).catch(error => {
						console.error('提现失败:', error); // 错误处理
						uni.showToast({
							title: '提现失败，请稍后重试',
							icon: 'none',
							duration: 2000
						});
					});
				} else {
					console.log('提现金额不能为空');
				}
				// #endif
			}
		}
	}
</script>

<style lang="scss">
	page {
		height: 100%;
	}

	.profily,
	.profily_header {
		border-radius: 8px;
	}

	.center {
		height: 100%;

		&_top {
			height: 18%;
			background: url('../../static/fumou-center-template/preview.jpg') no-repeat 0% 50%;
			background-size: cover;

			.mask {
				background: rgba(0, 0, 0, .4);
				height: 100%;
			}
		}

		&_box_bg {
			background: #F9F9F9;
			position: relative;

			.profily {
				position: absolute;
				width: 90%;
				margin: 0 auto;
				top: -100upx;
				left: 5%;
				background: #FEFEFE;
				padding: 35upx;
				box-sizing: border-box;
				box-shadow: 0px 2px 5px #EDEDED;
			}
		}
	}

	.base {
		display: flex;
		align-items: center;
		border-bottom: 2px solid #F6F6F6;
		padding-bottom: 35upx;
		position: relative;

		.profily_header {
			height: 120upx;
			width: 120upx;
			background-image: url('../../static/fumou-center-template/header.jpg');
			background-size: 100%;
		}

		text {
			margin-left: 20px;
			font-size: 30upx;
		}

		.store-name {
			margin-left: 20px;
			font-size: 24upx;
			color: #888;
		}

		image {
			position: absolute;
			height: 40upx;
			width: 40upx;
			right: 0px;
			top: 0px;
		}
	}

	.order_status {
		display: flex;
		justify-content: space-between;
		margin-top: 35upx;

		.status {
			width: 140upx;
			font-size: 24upx;
			text-align: center;
			letter-spacing: .5px;
			display: flex;
			flex-direction: column;

			.icon {
				width: 50upx;
				height: 50upx;
				margin: 0 auto;
				margin-bottom: 5px;
			}
		}
	}

	.baiban {
		background: #FEFEFE;
		height: 300upx;
	}

	.center_menu {
		width: 100%;
		display: inline-block;

		.menu_item {
			font-size: 28upx;
			letter-spacing: 1px;
			padding: 14px 5%;
			background: #FEFEFE;
			overflow: hidden;
			box-sizing: border-box;
			display: flex;
			align-items: center;
			position: relative;
			border-bottom: 1px solid #EFEFEF;

			&:hover {
				background: #F6F6F6 !important;
			}

			&::after {
				content: '';
				width: 30upx;
				height: 30upx;
				position: absolute;
				right: 5%;
				background: url('../../static/fumou-center-template/right.png') no-repeat;
				background-size: 100%;
			}

			text:nth-of-type(1) {
				margin-left: 10px;
			}

			image {
				width: 40upx;
				height: 40upx;
			}

			&:nth-of-type(4) {
				margin-top: 10px;
			}
		}
	}

	.profile_edit {
		background: #FFF;
		padding: 20upx;
		margin-top: 20upx;
		border: 1px solid #EEE;
		border-radius: 8px;

		.close_button {
			margin-top: 10upx;
			color: #007AFF;
			cursor: pointer;
		}
	}

	.disabled {
		opacity: 0.5;
		/* 使按钮看起来变淡 */
		pointer-events: none;
		/* 禁止点击事件 */
	}
</style>