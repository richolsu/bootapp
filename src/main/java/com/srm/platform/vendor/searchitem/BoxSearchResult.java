package com.srm.platform.vendor.searchitem;

import java.io.Serializable;
import java.util.Date;

public class BoxSearchResult implements Serializable {

	private static final long serialVersionUID = -7774864748081640759L;

	String id;
	String code;
	Integer type;
	Date bind_date;
	String box_class_name;
	String bind_property;
	String vendor_code;
	String vendor_name;
	String inventory_code;
	String inventory_name;
	String inventory_specs;
	String delivery_code;
	String delivery_number;
	Double quantity;
	String state;
	String used;

	public BoxSearchResult(String id, String code, Integer type, Date bind_date, String box_class_name, String bind_property,
			String vendor_code, String vendor_name, String inventory_code, String inventory_name, String inventory_specs,
			String delivery_code, String delivery_number, Double quantity, String state, String used) {

		this.id = id;
		this.code = code;
		this.type = type;
		this.bind_date = bind_date;
		this.box_class_name = box_class_name;
		this.bind_property = bind_property;
		this.vendor_code = vendor_code;
		this.vendor_name = vendor_name;
		this.inventory_code = inventory_code;
		this.inventory_name = inventory_name;
		this.inventory_specs = inventory_specs;
		this.delivery_code = delivery_code;
		this.delivery_number = delivery_number;
		this.quantity = quantity;
		this.state = state;
		this.used = used;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDelivery_code() {
		return delivery_code;
	}

	public void setDelivery_code(String delivery_code) {
		this.delivery_code = delivery_code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getBind_date() {
		return bind_date;
	}

	public void setBind_date(Date bind_date) {
		this.bind_date = bind_date;
	}

	public String getBox_class_name() {
		return box_class_name;
	}

	public void setBox_class_name(String box_class_name) {
		this.box_class_name = box_class_name;
	}

	public String getBind_property() {
		return bind_property;
	}

	public void setBind_property(String bind_property) {
		this.bind_property = bind_property;
	}

	public String getVendor_code() {
		return vendor_code;
	}

	public void setVendor_code(String vendor_code) {
		this.vendor_code = vendor_code;
	}

	public String getVendor_name() {
		return vendor_name;
	}

	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}

	public String getInventory_code() {
		return inventory_code;
	}

	public void setInventory_code(String inventory_code) {
		this.inventory_code = inventory_code;
	}

	public String getInventory_name() {
		return inventory_name;
	}

	public void setInventory_name(String inventory_name) {
		this.inventory_name = inventory_name;
	}



	public String getInventory_specs() {
		return inventory_specs;
	}

	public void setInventory_specs(String inventory_specs) {
		this.inventory_specs = inventory_specs;
	}

	public String getDelivery_number() {
		return delivery_number;
	}

	public void setDelivery_number(String delivery_number) {
		this.delivery_number = delivery_number;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

}
