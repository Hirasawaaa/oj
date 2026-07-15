<template>
  <div>
    <h2 class="page-title">创建题目</h2>
    <div class="card">
      <div class="form-group">
        <label>标题</label>
        <input v-model="title" placeholder="题目名称" />
      </div>
      <div class="form-group">
        <label>题目描述</label>
        <textarea v-model="description" rows="4" placeholder="描述题目要求"></textarea>
      </div>
      <div class="form-group">
        <label>输入描述</label>
        <textarea v-model="inputDesc" rows="2" placeholder="输入格式描述"></textarea>
      </div>
      <div class="form-group">
        <label>输出描述</label>
        <textarea v-model="outputDesc" rows="2" placeholder="输出格式描述"></textarea>
      </div>
      <div style="display:flex;gap:16px">
        <div class="form-group" style="flex:1">
          <label>样例输入</label>
          <textarea v-model="sampleInput" rows="3" style="font-family:monospace"></textarea>
        </div>
        <div class="form-group" style="flex:1">
          <label>样例输出</label>
          <textarea v-model="sampleOutput" rows="3" style="font-family:monospace"></textarea>
        </div>
      </div>
      <div style="display:flex;gap:16px">
        <div class="form-group" style="flex:1">
          <label>难度</label>
          <select v-model.number="difficulty">
            <option :value="0">简单</option>
            <option :value="1">中等</option>
            <option :value="2">困难</option>
          </select>
        </div>
        <div class="form-group" style="flex:1">
          <label>时间限制 (ms)</label>
          <input v-model.number="timeLimit" type="number" placeholder="1000" />
        </div>
        <div class="form-group" style="flex:1">
          <label>内存限制 (MB)</label>
          <input v-model.number="memoryLimit" type="number" placeholder="256" />
        </div>
      </div>
      <div style="margin-top:8px">
        <button class="btn btn-primary" @click="create" :disabled="creating">
          {{ creating ? '创建中...' : '创建题目' }}
        </button>
        <span v-if="msg" :style="{ marginLeft: '12px', color: msgOk ? '#2e7d32' : '#c62828', fontSize: '13px' }">
          {{ msg }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()

const title = ref('')
const description = ref('')
const inputDesc = ref('')
const outputDesc = ref('')
const sampleInput = ref('')
const sampleOutput = ref('')
const difficulty = ref(0)
const timeLimit = ref(1000)
const memoryLimit = ref(256)
const creating = ref(false)
const msg = ref('')
const msgOk = ref(false)

async function create() {
  if (!title.value) { msg.value = '请输入标题'; msgOk.value = false; return }
  creating.value = true
  msg.value = ''
  try {
    await request.post('/problem/create', {
      title: title.value,
      description: description.value,
      inputDesc: inputDesc.value,
      outputDesc: outputDesc.value,
      sampleInput: sampleInput.value,
      sampleOutput: sampleOutput.value,
      difficulty: difficulty.value,
      timeLimit: timeLimit.value,
      memoryLimit: memoryLimit.value,
    })
    msg.value = '创建成功！'
    msgOk.value = true
  } catch (e: any) {
    msg.value = e.message || '创建失败'
    msgOk.value = false
  } finally {
    creating.value = false
  }
}
</script>
