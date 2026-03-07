<script setup lang="ts">
import BasicLayout from './layouts/BasicLayout.vue'
import { health } from '@/api/healthController.ts'

health().then((res) => {
  console.log(res)
})
</script>

<template>
  <BasicLayout />
</template>

<style>
body {
  margin: 0;
  padding: 0;
}
</style>
