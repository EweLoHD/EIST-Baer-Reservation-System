<script setup lang="ts">
import LocalDate from '../types/LocalDate';
import LocalTime from '../types/LocalTime';
import Error from '../components/Error.vue';
import axios from 'axios';
import type Restaurant from '@/types/Restaurant';


</script>
<script lang="ts">
export default {
    data() {
        return({
            loading: true,
            cancellation: parseInt(this.$route.query.cancellation as string), 
            confirmation: parseInt(this.$route.query.confirmation as string), 
            reservation: {} as {
                id: string
                clientName: string
                clientEmail: string
                fromTime: string
                toTime: string
                date: string
                people: number
                confirmed: boolean
                confirmationMailSent: boolean
                restaurant: Restaurant
            }
        })
    },
    methods: {
        async getData() {
            await axios.get('http://localhost:8080/reservations/' + this.$route.params.id).then(response => {
                this.reservation = response.data;
                this.reservation.restaurant = response.data.restaurant as Restaurant

                //this.reservation.restaurant = response.data.restaurant as Restaurant;
                this.loading = false;
            }).catch(e => {
                console.error(e);
                alert(e);
            })
        },
        cancelReservation() {
            (document.getElementById("cancel") as HTMLInputElement).disabled = true;

            this.loading = true;

            //document.getElementById("main").innerHTML = "Your Reservation was cancelled. You can now close this Page!"

            var vm = this;
            axios.delete('http://localhost:8080/reservations/' + this.$route.params.id)
                .then(function (response) {
                    console.log(response);

                    vm.loading = false;
                    setTimeout(() => {
                        document.getElementById("main")!.innerHTML = "Your Reservation was cancelled. You can now close this Page!"
                    }, 100);
                })
                .catch(function (error) {
                    console.log(error);
                    alert(error);
                    vm.loading = false;
                });
        },
        confirmReservation() {
            (document.getElementById("confirm") as HTMLInputElement).disabled = true;

            var vm = this;
            axios.post('http://localhost:8080/reservations/' + this.$route.params.id + '/confirm').then(response => {
                console.log(response);

                vm.loading = false;
                setTimeout(() => {
                    document.getElementById("main")!.innerHTML = "Your Reservation was confirmed 🎉. You can now close this Page!"
                }, 100);
            }).catch(e => {
                console.error(e);
                alert(e);
                vm.loading = false;
            })
        }
    },
    created() {
        this.getData().then(() => {
            if (this.cancellation && this.reservation.confirmed) {
                document.getElementById("main")!.innerHTML = "❌ This Reservation can no longer be canceled, because it has already been confirmed."    
            }

            if (this.confirmation && this.reservation.confirmed) {
                document.getElementById("main")!.innerHTML = "🐧 This Reservation has already been confirmed!"    
            }
        })
       
    },
    mounted() {
        
    }
}
</script>

<template>
    <div class="flex flex-col items-center justify-center h-screen bg-gray-50">
        <a class="absolute top-0 right-0 m-3 p-2.5 text-center inline-flex items-center text-gray-700 hover:text-gray-500" href="/">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
        </a>
        <svg v-if="loading" role="status" class="w-10 h-10 mr-2 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
            <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
        </svg>
        <div v-else class="flex flex-col items-center justify-center" id="main">
            <h1 v-if="cancellation" class="text-4xl font-bold text-gray-800 inline-flex items-center mb-6">
                Cancel Reservation 😥
            </h1>
            <h1 v-else-if="confirmation" class="text-4xl font-bold text-gray-800 inline-flex items-center mb-6">
                Confirm Reservation 👌
            </h1>
            <h1 v-else class="text-4xl font-bold text-gray-800 inline-flex items-center">
                Reservation Completed 🎉
            </h1>
            <h3 v-if="!cancellation && !confirmation" class="text-md font-base text-gray-800 mb-5 mt-1">We sent you an E-Mail. You can now close this page.</h3>
            <div class="border px-4 pt-3 pb-4 rounded-lg w-128 bg-white shadow-md" id="card">
                <div class="flex flex-col">
                    <h2 class="text-xl font-semibold text-gray-800 mb-2">{{ reservation.restaurant.name }}</h2>
                    <p class="text-md inline-flex items-center">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
                        {{ LocalDate.parse(reservation.date).getFormated() }}
                        <svg class="w-4 h-4 ml-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        {{ reservation.fromTime.split(':')[0] + ':' + reservation.fromTime.split(':')[1] }}
                    </p>
                    <p class="text-md inline-flex items-center">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
                        {{ reservation.people }} People
                    </p>
                    <p class="text-md text-gray-800 inline-flex items-center mt-4">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
                        {{ reservation.clientName }}
                    </p>
                    <p class="text-md text-gray-800 inline-flex items-center">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path></svg>
                        {{ reservation.clientEmail }}
                    </p>
                    <div v-if="cancellation" class="flex flex-row justify-end w-full mt-4">
                        <button id="cancel" @click="cancelReservation()" class="focus:outline-none text-white bg-red-600 hover:bg-red-700 disabled:bg-red-800 font-medium rounded-lg text-sm px-5 py-2.5">
                            Cancel Reservation
                        </button>
                    </div>
                    <div v-else-if="confirmation" class="flex flex-row justify-end w-full mt-4">
                        <button id="confirm" @click="confirmReservation()" class="focus:outline-none text-white bg-green-500 hover:bg-green-600 disabled:bg-green-700 font-medium rounded-lg text-sm px-5 py-2.5">
                            Confirm Reservation
                        </button>
                    </div>
                    <div v-else class="flex flex-row justify-between w-full mt-8">
                        <a :href="'http://localhost:8080/generate-calendar/' + $route.params.id" class="inline-flex py-2.5 px-5 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700">
                            <svg class="w-5 h-5 mr-2 -ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
                            Add to your Calendar
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>