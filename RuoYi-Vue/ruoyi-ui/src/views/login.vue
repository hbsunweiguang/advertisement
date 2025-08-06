<template>
  <div class="login">
    <div class="login-container">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <h3 class="title">{{title}}</h3>
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
          >
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
        <el-form-item style="width:100%;">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
          <div style="float: right;" v-if="register">
            <router-link class="link-type" :to="'/register'">立即注册</router-link>
          </div>
          <!-- 弹窗滑块验证 -->
          <el-dialog
            title="滑块验证"
            :visible.sync="sliderDialogVisible"
            width="350px"
            :close-on-click-modal="false"
            :show-close="true"
          >
            <slide-verify
              ref="slideVerify"
              :l="42"
              :r="10"
              :w="310"
              :h="155"
              @success="onSlideSuccess"
              @fail="onSlideFail"
              @refresh="onSlideRefresh"
              :text="'请在滑块上滑动'"
            />
          </el-dialog>
        </el-form-item>

      </el-form>
    </div>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2025 xx All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import SlideVerify from 'vue-slide-verify'

export default {
  name: "Login",
  components: {
    SlideVerify
  },
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      sliderVerified: false,
      sliderDialogVisible: false, // 控制滑块弹窗显示
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
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
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    submitLogin() {
      this.loading = true
      if (this.loginForm.rememberMe) {
        Cookies.set("username", this.loginForm.username, { expires: 30 })
        Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 })
        Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
      } else {
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove('rememberMe')
      }
      this.$store.dispatch("Login", this.loginForm).then(() => {
        this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
      }).catch(() => {
        this.loading = false
        if (this.captchaEnabled) {
          this.getCode()
        }
      });
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          //this.submitLogin(); // 提交登录
          this.sliderDialogVisible = true; // 显示滑块弹窗
        }
      })
    },
    onSlideSuccess() {
      this.sliderVerified = true;
      this.sliderDialogVisible = false;
      this.submitLogin(); // 提交登录
    },
    onSlideFail() {
      this.sliderVerified = false;
      // 可以在这里处理验证失败的逻辑
    },
    onSlideRefresh() {
      // 可以在这里处理刷新时的逻辑
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: flex-end; /* 改为右侧对齐 */
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;

  .login-container {
    margin-right: 10%; /* 右侧留白 */
    width: 360px;
  }
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 100%;
  padding: 25px 25px 5px 25px;
  z-index: 1;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>
