package com.srm.platform.vendor.utility;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class VenPriceSaveForm {

	private String ccode;
	private String vendor;
	private Float tax_rate;
	private Integer state;
	private Date start_date;
	private Date end_date;
	private Integer type;
	private Integer provide_type;
	private Long maker;

	private Date make_date;
	private List<Map<String, String>> table;

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Float getTax_rate() {
		return tax_rate;
	}

	public void setTax_rate(Float tax_rate) {
		this.tax_rate = tax_rate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = Utils.parseDate(start_date);
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = Utils.parseDate(end_date);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getProvide_type() {
		return provide_type;
	}

	public void setProvide_type(Integer provide_type) {
		this.provide_type = provide_type;
	}

	public Long getMaker() {
		return maker;
	}

	public void setMaker(Long maker) {
		this.maker = maker;
	}

	public Date getMake_date() {
		return make_date;
	}

	public void setMake_date(String make_date) {
		this.make_date = Utils.parseDate(make_date);
	}

	public List<Map<String, String>> getTable() {
		return table;
	}

	public void setTable(List<Map<String, String>> table) {
		this.table = table;
	}

}
