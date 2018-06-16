package com.srm.platform.vendor.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srm.platform.vendor.model.AccountSearchItem;
import com.srm.platform.vendor.model.Unit;
import com.srm.platform.vendor.model.UnitNode;
import com.srm.platform.vendor.repository.AccountRepository;
import com.srm.platform.vendor.repository.UnitRepository;
import com.srm.platform.vendor.u8api.ApiClient;

@RestController
@RequestMapping(path = "/api")
public class ApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApiClient apiClient;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UnitRepository unitRepository;

	// 供应商管理
	@RequestMapping(value = "/vendor/batch_get", produces = "application/json")
	public String vendor(@RequestParam Map<String, String> requestParams) {
		return apiClient.getBatchVendor(requestParams);
	}

	@RequestMapping(value = "/vendor/get/{id}", produces = "application/json")
	public String vendor_get(@PathVariable("id") String id) {
		return apiClient.getVendor(id);
	}

	// 价格管理
	@RequestMapping(value = "/venpriceadjust/batch_get", produces = "application/json")
	public String venpriceadjust(@RequestParam Map<String, String> requestParams) {
		return apiClient.getBatchVenPriceAdjust(requestParams);
	}

	// 商品管理
	@RequestMapping(value = "/inventory/batch_get", produces = "application/json")
	public String inventory(@RequestParam Map<String, String> requestParams) {
		return apiClient.getBatchInventory(requestParams);
	}

	// 外购入库单
	@RequestMapping(value = "/purchasein/batch_get", produces = "application/json")
	public String purchasein(@RequestParam Map<String, String> requestParams) {
		return apiClient.getBatchPurchaseIn(requestParams);
	}

	// 对账单管理
	@RequestMapping(value = "/purinvoice/batch_get", produces = "application/json")
	public String purinvoice(@RequestParam Map<String, String> requestParams) {
		return apiClient.getBatchPurInvoice(requestParams);
	}

	// 订单管理
	@RequestMapping(value = "/purchaseorder/batch_get", produces = "application/json")
	public String purchaseorder(@RequestParam Map<String, String> requestParams) {
		return apiClient.getBatchPurchaseOrder(requestParams);
	}

	@RequestMapping(value = "/purchaseorder/get/{id}", produces = "application/json")
	public String purchaseorder_get(@PathVariable("id") String id) {
		return apiClient.getPurchaseOrder(id);
	}

	// 出货看板
	@RequestMapping(value = "/shipment/batch_get", produces = "application/json")
	public String shipment(@RequestParam Map<String, String> requestParams) {
		return apiClient.getBatchVendor(requestParams);
	}

	// 用户名单
	@RequestMapping(value = "/account/{search}", produces = "application/json")
	public Page<AccountSearchItem> account_search(@PathVariable("search") String search) {
		PageRequest request = PageRequest.of(0, 15, Direction.ASC, "realname");

		return accountRepository.findForAutoComplete(search, request);
	}

	// 用户名单
	@RequestMapping(value = "/unit/tree", produces = "application/json")
	public UnitNode unit_tree() {
		List<Unit> units = unitRepository.findAll(Sort.by(Direction.ASC, "id"));

		UnitNode root = null, tempNode;
		Unit temp;
		for (int i = 0; i < units.size(); i++) {
			temp = units.get(i);
			tempNode = new UnitNode(temp.getId(), temp.getName(), temp.getParentId());
			if (i == 0) {
				root = tempNode;
			} else {
				root.addNode(tempNode);
			}
		}

		return root;
	}
}
