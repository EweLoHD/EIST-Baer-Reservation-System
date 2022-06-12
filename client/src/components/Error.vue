<script setup lang="ts">
const props = defineProps({
    code: String,
    name: String,
    message: String
})
</script>
<template>
    <div class="flex flex-col items-center">
        <p class="mb-2 text-9xl font-bold text-gray-900 dark:text-white">{{ code }}</p>
        <p class="mb-5 text-5xl text-gray-900 dark:text-white">{{ name }}</p>
        <p class="text-4xl text-gray-900 dark:text-white">{{ message }}</p>
    </div>
</template>