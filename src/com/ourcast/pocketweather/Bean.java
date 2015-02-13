package com.ourcast.pocketweather;

public class Bean {

	private String date;
	private String maximumTemprature;
	private String minimumTemprature;
	private String description;
	private String city;
	private String imageUrl;
	
	public Bean() {
		// TODO Auto-generated constructor stub
	}	
	
	public Bean(String date, String maximum, String minimum, String desc, String city, String imageLink){
		this.date = date;
		this.maximumTemprature = maximum;
		this.minimumTemprature = minimum;
		this.description = desc;
		this.city = city;
		this.imageUrl = imageLink;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMaximumTemprature() {
		return maximumTemprature;
	}

	public void setMaximumTemprature(String maximumTemprature) {
		this.maximumTemprature = maximumTemprature;
	}

	public String getMinimumTemprature() {
		return minimumTemprature;
	}

	public void setMinimumTemprature(String minimumTemprature) {
		this.minimumTemprature = minimumTemprature;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

}
