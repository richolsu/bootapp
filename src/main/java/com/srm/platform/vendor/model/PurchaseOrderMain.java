package com.srm.platform.vendor.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.srm.platform.vendor.utility.PurchaseOrderSearchResult;

@Entity

@SqlResultSetMapping(name = "PurchaseOrderSearchResult", classes = {
		@ConstructorResult(targetClass = PurchaseOrderSearchResult.class, columns = {
				@ColumnResult(name = "code", type = String.class), @ColumnResult(name = "state", type = String.class),
				@ColumnResult(name = "vendorname", type = String.class),
				@ColumnResult(name = "deployername", type = String.class),
				@ColumnResult(name = "reviewername", type = String.class),
				@ColumnResult(name = "deploydate", type = Date.class),
				@ColumnResult(name = "reviewdate", type = Date.class),
				@ColumnResult(name = "maker", type = String.class), @ColumnResult(name = "makedate", type = Date.class),
				@ColumnResult(name = "sum", type = Float.class), @ColumnResult(name = "money", type = Float.class),
				@ColumnResult(name = "srmstate", type = Integer.class),
				@ColumnResult(name = "purchase_type_name", type = String.class),
				@ColumnResult(name = "prepaymoney", type = Float.class) }) })

@Table(name = "purchase_order_main")
public class PurchaseOrderMain {
	@Id
	private String code;

	@OneToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "vencode", referencedColumnName = "code")
	Vendor vendor;

	@JsonProperty("purchase_type_name")
	private String purchaseTypeName;

	private String deptcode;
	private String deptname;
	private String personcode;
	private String personname;

	private String remark;
	private String state;

	private Integer srmstate;

	private String maker;
	private Date makedate;

	private String verifier;

	private String closer;

	private String locker;
	private Date lockdate;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "deployer", referencedColumnName = "id")
	private Account deployer;
	private Date deploydate;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "reviewer", referencedColumnName = "id")
	private Account reviewer;
	private Date reviewdate;

	public PurchaseOrderMain() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Account getDeployer() {
		return deployer;
	}

	public void setDeployer(Account deployer) {
		this.deployer = deployer;
	}

	public Date getDeploydate() {
		return deploydate;
	}

	public void setDeploydate(Date deploydate) {
		this.deploydate = deploydate;
	}

	public Account getReviewer() {
		return reviewer;
	}

	public void setReviewer(Account reviewer) {
		this.reviewer = reviewer;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getPersoncode() {
		return personcode;
	}

	public void setPersoncode(String personcode) {
		this.personcode = personcode;
	}

	public String getPersonname() {
		return personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
	}

	public String getPurchaseTypeName() {
		return purchaseTypeName;
	}

	public void setPurchaseTypeName(String purchaseTypeName) {
		this.purchaseTypeName = purchaseTypeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public Date getMakedate() {
		return makedate;
	}

	public void setMakedate(Date makedate) {
		this.makedate = makedate;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public String getCloser() {
		return closer;
	}

	public void setCloser(String closer) {
		this.closer = closer;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	

	public Integer getSrmstate() {
		return srmstate;
	}

	public void setSrmstate(Integer srmstate) {
		this.srmstate = srmstate;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	public Date getLockdate() {
		return lockdate;
	}

	public void setLockdate(Date lockdate) {
		this.lockdate = lockdate;
	}

	public Date getReviewdate() {
		return reviewdate;
	}

	public void setReviewdate(Date reviewdate) {
		this.reviewdate = reviewdate;
	}

}
