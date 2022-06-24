<script setup lang="ts">
import Review from '@/types/Review';
import Rating from './Rating.vue';
</script>
<script lang="ts">
export default {
    props: {
        reviews: { type: Array<Review>, required: true }
    },
    methods: {
        getPercentageOfReviewsByStars(stars: Number): string {
            return (((this.reviews.filter(r => r.rating == stars).length / this.reviews.length) * 100) | 0) + "%";
        },
        getAverageRating() {
            return this.reviews.map(r => r.rating as number).reduce((a, b) => a + b, 0) / this.reviews.length;
        }
    }
}
</script>

<template>
    <div class="flex items-center mb-3">
        <svg class="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path></svg>
        <svg class="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path></svg>
        <svg class="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path></svg>
        <svg class="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path></svg>
        <svg class="w-5 h-5 text-gray-300 dark:text-gray-500" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path></svg>
        <p class="ml-2 text-sm font-medium text-gray-900 dark:text-white">{{ getAverageRating().toFixed(1) }} out of 5</p>
    </div>
    <p class="text-sm font-medium text-gray-500 dark:text-gray-400">{{ reviews.length }} ratings</p>
    <div class="flex items-center mt-4">
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">5 star</span>
        <div class="w-2/4 h-5 mx-4 bg-gray-200 rounded dark:bg-gray-700">
            <div class="h-5 bg-yellow-400 rounded" :style="{width: getPercentageOfReviewsByStars(5)}"></div>
        </div>
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">{{ getPercentageOfReviewsByStars(5) }}</span>
    </div>
    <div class="flex items-center mt-4">
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">4 star</span>
        <div class="w-2/4 h-5 mx-4 bg-gray-200 rounded dark:bg-gray-700">
            <div class="h-5 bg-yellow-400 rounded" :style="{width: getPercentageOfReviewsByStars(4)}"></div>
        </div>
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">{{ getPercentageOfReviewsByStars(4) }}</span>
    </div>
    <div class="flex items-center mt-4">
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">3 star</span>
        <div class="w-2/4 h-5 mx-4 bg-gray-200 rounded dark:bg-gray-700">
            <div class="h-5 bg-yellow-400 rounded" :style="{width: getPercentageOfReviewsByStars(3)}"></div>
        </div>
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">{{ getPercentageOfReviewsByStars(3) }}</span>
    </div>
    <div class="flex items-center mt-4">
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">2 star</span>
        <div class="w-2/4 h-5 mx-4 bg-gray-200 rounded dark:bg-gray-700">
            <div class="h-5 bg-yellow-400 rounded" :style="{width: getPercentageOfReviewsByStars(2)}"></div>
        </div>
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">{{ getPercentageOfReviewsByStars(2) }}</span>
    </div>
    <div class="flex items-center mt-4">
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">1 star</span>
        <div class="w-2/4 h-5 mx-4 bg-gray-200 rounded dark:bg-gray-700">
            <div class="h-5 bg-yellow-400 rounded" :style="{width: getPercentageOfReviewsByStars(1)}"></div>
        </div>
        <span class="text-sm font-medium text-blue-600 dark:text-blue-500">{{ getPercentageOfReviewsByStars(1) }}</span>
    </div>

    <div class="mt-10 flex flex-col gap-4">
        <article v-for="review in reviews" class="border rounded-lg p-3">
            <p class="inline-flex">
                <Rating :rating="(review.rating as number)" :size="5"></Rating>
                <a class="ml-2 block text-sm font-semibold text-gray-500">{{ review.creationDate.getFormated() }}</a>
            </p>       
            <p class="font-base text-gray-600">{{ review.comment }}</p>
        </article>
    </div>

</template>