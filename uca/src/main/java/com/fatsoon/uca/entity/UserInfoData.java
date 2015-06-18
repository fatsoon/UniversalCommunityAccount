package com.fatsoon.uca.entity;

public class UserInfoData {

	private String name;//用户昵称
	private String iconUrl;//头像地址
	private String sex;//性别
	private String location_province;//省份
	private String location_city;//城市
	private String birthday;//生日

	
	
	public UserInfoData() {
		this.name = "";
		this.iconUrl = "";
		this.sex = "";
		this.location_province = "";
		this.location_city = "";
		this.birthday = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLocation_province() {
		return location_province;
	}

	public void setLocation_province(String location_province) {
		this.location_province = location_province;
	}

	public String getLocation_city() {
		return location_city;
	}

	public void setLocation_city(String location_city) {
		this.location_city = location_city;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "UserInfoData [name=" + name + ", iconUrl=" + iconUrl + ", sex="
				+ sex + ", location_province=" + location_province
				+ ", location_city=" + location_city + ", birthday=" + birthday
				+ "]";
	}

}
