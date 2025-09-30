import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [
      vue(),
      tailwindcss(),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  server: {
    port: 3300,
    strictPort: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:6688',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, ''),
      }
    }
  }
});