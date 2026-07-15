<template>
  <div>
    <h2 class="page-title">题目列表</h2>
    <div class="card">
      <table>
        <thead>
          <tr>
            <th style="width:60px">#</th>
            <th>标题</th>
            <th style="width:80px">难度</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in problems" :key="p.id" @click="go(p.id)" style="cursor:pointer">
            <td>{{ p.id }}</td>
            <td>{{ p.title }}</td>
            <td>
              <span class="badge" :class="diffClass(p.difficulty)">{{ diffLabel(p.difficulty) }}</span>
            </td>
          </tr>
          <tr v-if="problems.length === 0">
            <td colspan="3" class="empty">暂无题目</td>
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
const problems = ref<any[]>([])
const page = ref(1)
const total = ref(0)
const pageSize = 20
const totalPages = computed(() => Math.ceil(total.value / pageSize))

onMounted(async () => {
  await load()
})

async function load() {
  const res: any = await request.get('/problem/list', {
    params: { page: page.value, pageSize }
  })
  problems.value = res.records || []
  total.value = res.total || 0
}

function changePage(p: number) {
  page.value = p
  load()
}

function go(id: number) {
  router.push('/problem/' + id)
}

function diffClass(d: number) {
  if (d === 1) return 'badge-ac'
  if (d === 2) return 'badge-wa'
  return 'badge-ce'
}
function diffLabel(d: number) {
  if (d === 0) return '简单'
  if (d === 1) return '中等'
  if (d === 2) return '困难'
  return '未知'
}
</script>
