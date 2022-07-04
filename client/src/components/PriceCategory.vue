<script setup lang="ts">
const props = defineProps<{
    category: number,
    size?: string
}>()
</script>
<template>
    <div class="flex items-center">
        <p :class="['text-'+(size? size : 5), 'font-bold']">
            <a v-for="i in category" class="text-gray-800">€&nbsp;</a>
            <a v-for="i in 3-category" class="text-gray-400">€&nbsp;</a>
        </p>
    </div>
</template>