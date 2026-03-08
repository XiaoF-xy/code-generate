<script setup lang="ts">
import type { MenuProps } from 'ant-design-vue'
import { ref, watch, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { LogoutOutlined } from '@ant-design/icons-vue'
import { userLogout } from '@/api/userController'
import { message } from 'ant-design-vue'

const router = useRouter()
const route = useRoute()
//获取登陆用户状态
const loginUserStore = useLoginUserStore()

// 菜单配置项
const originItems = [
  {
    key: '/',
    label: '主页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
  },
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const menuKey = menu?.key as string
    if (menuKey?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const menuItems = computed<MenuProps['items']>(() => filterMenus(originItems))

// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])

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

// 用户注销
const doLogout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
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
        :overflowed-indicator="null"
        @click="handleMenuClick"
      />
    </div>
    <div class="header-right">
      <div v-if="loginUserStore.loginUser.id">
        <a-dropdown>
          <a-space class="user-info">
            <a-avatar :src="loginUserStore.loginUser.userAvatar" />
            <span class="user-name">{{ loginUserStore.loginUser.userName ?? '无名' }}</span>
          </a-space>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="doLogout">
                <LogoutOutlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
      <div v-else>
        <a-button type="primary" href="/user/login">登录</a-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.logo {
  height: 32px;
  width: auto;
}

.site-title {
  color: #333;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
}

.header-menu {
  flex: 1;
  display: flex;
  justify-content: flex-start;
  margin-left: 24px;
  min-width: 500px;
}

.header-menu :deep(.ant-menu) {
  background: transparent;
  border-bottom: none;
  line-height: 64px;
  width: auto !important;
}

.header-menu :deep(.ant-menu-horizontal) {
  border: none;
  white-space: nowrap;
  display: inline-flex !important;
  flex-direction: row !important;
  flex-wrap: nowrap !important;
  width: auto !important;
}

.header-menu :deep(.ant-menu-item) {
  color: rgba(0, 0, 0, 0.65);
  padding: 0 12px;
  display: inline-block !important;
  float: left;
  flex-shrink: 0 !important;
}

.header-menu :deep(.ant-menu-overflow) {
  display: flex !important;
  flex-direction: row !important;
  flex-wrap: nowrap !important;
}

.header-menu :deep(.ant-menu-overflow-item) {
  display: inline-block !important;
  flex-shrink: 0 !important;
}

/* 隐藏三点按钮 */
.header-menu :deep(.ant-menu-overflow-item-rest),
.header-menu :deep(.ant-menu-overflow-popup),
.header-menu :deep(.ant-dropdown-trigger) {
  display: none !important;
}

.header-menu :deep(.ant-menu-item-selected),
.header-menu :deep(.ant-menu-item:hover) {
  color: #1890ff;
}

.header-menu :deep(.ant-menu-item::after) {
  border-bottom-color: #1890ff !important;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
  transition: opacity 0.3s;
}

.user-info:hover {
  opacity: 0.7;
}

.user-name {
  color: #333;
  font-size: 14px;
  margin-left: 8px;
}

:deep(.ant-dropdown-menu-item) {
  display: flex;
  align-items: center;
  gap: 8px;
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
