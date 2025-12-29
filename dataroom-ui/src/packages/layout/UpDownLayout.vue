<script setup lang="ts">

</script>

<template>
  <div class="dr-up-down-layout">
    <div class="header">
      <div class="logo"></div>
      <div class="title"></div>
      <div class="menu"></div>
      <div class="user"></div>
    </div>
    <RouterView/>
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
  }
}
</style>
