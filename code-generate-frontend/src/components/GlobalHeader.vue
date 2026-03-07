<script setup lang="ts">
import type { MenuProps } from 'ant-design-vue'
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])

const menuItems: MenuProps['items'] = [
  {
    key: '/',
    label: '首页',
  },
  {
    key: '/generate',
    label: '代码生成',
  },
  {
    key: '/templates',
    label: '模板管理',
  },
  {
    key: '/settings',
    label: '系统设置',
  },
]

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  router.push(key)
}

// 监听路由变化，更新当前选中菜单
watch(
  () => route.path,
  (newPath) => {
    selectedKeys.value = [newPath]
  },
  { immediate: true },
)
</script>

<template>
  <div class="header">
    <div class="header-left">
      <img class="logo" src="../assets/logo.png" alt="Logo" />
      <span class="site-title">代码生成平台</span>
    </div>
    <div class="header-menu">
      <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="horizontal"
        :items="menuItems"
        @click="handleMenuClick"
      />
    </div>
    <div class="header-right">
      <a-button type="primary">登录</a-button>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #001529;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  height: 32px;
  width: auto;
}

.site-title {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
}

.header-menu {
  flex: 1;
  display: flex;
  justify-content: center;
}

.header-menu :deep(.ant-menu) {
  background: transparent;
  border-bottom: none;
  line-height: 64px;
}

.header-menu :deep(.ant-menu-item) {
  color: rgba(255, 255, 255, 0.65);
}

.header-menu :deep(.ant-menu-item-selected),
.header-menu :deep(.ant-menu-item:hover) {
  color: #fff;
}

.header-menu :deep(.ant-menu-item::after) {
  border-bottom-color: #1890ff !important;
}

.header-right {
  display: flex;
  align-items: center;
}

@media (max-width: 768px) {
  .header {
    flex-wrap: wrap;
    padding: 0 16px;
  }

  .header-left {
    flex: 1;
  }

  .header-menu {
    order: 3;
    width: 100%;
    justify-content: flex-start;
  }

  .header-menu :deep(.ant-menu) {
    font-size: 14px;
  }

  .site-title {
    font-size: 16px;
  }
}
</style>
