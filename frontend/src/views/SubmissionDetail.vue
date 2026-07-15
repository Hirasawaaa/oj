<template>
  <div>
    <h2 class="page-title">提交详情</h2>

    <div v-if="submit" class="card">
      <div class="detail-grid">
        <div class="detail-item">
          <span class="detail-label">提交 ID</span>
          <span>{{ submit.id }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">题目</span>
          <router-link :to="'/problem/' + submit.problemId">#{{ submit.problemId }}</router-link>
        </div>
        <div class="detail-item">
          <span class="detail-label">语言</span>
          <span>{{ submit.language }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">状态</span>
          <span class="badge" :class="statusClass(submit.status)">{{ statusLabel(submit.status) }}</span>
        </div>
        <div class="detail-item" v-if="submit.status !== 0">
          <span class="detail-label">结果</span>
          <span>{{ submit.result }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">提交时间</span>
          <span style="color:#888;font-size:13px">{{ submit.createdAt }}</span>
        </div>
      </div>

      <h3 style="margin-top:20px;margin-bottom:8px;font-size:15px">提交代码</h3>
      <pre class="code-block"><code>{{ submit.code }}</code></pre>

      <div v-if="submit.status === 0" style="margin-top:16px;text-align:center;color:#e65100;font-size:14px">
        评测中，请稍候...
        <button class="btn btn-primary" style="margin-left:12px" @click="refresh">刷新</button>
      </div>
    </div>

    <div v-if="!submit && !error" class="card" style="text-align:center;padding:48px">
      加载中...
    </div>
    <div v-if="error" class="card" style="text-align:center;padding:48px;color:#c62828">
      {{ error }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'

const route = useRoute()
const router = useRouter()
const submit = ref<any>(null)
const error = ref('')

onMounted(async () => {
  await load()
})

async function load() {
  error.value = ''
  try {
    submit.value = await request.get('/submit/' + route.params.id)
  } catch (e: any) {
    error.value = e.message || '加载失败'
  }
}

function refresh() {
  load()
}

function statusClass(s: number) {
  switch (s) {
    case 0: return 'badge-pending'
    case 1: return 'badge-ac'
    case 2: return 'badge-wa'
    case 3: return 'badge-ce'
    case 4: return 'badge-tle'
    case 5: return 'badge-mle'
    case 6: return 'badge-re'
    default: return 'badge-pending'
  }
}
function statusLabel(s: number) {
  switch (s) {
    case 0: return '等待中'
    case 1: return '通过'
    case 2: return '答案错误'
    case 3: return '编译错误'
    case 4: return '超时'
    case 5: return '内存超限'
    case 6: return '运行错误'
    default: return '未知'
  }
}
</script>

<style scoped>
.detail-grid {
  display: grid; grid-template-columns: 1fr 1fr; gap: 12px;
}
.detail-item {
  font-size: 14px;
}
.detail-label {
  display: block; font-size: 12px; color: #888; margin-bottom: 2px;
}
.code-block {
  background: #f5f5f5; border-radius: 6px; padding: 16px;
  font-family: 'Consolas','Courier New',monospace; font-size: 13px;
  line-height: 1.5; overflow-x: auto; white-space: pre-wrap; word-break: break-all;
}
</style>
