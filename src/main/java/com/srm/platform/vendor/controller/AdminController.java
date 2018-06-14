package com.srm.platform.vendor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.srm.platform.vendor.form.AccountForm;
import com.srm.platform.vendor.model.Account;
import com.srm.platform.vendor.model.PermissionGroup;
import com.srm.platform.vendor.model.Unit;
import com.srm.platform.vendor.repository.AccountRepository;
import com.srm.platform.vendor.repository.PermissionGroupRepository;
import com.srm.platform.vendor.repository.UnitRepository;
import com.srm.platform.vendor.service.AccountService;

@Controller
@RequestMapping(path = "/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PermissionGroupRepository permissionGroupRepository;
	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private AccountService accountService;

	// Dashboard
	@GetMapping({ "", "/" })
	public String home() {
		return "admin/index";
	}

	// 用户管理->列表
	@GetMapping("/account")
	public String account_list(Model model) {

		PageRequest request = PageRequest.of(0, 2);
		Page<Account> result = accountRepository.findAll(request);
		result.getTotalElements();
		logger.info(result.toString());
		model.addAttribute("accounts", result.getContent());
		return "admin/account/list";
	}

	// 用户管理->列表
	@GetMapping("/account_ajax")
	public @ResponseBody Page<Account> ajax_account_list(@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "3"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page);
		Page<Account> result = accountRepository.findAll(request);

		return result;
	}

	// 用户管理->列表
	@GetMapping("/account_test")
	public @ResponseBody Page<Account> ajax_account_test(@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "3"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page, Sort.Direction.DESC, "id");

		return accountRepository.findBySearchTermNative("e", request);

	}

	// 用户管理->修改
	@GetMapping("/account/{id}/edit")
	public String account_edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("account", accountRepository.findOneById(id));
		return "admin/account/edit";
	}

	// 用户管理->新建
	@GetMapping("/account/add")
	public String account_add(Model model) {
		model.addAttribute("account", new Account());
		return "admin/account/edit";
	}

	// 用户管理->删除
	@GetMapping("/account/{id}/delete")
	public String account_delete(@PathVariable("id") Long id, Model model) {
		model.addAttribute("account", accountRepository.findOneById(id));
		return "admin/account/edit";
	}

	// 用户修改
	@PostMapping("/account/update")
	public String account_update(@Valid @ModelAttribute AccountForm accountForm, Errors errors, RedirectAttributes ra) {
		if (errors.hasErrors()) {
			return "admin/account/edit";
		}

		accountService.save(accountForm.createAccount());

		return "redirect:/admin/account";
	}

	// 组织架构管理
	@GetMapping("/unit")
	public String unit() {
		return "admin/unit/index";
	}

	// 组织架构管理->下级组织列表
	@GetMapping("/unit/{parent_id}/children")
	public @ResponseBody List<Map<String, Object>> get_units(@PathVariable("parent_id") Long parent_id) {
		List<Unit> children = unitRepository.findByParentId(parent_id);

		Unit temp;
		List<Unit> tempChildren;

		Map<String, Object> row = new HashMap<>();
		List<Map<String, Object>> response = new ArrayList<>();
		for (int i = 0; i < children.size(); i++) {

			temp = children.get(i);
			tempChildren = unitRepository.findByParentId(temp.getId());
			row = new HashMap<>();
			row.put("id", temp.getId());
			row.put("text", temp.getName());
			row.put("children", tempChildren.size() > 0 ? true : false);
			response.add(row);
		}

		return response;
	}

	// 组织架构管理->删除
	@GetMapping("/unit/{id}/delete")
	public @ResponseBody Boolean delete_unit(@PathVariable("id") Long id) {
		Unit unit = unitRepository.findOneById(id);
		unitRepository.delete(unit);
		return true;
	}

	// 组织架构管理->改名
	@GetMapping("/unit/{id}/rename/{name}")
	public @ResponseBody Unit rename_unit(@PathVariable("id") Long id, @PathVariable("name") String name) {
		Unit unit = unitRepository.findOneById(id);
		unit.setName(name);
		unit = unitRepository.save(unit);
		return unit;
	}

	// 组织架构管理->移动
	@GetMapping("/unit/{id}/move/{parent_id}")
	public @ResponseBody Unit move_unit(@PathVariable("id") Long id, @PathVariable("parent_id") Long parent_id) {
		Unit unit = unitRepository.findOneById(id);
		unit.setParent_id(parent_id);
		unit = unitRepository.save(unit);
		return unit;
	}

	// 组织架构管理->新建
	@GetMapping("/unit/add/{parent_id}/{name}")
	public @ResponseBody Unit add_unit(@PathVariable("parent_id") Long parentId, @PathVariable("name") String name) {
		Unit unit = new Unit(name, parentId);
		unitRepository.save(unit);
		return unit;
	}

	// 权限组管理->列表
	@GetMapping("/permission_group_ajax")
	public @ResponseBody Page<PermissionGroup> ajax_permission_group_list(
			@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "3"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String order = requestParams.getOrDefault("order", "name");
		String dir = requestParams.getOrDefault("dir", "asc");
		String search = requestParams.getOrDefault("search", "");

		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page,
				dir.equals("asc") ? Direction.ASC : Direction.DESC, order);
		Page<PermissionGroup> result = permissionGroupRepository.findBySearchTerm(search, request);

		return result;
	}

	// 权限组管理
	@GetMapping("/permission_group")
	public String permission_group(Model model) {
		return "admin/permission_group/list";
	}

	// 权限组管理->修改
	@GetMapping("/permission_group/{id}/edit")
	public String permission_group_edit(@PathVariable("id") Long id, Model model) {
		PermissionGroup temp = permissionGroupRepository.findOneById(id);
		model.addAttribute("permission_group", temp);
		model.addAttribute("accounts", StringUtils.join(permissionGroupRepository.findAccountsInGroupById(id), ","));
		return "admin/permission_group/edit";
	}

	// 权限组管理->修改
	@GetMapping("/permission_group/{id}/edit_perm")
	public String permission_group_edit_perm(@PathVariable("id") Long id, Model model) {
		PermissionGroup temp = permissionGroupRepository.findOneById(id);
		model.addAttribute("permission_group", temp);
		return "admin/permission_group/edit_perm";
	}

	// 权限组管理->新建
	@GetMapping("/permission_group/add")
	public String permission_group_add(Model model) {
		model.addAttribute("permission_group", new PermissionGroup());
		return "admin/permission_group/edit";
	}

	// 权限组管理->更新
	@PostMapping("/permission_group/update")
	public @ResponseBody PermissionGroup permission_group_update(@RequestParam Map<String, String> requestParams) {

		String name = requestParams.get("name");
		String description = requestParams.get("description");
		String id = requestParams.get("id");
		String accounts = requestParams.get("accounts");

		PermissionGroup group = new PermissionGroup(name, description);

		if (id != null && !id.isEmpty())
			group.setId(Long.parseLong(id));

		group = permissionGroupRepository.save(group);

		return group;
	}

	// 权限组管理->删除
	@GetMapping("/permission_group/{id}/delete")
	public @ResponseBody Boolean permission_group_delete(@PathVariable("id") Long id) {

		PermissionGroup temp = permissionGroupRepository.findOneById(id);

		permissionGroupRepository.delete(temp);

		return true;
	}

	@GetMapping("/account/addaaa") // Map ONLY GET Requests
	public @ResponseBody String addNewUser(@RequestParam String username, @RequestParam String password) {

		logger.info("start user add");
		Account n = new Account(username, password, "ROLE_VENDOR");
		accountService.save(n);

		return "Saved";
	}

}
