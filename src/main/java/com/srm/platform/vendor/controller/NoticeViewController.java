package com.srm.platform.vendor.controller;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.srm.platform.vendor.model.Account;
import com.srm.platform.vendor.model.Notice;
import com.srm.platform.vendor.model.NoticeClass;
import com.srm.platform.vendor.model.NoticeRead;
import com.srm.platform.vendor.repository.NoticeRepository;
import com.srm.platform.vendor.searchitem.AccountSearchResult;
import com.srm.platform.vendor.searchitem.NoticeReadSearchResult;
import com.srm.platform.vendor.searchitem.NoticeSearchResult;
import com.srm.platform.vendor.searchitem.VendorSearchItem;
import com.srm.platform.vendor.utility.Constants;
import com.srm.platform.vendor.utility.UploadFileHelper;
import com.srm.platform.vendor.utility.Utils;

@Controller
@RequestMapping(path = "/notice_view")
public class NoticeViewController {

	@PersistenceContext
	public EntityManager em;

	@Autowired
	public NoticeRepository noticeRepository;
	
	@GetMapping({ "/", "" })
	public String index(Model model) {
		return "notice_view/list";
	}

	// 用户管理->修改
	@GetMapping("/{id}/view")
	public String view(@PathVariable("id") Long id, Model model) {
		Notice notice = noticeRepository.findOneById(id);
		model.addAttribute("notice", notice);
		return "notice_view/view";
	}
	
	// 用户管理->列表
	@GetMapping("/list")
	public @ResponseBody Page<NoticeSearchResult> list_ajax(@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "10"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String order = requestParams.getOrDefault("order", "deploydate");
		String dir = requestParams.getOrDefault("dir", "desc");
		String search = requestParams.getOrDefault("search", "");
		String start_date = requestParams.getOrDefault("start_date", null);
		String end_date = requestParams.getOrDefault("end_date", null);
		String state = requestParams.getOrDefault("state", null);
		String create_account = requestParams.getOrDefault("create_account", null);

		Date startDate = Utils.parseDate(start_date);
		Date endDate = Utils.getNextDate(end_date);

		switch (order) {
		case "create_name":
			order = "b.realname";
			break;
		}
		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page,
				dir.equals("asc") ? Direction.ASC : Direction.DESC, order);

		String selectQuery = "SELECT distinct a.*, b.realname create_name, d.realname verify_name, e.name class_name, null read_date ";
		String countQuery = "select count(distinct a.id) ";
		String orderBy = " order by " + order + " " + dir;

		String bodyQuery = "FROM notice a left join account b on a.create_account=b.id left join account d on d.id=a.verify_account left join notice_class e on a.class_id=e.id where type=1 ";

		
		Map<String, Object> params = new HashMap<>();
		
		if (!search.trim().isEmpty()) {
			bodyQuery += " and (a.title like CONCAT('%',:search, '%') or a.content like CONCAT('%',:search, '%')) ";
			params.put("search", search.trim());
		}

		if (startDate != null) {
			bodyQuery += " and a.verify_date>=:startDate";
			params.put("startDate", startDate);
		}
		if (endDate != null) {
			bodyQuery += " and a.verify_date<:endDate";
			params.put("endDate", endDate);
		}
		if (create_account != null && !create_account.isEmpty()) {
			bodyQuery += " and b.realname like CONCAT('%',:createAccount, '%')";
			params.put("createAccount", create_account);
		}
		if (state != null && !state.equals("-1")) {
			bodyQuery += " and a.state=:state";
			params.put("state", state);
		}

		countQuery += bodyQuery;
		Query q = em.createNativeQuery(countQuery);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}

		BigInteger totalCount = (BigInteger) q.getSingleResult();

		selectQuery += bodyQuery + orderBy;
		q = em.createNativeQuery(selectQuery, "NoticeSearchResult");
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}

		List list = q.setFirstResult((int) request.getOffset()).setMaxResults(request.getPageSize()).getResultList();

		return new PageImpl<NoticeSearchResult>(list, request, totalCount.longValue());
	}



}
