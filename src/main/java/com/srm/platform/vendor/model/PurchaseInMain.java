package com.srm.platform.vendor.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "purchase_in_main")
public class PurchaseInMain {
	@Id
	private Long id;

	private String code;

	private Date date;
	private String maker;
	private String warehousecode;
	private String warehousename;
	private String receivecode;
	private String receivename;
	private String departmentcode;
	private String departmentname;
	private String purchasetypecode;
	private String purchasetypename;
	private String memory;

	private Date auditdate;
	private String handler;
	private Integer state;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "vendorcode", referencedColumnName = "code")
	Vendor vendor;

	private Integer bredvouch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getWarehousecode() {
		return warehousecode;
	}

	public void setWarehousecode(String warehousecode) {
		this.warehousecode = warehousecode;
	}

	public String getWarehousename() {
		return warehousename;
	}

	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}

	public String getReceivecode() {
		return receivecode;
	}

	public void setReceivecode(String receivecode) {
		this.receivecode = receivecode;
	}

	public String getReceivename() {
		return receivename;
	}

	public void setReceivename(String receivename) {
		this.receivename = receivename;
	}

	public String getDepartmentcode() {
		return departmentcode;
	}

	public void setDepartmentcode(String departmentcode) {
		this.departmentcode = departmentcode;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getPurchasetypecode() {
		return purchasetypecode;
	}

	public void setPurchasetypecode(String purchasetypecode) {
		this.purchasetypecode = purchasetypecode;
	}

	public String getPurchasetypename() {
		return purchasetypename;
	}

	public void setPurchasetypename(String purchasetypename) {
		this.purchasetypename = purchasetypename;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public Date getAuditdate() {
		return auditdate;
	}

	public void setAuditdate(Date auditdate) {
		this.auditdate = auditdate;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Integer getBredvouch() {
		return bredvouch;
	}

	public void setBredvouch(Integer bredvouch) {
		this.bredvouch = bredvouch;
	}

}