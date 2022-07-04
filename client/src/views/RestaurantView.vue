<script setup lang="ts">
import type { defineComponent } from "@vue/runtime-core";
import axios from 'axios';

import Carousel from 'flowbite/src/components/carousel'

import Restaurant from '@/types/Restaurant';
import Address from '@/types/Address';
import Review from '@/types/Review';
import LocalDate from "@/types/LocalDate";

import Rating from '@/components/Rating.vue';
import PriceCategory from '@/components/PriceCategory.vue';
import ReservationField from '@/components/ReservationField.vue'
import OpeningHours from "@/components/OpeningHours.vue";
import OpeningTime from "@/types/OpeningTime";
import Reviews from "@/components/Reviews.vue";


</script>
<script lang="ts">
export default {
    data() {
        return {
            loading: true,
            restaurant: {} as Restaurant,
            carousel: {} as Carousel
        }
    },
    methods: {
        getData() {
            this.loading = true;
            axios.get('http://localhost:8080/restaurants/' + this.$route.params.id).then(response => {
                this.restaurant = response.data as Restaurant;

                this.restaurant.openingTimes = response.data.openingTimes?.map((t: { dayOfWeek: Number; fromTime: String; toTime: String; }) => new OpeningTime(t.dayOfWeek, t.fromTime, t.toTime))
                this.restaurant.reviews = response.data.reviews?.map((t: { rating: Number; comment: String; creationDate: string; }) => new Review(t.rating, t.comment, LocalDate.parse(t.creationDate)));

                console.log(this.restaurant);
                this.loading = false;

                setTimeout(() => {
                    this.showCarousel();
                }, 100)
            }).catch(e => {
                console.error(e);
                alert(e);
                this.loading = false;
            })
        },
        showCarousel() {
            const items = new Array<{position: number, el: HTMLElement}>(this.restaurant.restaurantPictures.length);

            for (let i = 0; i < this.restaurant.restaurantPictures.length; i++) {
                items[i] = {
                    position: i,
                    el: document.getElementById('carousel-item-' + i)!
                }
            }

            const options = {
                interval: 3000,
                indicators: {
                    activeClasses: 'bg-white dark:bg-gray-800',
                    inactiveClasses: 'bg-white/50 dark:bg-gray-800/50 hover:bg-white dark:hover:bg-gray-800',
                    items: items
                }
            }

            this.carousel = new Carousel(items, options);
            //this.carousel.cycle();

        },
        prettifyWebsiteLink() {
            if(this.restaurant.websiteLink) {
                var link = this.restaurant.websiteLink.replace("https://", "").replace("http://", "");

                if(link.lastIndexOf("/") == link.length - 1) {
                    link = link.substring(0, link.lastIndexOf("/"));
                }

                return link;
            } else {
                return undefined;
            }
            
        }
    },
    created() {
        this.getData();
    },
    mounted() {
        // If a hash (e.g. #reservation), scroll to corresponding Anchor 
        setTimeout(() => {
            if(this.$route.hash) {
                document.getElementById(this.$route.hash.replace("#", ""))?.scrollIntoView({behavior: 'smooth'});
            }
        }, 400)
    }

}
</script>

<template>
    <div v-if="!loading" class="flex flex-col items-center justify-center dark:bg-gray-900">
        <div class="absolute left-2 top-2 z-50 text-white hover:bg-white hover:text-gray-800 rounded-lg w-fit h-fit p-2" onclick="history.back()" style="cursor: pointer;">
            <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clip-rule="evenodd"></path></svg>
        </div>
        <div v-if="restaurant.restaurantPictures.length > 1" id="pictures-carousel" class="w-full absolute top-0">
            <!-- Carousel wrapper -->
            <div class="overflow-hidden relative h-[42rem]">
                <div v-for="(pic, index) in restaurant.restaurantPictures" :id="'carousel-item-' + index" class="hidden duration-700 ease-in-out">
                    <img :src="pic" class="block absolute top-1/2 left-1/2 w-full -translate-x-1/2 -translate-y-1/2 brightness-75" alt="...">
                </div>
            </div>
            <!-- Slider indicators -->
            <!--<div class="flex absolute bottom-5 left-1/2 z-30 space-x-3 -translate-x-1/2">
                <button type="button" class="w-3 h-3 rounded-full" aria-current="false" aria-label="Slide 1" data-carousel-slide-to="0"></button>
                <button type="button" class="w-3 h-3 rounded-full" aria-current="false" aria-label="Slide 2" data-carousel-slide-to="1"></button>
                <button type="button" class="w-3 h-3 rounded-full" aria-current="false" aria-label="Slide 3" data-carousel-slide-to="2"></button>
            </div>-->
            <!-- Slider controls -->
            <button type="button" @click="carousel.prev()" class="flex absolute top-0 left-0 z-30 justify-center items-center px-4 h-full cursor-pointer group focus:outline-none">
                <span class="inline-flex justify-center items-center w-8 h-8 rounded-full sm:w-10 sm:h-10 bg-white/30 dark:bg-gray-800/30 group-hover:bg-white/50 dark:group-hover:bg-gray-800/60 group-focus:ring-4 group-focus:ring-white dark:group-focus:ring-gray-800/70 group-focus:outline-none">
                    <svg class="w-5 h-5 text-white sm:w-6 sm:h-6 dark:text-gray-800" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path></svg>
                    <span class="hidden">Previous</span>
                </span>
            </button>
            <button type="button" @click="carousel.next()" class="flex absolute top-0 right-0 z-30 justify-center items-center px-4 h-full cursor-pointer group focus:outline-none">
                <span class="inline-flex justify-center items-center w-8 h-8 rounded-full sm:w-10 sm:h-10 bg-white/30 dark:bg-gray-800/30 group-hover:bg-white/50 dark:group-hover:bg-gray-800/60 group-focus:ring-4 group-focus:ring-white dark:group-focus:ring-gray-800/70 group-focus:outline-none">
                    <svg class="w-5 h-5 text-white sm:w-6 sm:h-6 dark:text-gray-800" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path></svg>
                    <span class="hidden">Next</span>
                </span>
            </button>
        </div>
        <div v-else-if="restaurant.restaurantPictures.length = 1" class="w-full absolute top-0">
            <div class="overflow-hidden relative h-[42rem]">
                <img :src="restaurant.restaurantPictures[0]" class="object-cover w-full h-full brightness-75" alt="...">
            </div>
        </div>
        <div class="relative bg-transparent h-full w-[72rem] z-30 pb-10">
            <div class="flex h-[42rem] justify-start items-center">
                <div class="flex flex-col">
                    <h1 class="text-7xl font-bold text-white align-middle drop-shadow-[10px_10px_15px_rgba(0,0,0,10.0)]">{{ restaurant.name }}</h1>
                    <h2 class="text-3xl font-semibold text-white align-middle drop-shadow-[5px_5px_8px_rgba(0,0,0,10.0)] mt-6">{{ restaurant.description }}</h2>
                    <a class="bg-white w-fit mt-8 -mb-44 py-1.5 px-3 rounded-lg font-semibold text-md tracking-wide">🍽️ {{ restaurant.restaurantType }}</a>
                </div>
            </div>
            <div class="bg-white w-full -mt-11 px-7 py-8 rounded-lg shadow-lg grid grid-rows-1 grid-cols-3 items-center justify-between">
                <div class="inline-flex">
                    <Rating :rating="Math.floor(restaurant.rating)" :size="6" class="mt-1"/>
                    <a class="ml-1.5 text-lg font-semibold text-gray-500">• {{restaurant.rating == 0 ? "No Reviews" : restaurant.rating.toFixed(1) + " / 5.0"}}</a>
                </div>
                <PriceCategory :category="restaurant.priceCategory" :size="'lg'" class="justify-self-center"></PriceCategory>
                <a class="text-lg font-semibold justify-self-end inline-flex items-center text-gray-800" :href="restaurant.websiteLink" target="_blank">
                    <svg class="w-6 h-6 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"></path></svg>
                    {{ prettifyWebsiteLink() }}
                </a>
            </div>

            <div class="flex flex-row h-full">
                <!-- Left Col -->
                <div class="w-full h-full mr-4 p-4 pt-0 pl-1 mt-4">
                    <!-- Reservation -->
                    <h3 class="text-3xl font-bold text-gray-800 mb-6 pt-4" id="reservation">Reservation</h3>
                    <div class="border rounded-lg p-4">
                        <ReservationField></ReservationField>
                    </div>
                    <!-- Reviews -->
                    <h3 class="text-3xl font-bold text-gray-800 mb-6 mt-36" id="reservation">Reviews</h3>
                    <Reviews :reviews="restaurant.reviews!" />
                </div>
                <!-- Right Col-->
                <div class="w-full h-full basis-1/2 p-4 mt-5">
                    <h4 class="text-xl font-bold text-gray-800">Information</h4>
                    <div>
                        <h5 class="text-lg font-semibold mt-6 mb-2">Address</h5>
                        <p class="text-base">{{ restaurant.address.street }}</p>
                        <p class="text-base text-gray-600">{{ restaurant.address.postalCode }} {{ restaurant.address.city }}</p>
                        <p class="mt-3">
                            <a class="text-base font-medium text-blue-700 hover:text-blue-800 inline-flex items-center" :href="'https://maps.google.com/?q=' + restaurant.address.lat + ',' + restaurant.address.lon" target="_blank">
                                Open Map
                            </a>
                        </p>
                    </div>
                    <div>
                        <h5 class="text-lg font-semibold mt-8 mb-3">Opening Hours</h5>
                        <OpeningHours :opening-times="restaurant.openingTimes"/>
                    </div>

                </div>
            </div>
            
        </div>
    </div>

    
</template>