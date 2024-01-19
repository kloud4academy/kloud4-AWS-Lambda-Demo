package com.lambda.jav.lambda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomerData {

	 private String id;
	 private String customerId;
	 private String customerName;
	 private String customerAdderss;
	 private String customerCompany;
	 
	 
	 public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getCustomerAdderss() {
		return customerAdderss;
	}


	public void setCustomerAdderss(String customerAdderss) {
		this.customerAdderss = customerAdderss;
	}


	public String getCustomerCompany() {
		return customerCompany;
	}


	public void setCustomerCompany(String customerCompany) {
		this.customerCompany = customerCompany;
	}

	public CustomerData() {
		
	}
	public CustomerData(String json) {
		 Gson gson = new Gson();
		 CustomerData customerRequest = gson.fromJson(json, CustomerData.class);
		 this.customerId = customerRequest.getCustomerId();
		 this.customerName = customerRequest.getCustomerName();
		 this.customerAdderss = customerRequest.getCustomerAdderss();
		 this.customerCompany = customerRequest.getCustomerCompany();
		 this.id= customerRequest.getId();
		 
	 }
	
    public String toString() {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	return gson.toJson(this);
    }
}
