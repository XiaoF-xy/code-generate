<script setup lang="ts">
import GlobalHeader from '../components/GlobalHeader.vue'
import GlobalFooter from '../components/GlobalFooter.vue'
</script>

<template>
  <a-layout class="layout">
    <a-layout-header class="header-wrapper">
      <GlobalHeader />
    </a-layout-header>
    <a-layout-content class="content">
      <RouterView />
    </a-layout-content>
    <a-layout-footer class="footer-wrapper">
      <GlobalFooter />
    </a-layout-footer>
  </a-layout>
</template>

<style scoped>
.layout {
  min-height: 100vh;
}

.header-wrapper {
  padding: 0;
  background: #001529;
}

.content {
  margin: 0;
  padding: 24px;
  min-height: calc(100vh - 112px);
  background: #fff;
}

.footer-wrapper {
  padding: 0;
  background: transparent;
}

@media (max-width: 768px) {
  .content {
    padding: 16px;
    min-height: calc(100vh - 128px);
  }
}
</style>
