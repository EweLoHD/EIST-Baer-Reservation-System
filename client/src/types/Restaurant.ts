import type Address from "./Address";
import type OpeningTime from "./OpeningTime";
import type Review from "./Review";

class Restaurant {
    id: number;
    name: string;
    description?: string;
    websiteLink?: string;
    restaurantType: string;
    priceCategory: number;
    averageRating: number;
    restaurantPictures: Array<string>;
    address: Address;
    openingTimes?: Array<OpeningTime>;
    reviews?: Array<Review>;
    //TODO Tables
    
    constructor(
        id: number, 
        name: string, 
        restaurantType: string, 
        priceCategory: number, 
        averageRating: number, 
        restaurantPictures: Array<string>,
        address: Address,
        description?: string,
        websiteLink?: string,
        openingTimes?: Array<OpeningTime>,
        reviews?: Array<Review>
    ) {
        this.id = id
        this.name = name
        this.restaurantType = restaurantType
        this.priceCategory = priceCategory
        this.averageRating = averageRating
        this.restaurantPictures = restaurantPictures
        this.address = address
        this.description = description
        this.websiteLink = websiteLink
        this.openingTimes = openingTimes
        this.reviews = reviews
    }

}

export default Restaurant;