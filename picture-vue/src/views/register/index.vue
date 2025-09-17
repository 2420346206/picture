<template>
  <div class="register-container">
    <el-form
      ref="registerForm"
      :model="registerForm"
      :rules="registerRules"
      class="register-form"
      auto-complete="on"
      label-position="left"
    >

      <div class="title-container">
        <h3 class="title">云图库 - 用户注册</h3>
      </div>

      <!-- 用户名 -->
      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="registerForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <!-- 密码 -->
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="registerForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd('password')">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <!-- 确认密码 -->
      <el-form-item prop="confirmPassword">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType + '-confirm'"
          v-model="registerForm.confirmPassword"
          :type="passwordType"
          placeholder="确认密码"
          name="confirmPassword"
          tabindex="3"
          auto-complete="on"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd('confirmPassword')">
          <svg-icon :icon-class="confirmPasswordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>


      <div class="tips">
        <span style="margin-right:10px;">已有账号？</span>
        <el-link type="primary" @click="goLogin">去登录</el-link>
      </div>

      <!-- 注册按钮 -->
      <el-button
        :loading="loading"
        type="primary"
        style="width:100%;margin-bottom:30px;margin-top:10px"
        @click.native.prevent="handleRegister"
      >
        注册
      </el-button>

    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Register',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入正确的用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    // 确认密码校验
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      registerRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      confirmPasswordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (field === 'password') {
        this.passwordType = this.passwordType === 'password' ? '' : 'password'
        this.$nextTick(() => this.$refs.password && this.$refs.password.focus())
      } else {
        this.confirmPasswordType = this.confirmPasswordType === 'password' ? '' : 'password'
        this.$nextTick(() => this.$refs.confirmPassword && this.$refs.confirmPassword.focus())
      }
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/register', this.registerForm).then(() => {
            this.$router.push('/login')
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('注册失败，表单校验不通过')
          return false
        }
      })
    },
    goLogin() {
      this.$router.push('/login');
    }
  }
}
</script>

<style lang="scss">
/* 修复 input 背景和光标颜色 */
$bg:#283443;
$light_gray:#fff;
$cursor:#fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .register-container .el-input input {
    color: $cursor;
  }
}

/* 隐藏 Chrome 的密码显示/清除按钮 */
input::-webkit-contacts-auto-fill-button,
input::-webkit-credentials-auto-fill-button,
input::-webkit-clear-button,
input::-webkit-password-toggle {
  display: none !important;
  -webkit-appearance: none;
}

/* 隐藏 IE / Edge (旧版) 的按钮 */
input::-ms-reveal,
input::-ms-clear {
  display: none !important;
}

.register-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 90%;
    input {
      background: transparent;
      border: 0;
      -webkit-appearance: none;
      border-radius: 0;
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
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.register-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .register-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
    width: 100%;           // 让tips占满父元素宽度
    max-width: 520px;      // 与登录按钮同宽
    margin: 0 auto 10px;   // 居中显示
    display: flex;
    justify-content: flex-end; // 文字靠右对齐
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
      color: $light_gray;
      margin: 0 auto 40px;
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
}
</style>
