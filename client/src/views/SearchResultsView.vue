<script setup lang="ts">
import { onBeforeMount, onMounted, ref } from '@vue/runtime-core';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

import Modal from 'flowbite/src/components/modal'

import 'leaflet/dist/leaflet.css';
import * as L from 'leaflet';

import Error from '@/components/Error.vue';
import RestaurantCard from '@/components/RestaurantCard.vue'
import SearchBar from '@/components/SearchBar.vue';
import LocationSearch from '@/components/LocationSearch.vue'

import Restaurant from '@/types/Restaurant';
import Address from '@/types/Address';

const route = useRoute();
const router = useRouter();

defineExpose({
    route, 
    router
})
</script>

<script lang="ts">
export default {
    data() {
        return {
            restaurants: [],
            error: false,
            loading: true,
            locationSearchModal: null as Modal,
            filterData: {
                locationCenter: {}
            },
            mapData: {
                selectedRestaurant: {} as Restaurant
            }
        }
    },
    methods: {
        getData() {
            // TODO Send search request to backend
            axios.get('test.json').then(response => {
                console.log(response.data);
                this.restaurants = response.data.restaurants;
                this.loading = false;
            }).catch(e => {
                console.error(e);

                // TODO Handle Errors
                this.loading = false;
                this.error = true;
            })
        },
        showMap() {
            var map = L.map('map').setView([48.15, 11.58], 13);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '© OpenStreetMap'
            }).addTo(map);
            setTimeout(function(){ map.invalidateSize()}, 200);

            // If the user clicks in the map, the current Restaurant Card should close
            map.on('click', (e: any) => {
                // Remove red color from marker that was selected
                document.getElementsByClassName("huechange")[0]?.classList.remove("huechange");

                this.mapData.selectedRestaurant = {} as Restaurant;
            });

            // Add Marker for each restaurant 
            this.restaurants.forEach((r: Restaurant) => {
                var marker = L.marker([r.address.lat, r.address.long]).addTo(map);
                
                // If the user clicks on a marker, the corresponding Restaurant Card should be displayed.
                marker.on('click', (e: any) => {
                    // Remove red color from marker that was selected before
                    document.getElementsByClassName("huechange")[0]?.classList.remove("huechange");
                    // Set marker color to red (yes it's a stupid hack)
                    marker.getElement()?.classList.add("huechange");
                    

                    this.mapData.selectedRestaurant = r;
                });
            });
        },
        showLoctionSearch() {
            this.locationSearchModal.show();
        },
        onLocationSelected(location: {lat: number, lon: number}) {
            console.log(location);
            this.filterData.locationCenter = location;
            this.locationSearchModal.hide();
        }
    },
    created() {
        // TODO get query params with this.route.query
        

        this.getData();
    },
    mounted() {
        this.locationSearchModal = new Modal(document.getElementById('location-modal'), {});
    }
}
</script>

<template>
    <div class="flex flex-col items-center justify-screen h-screen dark:bg-gray-900 relative">
        <div class="fixed inline-flex justify-center mb-4 p-4 w-full bg-white border border-gray-200 shadow-md dark:bg-gray-800 dark:border-gray-700">
            <div class="w-fit">
                <SearchBar></SearchBar>
            </div>
        </div>
        <div class="flex flex-row justify-start w-256 h-full gap-4 pt-[88px]">
            <div class="flex flex-col w-96">
                <!--Show Map Button-->
                <button  data-modal-toggle="map-modal" v-on:click="showMap()" type="button" class="h-16 mb-4 shadow-md text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-base px-5 py-2.5 text-center flex flex-row justify-items-center justify-center items-center dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none">
                    Show Map
                    <svg class="w-6 h-6 ml-2 -mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7"></path></svg>
                </button>
                <!--Filter Sidebar-->
                <div class="h-fit px-4 py-3 max-w-sm bg-white rounded-lg border border-gray-200 shadow-md dark:bg-gray-800 dark:border-gray-700">
                    <!--Distance Filter-->
                    <label for="distance" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Distance</label>
                    <div class="flex flex-row">
                        <div class="w-full h-fit">
                            <select id="distance" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                                <option value="" selected>Select Range</option>
                                <option value="20">20 km</option>
                            </select> 
                        </div>
                        <!--📍 Button-->
                        <button type="button" @click="showLoctionSearch" class="w-fit h-auto ml-2 mb-3 text-gray-900 bg-gray-50 rounded-lg border focus:ring-1 border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 font-small text-sm px-2.5 py-1.5">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clip-rule="evenodd"></path></svg>
                        </button> 
                    </div>
                    
                    <!--Rating Filter-->
                    <label for="rating" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Min. Rating</label>
                    <select id="rating" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                        <option value="1" selected>1 Stars</option>
                        <option value="2">2 Stars</option>
                        <option value="3">3 Stars</option>
                        <option value="4">4 Stars</option>
                        <option value="5">5 Stars</option>
                    </select>
                    <!--Price Filter-->
                    <label for="price" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Max. Price</label>
                    <select id="price" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                        <option value="1">€</option>
                        <option value="2">€€</option>
                        <option value="3" selected>€€€</option>
                    </select>
                    <!--Category Filter-->
                    <label for="category" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Category</label>
                    <select id="category" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                        <option value="" selected>Filter Category</option>
                        <option value="bavarian">Bavarian</option>
                        <option value="italian">Italian</option>
                        <option value="burger">Burger</option>
                    </select>
                    <!--Apply Filter Button-->
                    <div class="flex flex-row-reverse mt-12">
                    <button type="button" class="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 inline-flex items-center font-medium rounded-lg text-sm px-5 py-2.5 mb-1.5 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
                            Apply Filter
                            <svg class="w-5 h-5 ml-2 -mr-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M5 4a1 1 0 00-2 0v7.268a2 2 0 000 3.464V16a1 1 0 102 0v-1.268a2 2 0 000-3.464V4zM11 4a1 1 0 10-2 0v1.268a2 2 0 000 3.464V16a1 1 0 102 0V8.732a2 2 0 000-3.464V4zM16 3a1 1 0 011 1v7.268a2 2 0 010 3.464V16a1 1 0 11-2 0v-1.268a2 2 0 010-3.464V4a1 1 0 011-1z"></path></svg>
                        </button> 
                    </div>
                </div>
            </div>
            
            <div class="flex items-center h-full w-full flex-grow ">
                <div id="loading" v-if="loading" class="w-full flex flex-col items-center">
                    <svg role="status" class="w-12 h-12 mr-2 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
                        <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
                    </svg>
                </div>
                <div v-else class="overflow-y-auto gap-4 self-stretch pr-4 scroll-ml-10 scroll-pb-10 w-full">
                    <div class="grid grid-col gap-4 pb-4">
                        <RestaurantCard v-for="(item, index) in restaurants" :restaurant="item" :key="index"></RestaurantCard>
                    </div>
                </div>
            </div>
        </div>

        <!--Map Modal-->
        <div id="map-modal" data-modal-show="false" tabindex="-1" class="hidden overflow-y-auto overflow-x-hidden fixed top-4 right-0 left-0 z-50 w-full md:inset-0 md:h-modal h-modal">
            <div class="relative p-4 w-full max-w-7xl h-auto">
                <div class="relative bg-white rounded-lg shadow-2xl dark:bg-gray-700">
                    <!--Close Button-->
                    <div class="absolute top-3 right-3 w-fit z-[314159] shadow-md">
                        <button type="button" class="text-gray-900 bg-gray-100 ring-2 ring-neutral-400 hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-2 ml-auto inline-flex items-center dark:hover:bg-gray-600 dark:hover:text-white" data-modal-toggle="map-modal">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>  
                        </button>
                    </div>
                    <div class="space-y-6 h-192 w-full">
                        <div id="map" class="h-full relative rounded-lg">

                        </div>
                        <!--Restaurant Card-->
                        <div class="absolute bottom-12 left-1/2 transform -translate-x-1/2 w-192 z-[314159] shadow-lg">
                            <div class="relative">
                                <RestaurantCard v-if="mapData.selectedRestaurant.name" :restaurant="mapData.selectedRestaurant"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--Location Search Modal-->
        <div id="location-modal" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 w-full md:inset-0 h-modal md:h-full">
            <LocationSearch @onSelected="onLocationSelected"/>
        </div>
    </div>
</template>

<style>
img.huechange { filter: hue-rotate(150deg); }
</style>