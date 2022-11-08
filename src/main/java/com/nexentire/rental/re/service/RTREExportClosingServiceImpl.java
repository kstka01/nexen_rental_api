package com.nexentire.rental.re.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nexentire.rental.constants.NexenConstants;
import com.nexentire.rental.exception.NexenUserDefineException;
import com.nexentire.rental.re.dao.RTREExportClosingDAO;
import com.nexentire.rental.re.vo.RTREExportClosingVO;
import com.nexentire.rental.util.email.EmailService;
import com.nexentire.rental.util.jandi.IncommingHook;
import com.nexentire.rental.util.paging.Pagination;
import com.nexentire.rental.util.redis.RedisProxyUtil;
import com.nexentire.rental.util.redis.RedisUtil;

@Repository("rtreExportClosingService")
public class RTREExportClosingServiceImpl implements RTREExportClosingService {

	Logger logger = LoggerFactory.getLogger(RTREExportClosingServiceImpl.class);
	
	@Autowired
	RTREExportClosingDAO rtreExportClosingDao;
	@Resource(name="emailService")
	EmailService emailService;
	@Resource(name="incommingHook")
	IncommingHook incommingHook;
	
	RedisUtil redisUtil;
	
	@Override
	public String runExportFile(RTREExportClosingVO vo) throws Exception {
		// TODO Auto-generated method stub
		
		List<RTREExportClosingVO> result =   null;
		String returnStr = "";
		int totCount = 0;
		
		//File destFile = null;
		try {
			
			totCount = getSndRcvTotCount(vo);
			
			logger.info("1.Row건수:" + totCount);
			
			//추출건수가 0이면 진행하지 않는다.
			if(totCount > 0) {
				
				int currentPage = 1;
				int cntPerPage = 10000;
				int pageSize = 10;
				
		        Map<String, Object> param = new HashMap<>();
		        param.put("rqstDay", vo.getRqstDay());
		        param.put("fileTp", vo.getFileTp());
		        param.put("fileNm", vo.getFileNm());
		        
		        logger.info("2.Redis초기화 시작...");
		        
		        //Redis Util init
				redisUtil = (RedisUtil) RedisProxyUtil.getInstance(RedisProxyUtil.LIST);
				
				//redis 초기화
				RedisProxyUtil.removeRedisItem(Arrays.asList(vo.getExportKey()));
				
				logger.info("2.Redis초기화 완료");
				
				//페이징처리
				Pagination pagination = new Pagination(currentPage, cntPerPage, pageSize);
		        pagination.setTotalRecordCount(totCount);
		        
		        logger.info("3.페이징처리 시작");
		        
		        while(currentPage <= pagination.getLastPage()) {
	
		        	param.put("firstRecordIndex", pagination.getFirstRecordIndex());
			        param.put("lastRecordIndex", pagination.getLastRecordIndex());
			        
			        logger.info("page:[" + currentPage + "]");
			        
		        	result = getSndRcvList(param);
		        	
		        	logger.info("Redis저장 시작...");
		        	//row data생성 후 redis insert
					makeRowData(result, vo);
					logger.info("Redis저장 완료");
					
					currentPage += 1;
		        	pagination.setCurrentPage(currentPage);
		        	pagination.setTotalRecordCount(totCount);
		        }
				
		        logger.info("4.Row데이터 가공 시작...");
		        
				Object resultStr = redisUtil.getRedisItem(vo.getExportKey(), 0, totCount);
				//line별 구분자 , 없애기 위한 로직 - java 8 or above
				String joinedObjects = String.join("", Arrays.stream((Object[])resultStr).map(String::valueOf).toArray(n->new String[n]));
				//String joinedObjects = String.join("", Arrays.stream((Object[])resultStr).map(String::valueOf).collect(Collectors.toList()));
				
				logger.info("4.Row데이터 가공 완료");
				
				//다운로드폴더 존재여부 체크
				File dir = new File(vo.getFilePath());
				if(!dir.exists()) {
					dir.mkdir();
				}
				
				//폴더내 파일 초기화
				File[] fileList = dir.listFiles();
				for(File file : fileList) {
					file.delete();
				}
				
				logger.info("5.전문파일 생성 시작...");
				
				// 전문파일 생성
//				destFile = new File(vo.getFilePath() + NexenConstants.SEPERATOR + vo.getFileNm());
//				destFile.createNewFile();
				
				// true 지정시 파일의 기존 내용에 이어서 작성 (UTF-8인코딩 미지원)
//				FileWriter fw = new FileWriter(destFile, false);
//				// 파일안에 문자열 쓰기
//				fw.write(joinedObjects);
//				fw.flush();
//				// 객체 닫기
//				fw.close();	
				
				//ANSI형식으로 파일 생성
				BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vo.getFilePath() + NexenConstants.SEPERATOR + vo.getFileNm())));
				//BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vo.getFilePath() + NexenConstants.SEPERATOR + vo.getFileNm()),"MS949"));
				output.write(joinedObjects);
				output.close();
				
				logger.info("5.전문파일 생성 완료");
				
				//email발송
				//boolean	sendResult = emailService.sendEmail("kstka@nexentire.com", "test", "", filePath, fileNm);
				
				//파일압축
				//CreateFileCompression.createZipInDirectory(vo.getFilePath(), vo.getFileNm());
				
				logger.info("6.잔디 발송 시작...");
				
				//jandi발송
				incommingHook.setTitle("file path");
				incommingHook.setBody("[" + vo.getRqstDay() + " file export]");
				//incommingHook.setFileUrl(vo.getHttpUrlAddress() + vo.getExportPath() + "/" + vo.getFileNm() + "." + FileExtType.ZIP);
				incommingHook.setFileUrl(vo.getHttpUrlAddress() + vo.getExportPath() + NexenConstants.HTTP_SEPERATOR + vo.getFileNm());
				
				returnStr = incommingHook.sendToJandi();
				if(!returnStr.equals("200")) {
					logger.info("6.잔디 발송 실패:[" + returnStr + "]");
					returnStr = "Incomming fail!!";
				}
				
				logger.info("6.잔디 발송 완료");
			
				resultStr = "";
			}
			
			returnStr = totCount + "건 파일 추출 완료되었습니다.";
			
		}catch(Exception e) {
			returnStr = "[에러]" + e.getMessage();
			new NexenUserDefineException(this.getClass(), e.getMessage(), e);
		}finally {
			//destFile.delete();
		}
		
		return returnStr;
	}
	
	@Override
	public List<RTREExportClosingVO> getSndRcvList(Map<String, Object> param)
			throws Exception {
		// TODO Auto-generated method stub
		return rtreExportClosingDao.getSndRcvList(param);
	}

	@Override
	public int getSndRcvTotCount(RTREExportClosingVO vo) throws Exception {
		// TODO Auto-generated method stub
		return rtreExportClosingDao.getSndRcvTotCount(vo);
	}

	@Override
	public void makeRowData(List<RTREExportClosingVO> param, RTREExportClosingVO vo) throws Exception {
		// TODO Auto-generated method stub
		
		String sCrLF 	= "";
		
		if("Y".equals(vo.getCrlfYn())) {
			sCrLF ="\r\n";
		}
		
		for (int i = 0; i < param.size(); i++) {
			RTREExportClosingVO data = param.get(i);
			String rowData = data.getRowData()+sCrLF;					
			
			redisUtil.saveRedisItem(vo.getExportKey(), rowData);
		}
	}

	@Override
	public void getProcessResult(RTREExportClosingVO vo, String logFilePath) throws Exception {
		// TODO Auto-generated method stub
		
		String returnStr = null;
		ReversedLinesFileReader reader = null;
		
		try {
			
			//로그파일 읽어서 최신내역 가져오기
		    reader = new ReversedLinesFileReader(new File(logFilePath), Charset.forName("UTF-8"));
		    String data = null;
		    StringBuffer lastLine = new StringBuffer();
		    while((data = reader.readLine()) != null){
		    	if(data.contains("[==== START ====]")){
		    		lastLine.append(data);
		    		break;
		    	}else if(data.contains("[[CFG ]]")){ //환경설정부분은 제외	
		    	}else if(data.contains("[[MAKE]]")){ //데이터송신부분은 제외
		    	}else{
		    		lastLine.append(data);
			    	lastLine.append("\r");
		    	}
		    }
		    
		    //jandi발송
		    incommingHook.setBody("[" + vo.getKcpReqDate() + "]" + "KCP file " + vo.getKcpReqType() + " result");
			incommingHook.setTitle(lastLine.toString());
			
			//다운로드인 경우에만 파일 다운로드 경로 첨부
			if(vo.getKcpReqType().equals("DOWN")) {
				incommingHook.setFileUrl(vo.getHttpUrlAddress() + vo.getExportPath() + NexenConstants.HTTP_SEPERATOR + vo.getFileNm());
			}
			
			returnStr = incommingHook.sendToJandi();
			if(!returnStr.equals("200")) {
				returnStr = "Incomming fail!!";
			}
		} catch (Exception e) {
			new NexenUserDefineException(this.getClass(), e.getMessage(), e);
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void incommingTest() {
		
		String returnStr = null;
		
		//jandi발송
		incommingHook.setTitle("file path");
		incommingHook.setBody("[file export]");
		//incommingHook.setFileUrl(vo.getHttpUrlAddress() + vo.getExportPath() + "/" + vo.getFileNm() + "." + FileExtType.ZIP);
		//incommingHook.setFileUrl(vo.getHttpUrlAddress() + vo.getExportPath() + NexenConstants.HTTP_SEPERATOR + vo.getFileNm());
		
		returnStr = incommingHook.sendToJandi();
		if(!returnStr.equals("200")) {
			logger.info("6.잔디 발송 실패:[" + returnStr + "]");
			returnStr = "Incomming fail!!";
		}
	}

}
