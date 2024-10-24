<template>
   <div id="app">
 
 <el-container>
    <el-aside style="width:200px; height: 120vh;">
        <el-row class="tac" >
            <el-col  >
               <el-menu
                default-active="2"
                class="el-menu-vertical-demo"
                background-color="#545c64"
                text-color="#fff"
                active-text-color="#fff"
                v-for="(item, index) in menuItems"
                :key="index"> 
                <router-link
                  :key="index"
                  to="/support/MainView/ListView">
                <el-menu-item :index="String(index)"
                @click="handle(item)">
                  <span slot="title">{{ item.userId }}</span>
                  <!-- 条件渲染未读标识 -->
                  <span v-if="item.state == 1" class="unread-badge">未读</span>
                </el-menu-item>
                </router-link>
               
              </el-menu> 
            </el-col>
          </el-row>
    </el-aside>
    <el-container>
      <!-- <el-header>
        <div>
          <el-button id="bt" style="margin-left: 60px;" @click="signOut()">退出</el-button>
        </div>
      </el-header> -->
      <el-main> <router-view :key="currentKey" :current-key="currentKey"></router-view> </el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </el-container>
</div>

</template>

<script>
var socket;
export default {
  name: 'App',
  data(){
    return {
      currentKey: "0", // 或者使用 this.$route.params.id
      menuItems: []
    }
  },

 created(){
  let th = this;
  let userId = 'admin';
  const chatType = 'admin_list';
    socket = new WebSocket('ws://127.0.0.1:9095/chatRequest?userId=' + userId + '&chatType=' + chatType);
    socket.onopen = function(event) {
      console.log(event);
      console.log('WebSocket connection established');
      socket.onerror = function(error) {
            console.error('WebSocket error: ' + error);
        };
    };

    socket.onmessage = function(event) {
        console.log('Message from server: ', event.data);
        const dataObject = JSON.parse(event.data);
        console.log('Parsed object from server:', dataObject);
        // 将原始数据转换为所需的对象数组
        let mo = Object.keys(dataObject).map(userId => {
        return {
            userId: userId,         // 添加 userId
            state: parseInt(dataObject[userId]) // 将 state 转换为整数
        };
      });
      th.menuItems = mo;
      console.log("当前用户名" , mo)
      const exists = th.menuItems.some(item => item.userId == th.currentKey);
      if (exists) {
        console.log(`currentKey 存在于 menuItems 中`);
      } else {
          if (th.$route.path !== '/MainView/ListView') {
          th.$router.push({ path: '/support/MainView/ListView' });
        } else {
          console.log('已经处于当前页，无需跳转');
        }
       th.currentKey='0'
      }

    };
 },
  methods:{
    handle(item) {
      socket.send(item.userId);
      // 执行逻辑
       console.log('点击了菜单:', item.userId);
        // 这里改变 currentKey 的方式
        this.currentKey = item.userId; // 每次点击后增加 currentKey，确保唯一性
    },
 
      }
  

}


</script>

<style scoped>

.unread-badge {
  background-color: red; /* 未读标识的背景色 */
  color: white; /* 未读标识的文字颜色 */
  padding: 2px 6px; /* 内边距 */
  border-radius: 12px; /* 圆角效果 */
  font-size: 12px; /* 字体大小 */
  margin-left: 8px; /* 与菜单项的间距 */
}

.el-header, .el-footer {
    background-color: #B3C0D0;
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  
  .el-aside {
    background-color: #545c64;
    color: #333;
    text-align: center;
    line-height: 200px;
  }
  
  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    /* line-height: 160px; */
  }
  
  body > .el-container {
    margin-bottom: 40px;
  }
  
  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }
  
  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
</style>