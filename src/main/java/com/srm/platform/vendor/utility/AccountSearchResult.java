package com.srm.platform.vendor.utility;

import java.io.Serializable;

public class AccountSearchResult implements Serializable {

	private static final long serialVersionUID = -7774864748081640759L;

	String id;
	String username;
	String realname;

	String unitname;

	String duty;

	String role;

	String vendorname;

	String email;

	String tel;

	String mobile;

	String state;

	public AccountSearchResult(String id, String username, String realname, String unitname, String duty, String role,
			String vendorname, String email, String tel, String mobile, String state) {

		this.id = id;
		this.username = username;
		this.realname = realname;
		this.unitname = unitname;
		this.duty = duty;
		this.role = role;
		this.vendorname = vendorname;
		this.email = email;
		this.tel = tel;
		this.mobile = mobile;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
