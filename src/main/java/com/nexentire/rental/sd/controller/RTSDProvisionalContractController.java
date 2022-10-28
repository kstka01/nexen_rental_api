package com.nexentire.rental.sd.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nexentire.rental.sd.service.RTSDProvisionalContractService;
import com.nexentire.rental.sd.vo.RTSDProvisionalContractVO;

@Controller
public class RTSDProvisionalContractController {

	@Resource(name="rtsdProvisionalContractService")
	private RTSDProvisionalContractService rtsdProvisionalContractService;
	
	public RTSDProvisionalContractController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("/api/saveProvisionalData.do")
	@ResponseBody
	public String saveProvisionalData(@RequestBody Map<String, Object> requestData) {
		
		RTSDProvisionalContractVO vo = null;
		
		SimpleDateFormat sdf = null;
		
		int successCode = 0;
		String returnMessage = null;
		try {
			 // 현재 날짜/시간        
			Date now = Calendar.getInstance().getTime();              
			// 포맷팅 정의        
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");         
			// 포맷팅 적용        
			String formatedNow = formatter.format(now);
			
			vo = new RTSDProvisionalContractVO();
			
			vo.setProvsnTp("H");
			vo.setProvsnDay(formatedNow);
			vo.setCustNm(String.valueOf(requestData.get("custNm")));
			vo.setMobNo(String.valueOf(requestData.get("mobNo")));
			vo.setMcNm(String.valueOf(requestData.get("mcNm")));
			vo.setSpecNm(String.valueOf(requestData.get("specNm")));
			vo.setCarNo(String.valueOf(requestData.get("carNo")));
			vo.setPetternNm(String.valueOf(requestData.get("petternNm")));
			vo.setEmailAddr(String.valueOf(requestData.get("emailAddr")));
			vo.setProvsnSt(String.valueOf(requestData.get("provsnSt")));
			vo.setProvsnStDtl(String.valueOf(requestData.get("provsnStDtl")));
			vo.setRegId(String.valueOf(requestData.get("hshopTp")));
			vo.setHshopTp(String.valueOf(requestData.get("hshopTp")));
			vo.setContactGet(String.valueOf(requestData.get("contactGet")));
			
			rtsdProvisionalContractService.saveProvisionalData(vo);
			
			successCode = 0;
			returnMessage = "정상적으로 처리완료되었습니다.";
		}catch(Exception e) {
			
			successCode = -1;
			returnMessage = e.getMessage();
		}
		
		Map<String, Object> result = new HashMap<>();
		result.put("successCode", successCode);
		result.put("resultMessage", returnMessage);
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(result);
		
		return String.valueOf(resultJson);
	}

}
