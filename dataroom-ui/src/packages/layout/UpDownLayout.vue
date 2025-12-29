<script setup lang="ts">
import logo from '@/assets/logo.png';
import {useRouter} from "vue-router";

const router = useRouter()
const jumpMenu = (path: string) => {
  router.push({
    path: path
  })
};
</script>

<template>
  <div class="dr-up-down-layout">
    <div class="header">
      <div class="logo"><img :src="logo"></div>
      <div class="title">DataRoom设计器</div>
      <div class="menu">
        <div class="item" @click="jumpMenu('/dataRoom/page/index')">页面设计</div>
        <div class="item" @click="jumpMenu('/dataRoom/resource/index')">素材库</div>
        <div class="item" @click="jumpMenu('/dataRoom/dataSource/index')">数据源</div>
        <div class="item" @click="jumpMenu('/dataRoom/dataset/index')">数据集</div>
      </div>
      <div class="user">用户</div>
    </div>
    <RouterView style="padding: 16px"/>
  </div>
</template>

<style scoped lang="scss">
.dr-up-down-layout {
  height: 100vh;
  width: 100%;
  & .header {
    background-color: var(--dr-primary);
    height: 60px;
    width: 100%;
    line-height: 60px;
    color: white;
    display: flex;
    align-items: center;
    padding: 0 20px;
    box-sizing: border-box;

    & .logo {
      display: flex;

      & img {
        height: 32px;
      }
    }

    & .title {
      margin-left: 32px;
      font-size: 18px;
      font-weight: 700;
    }

    & .menu {
      width: 50%;
      margin-left: auto;
      text-align: center;
      display: flex;
      gap: 32px;

      & .item {
        cursor: pointer;
        font-size: 16px;
      }
    }

    & .user {
      width: 100px;
      text-align: right;
    }
  }
}
</style>
