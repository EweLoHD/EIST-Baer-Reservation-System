import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SearchResultsView from '../views/SearchResultsView.vue'
import NotFoundView from '../views/NotFoundView.vue'
import RestaurantView from '../views/RestaurantView.vue'
//import TestView from '../views/TestView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { 
        path: '/:pathMatch(.*)*', 
        name: 'NotFound', 
        component: NotFoundView 
    },
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/search',
        name: 'search',
        component: SearchResultsView
    },
    {
        path: '/restaurant/:id',
        name: 'restaurant',
        component: RestaurantView
    } /*,
    {
        path: '/test',
        name: 'test',
        component: TestView
    }*/
  ]
})

export default router
