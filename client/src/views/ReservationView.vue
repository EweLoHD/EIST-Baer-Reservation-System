<script setup lang="ts">
import LocalDate from '../types/LocalDate';
import Error from '../components/Error.vue';
import axios from 'axios';


</script>
<script lang="ts">
export default {
    data() {
        return({
            loading: false,
            reservation: {
                clientName: "",
                clientEmail: "",
                date: this.$route.query.date as string,
                fromTime: this.$route.query.time as string,
                restaurant: parseInt(this.$route.params.id as string) as Number,
                restaurantTable: parseInt(this.$route.query.table as string) as Number,
                people: parseInt(this.$route.query.people as string) as Number
            }
        })
    },
    methods: {
        validQuery() {
            var query = this.$route.query;
            return query.people && query.date && query.time && query.table
        },
        submit() {
            //(document.getElementById('submit_btn') as HTMLInputElement).disabled = true
            this.loading = true;

            this.reservation.clientName = (document.getElementById('first_name') as HTMLFormElement).value.trim() + " " + (document.getElementById('last_name') as HTMLFormElement).value.trim()
            this.reservation.clientEmail = (document.getElementById('email') as HTMLFormElement).value.trim()

            var vm = this;
            axios.post('http://localhost:8080/reservations', this.reservation)
                .then(function (response) {
                    console.log(response);
                    window.location.href = "/reservation-confirmed/" + response.data.id
                })
                .catch(function (error) {
                    console.log(error);
                    alert(error);
                    vm.loading = false;
                });
        }
    },
    created() {
        
    }
}
</script>

<template>
    <div class="flex flex-col items-center justify-center h-screen bg-gray-50">
        <a class="absolute top-0 right-0 m-3 p-2.5 text-center inline-flex items-center text-gray-700 hover:text-gray-500" :href="'/restaurant/' + reservation.restaurant + '#reservation'">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
        </a>
        <h1 v-if="validQuery()" class="text-5xl font-bold text-gray-800 mb-5 -m-16 inline-flex items-center">Reservation</h1>
        <div v-if="validQuery()" class="border px-4 pt-3 pb-4 rounded-lg w-128 bg-white shadow-md">
            <div class="flex flex-col">
                <h2 class="text-xl font-semibold text-gray-800 mb-2">Hofbräuhaus München</h2>
                <p class="text-md inline-flex items-center">
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
                    {{ LocalDate.parse(reservation.date).getFormated() }}
                    <svg class="w-4 h-4 ml-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                    {{ reservation.fromTime }}
                </p>
                <p class="text-md inline-flex items-center">
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
                    {{ reservation.people }} People
                </p>
                <p v-if="$route.query.tableType" class="text-md text-gray-800 inline-flex items-center">
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"></path></svg>
                    {{ $route.query.tableType }}
                </p> 
            </div>
            <form @submit="submit()" onsubmit="return false">
                <div class="grid grid-cols-2 gap-6 mt-6 mb-2">
                    <div>
                        <label for="first_name" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-300">First name</label>
                        <input type="text" id="first_name" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Max" required>
                    </div>
                    <div>
                        <label for="last_name" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-300">Last name</label>
                        <input type="text" id="last_name" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Mustermann" required>
                    </div>
                </div>
                <label for="email" class="block mb-1 text-sm font-medium text-gray-900 dark:text-gray-300">Your Email</label>
                <div class="relative mb-4">
                    <div class="flex absolute inset-y-0 left-0 items-center pl-3 pointer-events-none">
                        <svg class="w-5 h-5 text-gray-500 dark:text-gray-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"></path><path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"></path></svg>
                    </div>
                    <input type="text" id="email" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5  dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="max@mustermann.com" required>
                </div>
                <div class="flex flex-row justify-end w-full mt-8">
                    <button v-if="!loading" type="submit" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 disabled:bg-gray-500 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center self-end" id="submit_btn">Send Reservation</button>
                    <button v-else disabled type="button" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center mr-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 inline-flex items-center">
                        <svg role="status" class="inline w-4 h-4 mr-3 text-white animate-spin" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="#E5E7EB"/>
                            <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentColor"/>
                        </svg>
                        Loading...
                    </button>
                </div>
            </form>
        </div>
        <div v-else>
            <Error code="400" name="Bad Request" message="🐧"></Error>
        </div>
    </div>
</template>