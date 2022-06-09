<script setup lang="ts">
import SearchBar from '@/components/SearchBar.vue';
</script>

<template>
  <div class="flex flex-col items-center justify-center h-screen dark:bg-gray-900">
    <p class="mb-16 text-5xl font-bold text-gray-900 dark:text-white">Reservation System</p>
    <div class="mb-32 p-4 w-fit bg-white rounded-lg border border-gray-200 shadow-md dark:bg-gray-800 dark:border-gray-700">
      <SearchBar :query-required="true"></SearchBar>
    </div>  
  </div>
</template>