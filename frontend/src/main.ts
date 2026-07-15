import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'

import Login from './views/Login.vue'
import Problems from './views/Problems.vue'
import ProblemDetail from './views/ProblemDetail.vue'
import CreateProblem from './views/CreateProblem.vue'
import Submissions from './views/Submissions.vue'
import SubmissionDetail from './views/SubmissionDetail.vue'

const routes = [
  { path: '/', redirect: '/problems' },
  { path: '/login', component: Login },
  { path: '/problems', component: Problems },
  { path: '/problem/:id', component: ProblemDetail },
  { path: '/submissions', component: Submissions },
  { path: '/submission/:id', component: SubmissionDetail },
  { path: '/create', component: CreateProblem },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

createApp(App).use(router).mount('#app')
