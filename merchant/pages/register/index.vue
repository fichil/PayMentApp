<template>
	<view class="mainContain">
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
					<uni-forms-item label="确认密码" required name="twicePassword" class="word">
						<uni-easyinput v-model="formData.twicePassword" placeholder="请确认密码" type="password"
							prefixIcon="info" />
					</uni-forms-item>
					<uni-forms-item label="昵称" required name="nickname" class="word">
						<uni-easyinput v-model="formData.nickname" placeholder="请输入昵称" 
							prefixIcon="info" />
					</uni-forms-item>
					<uni-forms-item label="验证码" required name="code" class="word">
						<view class="code-container">
							<uni-easyinput v-model="formData.code" placeholder="请输入验证码" maxlength="4"
								style="width: 40%;" />
							<image :src="imgUrl" style="height: 35px; width: 40%; margin-left: 10px;"
								@click="refreshCode"></image>
						</view>
					</uni-forms-item>
				</uni-forms>
				<button type="primary" @click="submit('valiForm')">注册</button>
				<button type="default" @click="back()" style="margin-top: 10px;">返回</button>
			</view>
		</view>

	</view>
</template>

<script>
	import axios from '@/utils/axios.js'
	import store from '../../store';

	export default {
		data() {
			return {
				imgUrl: "/apr/PayMentApp-merchant/merchant/code?id=" + new Date().getTime(),
				// 自定义表单数据
				formData: {
					account: 'merchant01',
					password: '123456',
					twicePassword: '',
					nickname: '',
					code: '',
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
					twicePassword: {
						rules: [{
							required: true,
							errorMessage: '确认密码不能为空'
						}, {
							validator: (rule, value, callback, source) => {
								console.log("12312312313")
								if (value !== source.password) {
									callback('确认密码与密码不一致');
								} else {
									callback(); // 验证通过
								}
							}
						}]
					},
					nickname: {
						rules: [{
							required: true,
							errorMessage: '昵称不能为空'
						}, {
							pattern: /^.{2,16}$/,
							errorMessage: '昵称需在2-16字符之间'
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
					axios.post("/PayMentApp-merchant/merchant/register", res).then((res) => {
						if (this.formData.password != this.formData.twicePassword) {
							uni.showToast({
								icon: 'error',
								title: '两次密码不一致'
							});
							this.refreshCode();
							this.formData.code = '';
							this.formData.password = '';
							return;
						}
						console.log("成功发送", res);
						if (res.data.code != 200) {
							uni.showToast({
									icon: 'error',
									title: res.data.msg
								}),
								this.refreshCode();
							this.formData.code = '';
							this.formData.password = '';
							this.formData.twicePassword = '';
							return
						}

						//成功注册
						uni.showToast({
							title: `注册成功,稍后跳转登陆界面`,
						});
						
						// 跳转
						setTimeout(() => {
							uni.switchTab({
								url: "../../pages/login/index"
							})
						}, 3000)

					})

				}).catch(err => {
					console.log('err', err);
					uni.showToast({
						icon: 'error',
						title: err[0].errorMessage
					});
					this.refreshCode();
					this.formData.code = '';
					this.formData.password = '';
				})
			},

			refreshCode() {
				this.imgUrl = "/apr/PayMentApp-merchant/merchant/code?id=" + new Date().getTime();
			},
			
			back(){
				uni.navigateBack()
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