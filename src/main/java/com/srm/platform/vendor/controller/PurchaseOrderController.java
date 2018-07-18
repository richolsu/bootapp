package com.srm.platform.vendor.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.srm.platform.vendor.model.Account;
import com.srm.platform.vendor.model.PurchaseOrderDetail;
import com.srm.platform.vendor.model.PurchaseOrderMain;
import com.srm.platform.vendor.repository.AccountRepository;
import com.srm.platform.vendor.repository.PurchaseOrderDetailRepository;
import com.srm.platform.vendor.repository.PurchaseOrderMainRepository;
import com.srm.platform.vendor.utility.PurchaseOrderSaveForm;
import com.srm.platform.vendor.utility.PurchaseOrderSearchItem;
import com.srm.platform.vendor.utility.Utils;

@Controller
@RequestMapping(path = "/purchaseorder")
@PreAuthorize("hasRole('ROLE_VENDOR') or hasAuthority('订单管理-查看列表')")
public class PurchaseOrderController extends CommonController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PurchaseOrderMainRepository purchaseOrderMainRepository;

	@Autowired
	private PurchaseOrderDetailRepository purchaseOrderDetailRepository;

	@Autowired
	private AccountRepository accountRepository;

	// 查询列表
	@GetMapping({ "/", "" })
	public String index() {
		return "purchaseorder/index";
	}

	@GetMapping({ "/{code}/edit" })
	public String edit(@PathVariable("code") String code, Model model) {
		PurchaseOrderMain main = this.purchaseOrderMainRepository.findOneByCode(code);
		if (main == null)
			show404();

		checkVendor(main.getVendor());

		model.addAttribute("main", main);
		return "purchaseorder/edit";
	}

	@RequestMapping(value = "/{code}/details", produces = "application/json")
	public @ResponseBody List<PurchaseOrderDetail> details_ajax(@PathVariable("code") String code) {
		List<PurchaseOrderDetail> list = purchaseOrderDetailRepository.findDetailsByCode(code);

		return list;
	}

	@RequestMapping(value = "/list", produces = "application/json")
	public @ResponseBody Page<PurchaseOrderSearchItem> list_ajax(@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "10"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String order = requestParams.getOrDefault("order", "ccode");
		String dir = requestParams.getOrDefault("dir", "asc");
		String vendor = requestParams.getOrDefault("vendor", "");
		String state = requestParams.getOrDefault("state", "0");
		String code = requestParams.getOrDefault("code", "");
		String start_date = requestParams.getOrDefault("start_date", null);
		String end_date = requestParams.getOrDefault("end_date", null);

		Date startDate = Utils.parseDate(start_date);
		Date endDate = Utils.getNextDate(end_date);

		switch (order) {
		case "vendorname":
			order = "b.name";
			break;
		case "deployername":
			order = "c.realname";
			break;
		case "reviewername":
			order = "d.realname";
			break;
		}
		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page,
				dir.equals("asc") ? Direction.ASC : Direction.DESC, order);

		Page<PurchaseOrderSearchItem> result = null;
		if (this.isVendor()) {
			result = purchaseOrderMainRepository.findBySearchTermForVendor(code,
					this.getLoginAccount().getVendor().getCode(), request);
		} else {
			List<String> unitList = this.getDefaultUnitList();
			result = purchaseOrderMainRepository.findBySearchTerm(unitList, code, vendor, request);
		}

		return result;
	}

	@Transactional
	@PostMapping("/update")
	public @ResponseBody PurchaseOrderMain update_ajax(PurchaseOrderSaveForm form, Principal principal) {

		Account account = accountRepository.findOneByUsername(principal.getName());
		PurchaseOrderMain main = purchaseOrderMainRepository.findOneByCode(form.getCode());
		main.setSrmstate(form.getState());
		main.setDeploydate(new Date());
		main.setDeployer(account);
		purchaseOrderMainRepository.save(main);

		if (form.getTable() != null) {
			for (Map<String, String> item : form.getTable()) {

				PurchaseOrderDetail detail = purchaseOrderDetailRepository.findOneById(Long.parseLong(item.get("id")));
				if (this.isVendor()) {
					detail.setConfirmdate(Utils.parseDate(item.get("confirmdate")));
					detail.setConfirmnote(item.get("confirmnote"));
				} else {
					if (item.get("prepaymoney") != null && !item.get("prepaymoney").isEmpty())
						detail.setPrepaymoney(Float.parseFloat(item.get("prepaymoney")));
					else
						detail.setPrepaymoney(null);
					detail.setArrivenote(item.get("arrivenote"));
					detail.setConfirmquantity(detail.getQuantity());
					detail.setConfirmdate(detail.getArrivedate());
					detail.setConfirmnote(null);
				}

				purchaseOrderDetailRepository.save(detail);
			}
		}

		return main;
	}

}
