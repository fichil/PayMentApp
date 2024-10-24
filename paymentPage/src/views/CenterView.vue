<template>
  <div>
    <div>
      <van-cell-group>
        <van-cell title="Aggregate支付"  size="large"  
      :style="{ backgroundColor: color }">
        <template #title>
          <img src="../static/imgs/笑脸.png" id="logo2">
          <span class="custom-title">Aggregate payments</span>
        </template>
      </van-cell>
    </van-cell-group>

      <!-- <div id="logo"> -->
          <img src="../static/imgs/logo.png" id="logo">
          
      <!-- </div> -->

      <van-field
      readonly
      clickable
      :value="value"
      type="text"
      maxlength="9"
      @input="onInput()"
      @touchstart.native.stop="show = true"
       label="金额￥"
       class="custom-field" 
      />

    </div>
      <van-number-keyboard
      :show="show" 
      theme="custom"
      extra-key="."
      close-button-text="支付"
      v-model="value"
      @close="onPay"
      />
     
  </div>

</template>

<script>
// import { Toast } from 'vant';
import { Dialog } from 'vant';
var socket;
export default {
  data() {
      return {
      //    show:true
      show: true,
      value: '', // 商品价格
      color: "lightblue",
      userId: "999",
      payment: ""
      };
  },
  mounted() {
 

    socket = new WebSocket('ws://ccj.nat300.top/browserInfo');
    socket.onopen = function(event) {
      console.log(event);
      console.log('WebSocket connection established');
      socket.onerror = function(error) {
            console.error('WebSocket error: ' + error);
        };
    };

    socket.onmessage = function(event) {
        console.log('Message from server: ', event.data);
    };
    socket.onclose = function(event) {
        console.log(event);
        console.log('Connection closed');
    };

    console.log('当前路由信息:', this.$route);
    console.log('ID:', this.$route.query.id);
    console.log('Payment:', this.$route.query.payment);
    this.userId = this.$route.query.id;
    this.payment = this.$route.query.payment;
  },
  methods: {
    pay() {
      
    },
    onPay() {
      const regex = /^\d{0,6}(\.\d{0,2})?$/;
      if(this.value != "" && regex.test(this.value)) {
        socket.send("请求支付");
        var self = this
          socket.onmessage = function(event) {
          console.log('Message from server: ', event.data);
          if( event.data == "ok"){
            console.log("p",self.payment)
              if (self.payment != "") {
                 var params = "?merchantId="+self.userId+"&total=0.01"
                if (self.payment == "wechat") {
                  console.log("微信支付")
                  const backendUrl = 'http://ccj.nat300.top/wxPay/getWXOAuth2Code'+params; 
                  window.location.href = backendUrl; 
                } else if (self.payment == "alipay") {
                  const backendUrl = 'http://ccj.nat300.top/aliPay/createOrder'+params; 
                  window.location.href = backendUrl; 
                }
            }        
          }
          };
       
      } else {
        Dialog.alert({
                title: '输入有误',
                message: '正整数输入不超过6位，带小数不超过两位，特殊字符会导致支付失败。',
                theme: 'round-button',
              }).then(() => {
                this.value = ""
              });
        return;
      }
    },
    // 处理输入框限制逻辑
    onInput(value) {
      // alert("onInput work.")
      // 正则表达式限制正整数或者两位小数
      const regex = /^\d{0,6}(\.\d{0,2})?$/;
      if (regex.test(value)) {
          // 如果是合法输入，检查位数
          if (value.includes('.')) {
              // 小数的总长度限制为8位
              if (value.length <= 8) {
                  this.value = value;
              }else {
                // Dialog.alert({
                //     message: '超出支付限制',
                //     theme: 'round-button',
                //   }).then(() => {
                //     this.value = ''
                //   });
                alert("213123")
              }
          } else {
              // 整数限制为6位
              if (value.length <= 6) {
                  this.value = value;
              }else {
                alert("21312123123")
              }
          }
        }else{
              Dialog.alert({
                title: '输入有误',
                message: '正整数输入不超过6位，带小数不超过两位',
                theme: 'round-button',
              }).then(() => {
                this.value = ""
              });
        }
      }


  },
};
</script>

<style scoped>

.van-cell--large {
height: 60px;
}

.custom-title{
margin: 0 auto;
font-size: larger;
font-style: fangsong;
font-weight: 800;
color: aliceblue;
}

.custom-field {
height: 100px; /* 调整为你想要的高度 */
width: 100%;
line-height: 120px; /* 确保文本垂直居中 */
font-size: 30px; /* 调整字体大小 */
position: relative;
  top: 140px;
  /* border: 1px solid; */
}
/* 调整图标大小 */
.custom-field .van-field__left-icon {
font-size: 30px; /* 设置图标的大小 */
}

#logo{
position: relative;
/* border: 1px solid; */
top: 50px;
width: 100px;
height: 100px;
}

#logo2 {
  height: 27px;
  position: absolute;
  top: 12px;
  left: 15%;
}
</style>