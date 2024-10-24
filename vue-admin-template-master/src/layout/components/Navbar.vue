<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar + '?imageView2/1/w/80/h/80'" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">

          <el-dropdown-item divided @click.native="openChangeNameDialog">
            <span style="display:block;">修改姓名</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="openChangePasswordDialog">
            <span style="display:block;">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">登出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <!-- 修改密码的弹窗 -->
      <el-dialog title="修改密码" :visible.sync="isChangePasswordDialogVisible">
        <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请确认新密码"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="isChangePasswordDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitChangePassword">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 修改姓名弹窗 -->
      <el-dialog title="修改姓名" :visible.sync="isChangeNameDialogVisible">
        <el-form :model="nameForm" :rules="nameRules" ref="nameForm">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="nameForm.name" placeholder="请输入姓名"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="isChangeNameDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitChangeName">确 定</el-button>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import { updatePassword, updateName } from '@/api/user'
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data() {
    return {
      isChangePasswordDialogVisible: false, // 控制修改密码弹窗的显示状态
      isChangeNameDialogVisible: false,
      nameForm: {
        name: ''
      },
      nameRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 18, message: '密码长度必须在 6 到 18 个字符之间', trigger: 'blur' }
        ],

        confirmPassword: [{ required: true, message: '请确认新密码', trigger: 'blur' }]
      }
    };
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'id'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    openChangeNameDialog() {
      this.isChangeNameDialogVisible = true; // 打开修改姓名弹窗
    },
    async submitChangeName() {
  this.$refs.nameForm.validate(async (valid) => {
    if (valid) {
      await updateName({
        id: this.id, // 使用从 Vuex 获取的 id
        nickname: this.nameForm.name
      })
      .then((res) => {
        
        this.$message.success('姓名修改成功');
        this.isChangeNameDialogVisible = false; // 关闭弹窗
        this.resetNameForm(); // 重置表单
      })
      
    }
  });
},
    openChangePasswordDialog() {
      this.isChangePasswordDialogVisible = true; // 打开修改密码弹窗
    },
    async submitChangePassword() {
      this.$refs.passwordForm.validate(async (valid) => {
        if (valid) {
          if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
            this.$message.error('新密码与确认密码不匹配');
            return;
          }

          try {
            await updatePassword({
              id: this.id,
              oldPassword: this.passwordForm.oldPassword,
              newPassword: this.passwordForm.newPassword
            });
            this.$message.success('密码修改成功');
            this.isChangePasswordDialogVisible = false; // 关闭弹窗
            this.resetPasswordForm(); // 重置表单
          } catch (error) {
            this.$message.error('密码修改失败，请重试');
          }
        }
      });
    },
    resetPasswordForm() {
      this.passwordForm.oldPassword = '';
      this.passwordForm.newPassword = '';
      this.passwordForm.confirmPassword = '';
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
