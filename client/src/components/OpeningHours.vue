<script setup lang="ts">
import OpeningTime from '@/types/OpeningTime'

</script>
<script lang="ts">
export default {
    props: {
        openingTimes: Array<OpeningTime>
    },
    methods: {
        getOpeningTimesByDay(dayOfWeek: Number): Array<OpeningTime> {
            return this.openingTimes!.filter(t => t.dayOfWeek.value == dayOfWeek).sort((a, b) => {
                if(a.fromTime.hours > b.toTime.hours) return 1;
                if(a.toTime.hours < b.fromTime.hours) return -1;
                return 0;
            })
        },
        openingTimesToText(dayOfWeek: Number): String {
            if(this.getOpeningTimesByDay(dayOfWeek).length > 0) {
                return this.getOpeningTimesByDay(dayOfWeek).map(t => {
                        return this.zeroPad(t.fromTime.hours, 2) 
                            + ':' + this.zeroPad(t.fromTime.minutes, 2)
                            + ' - ' 
                            + this.zeroPad(t.toTime.hours, 2)
                            + ':' + this.zeroPad(t.toTime.minutes, 2)
                    }).join('\n');
            } else {
                return "Closed";
            }
        },
        // Add leading Zeroes to number (Source: https://stackoverflow.com/a/2998874/9189184)
        zeroPad(num : Number, places : number) {
            var zero = places - num.toString().length + 1;
            return Array(+(zero > 0 && zero)).join("0") + num;
        }
    },
    created() {
        console.log(this.getOpeningTimesByDay(2));
    }
}
</script>
<template>
    <div class="grid grid-cols-2 w-56 gap-1.5 gap-x-4">
        <a class="text-base">Monday</a>
        <a class="text-base text-left text-gray-600">{{ openingTimesToText(1) }}</a>
        <a class="text-base">Tuesday</a>
        <a class="text-base text-left text-gray-600">{{ openingTimesToText(2) }}</a>
        <a class="text-base">Wednesday</a>
        <a class="text-base text-left text-gray-600">{{ openingTimesToText(3) }}</a>
        <a class="text-base">Thursday</a>
        <a class="text-base text-left text-gray-600">{{ openingTimesToText(4) }}</a>
        <a class="text-base">Friday</a>
        <a class="text-base text-left text-gray-600">{{ openingTimesToText(5) }}</a>
        <a class="text-base">Saturday</a>
        <a class="text-base text-left text-gray-600">{{ openingTimesToText(6) }}</a>
        <a class="text-base">Sunday</a>
        <a class="text-base text-left text-gray-600">{{ openingTimesToText(7) }}</a>
    </div>
</template>