package com.nexentire.rental.cm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nexentire.rental.util.jwt.JwtTokenUtil;

@Controller
public class JwtController {

	Logger logger = LoggerFactory.getLogger(JwtController.class);
	
	@RequestMapping("/v1/apis/api/admin/authenticate.do")
	@ResponseBody
	public String createJWT(HttpServletRequest req, @RequestBody Map<String, Object> requestData) {
		
		String id = null;
		String accessToken = null;
		String refreshToken = null;
		String successCode = "0";
		String returnMessage = null;
		
		JwtTokenUtil tokenUtil = null;
		try {
			
			logger.info("token발급 시작...");
			logger.info("=========================================================================");
			logger.info("ID:" + requestData.get("id"));
			id = String.valueOf(requestData.get("id"));
			
			tokenUtil = new JwtTokenUtil();
			
			accessToken = tokenUtil.create(id);
			refreshToken = tokenUtil.createRefreshToken();
			
		} catch (Exception e) {
			// TODO: handle exception
			successCode = "-1";
			returnMessage = e.getMessage();
		}
		
		Map<String, Object> result = new HashMap<>();
		result.put("accessToken", accessToken);
		result.put("refreshToken", refreshToken);
		result.put("successCode", successCode);
		result.put("resultMessage", returnMessage);
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(result);
		
		logger.info("ACCESS_TOKEN:" + accessToken);
		logger.info("REFRESH_TOKEN:" + refreshToken);
		logger.info("=========================================================================");
		logger.info("token발급완료");
		return String.valueOf(resultJson);
	}

	@RequestMapping("/v1/apis/api/admin/confirmAuthenticate.do")
	@ResponseBody
	public String confirmJWT(HttpServletRequest req, @RequestHeader Map<String, Object> requestHeader, @RequestBody Map<String, Object> requestData) {
		
		JwtTokenUtil tokenUtil = null;
		
		String userId = null;
		String token = null;
		String successCode = "0";
		String returnMessage = null;
		
		try {
			
			//String authorization = String.valueOf(requestHeader.get("authorization"));
			token = String.valueOf(requestData.get("token"));
			
			tokenUtil = new JwtTokenUtil();
			userId = tokenUtil.validateAndgetUserId("token");
			
			if(userId.equals(null)) {
				successCode = "-1";
				returnMessage = "정상적인 token이 아닙니다.!!";
			}
		} catch (Exception e) {
			// TODO: handle exception
			successCode = "-1";
			returnMessage = e.getMessage();
		}
		
		
		Map<String, Object> result = new HashMap<>();
		result.put("userId", userId);
		result.put("successCode", successCode);
		result.put("resultMessage", returnMessage);
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(result);
		
		return String.valueOf(resultJson);
	}
}
