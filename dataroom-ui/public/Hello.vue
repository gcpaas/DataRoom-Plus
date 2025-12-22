<script lang="ts" setup>
console.log('hello 我是远程组件自动加载')
import { getComponentInstance } from '@/packages/components/install.ts'
let config = getComponentInstance("DrText")
console.log(config)
</script>

<template>
    <h1>hello 你好啊</h1>
</template>
