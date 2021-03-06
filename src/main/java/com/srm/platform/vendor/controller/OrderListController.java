package com.srm.platform.vendor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.srm.platform.vendor.model.Vendor;
import com.srm.platform.vendor.repository.VendorRepository;
import com.srm.platform.vendor.utility.VendorSearchItem;
import com.srm.platform.vendor.view.ExcelDeliveryReportView;
import com.srm.platform.vendor.view.ExcelOrderlistReportView;

//库存报表
@Controller
@RequestMapping(path = "/orderlist")
public class OrderListController extends CommonController {
	@Autowired
	private VendorRepository vendorRepository;
	
	@GetMapping({ "/", "" })
	public String index() {
		return "report/orderlist";
	}

	@RequestMapping(value = "/list")
	public @ResponseBody String list_ajax(@RequestParam Map<String, String> requestParams) throws IOException, JSONException {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "10"));
		int page_index = Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String order = requestParams.getOrDefault("order", "");
		String dir = requestParams.getOrDefault("dir", "asc");
		String date_begin = requestParams.getOrDefault("start_date", null);
		String date_end = requestParams.getOrDefault("end_date", null);
		Vendor vendor = this.getLoginAccount().getVendor();
		String vendorStr = vendor == null ? "0" : vendor.getCode();
		
		if (vendor == null) {
			List<String> unitList = this.getDefaultUnitList();
			PageRequest request = PageRequest.of(0, 999,
					dir.equals("asc") ? Direction.ASC : Direction.DESC, "b.name");
			
			Page<VendorSearchItem> result = null;
			result = vendorRepository.findBySearchTerm("", unitList, request);
			
			if (result.getTotalElements() > 0) {
				List<VendorSearchItem> listVendor = result.getContent();
				String vendorCode = "";
				
				for (int i = 0; i < listVendor.size(); ++i) {
					VendorSearchItem item = listVendor.get(i);
					vendorCode = item.getCode();
					vendorStr += vendorCode;
					
					if (i != listVendor.size() - 1) {
						vendorStr += ",";
					}
				}
			}
		}
		
		String base_url = "http://183.249.171.190:5588/Service.asmx/GetMoPo?";
		String request_params = String.format("sys_PageIndex=%d&sys_PageSize=%d&cpoid=&cbustype=&vendorcode=%s&verifier_begin=&verifier_end=&invcode_begin=&invcode_end=&sys_Order=",
											  page_index, rows_per_page, vendorStr);
		String request_url = base_url + request_params;
		
		if (order != "") {
			request_url += order + "%20" + dir.toUpperCase();
		}
		
		if (date_begin != null && date_begin != "") {
			request_url += "&date_begin=" + date_begin + "%2000:00:00";
		}
		else {
			request_url += "&date_begin=";
		}
		
		if (date_end != null && date_end != "") {
			request_url += "&date_end=" + date_end + "%2000:00:00";
		}
		else {
			request_url += "&date_end=";
		}
		
		JSONObject json = this.readJsonFromUrl(request_url);
		return json.toString();
	}
	
	@RequestMapping(value = "/export")
	public ModelAndView export_file(@RequestParam Map<String, String> requestParams, Principal principal)  throws JSONException, IOException {
		int rows_per_page = Integer.parseInt(requestParams.getOrDefault("rows_per_page", "10"));
		int page_index = 1; //Integer.parseInt(requestParams.getOrDefault("page_index", "1"));
		String dir = requestParams.getOrDefault("dir", "asc");
		Vendor vendor = this.getLoginAccount().getVendor();
		String vendorStr = vendor == null ? "0" : vendor.getCode();
		
		if (vendor == null) {
			List<String> unitList = this.getDefaultUnitList();
			PageRequest request = PageRequest.of(0, 999,
					dir.equals("asc") ? Direction.ASC : Direction.DESC, "b.name");
			
			Page<VendorSearchItem> result = null;
			result = vendorRepository.findBySearchTerm("", unitList, request);
			
			if (result.getTotalElements() > 0) {
				List<VendorSearchItem> listVendor = result.getContent();
				String vendorCode = "";
				
				for (int i = 0; i < listVendor.size(); ++i) {
					VendorSearchItem item = listVendor.get(i);
					vendorCode = item.getCode();
					vendorStr += vendorCode;
					
					if (i != listVendor.size() - 1) {
						vendorStr += ",";
					}
				}
			}
		}
		
		String base_url = "http://183.249.171.190:5588/Service.asmx/GetMoPo?";
		String request_params = String.format("sys_PageIndex=%d&sys_PageSize=%d&cpoid=&cbustype=&vendorcode=%s&verifier_begin=&verifier_end=&invcode_begin=&invcode_end=",
											  page_index, rows_per_page, vendorStr);
		String request_url = base_url + request_params;
		request_url += "&sys_Order=";
		request_url += "&date_begin=";
		request_url += "&date_end=";
		
		JSONObject json = this.readJsonFromUrl(request_url);
		return new ModelAndView(new ExcelOrderlistReportView(), "exportList", json);
	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
	    	sb.append((char) cp);
	    }
		return sb.toString();
	}
	
	public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("GB2312")));
	    	String jsonText = readAll(rd);
	    	JSONObject json = new JSONObject(jsonText);
	    	
	    	if (json != null) {
	    		JSONArray dataList = json.getJSONArray("list");
	    		
	    		for (int i = 0; i < dataList.length(); ++i) {
	    			JSONObject objects = dataList.getJSONObject(i);
	    			double receiveQty = this.TryParseDouble(objects.getString("freceivedqty"));
	    			double poretQty = this.TryParseDouble(objects.getString("fPoRetQuantity"));
	    			double totalQty = this.TryParseDouble(objects.getString("iQuantity"));
	    			double stockQty = receiveQty - poretQty;
	    			double noarriveQty = totalQty - stockQty;
	    			json.getJSONArray("list").getJSONObject(i).put("fnoarriveqty", noarriveQty);
	    			json.getJSONArray("list").getJSONObject(i).put("fstockqty", stockQty);
	    		}
	    	}
	    	
	    	return json;
	    } finally {
	    	is.close();
	    }
	}
	
	@SuppressWarnings("finally")
	public double TryParseDouble(String data) {
		try {
			return Double.parseDouble(data);
		}
		finally {
			return 0.0;
		}
	}
}
