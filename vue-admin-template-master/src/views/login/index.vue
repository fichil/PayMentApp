<template>
  <div class="login-container">
    <div class="image-placeholder">
      <!-- Logo 图像 -->
      <img src="../../icons/img/logo.png" alt="Logo" style="margin-top: 100px;">
    </div>

    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
      label-position="left">

      <div class="title-container">
        <!-- 标题 -->
        <h3 class="title">运营平台</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <!-- 用户名图标 -->
          <svg-icon icon-class="user" />
        </span>
        <el-input ref="username" v-model="loginForm.username" placeholder="用户名" name="username" type="text" tabindex="1"
          auto-complete="on" />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <!-- 密码图标 -->
          <svg-icon icon-class="password" />
        </span>
        <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType" placeholder="密码"
          name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
        <span class="show-pwd" @click="showPwd">
          <!-- 显示/隐藏密码图标 -->
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-form-item>
        <div class="captcha-container">
          <!-- 验证码输入框 -->
          <el-input v-model="loginForm.code" placeholder="请输入验证码" id="captcha" class="captcha-input" style="width:66%"
            ref="code" name="code"></el-input>
          <!-- 验证码图像，点击可刷新 -->
          <img :src="captchaUrl" alt="验证码" @click="refreshCaptcha" style="width: 34%; height: 40px; cursor: pointer">
        </div>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
        @click.native.prevent="handleLogin">登录</el-button>

    </el-form>
    <div class="footer">
      <a href="https://beian.miit.gov.cn/" target="_blank">闽ICP备2024072712号-1</a>
    </div>
  </div>

</template>

<script>


export default {
  name: 'Login',
  data() {

    const validatePassword = (rule, value, callback) => {
      if (value.length <= 0) {
        callback(new Error('请输入密码'));
      } else {
        callback();
      }
    };

    return {
      loginForm: {
        username: 'admin',
        password: 'admin',
        code: ''
      },
      loginRules: {

        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      code: '',
      captchaUrl: "api/PayMentApp-platform/admin/code"

    };
  },
  created() {
    this.getcode();
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  methods: {

    getcode() {
      this.refreshCaptcha();
    },
    refreshCaptcha() {
      this.$store.dispatch("user/refreshCaptcha").then((response) => {
        this.captchaUrl = response
      })
      // this.captchaUrl = 

      // this.captchaUrl = "api/PayMentApp-platform/admin/code" + "?" + new Date().getTime();

    },
    showPwd() {
      this.passwordType = this.passwordType === 'password' ? '' : 'password';
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' });
            this.loading = false;
          }).catch(error => {
            console.error('Login error:', error);
            this.loading = false;
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  }
};
</script>

<style lang="scss">
$bg: #ffffff;
/* 修改背景颜色为白色 */
$dark_gray: #666;
/* 修改深灰色以匹配新的背景 */
$light_gray: #eee;
/* 可根据需要调整 */
$cursor: #000;
/* 修改光标颜色为黑色 */

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  /* 使元素居中对齐 */
  min-height: 100vh;
  /* 使容器至少填满整个视口高度 */
  width: 100%;
  background-color: $bg;
  background-image: url('../../icons/img/bg.jpeg');
  background-size: cover;

  /* 应用白色背景 */
  overflow: hidden;

  .image-placeholder {
    display: flex;
    justify-content: center;

    /* 水平居中 */
    margin-bottom: 20px;
    /* 图片与表单之间的间距 */

    img {
      max-width: 30%;
      /* 设置最大宽度为容器的 30% */
      height: auto;
      /* 保持比例 */
    }
  }

  .captcha-container {
    display: flex;
    align-items: center;
    /* 垂直居中 */
    justify-content: space-between;
    /* 水平分布 */
    width: 100%;
    /* 确保容器宽度为100% */

    .captcha-input {
      width: 66%;
      /* 保持原样 */
    }

    img {
      width: 34%;
      /* 保持原样 */
      height: 40px;
      /* 保持原样 */
      cursor: pointer;
      /* 保持原样 */
    }
  }

  .login-form {
    position: relative;
    width: 100%;
    max-width: 520px;
    /* 最大宽度 */
    padding: 0px 35px 0;
    /* 调整上部填充以保持间距 */
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: $dark_gray;
    /* 使用适合白色背景的颜色 */
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $dark_gray;
      /* 更新标题颜色以适应背景 */
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;

      input {
        -webkit-appearance: none;
        /* Webkit browsers */
        appearance: none;
        /* Standard property for better compatibility */
      }

      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(0, 0, 0, 0.1);
    /* 调整边框颜色为适应白色背景 */
    background: rgba(255, 255, 255, 0.9);
    /* 新增背景使其更明显 */
    border-radius: 5px;
    color: #454545;
    /* 字体颜色 */
  }

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .el-input input {
      color: $cursor;
      /* 支持条件下设置输入框颜色 */
    }
  }
}

.footer {
  position: fixed;
  /* 固定定位 */
  bottom: 0;
  /* 距离底部0 */
  left: 0;
  /* 距离左边0 */
  right: 0;
  /* 距离右边0 */
  background-color: #f1f1f1;
  /* 背景色 */
  text-align: center;
  /* 内容居中 */
  padding: 10px;
  /* 内边距 */
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  /* 上边阴影 */
}

.footer a {
  color: #007BFF;
  /* 链接颜色 */
  text-decoration: none;
  /* 去掉下划线 */
  margin: 0 10px;
  /* 左右间距 */
}

.footer a:hover {
  text-decoration: underline;
  /* 鼠标悬停时加下划线 */
}
</style>
