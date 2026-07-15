<template>
  <div>
    <h2 class="page-title">提交记录</h2>
    <div class="card" style="margin-bottom:16px;padding:12px 16px;display:flex;align-items:center;gap:12px">
      <label style="font-size:14px;color:#666">按题目筛选：</label>
      <input v-model="filterProblemId" type="number" placeholder="题目 ID" style="width:120px" @keyup.enter="load" />
      <button class="btn btn-primary" @click="load">筛选</button>
      <button class="btn" style="border:1px solid #ddd" @click="filterProblemId='';load()">清除</button>
    </div>
    <div class="card">
      <table>
        <thead>
          <tr>
            <th style="width:50px">#</th>
            <th style="width:70px">题目</th>
            <th>语言</th>
            <th style="width:80px">状态</th>
            <th>结果</th>
            <th style="width:160px">提交时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in submits" :key="s.id" @click="go(s.id)" style="cursor:pointer">
            <td>{{ s.id }}</td>
            <td>{{ s.problemId }}</td>
            <td>{{ s.language }}</td>
            <td><span class="badge" :class="statusClass(s.status)">{{ statusLabel(s.status) }}</span></td>
            <td style="max-width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ s.result }}</td>
            <td style="color:#888;font-size:13px">{{ s.createdAt }}</td>
          </tr>
          <tr v-if="submits.length === 0">
            <td colspan="6" class="empty">暂无提交记录</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="total > 0">
        <button class="btn" :disabled="page <= 1" @click="changePage(page - 1)">上一页</button>
        <span class="info">第 {{ page }} / {{ totalPages }} 页</span>
        <button class="btn" :disabled="page >= totalPages" @click="changePage(page + 1)">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()
const submits = ref<any[]>([])
const page = ref(1)
const total = ref(0)
const pageSize = 20
const filterProblemId = ref('')
const totalPages = computed(() => Math.ceil(total.value / pageSize))

onMounted(async () => {
  await load()
})

async function load() {
  const params: any = { page: page.value, pageSize }
  if (filterProblemId.value) params.problemId = filterProblemId.value
  const res: any = await request.get('/submit/list', { params })
  submits.value = res.records || []
  total.value = res.total || 0
}

function changePage(p: number) {
  page.value = p
  load()
}

function go(id: number) {
  router.push('/submission/' + id)
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
