import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'


export default defineConfig({
    plugins: [vue()],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    css: {
        preprocessorOptions: {
            less: {
                math: "always"
            }
        }
    },


    //开发环境设置
    server: {
        port: 8081,
        proxy: {
            "/api": {
                target: process.env.VITE_APP_URL,
                changeOrigin: true,
            },
        },
    },



    //打包设置
    build: {
        outDir: "dist",         // 指定输出路径
        assetsInlineLimit: 0,
        assetsDir: "assets",    // 指定生成静态资源的存放路径
        minify: "terser",       // 混淆器,terser构建后文件体积更小
        sourcemap: false,       // 是否构建sourcemap文件
        terserOptions: {
            compress: {
                drop_console: true,  // 生产环境移除console
                drop_debugger: true,  // 生产环境移除debugger
            },
        },
    },

})
