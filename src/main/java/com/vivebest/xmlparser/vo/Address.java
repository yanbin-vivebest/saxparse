package com.vivebest.xmlparser.vo;

public class Address {

	private String address1;
	private String city;
	private String country;
	private String countryName;
	private String postalCode;
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	@Override
	public String toString() {
		if(this.address1 == null && this.city == null && this.country == null && this.countryName == null && this.postalCode == null) {
			return "";
		}
		return address1 + "-" + city + "-" + country + "-" + countryName + "-" + "postalCode";
	}
}
