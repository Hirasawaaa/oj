import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/user': { target: 'http://localhost:8080', changeOrigin: true },
      '/problem': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req) => {
          if (req.headers.accept?.includes('text/html')) {
            return '/index.html'
          }
        }
      },
      '/submit': { target: 'http://localhost:8080', changeOrigin: true },
    }
  }
})
