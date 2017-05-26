package Project;

public class Address {
	
	private String street;
	private String city;
	private String state;
	private String zip;
	private String country;
	
	public Address(String street, String city, String state, String zip, String country) {
		this.street = street.trim();
		this.city = city.trim();
		this.state = state.trim();
		this.zip = zip.trim();
		this.country = country.trim();
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

}
