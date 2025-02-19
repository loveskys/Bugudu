<template>
    <ElConfigProvider :locale="locale">
        <RouterView />
    </ElConfigProvider>
</template>
<script setup>
import { computed, ref } from 'vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import en from 'element-plus/dist/locale/en.mjs'

const language = ref('zh-cn')
const locale = computed(() => (language.value === 'zh-cn' ? zhCn : en))

// const toggle = () => {
//   language.value = language.value === 'zh-cn' ? 'en' : 'zh-cn'
// }
</script>
<style>


#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  height: 100%;
}

[class^="micon"],
[class*=" micon"] {
    font-family: "iconfont" !important;
    font-size: 16px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}

.el-image-viewer__canvas {
    max-width: 80%;
    max-height: 80%;
    margin: 5% auto;
}
</style>
