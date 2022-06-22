import type Address from "./Address";

class Restaurant {
    id: number;
    name: string;
    description?: string;
    websiteLink?: string;
    restaurantType: string;
    priceCategory: number;
    averageRating: number;
    restaurantPictures: Array<string>
    address: Address
    //TODO Reviews
    //TODO openingTimes
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
        websiteLink?: string
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
    }


}

export default Restaurant;