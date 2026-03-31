import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: 8080,
    strictPort: false,
    hmr: true,
    open: false,
    proxy: {
      '/api': {
        target: 'http://localhost:8088',
        changeOrigin: true,
        rewrite: (path) => path
      }
    }
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  optimizeDeps: {
    include: ['element-plus', 'pinia'],
    exclude: ['axios']
  }
});