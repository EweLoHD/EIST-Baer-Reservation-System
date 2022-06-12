<script setup lang="ts">
import Rating from '@/components/Rating.vue';
import PriceCategory from '@/components/PriceCategory.vue';
import type Restaurant from '@/types/Restaurant';

const props = defineProps<{
    /*name: string,
    rating: number,
    price: number,
    category: string,
    location: string*/
    restaurant: Restaurant
}>()
</script>
<template>
<div class="flex flex-row items-center justify-between h-40 bg-white rounded-lg border hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-700">
    <!--<div class="w-56 h-full bg-[url('https://www.schwabinger-wassermann.de/images/com_osgallery/gal-1/original/gastraum-mit-barE1E9DB63-3DCB-69E3-04AC-9E8B5B1D3EE3.jpg')] bg-cover bg-origin-border bg-clip-content rounded-l-lg"></div>-->
    <div class="w-56 h-full bg-cover bg-origin-border bg-clip-content rounded-l-lg bg-center" :style="{ backgroundImage: 'url(\'' + restaurant.restaurantPictures[0] + '\')' }"></div>
    <div class="flex flex-col justify-between px-4 pt-2 pb-4 h-full w-full">
        <div>
            <p class="text-2xl flex-none font-bold dark:text-white">{{ restaurant.name }}</p>
            <Rating :rating="Math.floor(restaurant.averageRating)" class="mt-1"/>
            <div class="inline-flex mt-2">
                <PriceCategory :category="restaurant.priceCategory" class=""></PriceCategory>
                <a class="mx-1 text-dark-800"> •&nbsp; </a>
                <a class="text-base font-medium text-gray-500">{{ restaurant.restaurantType }}</a>
            </div>
        </div>
        <div class="flex flex-row w-full justify-between items-end">
            <p class="inline-flex items-center text-gray-800">
                <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clip-rule="evenodd"></path></svg>
                <a class="text-base font-medium ml-1">{{ restaurant.address.addressLine }}, {{ restaurant.address.city }}</a>
            </p>
            <button type="button" class="w-fit text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Book Table
                <svg class="w-5 h-5 ml-2 -mr-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
            </button> 
        </div>
    </div>
</div>
</template>