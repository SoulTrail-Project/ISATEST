// import { defineConfig } from 'vite'
// import uni from '@dcloudio/vite-plugin-uni'
// // https://vitejs.dev/config/
// export default defineConfig({
//   plugins: [
//     uni(),
//   ],
// })

import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

export default defineConfig({
  plugins: [uni()],
  
  // ✅ 生产环境优化配置
  build: {
    // 生产环境移除console
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,  // 生产环境移除console
        drop_debugger: true
      }
    },
    // 分包配置（可选，如果项目大可以开启）
    rollupOptions: {
      output: {
        manualChunks: {
          // 如果有其他大依赖可以分包
        },
        entryFileNames: 'static/[name].[hash].js',
        chunkFileNames: 'static/[name].[hash].js',
        assetFileNames: 'static/[name].[hash].[ext]'
      }
    },
    // 解决打包后资源路径问题
    assetsDir: 'static',
    // 生成sourcemap便于排查问题（可选）
    sourcemap: process.env.NODE_ENV === 'development'
  },
  
  // ✅ 确保CDN资源不被vite处理
  optimizeDeps: {
    exclude: ['echarts']  // 排除echarts，让它走CDN
  },
  base: '/',
})