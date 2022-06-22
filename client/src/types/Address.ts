class Address {
    lat: number
    lon: number
    addressLine: string
    city: string
    postalCode: string
    country: string


	constructor(lat: number, lon: number, addressLine: string, city: string, postalCode: string, country: string) {
        this.lat = lat;
        this.lon = lon;
        this.addressLine = addressLine;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    
}



export default Address;