package com.model;

public class Address {
	private String address;
	private String postalCode;
	private String city;
	private String prov;
	private String primaryKey;
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public void setPrimaryKey(String text) {
		primaryKey = text;
		
	}
	public String getPrimaryKey() {
		return primaryKey;
	}

}
