<script setup lang="ts">
import LocationSearch from '@/components/LocationSearch.vue'
import Modal from 'flowbite/src/components/modal'
import type { PropType } from '@vue/runtime-core'
import axios from 'axios'
</script>

<script lang="ts">
export interface FilterData {
    location: {
        lat: number,
        lon: number
    },
    maxDistance: number,
    minRating: number,
    maxPrice: number,
    category: String
}

export default {
    props: {
        preselectedFilterData: {
            type: Object as PropType<FilterData>,
            required: false
        }
    },
    data() {
        return {
            locationSearchModal: null as Modal,
            filterData: {
                location: {
                    // lat & lon of Munich
                    /*lat: 48.1371079,
                    lon: 11.5753822*/
                } as {lat: number, lon: number},
                maxDistance: 0,
                minRating: 1,
                maxPrice: 3,
                category: ""
            } as FilterData,
            types: [] as Array<string>
        }
    },
    watch: {
        'preselectedFilterData.category'(value) {
            this.filterData.category = this.preselectedFilterData!.category;
        }
    },
    emits: {
        onFilterApplied(playLoad: FilterData) {
            return true;
        }
    },
    methods: {
        getTypes() {
            axios.get('http://localhost:8080/restaurants/types').then(response => {
                this.types = response.data as Array<string>
            }).catch(e => {
                console.error(e);
                alert(e);
            })
        },
        showLoctionSearch() {
            this.locationSearchModal.show();
        },
        onLocationSelected(location: {lat: number, lon: number}) {
            this.filterData.location = location;

            if(this.filterData.maxDistance == 0 && Object.keys(location).length > 0) {
                (document.getElementById('distance') as HTMLInputElement).disabled = false;
                this.filterData.maxDistance = 20; // Default 20 km
            }

            this.locationSearchModal.hide();
        },
        onApply() {
            this.$emit('onFilterApplied', this.filterData)
        }
    },
    beforeMount() {
        this.getTypes();
    },
    created() {
        /*if (this.preselectedFilterData && Object.keys(this.preselectedFilterData).length > 0) {
            this.filterData = this.preselectedFilterData as FilterData;
        }*/

    },
    mounted() {
        this.locationSearchModal = new Modal(document.getElementById('location-modal'), {});

        // Not required atm, but might be necessary later if we provide a preselected location
        if((this.filterData.location && Object.keys(this.filterData.location).length > 0) && this.filterData.maxDistance == 0) {
            (document.getElementById('distance') as HTMLInputElement).disabled = false;
            this.filterData.maxDistance = 20; // Default 20 km
        }
    }
}
</script>


<template>
    <!--Distance Filter-->
    <label for="distance" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Max Distance</label>
    <div class="flex flex-row">
        <div class="w-full h-fit">
            <select id="distance" v-model.number="filterData.maxDistance" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" disabled>
                <option value=0 disabled v-if="filterData.maxDistance == 0">No Location slected</option>
                <option value=2>2 km</option>
                <option value=5>5 km</option>
                <option value=10>10 km</option>
                <option value=15>15 km</option>
                <option value=20>20 km</option>
                <option value=50>50 km</option>
                <option value=100>100 km</option>
                <option value=-1>100 km +</option>
            </select> 
        </div>
        <!--📍 Button-->
        <button type="button" @click="showLoctionSearch" class="w-fit h-auto ml-2 mb-3 text-gray-900 bg-gray-50 rounded-lg border focus:ring-1 border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 font-small text-sm px-2.5 py-1.5">
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clip-rule="evenodd"></path></svg>
        </button> 
    </div>
    
    <!--Rating Filter-->
    <label for="rating" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Min. Rating</label>
    <select id="rating" v-model.number="filterData.minRating" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
        <option value=1>1 Stars</option>
        <option value=2>2 Stars</option>
        <option value=3>3 Stars</option>
        <option value=4>4 Stars</option>
        <option value=5>5 Stars</option>
    </select>
    <!--Price Filter-->
    <label for="price" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Max. Price</label>
    <select id="price" v-model.number="filterData.maxPrice" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
        <option value=1>€</option>
        <option value=2>€€</option>
        <option value=3>€€€</option>
    </select>
    <!--Category Filter-->
    <label for="category" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-400">Category</label>
    <select id="category" v-model="filterData.category" class="block p-2 mb-3 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
        <option value="">Filter Category</option>
        <option v-for="t in types" :value="t">{{t}}</option>
    </select>
    <!--Apply Filter Button-->
    <div class="flex flex-row-reverse mt-12">
    <button type="button" @click="onApply()" class="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 inline-flex items-center font-medium rounded-lg text-sm px-5 py-2.5 mb-1.5 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
            Apply Filter
            <svg class="w-5 h-5 ml-2 -mr-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M5 4a1 1 0 00-2 0v7.268a2 2 0 000 3.464V16a1 1 0 102 0v-1.268a2 2 0 000-3.464V4zM11 4a1 1 0 10-2 0v1.268a2 2 0 000 3.464V16a1 1 0 102 0V8.732a2 2 0 000-3.464V4zM16 3a1 1 0 011 1v7.268a2 2 0 010 3.464V16a1 1 0 11-2 0v-1.268a2 2 0 010-3.464V4a1 1 0 011-1z"></path></svg>
        </button> 
    </div>

    <!--Location Search Modal-->
    <div id="location-modal" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 w-full md:inset-0 h-modal md:h-full">
        <LocationSearch @onSelected="onLocationSelected"/>
    </div>
</template>