<template>
	<view class="chat-container">
		<!-- 聊天消息容器 -->
		<view class="chat-messages" :style="{ height: '400px', overflow: 'scroll' }">
			<view v-for="(message, index) in messages" :key="index" class="message">
				<strong>{{ message.user }}</strong>: {{ message.text }}
			</view>
		</view>
		<!-- 输入框用于输入新消息 -->
		<input v-model="newMessage" placeholder="输入消息..." @confirm="sendMessage" class="chat-input" />
		<!-- 发送按钮 -->
		<button @click="sendMessage" type="primary">发送</button>
	</view>
</template>

<script>
	import store from '../../store';
	let socket; // 定义 WebSocket 变量

	export default {
		data() {
			return {
				messages: [], // 存储聊天消息的数组
				newMessage: '', // 存储用户输入的消息
			};
		},
		onLoad() {
			const chatType = 'user_chat'; // 定义聊天类型
			// let userId = Math.floor(Math.random() * 100); // 随机生成用户 ID
			let userId = store.getters.getNickname;

			// 创建 WebSocket 连接
			socket = new WebSocket(`ws://127.0.0.1:9095/chatRequest?userId=${userId}&chatType=${chatType}`);

			// WebSocket 连接打开时触发
			socket.onopen = (event) => {
				console.log(event);
				console.log('WebSocket connection established');
			};

			// WebSocket 连接出错时触发
			socket.onerror = (error) => {
				console.error('WebSocket error: ' + error);
			};

			// 当接收到消息时触发
			socket.onmessage = (event) => {
				if (event.data === "请重新发起会话") {
					uni.showToast({
						title: event.data,
						icon: 'none'
					});
					return;
				}
				console.log('Message from server: ', event.data);
				const messagesArray = JSON.parse(event.data);
				this.messages = []; // 清空原有的 messages 数组
				messagesArray.forEach(item => {
					const messageData = JSON.parse(item);
					this.messages.push({
						user: messageData.userId,
						text: messageData.message,
					});
				});

				// 自动滚动到最新消息
				this.$nextTick(() => {
					const messagesContainer = this.$el.querySelector('.chat-messages');
					messagesContainer.scrollTop = messagesContainer.scrollHeight;
				});
			};
		},
		onUnload() {
			if (socket) {
				socket.close();
				console.log('WebSocket connection closed');
			}
		},
		methods: {
			sendMessage() {
				if (this.newMessage.trim()) {
					socket.send(this.newMessage);
					this.newMessage = ''; // 清空输入框
				}
			},
		},
	};
</script>

<style scoped>
	.chat-container {
		width: auto;
		margin: 0 auto;
		padding: 20px;
		border: 1px solid #ccc;
		border-radius: 5px;
		background-color: #f9f9f9;
	}

	.chat-messages {
		margin-bottom: 10px;
	}

	.message {
		margin-bottom: 5px;
	}

	.chat-input {
		border: 1px gray solid;
		margin-bottom: 10px;
	}
</style>