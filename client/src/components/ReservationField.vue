<script setup lang="ts">
import Datepicker from 'flowbite-datepicker/Datepicker';
import { onMounted } from '@vue/runtime-core';
import { useRouter } from 'vue-router';

const props = defineProps({
    date: { type: Number, required: false },
    time: { type: Number, required: false },
    people: { type: Number, required: false },
    query: { type: String, required: false },
    queryRequired: { type: Boolean, required: false, default: false }
})

const router = useRouter();

const today = new Date().setHours(0, 0, 0, 0);

// The dropdown goes from timeDropdownStart to timeDropdownEnd in 0,5h increments
const timeDropdownStart = 6; // The dropdown starts at 6h
const timeDropdownEnd = 23; // The dropdown ends at 23h

let datePicker: Datepicker;
let timeDropdown : Dropdown;
let peopleDropdown : Dropdown;

var selectedTime = 0;
var selectedPeopleCount = 0;

onMounted(() => {
  // DatePicker
  const datepickerEl = document.getElementById('datepickerId');
  datePicker = new Datepicker(datepickerEl, {
    autohide: true,
    //orientation: 'bottom',
    format: 'dd/mm/yyyy',
    weekStart: 1, // Start on Monday
    minDate: today,
    beforeShowDay: function(date: Date) {
      // Grey out Dates before today
      if (date.getTime() < today) {
        return 'text-gray-400 dark:text-gray-500';
      }
    }
  });

  (datePicker.pickerElement as HTMLDivElement).classList.remove('z-20');
  (datePicker.pickerElement as HTMLDivElement).classList.add('z-50');
  
  // If provided, show pre-selcted date passed by the props
  if (props.date) {
    datePicker.setDate(props.date);
  }

  // Time Dropdown
  const timeDropTargetEl = document.getElementById('dropdownTime');
  const timeDropTriggerEl = document.getElementById('dropdownButtonTime');
  timeDropdown = new Dropdown(timeDropTargetEl, timeDropTriggerEl);

  // If provided, show pre-selcted time passed by the props
  if (props.time) {
      onTimeSelect(props.time);
  }

  // People Dropdown
  const peopleDropTargetEl = document.getElementById('dropdownPeople');
  const peopleDropTriggerEl = document.getElementById('dropdownButtonPeople');
  peopleDropdown = new Dropdown(peopleDropTargetEl, peopleDropTriggerEl);

  // If provided, show pre-selcted people count passed by the props
  if (props.people) {
      onPeopleSelect(props.people);
  }
})

// Gets executed when the user selects a time in the dropdown. Saves the selected time, updates the content of the dropdown button and closes the dropdown
function onTimeSelect(time: number) {
  selectedTime = time;
  document.querySelector('#dropdownButtonTime a')!.innerHTML = new Date(time).getHours() + ":" + zeroPad(new Date(time).getMinutes(), 2);

  timeDropdown.hide();
}

// Gets executed when the user selects a people count in the dropdown. Saves the selected count, updates the content of the dropdown button and closes the dropdown
function onPeopleSelect(count: number) {
  selectedPeopleCount = count;
  document.querySelector('#dropdownButtonPeople a')!.innerHTML = count  + " People"

  peopleDropdown.hide();
}

// Add leading Zeroes to number (Source: https://stackoverflow.com/a/2998874/9189184)
function zeroPad(num : number, places : number) {
  var zero = places - num.toString().length + 1;
  return Array(+(zero > 0 && zero)).join("0") + num;
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
                <!--<li key="20">
                    <a href="#" class="block px-4 py-1 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">Select People</a>
                </li>-->
                <li v-for="index in 19" :key="index">
                    <a href="javascript:;" class="block px-4 py-1 text-base hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white" @click="onPeopleSelect(index)">{{ index }}</a>
                </li>
                <li key="20+">
                    <a href="javascript:;" class="block px-4 py-1 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white" @click="onPeopleSelect(16)">20 +</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="grid grid-rows-1 grid-cols-7 gap-4 mt-5">
        <a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>
        <button class="w-full h-14 bg-blue-600 hover:bg-blue-700 rounded-md inline-flex items-center justify-center text-white font-semibold">18:00</button>
        <a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>
        <button class="w-full h-14 bg-blue-600 hover:bg-blue-700 rounded-md inline-flex items-center justify-center text-white font-semibold">19:00</button>
        <button class="w-full h-14 bg-blue-600 hover:bg-blue-700 rounded-md inline-flex items-center justify-center text-white font-semibold">19:30</button>
        <a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>
        <a class="w-full h-14 bg-gray-200 rounded-md inline-flex items-center justify-center"></a>
    </div>

</template>