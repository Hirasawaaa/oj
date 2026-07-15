<template>
  <div>
    <h2 class="page-title">{{ problem?.title || '加载中...' }}</h2>

    <div v-if="problem" class="problem-layout">
      <!-- 左：题目描述 -->
      <div class="problem-left">
        <div class="card">
          <div class="problem-meta">
            <span class="badge" :class="diffClass(problem.difficulty)">{{ diffLabel(problem.difficulty) }}</span>
            <span style="margin-left:12px;color:#888;font-size:13px">时间: {{ problem.timeLimit }}ms</span>
            <span style="margin-left:12px;color:#888;font-size:13px">内存: {{ problem.memoryLimit }}MB</span>
          </div>

          <h3 style="margin-top:16px;font-size:16px">题目描述</h3>
          <p style="margin-top:6px;line-height:1.7;white-space:pre-wrap">{{ problem.description }}</p>

          <h3 style="margin-top:16px;font-size:16px">输入描述</h3>
          <p style="margin-top:6px;line-height:1.7;white-space:pre-wrap">{{ problem.inputDesc }}</p>

          <h3 style="margin-top:16px;font-size:16px">输出描述</h3>
          <p style="margin-top:6px;line-height:1.7;white-space:pre-wrap">{{ problem.outputDesc }}</p>

          <div class="sample-row">
            <div class="sample-box">
              <h4>样例输入</h4>
              <pre>{{ problem.sampleInput }}</pre>
            </div>
            <div class="sample-box">
              <h4>样例输出</h4>
              <pre>{{ problem.sampleOutput }}</pre>
            </div>
          </div>
        </div>
      </div>

      <!-- 右：代码编辑器 -->
      <div class="problem-right">
        <div class="card" style="padding:16px">
          <div style="margin-bottom:8px;font-size:13px;color:#888">提交代码</div>
          <select v-model="language" class="language-select">
            <option value="java">Java</option>
            <option value="c">C</option>
            <option value="cpp">C++</option>
            <option value="python">Python</option>
            <option value="pypy3">PyPy3</option>
          </select>
          <textarea v-model="code" class="code-editor" spellcheck="false"></textarea>
          <div style="margin-top:10px;display:flex;gap:8px;align-items:center">
            <button class="btn btn-primary" @click="submitCode" :disabled="submitting">
              {{ submitting ? '评测中...' : '提交' }}
            </button>
            <span v-if="submitMsg" :style="{ color: submitOk ? '#2e7d32' : '#c62828', fontSize: '13px' }">
              {{ submitMsg }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 无数据时显示非 null 不会自动隐藏 -->
    <div v-if="!problem && !loadingError" class="card" style="text-align:center;padding:48px">
      加载中...
    </div>
    <div v-if="loadingError" class="card" style="text-align:center;padding:48px;color:#c62828">
      {{ loadingError }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'

const route = useRoute()
const router = useRouter()
const problem = ref<any>(null)
const code = ref(`import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

    }
}`)
const submitting = ref(false)
const submitMsg = ref('')
const submitOk = ref(false)
const loadingError = ref('')
const language = ref('java')

onMounted(async () => {
  try {
    problem.value = await request.get('/problem/' + route.params.id)
  } catch (e: any) {
    loadingError.value = e.message || '加载失败'
  }
})

async function submitCode() {
  submitting.value = true
  submitMsg.value = ''
  try {
    const submitId = await request.post('/submit/submit', {
      problemId: Number(route.params.id),
      code: code.value,
      language: language.value
    })
    submitOk.value = true
    submitMsg.value = '提交成功，正在跳转...'
    setTimeout(() => router.push('/submission/' + submitId), 800)
  } catch (e: any) {
    submitOk.value = false
    submitMsg.value = e.message || '提交失败'
  } finally {
    submitting.value = false
  }
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

<style scoped>
.problem-layout {
  display: flex; gap: 16px;
}
.problem-left {
  flex: 1; min-width: 0;
}
.problem-right {
  width: 420px; flex-shrink: 0;
}
.code-editor {
  width: 100%; height: 360px;
  font-family: 'Consolas','Courier New',monospace;
  font-size: 13px; line-height: 1.5;
  resize: vertical; tab-size: 4;
}
.language-select {
  width: 100%; padding: 6px 8px;
  font-size: 13px; margin-bottom: 8px;
  border: 1px solid #ccc; border-radius: 4px;
  background: #fff; cursor: pointer;
}
.problem-meta {
  font-size: 13px;
}
.sample-row {
  display: flex; gap: 16px; margin-top: 16px;
}
.sample-box {
  flex: 1; background: #f8f9fa; border-radius: 6px; padding: 12px;
}
.sample-box h4 {
  font-size: 13px; color: #666; margin-bottom: 6px;
}
.sample-box pre {
  font-family: 'Consolas','Courier New',monospace;
  font-size: 13px; white-space: pre-wrap; line-height: 1.5;
}
@media (max-width: 800px) {
  .problem-layout { flex-direction: column; }
  .problem-right { width: 100%; }
}
</style>
