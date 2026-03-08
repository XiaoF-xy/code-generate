<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { updateMyUser } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const loginUserStore = useLoginUserStore()

// 表单数据
const formState = reactive({
  userName: '',
  userAvatar: '',
  userProfile: '',
  userPassword: '',
  oldPassword: '',
  checkPassword: '',
})

// 加载状态
const loading = ref(false)

// 密码修改开关
const changePassword = ref(false)

// 表单验证规则
const rules = {
  userName: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, message: '昵称至少 2 个字符', trigger: 'blur' },
  ],
  userAvatar: [{ type: 'url', message: '请输入有效的头像 URL', trigger: 'blur' }],
  userPassword: [
    { min: 8, message: '密码至少 8 个字符', trigger: 'blur' },
  ],
  checkPassword: [
    { min: 8, message: '确认密码至少 8 个字符', trigger: 'blur' },
  ],
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' },
    { min: 8, message: '原密码至少 8 个字符', trigger: 'blur' },
  ],
}

// 验证密码一致性
const validatePassword = async (_rule: any, value: string) => {
  if (value && formState.userPassword !== value) {
    throw new Error('两次输入的密码不一致')
  }
}

// 初始化表单数据
onMounted(() => {
  const loginUser = loginUserStore.loginUser
  if (loginUser && loginUser.id) {
    formState.userName = loginUser.userName || ''
    formState.userAvatar = loginUser.userAvatar || ''
    formState.userProfile = loginUser.userProfile || ''
  }
})

// 提交表单
const handleSubmit = async () => {
  try {
    loading.value = true

    // 如果开启密码修改，验证新密码
    if (changePassword.value) {
      if (!formState.userPassword || !formState.checkPassword || !formState.oldPassword) {
        message.error('请填写完整的密码信息')
        loading.value = false
        return
      }
      if (formState.userPassword !== formState.checkPassword) {
        message.error('两次输入的新密码不一致')
        loading.value = false
        return
      }
      if (formState.userPassword.length < 8) {
        message.error('新密码长度不能少于 8 位')
        loading.value = false
        return
      }
    }

    // 构建请求数据
    const requestData: API.UserUpdateMyRequest = {
      userName: formState.userName,
      userAvatar: formState.userAvatar,
      userProfile: formState.userProfile,
    }

    // 如果要修改密码，添加密码字段
    if (changePassword.value && formState.userPassword) {
      requestData.userPassword = formState.userPassword
      requestData.oldPassword = formState.oldPassword
    }

    // 调用接口
    const res = await updateMyUser(requestData)
    if (res.data.code === 0) {
      message.success('修改成功')
      // 更新本地存储的用户信息
      await loginUserStore.fetchLoginUser()
      // 返回上一页或首页
      setTimeout(() => {
        router.back()
      }, 1000)
    } else {
      message.error('修改失败，' + res.data.message)
    }
  } catch (error) {
    console.error('修改失败:', error)
    message.error('修改失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 取消修改
const handleCancel = () => {
  router.back()
}
</script>

<template>
  <div class="settings-page">
    <div class="settings-container">
      <div class="settings-header">
        <h1>个人设置</h1>
        <p>修改您的个人信息</p>
      </div>

      <a-card class="settings-card">
        <a-form
          layout="vertical"
          :model="formState"
          :rules="rules"
          ref="formRef"
          @finish="handleSubmit"
        >
          <a-form-item label="昵称" name="userName">
            <a-input
              v-model:value="formState.userName"
              placeholder="请输入昵称"
              size="large"
            />
          </a-form-item>

          <a-form-item label="头像 URL" name="userAvatar">
            <a-input
              v-model:value="formState.userAvatar"
              placeholder="请输入头像图片链接"
              size="large"
            >
              <template #prefix>
                <a-avatar
                  v-if="formState.userAvatar"
                  :src="formState.userAvatar"
                  :size="24"
                />
              </template>
            </a-input>
            <div v-if="formState.userAvatar" class="avatar-preview">
              <a-avatar :src="formState.userAvatar" :size="80" />
            </div>
          </a-form-item>

          <a-form-item label="个人简介" name="userProfile">
            <a-textarea
              v-model:value="formState.userProfile"
              placeholder="请输入个人简介"
              :rows="4"
              size="large"
            />
          </a-form-item>

          <a-divider />

          <a-form-item>
            <a-checkbox v-model:checked="changePassword">修改密码</a-checkbox>
          </a-form-item>

          <template v-if="changePassword">
            <a-form-item label="原密码" name="oldPassword">
              <a-input-password
                v-model:value="formState.oldPassword"
                placeholder="请输入原密码"
                size="large"
              />
            </a-form-item>

            <a-form-item label="新密码" name="userPassword">
              <a-input-password
                v-model:value="formState.userPassword"
                placeholder="请输入新密码"
                size="large"
              />
            </a-form-item>

            <a-form-item label="确认新密码" name="checkPassword" :validator="validatePassword">
              <a-input-password
                v-model:value="formState.checkPassword"
                placeholder="请再次输入新密码"
                size="large"
              />
            </a-form-item>
          </template>

          <a-form-item class="form-actions">
            <a-button
              type="primary"
              html-type="submit"
              :loading="loading"
              size="large"
              class="submit-btn"
            >
              保存修改
            </a-button>
            <a-button
              html-type="button"
              @click="handleCancel"
              size="large"
              class="cancel-btn"
            >
              取消
            </a-button>
          </a-form-item>
        </a-form>
      </a-card>
    </div>
  </div>
</template>

<style scoped>
.settings-page {
  min-height: calc(100vh - 64px);
  background: #f5f5f5;
  padding: 24px;
}

.settings-container {
  max-width: 600px;
  margin: 0 auto;
}

.settings-header {
  margin-bottom: 24px;
  text-align: center;
}

.settings-header h1 {
  color: #333;
  font-size: 28px;
  margin: 0 0 8px 0;
}

.settings-header p {
  color: #666;
  font-size: 14px;
  margin: 0;
}

.settings-card {
  border-radius: 8px;
}

.avatar-preview {
  margin-top: 16px;
  text-align: center;
}

.form-actions {
  margin-top: 32px;
  text-align: center;
}

.submit-btn {
  width: 120px;
  margin-right: 16px;
}

.cancel-btn {
  width: 120px;
}

:deep(.ant-form-item-label > label) {
  font-weight: 500;
  color: #333;
}

:deep(.ant-input-affix-wrapper) {
  padding-left: 12px;
}

:deep(.ant-avatar) {
  cursor: pointer;
}
</style>
