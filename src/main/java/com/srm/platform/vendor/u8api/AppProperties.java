package com.srm.platform.vendor.u8api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "u8api")
public class AppProperties {
	private String from_account;

	private String app_key;
	private int error_code_success;

	private PropertySystem system;
	private PropertyVendor vendor;
	private PropertyPurchaseOrder purchaseOrder;
	private PropertyPurInvoice purInvoice;

	private PropertyInventory inventory;
	private PropertyMeasurementUnit measurementUnit;
	private PropertyInventoryClass inventoryClass;
	private PropertyVenPriceAdjust venPriceAdjust;
	
	private PropertyCurrentStock currentStock;

	private PropertyLinkU8 linku8;

	public String getFrom_account() {
		return from_account;
	}

	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public int getError_code_success() {
		return error_code_success;
	}

	public void setError_code_success(int error_code_success) {
		this.error_code_success = error_code_success;
	}

	public PropertySystem getSystem() {
		return system;
	}

	public void setSystem(PropertySystem system) {
		this.system = system;
	}

	public PropertyLinkU8 getLinku8() {
		return linku8;
	}

	public void setLinku8(PropertyLinkU8 linku8) {
		this.linku8 = linku8;
	}

	public PropertyVendor getVendor() {
		return vendor;
	}

	public void setVendor(PropertyVendor vendor) {
		this.vendor = vendor;
	}

	public PropertyPurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PropertyPurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public PropertyCurrentStock getCurrentStock() {
		return currentStock;
	}
	
	public void setCurrentStock(PropertyCurrentStock currentStock) {
		this.currentStock = currentStock;
	}

	public PropertyPurInvoice getPurInvoice() {
		return purInvoice;
	}

	public void setPurInvoice(PropertyPurInvoice purInvoice) {
		this.purInvoice = purInvoice;
	}
	

	public PropertyInventory getInventory() {
		return inventory;
	}

	public void setInventory(PropertyInventory inventory) {
		this.inventory = inventory;
	}

	public PropertyMeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(PropertyMeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public PropertyInventoryClass getInventoryClass() {
		return inventoryClass;
	}

	public void setInventoryClass(PropertyInventoryClass inventoryClass) {
		this.inventoryClass = inventoryClass;
	}

	public PropertyVenPriceAdjust getVenPriceAdjust() {
		return venPriceAdjust;
	}

	public void setVenPriceAdjust(PropertyVenPriceAdjust venPriceAdjust) {
		this.venPriceAdjust = venPriceAdjust;
	}

	public static class PropertyVendor {
		private String batch_get;

		public String getBatch_get() {
			return batch_get;
		}

		public void setBatch_get(String batch_get) {
			this.batch_get = batch_get;
		}

	}

	public static class PropertyLinkU8 {
		private String batch_get;
		private String batch_get_weiwai;
		private String purinvoice_add;
		private String lock;
		private String unlock;
		private String purchaseorder;

		public String getBatch_get() {
			return batch_get;
		}

		public void setBatch_get(String batch_get) {
			this.batch_get = batch_get;
		}

		public String getBatch_get_weiwai() {
			return batch_get_weiwai;
		}

		public void setBatch_get_weiwai(String batch_get_weiwai) {
			this.batch_get_weiwai = batch_get_weiwai;
		}

		public String getPurinvoice_add() {
			return purinvoice_add;
		}

		public void setPurinvoice_add(String purinvoice_add) {
			this.purinvoice_add = purinvoice_add;
		}

		public String getLock() {
			return lock;
		}

		public void setLock(String lock) {
			this.lock = lock;
		}

		public String getUnlock() {
			return unlock;
		}

		public void setUnlock(String unlock) {
			this.unlock = unlock;
		}

		public String getPurchaseorder() {
			return purchaseorder;
		}

		public void setPurchaseorder(String purchaseorder) {
			this.purchaseorder = purchaseorder;
		}

	}

	public static class PropertyPurchaseOrder {
		private String batch_get;
		private String get;

		public String getBatch_get() {
			return batch_get;
		}

		public void setBatch_get(String batch_get) {
			this.batch_get = batch_get;
		}

		public String getGet() {
			return get;
		}

		public void setGet(String get) {
			this.get = get;
		}

	}

	public static class PropertyPurInvoice {
		private String add;

		public String getAdd() {
			return add;
		}

		public void setAdd(String add) {
			this.add = add;
		}

	}

	public static class PropertySystem {
		private String token;
		private String sms;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getSms() {
			return sms;
		}

		public void setSms(String sms) {
			this.sms = sms;
		}

	}

	public static class PropertyInventory {
		private String batch_get;

		public String getBatch_get() {
			return batch_get;
		}

		public void setBatch_get(String batch_get) {
			this.batch_get = batch_get;
		}
	}

	public static class PropertyInventoryClass {
		private String batch_get;

		public String getBatch_get() {
			return batch_get;
		}

		public void setBatch_get(String batch_get) {
			this.batch_get = batch_get;
		}
	}

	public static class PropertyMeasurementUnit {
		private String batch_get;

		public String getBatch_get() {
			return batch_get;
		}

		public void setBatch_get(String batch_get) {
			this.batch_get = batch_get;
		}
	}

	public static class PropertyVenPriceAdjust {

		private String add;

		public String getAdd() {
			return add;
		}

		public void setAdd(String add) {
			this.add = add;
		}

	}
	
	public static class PropertyCurrentStock {
		private String batch_get;

		public String getBatch_get() {
			return batch_get;
		}

		public void setBatch_get(String batch_get) {
			this.batch_get = batch_get;
		}
	}

}
