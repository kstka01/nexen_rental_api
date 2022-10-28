package com.nexentire.rental.util.jandi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class IncommingHook {

	Logger logger = LoggerFactory.getLogger(IncommingHook.class);
	
	//@Autowired
	HttpURLConnection httpUrlConnection;
	
	@Value("${jandi.incomming.url}")
	private String sendUrl;
	@Value("${jandi.incomming.email}")
	private String sendEmail;
	
	private String runningDay;
	private String fileUrl;
	private String title;
	private String body;
	
	public String getRunningDay() {
		return runningDay;
	}

	public void setRunningDay(String runningDay) {
		this.runningDay = runningDay;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public IncommingHook() {}
	
	public String sendToJandi() {
		
		// TODO Auto-generated constructor stub		
		Map<String, Object> requestMap = new HashMap();
		Map<String, Object> resultMap = new HashMap();
		List<Map> connectionInfos = new ArrayList();
		Map<String, Object> connectionInfo = new HashMap();
			
		String data = null;
		String inputLine = null;
		String returnStr = null;
		
		DataOutputStream wr = null;
		BufferedReader in = null;
		
		URL url = null;
		try {
			
			//테스트 결과 잔디 connect는 매번 연결을 해줘야 한다...
			url = new URL(sendUrl);
			httpUrlConnection = (HttpURLConnection)url.openConnection();
			
			httpUrlConnection.setConnectTimeout(30000); //서버에 연결되는 Timeout 시간 설정
			httpUrlConnection.setReadTimeout(30000); // InputStream 읽어 오는 Timeout 시간 설정

			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDefaultUseCaches(false);
			
			httpUrlConnection.setRequestMethod("POST");
			
			// Header 영역에 쓰기
			httpUrlConnection.addRequestProperty("Content-Type", "application/json");
			httpUrlConnection.addRequestProperty("Accept", "application/vnd.tosslab.jandi-v2+json");
			httpUrlConnection.addRequestProperty("encoding", "utf-8");
			
			requestMap.put("email", sendEmail);
			requestMap.put("body", this.body);
			requestMap.put("connectColor", "#FAC11B");
			
			
			connectionInfo.put("title", this.title);
			connectionInfo.put("description", this.fileUrl);
			//connectionInfo.put("imageUrl", "test");
			
			connectionInfos.add(connectionInfo);
			
			requestMap.put("connectInfo", connectionInfos);
			
			String json = new Gson().toJson(requestMap);

			data = json.toString();
			wr = new DataOutputStream(httpUrlConnection.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			
			if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				returnStr = "200";
				
			}else {
				returnStr = "500";
			}
			
		}catch(Exception e) {
			returnStr = "500";
		}finally {
			httpUrlConnection.disconnect();
		}
		
		return returnStr;
	}
}
