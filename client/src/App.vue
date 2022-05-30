<script setup lang="ts">
import HelloWorld from './components/HelloWorld.vue'
import TheWelcome from './components/TheWelcome.vue'
import SearchBar2 from './components/SearchBar2.vue';
import SearchBar from './components/SearchBar.vue';
import 'flowbite';

//var test = ["Italian", "Chinese", "Burger", "Vegetarian"]
</script>

<template>
  <div class="flex items-center justify-center h-screen">
    <!--<div class="p-6 w-full max-w-2xl">
        <SearchBar></SearchBar>
    </div>-->
    <a href="#" class="flex flex-col items-center bg-white rounded-lg border shadow-md md:flex-row md:max-w-xl hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-700">
    <img class="object-cover h-96 rounded-t-lg md:h-auto md:w-48 md:rounded-none md:rounded-l-lg" src="https://flowbite.com/docs/images/blog/image-4.jpg" alt="">
    <div class="flex flex-col justify-between p-4 leading-normal">
        <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">Noteworthy technology acquisitions 2021</h5>
        <p class="mb-3 font-normal text-gray-700 dark:text-gray-400">Here are the biggest enterprise technology acquisitions of 2021 so far, in reverse chronological order.</p>
    </div>
</a>
  </div>
  
</template>
