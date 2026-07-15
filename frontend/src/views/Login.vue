<template>
  <div class="login-page">
    <div class="login-card card">
      <h2 class="login-title">{{ isRegister ? '注册' : '登录' }}</h2>
      <div class="form-group">
        <label>用户名</label>
        <input v-model="username" type="text" placeholder="输入用户名" @keyup.enter="submit" />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="password" type="password" placeholder="输入密码" @keyup.enter="submit" />
      </div>
      <div class="form-group">
        <button class="btn btn-primary" style="width:100%" @click="submit" :disabled="loading">
          {{ loading ? '处理中...' : (isRegister ? '注册' : '登录') }}
        </button>
      </div>
      <div class="login-switch">
        <a href="javascript:void(0)" @click="isRegister = !isRegister">
          {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
        </a>
      </div>
      <div v-if="error" class="login-error">{{ error }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()
const username = ref('')
const password = ref('')
const isRegister = ref(false)
const loading = ref(false)
const error = ref('')

async function submit() {
  error.value = ''
  if (!username.value || !password.value) {
    error.value = '用户名和密码不能为空'
    return
  }
  loading.value = true
  try {
    if (isRegister.value) {
      // 注册
      await request.post('/user/register', null, {
        params: { username: username.value, password: password.value }
      })
      // 注册成功，切到登录
      isRegister.value = false
      error.value = ''
      loading.value = false
      return
    }
    // 登录
    const token = await request.post('/user/login', null, {
      params: { username: username.value, password: password.value }
    }) as string
    localStorage.setItem('token', token)
    localStorage.setItem('username', username.value)
    router.push('/problems')
  } catch (e: any) {
    error.value = e.message || '操作失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex; justify-content: center; padding-top: 80px;
}
.login-card {
  width: 360px;
}
.login-title {
  text-align: center; margin-bottom: 24px; font-size: 22px;
}
.login-switch {
  text-align: center; font-size: 13px;
}
.login-error {
  margin-top: 12px; padding: 8px 12px; background: #fce4ec; border-radius: 6px;
  color: #c62828; font-size: 13px; text-align: center;
}
</style>
