class Address {
    lat: number
    lon: number
    street: string
    city: string
    postalCode: string
    country: string


	constructor(lat: number, lon: number, street: string, city: string, postalCode: string, country: string) {
        this.lat = lat;
        this.lon = lon;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    
}



export default Address;