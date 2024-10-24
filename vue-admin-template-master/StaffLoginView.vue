<template>
    <div class="login-container">
      <el-form class="login-form">
        <div>
          <h1>管理员登陆</h1>
        </div>
        <el-form-item>
          <el-input v-model="account" placeholder="请输入账号" id="account"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input placeholder="请输入密码" v-model="password" show-password id="password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-input v-model="code" placeholder="请输入验证码" id="captcha"></el-input>
        </el-form-item>
        <div class="demo-image">
          <img :src="captchaUrl" @click="refreshCaptcha" alt="验证码" >

          <el-form-item class="login-button">
            <el-button type="primary" @click="doLogin" id="login">登陆</el-button>
          </el-form-item>
        </div>



      </el-form>
    </div>
  </template>
  
  <script>

  import { setToken } from '@/utils/authToken';
  import{ removeToken } from "@/utils/authToken";
  export default {
      data() {
          return {
              account: "",
              password: "",
              code: "",
              captchaUrl: "/api/captcha",
          };
      },
      methods: {
            refreshCaptcha() {
            this.captchaUrl = "/api/captcha?time="+new Date().getTime();
          },
          doLogin() {
            removeToken();
            this.$axios.post(
                "/staff/login",
                {
                    account: this.account,
                    password: this.password,
                    code:this.code,
                }
            ).then(res => {
                console.log(res)
                if (res.code == 1) {
                    console.log(res);
                    setToken(res.data)
                    this.$alert('登陆成功', '温馨提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                            this.$message({
                                type: 'info',
                                message: `action: ${ action }`
                            });
                            this.$store.dispatch("getStaffInfo")
                            console.log(this.$store.state);
                            
                            this.$router.push('/main'); // 在用户点击确定后跳转
                        }
                    });
                    
                } else if(res.code == -1){
                    this.$alert('账号或密码错误', '温馨提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                            this.$message({
                            type: 'info',
                            message: `action: ${ action }`
                            });
                        }
                    });
                }else if(res.code == 0){
                    this.$alert('验证码错误', '温馨提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                            this.$message({
                            type: 'info',
                            message: `action: ${ action }`
                            });
                        }
                    });
                }
              })
          }
      },
      created(){

      }
  }
  </script>
  
  <style>
  html, body, #app {
    height: 100%;
    margin: 0;
  }
  
  .login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f0f2f5;
  }
  
  .login-form {
    width: 300px;
    padding: 20px;
    background-color: white;
    box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
  }
  
  .el-form-item {
    margin-bottom: 20px;
  }
  
 
  .demo-image {
    display: flex;
    align-items: center;
    margin-top: -20px;
  }
  
  .demo-image img {
    margin-right: 70px; 

  }
  .demo-image button {
    margin-top: 20px; 
  }
  </style>