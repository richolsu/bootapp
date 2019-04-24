package com.srm.platform.vendor.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "inventory_class")
public class InventoryClass {
	@Id
	private String code;
	private String name;
	
	@JsonIgnore
	@Nullable
	private Integer rank;
	
	@JsonIgnore
	@Nullable
	private Integer endRankFlag;
	
	@JsonProperty("parent_code")
	private String parentCode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Integer getEndRankFlag() {
		return endRankFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer isEndRankFlag() {
		return endRankFlag;
	}

	public void setEndRankFlag(Integer endRankFlag) {
		this.endRankFlag = endRankFlag;
	}

}
