package com.srm.platform.vendor.controller;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.platform.vendor.model.Account;
import com.srm.platform.vendor.model.PurchaseOrderDetail;
import com.srm.platform.vendor.model.PurchaseOrderMain;
import com.srm.platform.vendor.model.Vendor;
import com.srm.platform.vendor.repository.AccountRepository;
import com.srm.platform.vendor.repository.PurchaseOrderDetailRepository;
import com.srm.platform.vendor.repository.PurchaseOrderMainRepository;
import com.srm.platform.vendor.saveform.ExportShipForm;
import com.srm.platform.vendor.searchitem.ShipSearchResult;
import com.srm.platform.vendor.utility.AccountPermission;
import com.srm.platform.vendor.utility.AccountPermissionInfo;
import com.srm.platform.vendor.utility.Constants;
import com.srm.platform.vendor.utility.UploadFileHelper;
import com.srm.platform.vendor.utility.Utils;
import com.srm.platform.vendor.view.ExcelShipReportView;

@Controller
@RequestMapping(path = "/ship")
@PreAuthorize("hasRole('ROLE_VENDOR') or hasAuthority('出货看板-查看列表')")
public class ShipController extends CommonController {
	
	private static Long LIST_FUNCTION_ACTION_ID = 30L;
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PurchaseOrderMainRepository purchaseOrderMainRepository;

	@Autowired
	private PurchaseOrderDetailRepository purchaseOrderDetailRepository;

	// 查询列表
	@GetMapping({ "", "/" })
	public String index() {
		return "ship/index";
	}

	@RequestMapping(value = "/list", produces = "application/json")
	public @ResponseBody Page<ShipSearchResult> list_ajax(Principal principal,
			@RequestParam Map<String, String> requestParams) {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "10"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String order = requestParams.getOrDefault("order", "code");
		String dir = requestParams.getOrDefault("dir", "desc");
		
		String vendorStr = requestParams.getOrDefault("vendor", "");
		String inventory = requestParams.getOrDefault("inventory", "");		
		String stateStr = requestParams.getOrDefault("state", "0");
		String start_date = requestParams.getOrDefault("start_date", null);
		String end_date = requestParams.getOrDefault("end_date", null);
		
		switch (order) {
		case "code":
			order = "b.code";
			break;
		}
		
		Date startDate = Utils.parseDate(start_date);
		Date endDate = Utils.getNextDate(end_date);
		Integer state = Integer.parseInt(stateStr);

		page_index--;
		PageRequest request = PageRequest.of(page_index, rows_per_page,
				dir.equals("asc") ? Direction.ASC : Direction.DESC, order, "rowno");

		String selectQuery = "select a.*, b.code, b.orderdate, b.vencode, c.name company_name, e.name box_class_name, f.name vendor_name, d.specs  ";
		String countQuery = "select count(*) ";
		String orderBy = " order by " + order + " " + dir;

		String bodyQuery = "from purchase_order_detail a "
				+ "left join purchase_order_main b on a.main_id=b.id left join company c on b.company_id=c.id left join account emp on b.employee_no=emp.employee_no "
				+ "left join inventory d on  a.inventory_code=d.code left join box_class e on d.box_class_id=e.id "
				+ "left join vendor f on b.vencode=f.code where b.state='审核'  ";

		Map<String, Object> params = new HashMap<>();

		if (isVendor()) {
			Vendor vendor = this.getLoginAccount().getVendor();
			vendorStr = vendor == null ? "0" : vendor.getCode();
			bodyQuery += " and f.code= :vendor";
			params.put("vendor", vendorStr);
		} else {
			String subWhere = " 1=0 ";

			AccountPermissionInfo accountPermissionInfo = this.getPermissionScopeOfFunction(LIST_FUNCTION_ACTION_ID);
			if (accountPermissionInfo.isNoPermission()) {
				subWhere = " 1=0 ";
			} else if (accountPermissionInfo.isAllPermission()) {
				subWhere = " 1=1 ";
			} else {
				int index = 0;
				String key = "";
				for (AccountPermission accountPermission : accountPermissionInfo.getList()) {

					String tempSubWhere = " 1=1 ";
					List<String> allowedVendorCodeList = accountPermission.getVendorList();
					if (allowedVendorCodeList.size() > 0) {
						key = "vendorList" + index;
						tempSubWhere += " and b.vencode in :" + key;
						params.put(key, allowedVendorCodeList);
					}

					List<Long> allowedAccountIdList = accountPermission.getAccountList();
					if (allowedAccountIdList.size() > 0) {
						key = "accountList" + index;
						tempSubWhere += " and (emp.id in :" + key + ") ";
						params.put(key, allowedAccountIdList);
					}

					List<Long> allowedCompanyIdList = accountPermission.getCompanyList();
					if (allowedCompanyIdList.size() > 0) {
						key = "companyList" + index;
						tempSubWhere += " and b.company_id in :" + key;
						params.put(key, allowedCompanyIdList);
					}
					
					List<Long> allowedStoreIdList = accountPermission.getStoreList();
					if (allowedStoreIdList.size() > 0) {
						key = "storeList" + index;
						tempSubWhere += " and b.store_id in :" + key;
						params.put(key, allowedStoreIdList);
					}


					subWhere += " or (" + tempSubWhere + ") ";

					index++;
				}
			}

			bodyQuery += " and (" + subWhere + ") ";

		}

		if (state >= 0) {
			bodyQuery += " and b.srmstate=:state";
			params.put("state", state);
		}
		
		if (!inventory.trim().isEmpty()) {
			bodyQuery += " and (d.name like CONCAT('%',:inventory, '%') or d.code like CONCAT('%',:inventory, '%')) ";
			params.put("inventory", inventory.trim());
		}

		if (!vendorStr.trim().isEmpty()) {
			bodyQuery += " and (f.name like CONCAT('%',:vendor, '%') or f.code like CONCAT('%',:vendor, '%')) ";
			params.put("vendor", vendorStr.trim());
		}

		if (startDate != null) {
			bodyQuery += " and b.orderdate>=:startDate";
			params.put("startDate", startDate);
		}
		if (endDate != null) {
			bodyQuery += " and b.orderdate<:endDate";
			params.put("endDate", endDate);
		}

		countQuery += bodyQuery;
		Query q = em.createNativeQuery(countQuery);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}

		BigInteger totalCount = (BigInteger) q.getSingleResult();

		selectQuery += bodyQuery + orderBy;
		q = em.createNativeQuery(selectQuery, "ShipSearchResult");
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}

		List list = q.setFirstResult((int) request.getOffset()).setMaxResults(request.getPageSize()).getResultList();

		return new PageImpl<ShipSearchResult>(list, request, totalCount.longValue());

	}

	@PostMapping("/export")
	public ModelAndView export_file(@RequestParam(value = "export_data") String exportData, Principal principal) {

		List<PurchaseOrderDetail> exportList = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ExportShipForm shipForm = objectMapper.readValue(exportData, new TypeReference<ExportShipForm>() {
			});

			String query = "select a.*, d.code vendorcode, (a.quantity-ifnull(a.shipped_quantity,0)) remain_quantity, d.name vendorname, c.name inventoryname, c.specs, e.name unitname "
					+ "from purchase_order_detail a left join purchase_order_main b on a.main_id = b.id "
					+ "left join inventory c on a.inventorycode=c.code left join vendor d on b.vencode=d.code "
					+ "left join measurement_unit e on c.main_measure=e.code where a.id in :idList ";

			List<Long> idList = shipForm.getList();

			String order = "code";
			switch (shipForm.getOrder()) {
			case "inventoryname":
				order = "c.name";
				break;
			case "specs":
				order = "c.specs";
				break;
			case "unitname":
				order = "c.puunit_name";
				break;
			}

			query += "order by " + order + " " + shipForm.getDir();
			Query q = em.createNativeQuery(query, PurchaseOrderDetail.class);
			q.setParameter("idList", idList);

			exportList = q.getResultList();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return new ModelAndView(new ExcelShipReportView(), "exportList", exportList);
	}

	@Transactional
	@RequestMapping("/import")
	public String import_file(@RequestParam("import_file") MultipartFile excelFile, 
			RedirectAttributes redirectAttributes, Principal principal) {
		File file = UploadFileHelper.simpleUpload(excelFile, true, Constants.PATH_UPLOADS_SHIP);

		List<ArrayList<String>> importList = new ArrayList<>();

		int importCount = 0;

		try {

			InputStream excelFileStream = excelFile.getInputStream();
			Workbook workbook = new HSSFWorkbook(excelFileStream);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			int index = 0;
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				index++;
				if (index == 1)
					continue;

				ArrayList<String> row = new ArrayList<>();
				List<Integer> valueList = Arrays.asList(1, 11, 16);
				for (int column : valueList) {

					Cell currentCell = currentRow.getCell(column);
					if (currentCell == null) {
						row.add(null);
					} else if (currentCell.getCellTypeEnum() == CellType.STRING) {
						row.add(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
						row.add(String.valueOf(currentCell.getNumericCellValue()));
					}
				}
				importList.add(row);
			}
			workbook.close();

			file.delete();

			Account account = accountRepository.findOneByUsername(principal.getName());
			Vendor vendor = account.getVendor();

			if (vendor != null && importList != null) {

				for (ArrayList<String> row : importList) {

					PurchaseOrderMain main = purchaseOrderMainRepository.findOneByCode(row.get(0));
					if (main.getVendor().getCode().equals(vendor.getCode())) {

						PurchaseOrderDetail detail = purchaseOrderDetailRepository
								.findOneById((long) Float.parseFloat(row.get(2)));

						if (detail != null) {
							if (row.get(1) != null) {
								
								detail = purchaseOrderDetailRepository.save(detail);

								List<Account> toList = new ArrayList<>();
								toList.add(main.getDeployer());
								String title = String.format("订单【%s】已由【%s】订单出货，请及时查阅和处理！", main.getCode(),
										account.getRealname());
								this.sendmessage(title, toList, String.format("/purchaseorder/%s/read", main.getCode()));

								importCount++;
							}
						}
					}
				}
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("message", "成功导入" + importCount + "行数据！");
		return "redirect:/ship/index";
	}

	@RequestMapping("/save")
	@Transactional
	public @ResponseBody Boolean save(@RequestParam Map<String, String> requestParams) {

		for (Entry<String, String> entry : requestParams.entrySet()) {
			if (entry.getKey().equals("_csrf"))
				continue;
			PurchaseOrderDetail detail = purchaseOrderDetailRepository.findOneById(Long.valueOf(entry.getKey()));
			if (detail != null && entry.getValue() != null && !entry.getValue().isEmpty()) {
				purchaseOrderDetailRepository.save(detail);

				PurchaseOrderMain main = purchaseOrderMainRepository.findOneByCode(detail.getMain().getCode());
				List<Account> toList = new ArrayList<>();
				toList.add(main.getDeployer());
				String title = String.format("订单【%s】已由【%s】订单出货，请及时查阅和处理！", main.getCode(),
						this.getLoginAccount().getRealname());
				this.sendmessage(title, toList, String.format("/purchaseorder/%s/read", main.getCode()));
			}
		}

		return true;
	}
}
