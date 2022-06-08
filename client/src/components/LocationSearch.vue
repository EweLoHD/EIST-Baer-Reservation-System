<script setup lang="ts">
import axios from 'axios';
import { defineComponent } from 'vue'
</script>

<script lang="ts">
interface Location {
  lat: string
  lon: string
  display_name: string
}

export default defineComponent({
    data() {
        return {
            searchResults: [] as Array<Location>
        }
    },
    emits: {
        onSelected(payload: {lat: number, lon: number}) {
            return true;
        }
    },
    methods: {
        select(location: Location) {
            this.$emit('onSelected', {
                lat: parseFloat(location.lat),
                lon: parseFloat(location.lon)
            });
        }
    },
    mounted() {
        let input = (document.getElementById('search-input') as HTMLInputElement);
        let timeout: number = 0;

        input?.addEventListener('keyup', function (e) {
            clearTimeout(timeout);
            timeout = setTimeout(function () {
                searchLocation(input.value);
            }, 600);
        });

        var vm = this;
        function searchLocation(query: string) {
            if (query) {
                const params = new URLSearchParams([['q', query.trim()], ['format', 'json'], ['limit', '5']]);

                axios.get('https://nominatim.openstreetmap.org/search', { params }).then(response => {
                    //console.log(response.data);
                    vm.searchResults = response.data;
                }).catch(e => {
                    console.error(e);
                })
            } else {
                vm.searchResults = [];
            }
        }
    }
})
</script>


<template>
    <div class="relative p-4 w-224 max-w-224 h-full md:h-auto">
        <!-- Modal content -->
        <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
            <!-- Modal header -->
            <form class="flex justify-between items-center p-0 rounded-t border-b dark:border-gray-600" onsubmit="return false;">
                <label for="simple-search" class="sr-only">Search</label>
                <div class="relative w-full">
                    <div class="flex absolute inset-y-0 left-0 items-center pl-4 pointer-events-none">
                        <svg class="w-5 h-5 text-gray-500 dark:text-gray-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd"></path></svg>
                    </div>
                    <input type="text" id="search-input" autocomplete="off" list="autocompleteOff" role="search" class="bg-transparent border-0 text-gray-900 focus:ring-0 text-md appearance-none block w-full pl-12 p-4" placeholder="Search Location">
                </div>
                <button type="button" @click="$emit('onSelected', {})" class="mr-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-2 ml-auto inline-flex items-center dark:hover:bg-gray-600 dark:hover:text-white">
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>  
                </button>
            </form>
            <!-- Modal body -->
            <div class="p-4 space-y-3">
                <div v-if="searchResults && searchResults.length > 0" class="grid grid-col gap-3">
                    <button v-for="(r, index) in searchResults" :key="index" @click="select(r)" class="h-12 rounded-md bg-gray-100 hover:bg-blue-700 text-gray-900 hover:text-gray-100 text-sm font-medium text-left pl-4 inline-flex items-center">
                        {{ r.display_name }}
                    </button>
                </div>
                <div v-else class="inline-flex h-24 items-center justify-center text-center w-full">
                    <a class="text-xl font-medium">Nothing found 🙁</a>
                </div>
            </div>
        </div>
    </div>
</template>