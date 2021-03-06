package com.srm.platform.vendor.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.srm.platform.vendor.utility.AccountSearchResult;

@Entity

@SqlResultSetMapping(name = "AccountSearchResult", classes = {
		@ConstructorResult(targetClass = AccountSearchResult.class, columns = {
				@ColumnResult(name = "id", type = String.class), @ColumnResult(name = "username", type = String.class),
				@ColumnResult(name = "realname", type = String.class),
				@ColumnResult(name = "unitname", type = String.class),
				@ColumnResult(name = "duty", type = String.class), @ColumnResult(name = "role", type = String.class),
				@ColumnResult(name = "vendorname", type = String.class),
				@ColumnResult(name = "email", type = String.class), @ColumnResult(name = "tel", type = String.class),
				@ColumnResult(name = "mobile", type = String.class),
				@ColumnResult(name = "state", type = String.class) }) })

@Table(name = "account")
public class Account implements Serializable {

	private static final long serialVersionUID = -2584865763834767175L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@JsonIgnore
	private String password;
	private String role = "ROLE_BUYER";
	private String realname;
	private String duty;

	private String address;
	@Column(name = "entry_time")
	private Date entryTime;

	@JoinColumn(name = "unit_id")
	@ManyToOne()
	private Unit unit;

	@OneToOne()
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "vendor_code", referencedColumnName = "code", nullable=true)
	private Vendor vendor;

	private String email;
	private String tel;
	private String qq;
	private String weixin;
	private String yahoo;
	private String gtalk;
	private String wangwang;
	private String mobile;
	@Column(name = "modify_time")
	private Instant modifyTime;

	private Integer state = 1;

	@Column(name = "start_date")
	private Date startDate = new Date();

	@Column(name = "stop_date")
	private Date stopDate;

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
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

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getYahoo() {
		return yahoo;
	}

	public void setYahoo(String yahoo) {
		this.yahoo = yahoo;
	}

	public String getGtalk() {
		return gtalk;
	}

	public void setGtalk(String gtalk) {
		this.gtalk = gtalk;
	}

	public String getWangwang() {
		return wangwang;
	}

	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Account() {

	}

	public Account(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.modifyTime = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Instant getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Instant modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRolename() {
		switch (this.role) {
		case "ROLE_BUYER":
			return "采购员";
		case "ROLE_VENDOR":
			return "供应商";
		case "ROLE_ADMIN":
			return "管理员";
		default:
			return "";
		}
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

}
