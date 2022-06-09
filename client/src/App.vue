<script setup lang="ts">
import { RouterView } from 'vue-router'
import 'flowbite';
</script>

<template>
  <RouterView :key="$route.fullPath" />
</template>