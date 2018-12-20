package com.srm.platform.vendor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.platform.vendor.model.Account;
import com.srm.platform.vendor.model.ProvideClass;
import com.srm.platform.vendor.model.UnitProvide;
import com.srm.platform.vendor.model.Vendor;
import com.srm.platform.vendor.model.VendorProvide;
import com.srm.platform.vendor.repository.AccountRepository;
import com.srm.platform.vendor.repository.UnitRepository;
import com.srm.platform.vendor.repository.VendorProvideRepository;
import com.srm.platform.vendor.repository.VendorRepository;
import com.srm.platform.vendor.utility.GenericJsonResponse;
import com.srm.platform.vendor.utility.SearchItem;
import com.srm.platform.vendor.utility.VendorSaveForm;
import com.srm.platform.vendor.utility.VendorSearchItem;

// 供应商管理
@Controller
@RequestMapping(path = "/vendor")

public class VendorController extends CommonController {

	private static String DEFAULT_PASSWORD = "111";

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private VendorProvideRepository vendorProvideRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// 查询列表
	@PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_ADMIN')")
	@GetMapping({ "", "/" })
	public String index() {
		return "vendor/index";
	}

	// 详细
	@PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/add")
	public String add(Model model) {
		Vendor vendor = new Vendor();
		model.addAttribute("data", vendor);
		model.addAttribute("provideClassList", "[]");
		model.addAttribute("accountState", "2");
		return "vendor/edit";
	}

	// 详细
	@PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/{code}/edit")
	public String edit(@PathVariable("code") String code, Model model) {
		Vendor vendor = vendorRepository.findOneByCode(code);
		if (vendor == null)
			show404();

		if (!isAdmin()) {
			checkVendor(vendor);
		}

		List<ProvideClass> provideClassList = provideClassRepository.findProvideClassesByVendorCode(code);

		Account account = accountRepository.findOneByUsername(code);

		ObjectMapper mapper = new ObjectMapper();
		String jsonGroupString = "";
		try {
			jsonGroupString = mapper.writeValueAsString(provideClassList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("data", vendor);
		model.addAttribute("provideClassList", jsonGroupString);
		model.addAttribute("accountState", account == null ? 2 : account.getState());
		return "vendor/edit";
	}

	// 查询列表API
	@PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/list", produces = "application/json")
	public @ResponseBody Page<VendorSearchItem> list_ajax(@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "3"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String order = requestParams.getOrDefault("order", "name");
		String dir = requestParams.getOrDefault("dir", "asc");
		String search = requestParams.getOrDefault("search", "");

		List<String> unitList = this.getDefaultUnitList();

		if (order.equals("unitname")) {
			order = "b.name";
		}
		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page,
				dir.equals("asc") ? Direction.ASC : Direction.DESC, order);

		Page<VendorSearchItem> result = null;
		if (isAdmin())
			result = vendorRepository.findBySearchTerm(search, request);
		else
			result = vendorRepository.findBySearchTerm(search, unitList, request);

		return result;
	}

	// 查询列表API
	@PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/listall", produces = "application/json")
	public @ResponseBody Page<VendorSearchItem> list_all_ajax(@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "3"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String order = requestParams.getOrDefault("order", "name");
		String dir = requestParams.getOrDefault("dir", "asc");
		String search = requestParams.getOrDefault("search", "");

		if (order.equals("unitname")) {
			order = "b.name";
		}
		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page,
				dir.equals("asc") ? Direction.ASC : Direction.DESC, order);

		Page<VendorSearchItem> result = null;
		result = vendorRepository.findBySearchTerm(search, request);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_VENDOR') or hasRole('ROLE_ADMIN')")
	@ResponseBody
	@RequestMapping(value = "/search", produces = "application/json")
	public Page<SearchItem> search_ajax(@RequestParam(value = "q") String search) {
		PageRequest request = PageRequest.of(0, 15, Direction.ASC, "name");

		if (isAdmin()) {
			return vendorRepository.findForSelect(search, request);
		} else {
			List<String> unitList = this.getDefaultUnitList();
			return vendorRepository.findForSelect(unitList, search, request);
		}

	}

	@PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_VENDOR') or hasRole('ROLE_ADMIN')")
	@ResponseBody
	@RequestMapping(value = "/select", produces = "application/json")
	public Page<SearchItem> select_ajax(@RequestParam(value = "q") String search) {
		PageRequest request = PageRequest.of(0, 15, Direction.ASC, "b.name");

		List<String> unitList = this.getDefaultUnitList();
		return vendorRepository.findForNotice(unitList, search, request);

	}

	// 修改
	@Transactional
	@PostMapping("/update")
	public @ResponseBody Vendor update_ajax(VendorSaveForm vendorSaveForm) {
		Vendor vendor = vendorRepository.findOneByCode(vendorSaveForm.getCode());
		vendorProvideRepository.deleteByVendorCode(vendorSaveForm.getCode());

		List<Long> provideClassIdList = vendorSaveForm.getProvideclasses();
		if (provideClassIdList != null) {
			for (Long id : provideClassIdList) {
				VendorProvide temp = new VendorProvide(id, vendorSaveForm.getCode());
				vendorProvideRepository.save(temp);
			}
		}

		Account account = accountRepository.findOneByUsername(vendor.getCode());

		if (account == null) {
			account = new Account();
			account.setUsername(vendor.getCode());
			account.setRealname(vendor.getAbbrname());
			account.setMobile(vendor.getMobile());
			account.setAddress(vendor.getAddress());
			account.setEmail(vendor.getEmail());
			account.setRole("ROLE_VENDOR");
			account.setVendor(vendor);
			account.setDuty("供应商");
			account.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		}

		if (vendorSaveForm.getState() == 1) {
			account.setState(1);
			account.setStartDate(new Date());
			account.setStopDate(null);
		} else {
			account.setState(0);
			account.setStopDate(new Date());
		}

		account = accountRepository.save(account);

		return vendor;
	}

	// 修改
	@Transactional
	@PostMapping("/set_unit")
	public @ResponseBody Integer setUnit(@RequestParam Map<String, String> requestParams) {
		String vendorList = requestParams.get("vendor_list");

		String unitId = requestParams.get("unit_id");

		String[] vendorCodeList = StringUtils.split(vendorList, ",");

		for (String vendorCode : vendorCodeList) {
			Vendor vendor = vendorRepository.findOneByCode(vendorCode);
			vendor.setUnit(unitRepository.findOneById(Long.parseLong(unitId)));
			vendorRepository.save(vendor);
		}

		return 1;
	}
}
