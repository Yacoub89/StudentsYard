package com.model;

public class Registration {
	
	private String name;
	private String email;
	private String password;
	private String phone;
	
	
	public String getName()
	{
		return name;
	}
	public String getEmail()
	{
		return email;
	}
	public String getPassword()
	{
		return password;
	}
	public String getPhone()
	{
		return phone;
	}
	
	public void setName(String aName){
		name = aName;
	}
	public void setEmail(String aEmail){
		email = aEmail;
	}
	public void setPassword(String aPassword){
		password = aPassword;
	}
	public void setPhone(String aPhon){
		phone = aPhon;
	}

}
