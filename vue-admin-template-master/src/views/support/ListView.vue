<template>
  <div class="chat-container">
    <div class="chat-messages">
      <el-scrollbar style="height: 400px;">
        <div v-for="(message, index) in messages" :key="index" class="message">
          <strong>{{ message.user }}</strong>: {{ message.text }}
        </div>
      </el-scrollbar>
    </div>
    <el-input
      v-model="newMessage"
      placeholder="输入消息..."
      @keyup.enter="sendMessage"
      class="chat-input"
    />
    <el-button type="primary" @click="sendMessage">发送</el-button>
  </div>
</template>

<script>
var socket;
export default {
  data() {
    return {
      messages: [], // 存储聊天消息
      newMessage: '', // 用户输入的消息
      //modifieCurrentKey:'',
    };
  },
  props: {
    currentKey: {//0
      type: String,
      required: true,
    },
  },
  created() {
    console.log('子组件加载，当前的 key 值:', this.currentKey);
    //this.modifieCurrentKey = this.currentKey.split('?')[0]
    //console.log(this.modifieCurrentKey)
  const chatType = 'admin_chat';
    socket = new WebSocket('ws://127.0.0.1:9095/chatRequest?userId=' + this.currentKey + '&chatType=' + chatType);
    socket.onopen = function(event) {
      console.log(event);
      console.log('WebSocket connection established');
      socket.onerror = function(error) {
            console.error('WebSocket error: ' + error);
        };
    };

    socket.onmessage = function(event) {
        console.log('Message from server: ', event.data); // 输出来自服务器的消息
            // 可以在这里处理接收到的消息并更新 messages 数组
              // 假设 event.data 是你提供的 JSON 字符串数组
            const messagesArray = JSON.parse(event.data); // 解析 JSON 字符串数组
            // 清空原有的 messages 数组
           this.messages = []; // 重置 messages 数组
            messagesArray.forEach(item => {
                const messageData = JSON.parse(item); // 解析每个消息字符串
                // 提取 userId 和 message，并将其按指定格式推送到 messages 数组中
                this.messages.push({
                    user: messageData.userId, // 设置发送者为 userId
                    text: messageData.message, // 设置消息内容
                });
            });

            // 可选：如果需要自动滚动到最新消息
            this.$nextTick(() => {
                const messagesContainer = this.$el.querySelector('.chat-messages'); // 获取消息容器
                messagesContainer.scrollTop = messagesContainer.scrollHeight; // 自动滚动到底部
            });
    }.bind(this)
    // 在这里可以根据 currentKey 进行数据加载
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim()) {
        socket.send(this.newMessage);
        this.newMessage = ''; // 清空输入框
        // this.messages.push({
        //   user: '我', // 可以根据需要动态更改用户
        //   text: this.newMessage.trim(),
        // });
        // this.newMessage = ''; // 清空输入框
        // this.$nextTick(() => {
        //   const messagesContainer = this.$el.querySelector('.chat-messages');
        //   messagesContainer.scrollTop = messagesContainer.scrollHeight; // 自动滚动到底部
        // });
      }
    },
  },
};
</script>

<style scoped>
.chat-container {
  width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.chat-messages {
  margin-bottom: 10px;

}
.el-scrollbar__wrap {
    overflow-x: hidden;
}
.message {
  margin-bottom: 5px;
}
</style>