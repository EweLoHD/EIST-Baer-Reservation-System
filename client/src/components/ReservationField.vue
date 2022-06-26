<script setup lang="ts">
import Datepicker from 'flowbite-datepicker/Datepicker';
import Modal from 'flowbite/src/components/modal'
import { onMounted } from '@vue/runtime-core';
import { useRoute, useRouter } from 'vue-router';
import dateFormat, { masks } from 'dateformat';

import axios from 'axios';
</script>

<script lang="ts">
export default {
    expose: ['slots'],
    props: {
        pDate: { type: Number, required: false },
        pTime: { type: Number, required: false },
        pPeople: { type: Number, required: false }
    },
    data() {
        return {
            // The dropdown goes from timeDropdownStart to timeDropdownEnd in 0,5h increments
            timeDropdownStart: 6, // The dropdown starts at 6h
            timeDropdownEnd: 23, // The dropdown ends at 23h
            datePicker: {} as DatePicker,
            timeDropdown: {} as Dropdown,
            peopleDropdown: {} as Dropdown,
            selectedTime: 0,
            selectedPeopleCount: 0,
            dataSelected: false,
            slots: {} as Array<{ time: string, tables: Array<{ id: string, tableType: string }> }>,
            tableTypeModal: {} as Modal,
            tables: {} as Array<{ id: string, tableType: string }>
        }
    },
    methods: {
        onDateSelect() {
            this.submit();
        },
        onTimeSelect(time: number) {
            this.selectedTime = time;
            document.querySelector('#dropdownButtonTime a')!.innerHTML = new Date(time).getHours() + ":" + this.zeroPad(new Date(time).getMinutes(), 2);

            this.timeDropdown.hide();

            this.submit();
        },
        onPeopleSelect(count: number) {
            this.selectedPeopleCount = count;
            document.querySelector('#dropdownButtonPeople a')!.innerHTML = count  + " People"

            this.peopleDropdown.hide();

            this.submit();
        },
        slotClick(s: number) {
            console.log(s);
        },
        // Add leading Zeroes to number (Source: https://stackoverflow.com/a/2998874/9189184)
        zeroPad(num : number, places : number) {
            var zero = places - num.toString().length + 1;
            return Array(+(zero > 0 && zero)).join("0") + num;
        },
        submit() {
            this.dataSelected = (this.selectedTime != 0 && this.selectedPeopleCount != 0 && this.datePicker.getDate() != undefined)
            if (this.dataSelected) {
                //(document.getElementById('message') as HTMLDivElement).style.display = "none";

                axios.get('http://localhost:8080/restaurants/' + this.$route.params.id + '/available-timeslots'
                    + '?date=' + dateFormat(this.datePicker.getDate(), "yyyy-mm-dd") 
                    + '&time=' + dateFormat(new Date(this.selectedTime), "HH:MM")
                    + '&people=' + this.selectedPeopleCount).then(response => {
                    
                    console.log(response);

                    this.slots = response.data as Array<{ time: string, tables: Array<{ id: string, tableType: string }> }>
                    //var slots = response.data as Array<{ time: string, tables: Array<{ id: string, tableType: string }> }>

                    this.slots.forEach((slot, i) => {
                        if (slot.tables.length > 0) {
                        }
                    });

                }).catch(e => {
                    console.error(e);
                    alert(e);
                });
                
            } else {
                //(document.getElementById('grid') as HTMLDivElement).style.display = "none";
                //(document.getElementById('message') as HTMLDivElement).style.display = "flex";
            }
        },
        tablesFound() {
            for (let i = 0; i < this.slots.length; i++) {
                if (this.slots[i].tables.length > 0) {
                    return true;
                }
            }

            return false;
        },
        selectSlot(slot: { time: string, tables: Array<{ id: string, tableType: string }> }) {
            this.tables = slot.tables;
            if (slot.tables.length > 1) {
                this.showTableTypeModal();
            } else {
                this.selectTable(slot.tables[0]);
            }
        },
        onTableTypeSelected() {
            var index = parseInt(document.querySelector('input[name="default-radio"]:checked')!.id.replace("default-radio-", ""));
            this.tableTypeModal.hide();
            this.selectTable(this.tables[index]);
        },
        selectTable(table: { id: string, tableType: string }) {
            var params = new URLSearchParams({
                "date": dateFormat(this.datePicker.getDate(), "yyyy-mm-dd"),
                "time": dateFormat(new Date(this.selectedTime), "HH:MM"),
                "people": this.selectedPeopleCount+"",
                "table": table.id
            });

            window.location.href = this.$route.params.id + "/reservation?" + params.toString();
        },
        showTableTypeModal() {
            this.tableTypeModal.show();
            document.getElementById('app')!.appendChild(document.getElementById('table-type-modal')!)
        }
    },
    mounted() {
        // DatePicker
        const datepickerEl = document.getElementById('datepickerId');
        this.datePicker = new Datepicker(datepickerEl, {
            autohide: true,
            //orientation: 'bottom',
            format: 'dd/mm/yyyy',
            weekStart: 1, // Start on Monday
            minDate: new Date().setHours(0, 0, 0, 0),
            beforeShowDay: function(date: Date) {
                // Grey out Dates before today
                if (date.setHours(0, 0, 0, 0) < new Date().setHours(0, 0, 0, 0)) {
                    return 'text-gray-400 dark:text-gray-500';
                }
            }
        });

        (this.datePicker.pickerElement as HTMLDivElement).classList.remove('z-20');
        (this.datePicker.pickerElement as HTMLDivElement).classList.add('z-50');

        (this.datePicker.pickerElement as HTMLDivElement).addEventListener('click', this.onDateSelect);

        // If provided, show pre-selcted date passed by the props
        if (this.pDate) {
            this.datePicker.setDate(this.pDate);
        }

        // Time Dropdown
        const timeDropTargetEl = document.getElementById('dropdownTime');
        const timeDropTriggerEl = document.getElementById('dropdownButtonTime');
        this.timeDropdown = new Dropdown(timeDropTargetEl, timeDropTriggerEl);

        // If provided, show pre-selcted time passed by the props
        if (this.pTime) {
            this.onTimeSelect(this.pTime);
        }

        // People Dropdown
        const peopleDropTargetEl = document.getElementById('dropdownPeople');
        const peopleDropTriggerEl = document.getElementById('dropdownButtonPeople');
        this.peopleDropdown = new Dropdown(peopleDropTargetEl, peopleDropTriggerEl);

        // If provided, show pre-selcted people count passed by the props
        if (this.pPeople) {
            this.onPeopleSelect(this.pPeople);
        }

        this.submit();

        // Create TableType Modal
        const tableTypeModaltargetEl = document.getElementById('table-type-modal');
        this.tableTypeModal = new Modal(tableTypeModaltargetEl, {backdropClasses: 'bg-gray-900 bg-opacity-50 dark:bg-opacity-80 fixed inset-0 z-50'});
    }

}
</script>

<template>
    <!--Dropdowns (Date, Time, People)-->
    <div class="justify-self-end inline-flex rounded-md gap-x-4" role="group">
        <!--Date Picker-->
        <div class="relative">
            <div class="absolute inset-y-0 left-0 flex items-center pl-4 pointer-events-none dark:text-white">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                </svg>
            </div>
            <input id="datepickerId" type="text" class="block py-2 px-0 text-sm font-medium text-center items-center placeholder-gray-900 text-gray-900 rounded-md border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-1 focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white" placeholder="Select Date">
            <div class="absolute inset-y-0 right-0 flex items-center pr-4 pointer-events-none dark:text-white">
                <svg class="w-4 h-4 ml-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
            </div>
        </div>
        <!--Time Picker-->
        <button id="dropdownButtonTime" class="py-2 w-32 px-4 text-sm font-medium text-center inline-flex items-center justify-between text-gray-900 bg-white rounded-md border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-2 focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white" type="button">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <a>Time</a> 
            <svg class="w-4 h-4 ml-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
            </svg>
        </button>
        <!--Time Dropdown-->
        <div id="dropdownTime" class="overflow-y-scroll max-h-96 z-10 hidden bg-white divide-y divide-gray-100 rounded shadow w-36 dark:bg-gray-700">
            <ul class="py-1 text-sm text-gray-700 dark:text-gray-200">
                <li v-for="index in ((timeDropdownEnd-timeDropdownStart)*2 + 1)" :key="index">
                    <a href="javascript:;" class="block px-4 py-1 text-base hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white" 
                        @click="onTimeSelect((index%2 == 0) ? new Date(0).setHours(Math.floor(index/2)+timeDropdownStart-1, 30, 0, 0) : new Date(0).setHours(Math.floor(index/2)+timeDropdownStart, 0, 0, 0))"
                    >
                        {{ (index%2 == 0) ? (zeroPad(Math.floor(index/2)+timeDropdownStart-1, 2) + ":30") : (zeroPad(Math.floor(index/2)+timeDropdownStart, 2) + ":00")}}
                    </a>
                </li>
            </ul>
        </div>
        <!--People Picker-->
        <button id="dropdownButtonPeople" class="py-2 w-40 px-4 text-sm font-medium text-center inline-flex justify-between items-center text-gray-900 bg-white rounded-md border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-2 focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white" type="button">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
            </svg>
            <a>People</a>
            <svg class="w-4 h-4 ml-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
            </svg>
        </button>
        <!--People Dropdown-->
        <div id="dropdownPeople" class="overflow-y-scroll max-h-96 z-10 hidden bg-white divide-y divide-gray-100 rounded shadow w-40 dark:bg-gray-700">
            <ul class="py-1 text-sm text-gray-700 dark:text-gray-200">
                <li v-for="index in 19" :key="index">
                    <a href="javascript:;" class="block px-4 py-1 text-base hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white" @click="onPeopleSelect(index)">{{ index }}</a>
                </li>
                <li key="20+">
                    <a href="javascript:;" class="block px-4 py-1 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white" @click="onPeopleSelect(16)">20 +</a>
                </li>
            </ul>
        </div>
    </div>
    <div v-if="dataSelected">
        <div v-if="tablesFound()" class="grid grid-rows-1 grid-cols-7 gap-4 mt-5 h-14" id="grid">
            <!--<a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>
            <button class="w-full h-14 bg-blue-600 hover:bg-blue-700 rounded-md inline-flex items-center justify-center text-white font-semibold">18:00</button>
            <a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>
            <button class="w-full h-14 bg-blue-600 hover:bg-blue-700 rounded-md inline-flex items-center justify-center text-white font-semibold">19:00</button>
            <button class="w-full h-14 bg-blue-600 hover:bg-blue-700 rounded-md inline-flex items-center justify-center text-white font-semibold">19:30</button>
            <a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>
            <a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>-->
            <button v-for="slot in slots" @click="selectSlot(slot)" :disabled="slot.tables.length == 0" class="w-full h-14 bg-blue-600 hover:bg-blue-700 disabled:bg-gray-300 rounded-md inline-flex items-center justify-center text-white font-semibold">
                {{ slot.time.split(':')[0] + ':' + slot.time.split(':')[1] }}
            </button>
        </div>
        <div v-else class="flex flex-col items-center justify-center mt-5 h-14" id="message">
            <a class="text-base text-gray-600">No Tables were found for your Selection</a>
        </div>
    </div>
    <div v-else class="flex flex-col items-center justify-center mt-5 h-14" id="message">
        <a class="text-base text-gray-600">Please select a Date, Time and Number of People</a>
    </div>

    <div id="table-type-modal" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-[5000] w-full md:inset-0 h-modal md:h-full">
        <div class="relative p-4 w-96 max-w-md h-full md:h-auto">
            <!-- Modal content -->
            <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
                <button @click="tableTypeModal.hide()" type="button" class="absolute top-3 right-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center dark:hover:bg-gray-800 dark:hover:text-white">
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>  
                </button>
                <div class="py-4 px-4 ">
                    <h3 class="mb-4 text-xl font-medium text-gray-900 dark:text-white">Select Table</h3>
                    <div v-for="(table, index) in tables" class="flex items-center mb-3 ml-2">
                        <input :id="'default-radio-'+index" :checked="index==0" type="radio" value="" name="default-radio" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-transparent">
                        <label :for="'default-radio-'+index" class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">{{ table.tableType }}</label>
                    </div>
                    <div class="flex justify-end">
                        <button @click="onTableTypeSelected()" type="button" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 font-medium rounded-lg text-sm px-5 py-2.5">Confirm</button>
                    </div>
                </div>
                
            </div>
        </div>
    </div> 

</template>