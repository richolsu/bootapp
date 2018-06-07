package com.srm.platform.vendor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/buyer")
public class BuyerController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 供应商管理
	@GetMapping("/vendor")
	public String vendor() {
		return "buyer/vendor/index";
	}

	// 价格管理
	@GetMapping("/venpriceadjust")
	public String venpriceadjust() {
		return "buyer/vendor/index";
	}

	// 商品管理
	@GetMapping("/inventory")
	public String inventory() {
		return "buyer/vendor/index";
	}

	// 外购入库单
	@GetMapping("/purchasein")
	public String purchasein() {
		return "buyer/vendor/index";
	}

	// 对账单管理
	@GetMapping("/purinvoice")
	public String purinvoice() {
		return "buyer/vendor/index";
	}

	// 订单管理
	@GetMapping("/purchaseorder")
	public String purchaseorder() {
		return "buyer/vendor/index";
	}

	// 出货看板
	@GetMapping("/shipment")
	public String shipment() {
		return "buyer/vendor/index";
	}

}
