class Address {
    lat: number
    long: number
    addressLine: string
    city: string
    postalCode: string
    country: string


	constructor(lat: number, long: number, addressLine: string, city: string, postalCode: string, country: string) {
        this.lat = lat;
        this.long = long;
        this.addressLine = addressLine;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    
}



export default Address;