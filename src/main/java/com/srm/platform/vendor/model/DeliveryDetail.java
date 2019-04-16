package com.srm.platform.vendor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.srm.platform.vendor.searchitem.BoxSearchResult;
import com.srm.platform.vendor.searchitem.SellerSearchResult;
import com.srm.platform.vendor.utility.Constants;

@Entity
@Table(name = "delivery_detail")
public class DeliveryDetail implements Serializable {
	private static final long serialVersionUID = 5855332316773551036L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "code", referencedColumnName = "code")
	@ManyToOne()
	private DeliveryMain main;

	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "order_detail_id")
	@ManyToOne()
	private PurchaseOrderDetail purchaseOrderDetail;

	@JsonProperty("delivered_quantity")
	private Double deliveredQuantity;
	
	@JsonProperty("row_no")
	private Integer rowNo;
	
	@JsonProperty("deliver_number")
	private String deliverNumber;

	@JsonProperty("accepted_quantity")
	private Double acceptedQuantity;
	
	private String memo;
	
	@JsonProperty("buyer_memo")
	private String buyerMemo;
	
	private Integer state = Constants.DELIVERY_ROW_STATE_NEW;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public DeliveryMain getMain() {
		return main;
	}

	public void setMain(DeliveryMain main) {
		this.main = main;
	}

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public String getBuyerMemo() {
		return buyerMemo;
	}

	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Double getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Double acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public PurchaseOrderDetail getPurchaseOrderDetail() {
		return purchaseOrderDetail;
	}

	public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

	public Double getDeliveredQuantity() {
		return deliveredQuantity;
	}

	public void setDeliveredQuantity(Double deliveredQuantity) {
		this.deliveredQuantity = deliveredQuantity;
	}

	public String getDeliverNumber() {
		return deliverNumber;
	}

	public void setDeliverNumber(String deliverNumber) {
		this.deliverNumber = deliverNumber;
	}

}
