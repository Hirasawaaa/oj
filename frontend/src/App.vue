<template>
  <div class="app">
    <nav class="navbar">
      <div class="nav-left">
        <router-link to="/problems" class="nav-brand">OJ</router-link>
        <router-link to="/problems" class="nav-link">题目</router-link>
        <router-link to="/submissions" class="nav-link">提交记录</router-link>
        <router-link to="/create" class="nav-link">创建题目</router-link>
      </div>
      <div class="nav-right" v-if="loggedIn">
        <span class="nav-user">{{ username }}</span>
        <a href="javascript:void(0)" @click="logout" class="nav-link">退出</a>
      </div>
      <div class="nav-right" v-else>
        <router-link to="/login" class="nav-link">登录</router-link>
      </div>
    </nav>
    <main class="main">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 用 ref 而不是 computed(() => localStorage...) 的原因：
// Vue 的 computed 只追踪 reactive 的依赖，localStorage 不是 reactive 的，
// 所以 computed 只会算一次，登录后 navigator 不会出现。
// 这里每次路由切换后重新读 localStorage，保证 nav 及时更新。
const token = ref(localStorage.getItem('token') || '')
const username = ref(localStorage.getItem('username') || '')
const loggedIn = computed(() => !!token.value)

router.afterEach(() => {
  token.value = localStorage.getItem('token') || ''
  username.value = localStorage.getItem('username') || ''
})

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  token.value = ''
  username.value = ''
  router.push('/login')
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #f5f5f5; color: #333; }
a { color: #1a73e8; text-decoration: none; }
a:hover { text-decoration: underline; }

.navbar {
  display: flex; align-items: center; justify-content: space-between;
  background: #1a73e8; color: #fff; padding: 0 24px; height: 48px;
}
.nav-left { display: flex; align-items: center; gap: 20px; }
.nav-brand { font-weight: 700; font-size: 18px; color: #fff !important; text-decoration: none !important; }
.nav-link { color: rgba(255,255,255,.85) !important; font-size: 14px; text-decoration: none !important; }
.nav-link:hover { color: #fff !important; }
.nav-right { display: flex; align-items: center; gap: 16px; }
.nav-user { font-size: 14px; opacity: .8; }

.main {
  max-width: 1000px; margin: 0 auto; padding: 24px 16px;
}

.card {
  background: #fff; border-radius: 8px; padding: 24px; box-shadow: 0 1px 3px rgba(0,0,0,.1);
}
.card + .card { margin-top: 16px; }

.btn {
  display: inline-block; padding: 8px 20px; border: none; border-radius: 6px;
  font-size: 14px; cursor: pointer; transition: background .2s;
}
.btn-primary { background: #1a73e8; color: #fff; }
.btn-primary:hover { background: #1557b0; }
.btn-primary:disabled { background: #93b8f0; cursor: not-allowed; }
.btn-danger { background: #d32f2f; color: #fff; }
.btn-danger:hover { background: #b71c1c; }

input, textarea, select {
  width: 100%; padding: 8px 12px; border: 1px solid #ddd; border-radius: 6px;
  font-size: 14px; outline: none; transition: border-color .2s;
}
input:focus, textarea:focus, select:focus { border-color: #1a73e8; }

table {
  width: 100%; border-collapse: collapse;
}
th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #eee; font-size: 14px; }
th { background: #fafafa; font-weight: 600; color: #666; }
tr:hover { background: #f8f9ff; }

.badge {
  display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 12px; font-weight: 600;
}
.badge-ac { background: #e8f5e9; color: #2e7d32; }
.badge-wa { background: #fce4ec; color: #c62828; }
.badge-pending { background: #fff3e0; color: #e65100; }
.badge-ce { background: #f3e5f5; color: #6a1b9a; }
.badge-tle { background: #fff3e0; color: #e65100; }
.badge-mle { background: #e0f2f1; color: #00695c; }
.badge-re { background: #fce4ec; color: #c62828; }

.pagination {
  display: flex; align-items: center; justify-content: center; gap: 8px; margin-top: 16px;
}
.pagination .btn { min-width: 36px; text-align: center; }
.pagination .active { background: #1a73e8; color: #fff; }
.pagination .info { font-size: 14px; color: #666; }

.form-group { margin-bottom: 16px; }
.form-group label { display: block; font-size: 14px; font-weight: 600; margin-bottom: 6px; color: #555; }

.page-title { font-size: 20px; font-weight: 700; margin-bottom: 16px; }

.empty { text-align: center; padding: 48px 0; color: #999; font-size: 14px; }
</style>
