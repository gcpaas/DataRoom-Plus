<script setup lang="ts">
import logo from '@/assets/logo.png';
import {useRouter, useRoute} from "vue-router";
import request from "@/packages/_common/_request.ts";
import {removeCookie} from "@/packages/_common/_cookie";
import {onMounted, ref, computed} from "vue";
import {ElMessage} from 'element-plus';
import {ArrowDown} from '@element-plus/icons-vue';

const router = useRouter()
const route = useRoute()
const jumpMenu = (path: string) => {
  router.push({
    path: path
  })
};
const username = ref('')

// 判断菜单是否激活
const isMenuActive = (path: string) => {
  return route.path.includes(path)
}

// 退出登录
const handleLogout = () => {
  // 删除cookie中的token
  removeCookie()
  ElMessage.success('退出成功')
  // 跳转到登录页
  router.push('/login')
}

onMounted(() => {
  request.get<any>(`/dataRoom/user/current`).then((res) => {
    console.log(res)
    username.value = res.realName || res.username
  })
})
</script>

<template>
  <div class="dr-up-down-layout">
    <div class="header">
      <div class="logo"><img :src="logo"></div>
      <div class="title">DataRoom设计器</div>
      <div class="menu">
        <div class="item" :class="{active: isMenuActive('/dataRoom/page')}" @click="jumpMenu('/dataRoom/page/index')">页面设计</div>
        <div class="item" :class="{active: isMenuActive('/dataRoom/resource')}" @click="jumpMenu('/dataRoom/resource/index')">素材库</div>
        <div class="item" :class="{active: isMenuActive('/dataRoom/dataSource')}" @click="jumpMenu('/dataRoom/dataSource/index')">数据源</div>
        <div class="item" :class="{active: isMenuActive('/dataRoom/dataset')}" @click="jumpMenu('/dataRoom/dataset/index')">数据集</div>
      </div>
      <div class="user">
        <el-dropdown @command="handleLogout">
          <span class="el-dropdown-link">
            {{ username }}
            <el-icon class="el-icon--right">
              <arrow-down />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <RouterView style="padding: 16px;box-sizing: border-box"/>
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

      & .item {
        cursor: pointer;
        font-size: 16px;
        padding: 4px 16px;
        border-radius: 4px;
        transition: background-color 0.3s ease;
        position: relative;

        &:hover {
          background-color: rgba(255, 255, 255, 0.15);
        }

        &.active {
          background-color: rgba(255, 255, 255, 0.2);

          &::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 60%;
            height: 3px;
            background-color: white;
            border-radius: 2px;
          }
        }
      }
    }

    & .user {
      width: 100px;
      font-size: 14px;
      display: flex;
      align-items: center;
      justify-content: flex-end;

      .el-dropdown-link {
        cursor: pointer;
        color: white;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        font-size: 14px;
        transition: opacity 0.3s ease;
        line-height: 1;

        &:hover {
          opacity: 0.8;
        }

        .el-icon--right {
          margin-left: 4px;
        }
      }
    }
  }
}
</style>
