package com.nexentire.rental.re.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nexentire.rental.cm.NexenConstants;
import com.nexentire.rental.re.service.RTREExportClosingService;
import com.nexentire.rental.re.vo.RTREExportClosingVO;
import com.nexentire.rental.re.vo.RTREExportEnum.FileExtType;


@Controller
public class RTREExportClosingController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="rtreExportClosingService")
	RTREExportClosingService rtreExportClosingService;
	
	@Value("${sys.export.path}")
	private String exportPath;
	@Value("${sys.export.key}")
	private String exportKey;
	
	/* kcp환경설정 */
	@Value("${kcp.setting.exeFile}")
	private String exeFile;
	@Value("${kcp.setting.confFile}")
	private String configFile;
	@Value("${kcp.setting.batch.path}")
	private String batchPath;
	@Value("${kcp.setting.cash.path}")
	private String cashPath;
	@Value("${kcp.setting.logPath}")
	private String logPath;
	@Value("${kcp.setting.siteId}")
	private String siteId;

	/*
	 * 정기출금파일생성
	 */
	@RequestMapping("/api/exportFile.do")
	@ResponseBody
	public String exportFile(RTREExportClosingVO vo, Model model, HttpServletRequest req, @RequestBody Map<String, Object> requestData) {
		
		String returnStr = "";
		try {
			String httpFullAddress = req.getRequestURL().toString();
			String httpUrlAddress = httpFullAddress.replace(req.getServletPath(), "");
			
			req.getSession().setAttribute("regId", "kstka");
			String regId    = req.getSession().getAttribute("regId").toString();
			vo.setRegId(regId);
			
			vo.setHttpFullAddress(httpFullAddress);
			vo.setHttpUrlAddress(httpUrlAddress);
			
			String filePath = req.getSession().getServletContext().getRealPath(this.exportPath);
			vo.setFilePath(filePath);
			vo.setExportPath(this.exportPath);
			vo.setExportKey(this.exportKey);
			
			vo.setRqstDay(String.valueOf(requestData.get("rqstDay")));
			vo.setFileTp(String.valueOf(requestData.get("fileTp")));
			vo.setFileNm(String.valueOf(requestData.get("fileNm")));
			vo.setGubun(String.valueOf(requestData.get("gubun")));
			vo.setCrlfYn(String.valueOf(requestData.get("crlfYn")));
			vo.setDelYn(String.valueOf(requestData.get("delYn")));
			vo.setRowSeq(String.valueOf(requestData.get("0")));
			
			returnStr = rtreExportClosingService.runExportFile(vo);
			
		} catch (Exception e) {
			returnStr = "[에러]" + e.getMessage();
			logger.info(returnStr);
			//new NexenUserDefineException(this.getClass(), e.getMessage(), e);
		}
		
		return String.valueOf(returnStr);
	}
	
	/*
	 * 정기출금파일 Nexen -> KCP
	 */
	@RequestMapping("/api/uploadKCP.do")
	@ResponseBody
	public String uploadKCP(RTREExportClosingVO vo, Model model, HttpServletRequest req, @RequestBody Map<String, Object> requestData) {
		
		String returnStr = null;
		String workFilePath = "";
		
		Process process = null;
		
		try {
			
			String httpFullAddress = req.getRequestURL().toString();
			String httpUrlAddress = httpFullAddress.replace(req.getServletPath(), "");
			
			String kcpFlag = String.valueOf(requestData.get("kcpFlag"));
			String kcpReqType = String.valueOf(requestData.get("kcpReqType"));
			String kcpReqDate = String.valueOf(requestData.get("kcpReqDate"));
			String fileNm = String.valueOf(requestData.get("fileNm"));
			
			String rootPath = "";
			
			logger.info("[kcpFlag]:" + kcpFlag);
			logger.info("[kcpReqType]:" + kcpReqType);
			logger.info("[kcpReqDate]:" + kcpReqDate);
			logger.info("[fileNm]:" + fileNm);
			
			vo.setHttpFullAddress(httpFullAddress);
			vo.setHttpUrlAddress(httpUrlAddress);
			
			vo.setExportPath(this.exportPath);
			
			vo.setKcpFlag(kcpFlag);
			vo.setKcpReqDate(kcpReqDate);
			vo.setFileNm(fileNm);
			vo.setKcpReqType(kcpReqType);
			
			if(kcpFlag.equals(NexenConstants.BACH)) {
				rootPath = batchPath;
			}else {
				rootPath = cashPath;
			}
			
			//파일 경로 설정
			workFilePath = rootPath + NexenConstants.SEPERATOR + kcpReqType + NexenConstants.SEPERATOR + fileNm + "." + FileExtType.TXT;
			
			//업로드 폴더내 과거 파일 삭제
			File[] files = new File(rootPath + NexenConstants.SEPERATOR + kcpReqType).listFiles();
			for(File file : files) {
				if(file.exists()) {
					file.delete();
				}
			}
			
			//생성된 파일 실제 경로
			String filePath = req.getSession().getServletContext().getRealPath(this.exportPath);
			logger.info("export경로:" + filePath);
			
			logger.info("파일 복사 시작...");
			//생성 파일 이동
			Path oldFile = Paths.get(filePath + NexenConstants.SEPERATOR + fileNm);
			Path newFile = Paths.get(workFilePath);
			Files.move(oldFile, newFile, StandardCopyOption.ATOMIC_MOVE);
			logger.info("파일 복사 완료");
			
			logger.info("파일 업로드 시작...");
			process = new ProcessBuilder(exeFile, configFile, kcpReqType, kcpFlag, kcpReqDate, siteId, workFilePath).start();

			//프로세스 실행 여부 체크
			while(process.isAlive()){
		    	//System.out.println("1");
		    }
			logger.info("파일 업로드 완료");
			
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			
			logger.info("로그 분석 시작...");
			String logFilePath = logPath + NexenConstants.SEPERATOR + "info.log_" + format.format(now);
			rtreExportClosingService.getProcessResult(vo, logFilePath);
			logger.info("로그 분석 완료");
		    
			returnStr = "KCP작업완료되었습니다.";
		} catch (Exception e) {
			returnStr = "[에러]" + e.getMessage();
			logger.info(returnStr);
			//new NexenUserDefineException(this.getClass(), e.getMessage(), e);
		}finally {
			try {
				//기존 프로세스 강제 종료
				process.destroy();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return String.valueOf(returnStr);
	}
	
	/*
	 * 정기출금파일 KCP -> Nexen
	 */
	@RequestMapping("/api/downloadKCP.do")
	@ResponseBody
	public String downloadKCP(RTREExportClosingVO vo, Model model, HttpServletRequest req, @RequestBody Map<String, Object> requestData) {
		
		String returnStr = null;
		String workFilePath = "";
		
		Process process = null;
		
		try {
			
			String kcpFlag = String.valueOf(requestData.get("kcpFlag"));
			String kcpReqType = String.valueOf(requestData.get("kcpReqType"));
			String kcpReqDate = String.valueOf(requestData.get("kcpReqDate"));
			String fileNm = String.valueOf(requestData.get("fileNm"));
			
			String httpFullAddress = req.getRequestURL().toString();
			String httpUrlAddress = httpFullAddress.replace(req.getServletPath(), "");
			
			vo.setHttpFullAddress(httpFullAddress);
			vo.setHttpUrlAddress(httpUrlAddress);
			
			vo.setExportPath(this.exportPath);
			
			vo.setKcpFlag(kcpFlag);
			vo.setKcpReqDate(kcpReqDate);
			vo.setFileNm(fileNm);
			vo.setKcpReqType(kcpReqType);
			
			String rootPath = "";
			
			logger.info("[kcpFlag]:" + kcpFlag);
			logger.info("[kcpReqType]:" + kcpReqType);
			logger.info("[kcpReqDate]:" + kcpReqDate);
			logger.info("[fileNm]:" + fileNm);
			
			if(kcpFlag.equals(NexenConstants.BACH)) {
				rootPath = batchPath;
			}else {
				rootPath = cashPath;
			}
			
			//파일 경로 설정
			workFilePath = rootPath + NexenConstants.SEPERATOR + kcpReqType;
			
			logger.info("파일 다운로드 시작...");
			process = new ProcessBuilder(exeFile, configFile, kcpReqType, kcpFlag, kcpReqDate, siteId, workFilePath).start();

			//프로세스 실행 여부 체크
			while(process.isAlive()){
		    	//System.out.println("1");
		    }
			logger.info("파일 다운로드 완료");
			
			//생성된 파일 실제 경로
			String filePath = req.getSession().getServletContext().getRealPath(this.exportPath);
			logger.info("export경로:" + filePath);	
			
			//폴더 생성 여부 확인
			File dir = new File(filePath);
			if(!dir.exists()) {
				dir.mkdir();
			}
			
			logger.info("파일 복사 시작...");
			//생성 파일 이동
			Path oldFile = Paths.get(workFilePath + NexenConstants.SEPERATOR + fileNm);
			Path newFile = Paths.get(filePath + NexenConstants.SEPERATOR + fileNm);			
			Files.move(oldFile, newFile, StandardCopyOption.ATOMIC_MOVE);
			logger.info("파일 복사 완료");
			
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			
			logger.info("로그 분석 시작...");
			String logFilePath = logPath + NexenConstants.SEPERATOR + "info.log_" + format.format(now);
			rtreExportClosingService.getProcessResult(vo, logFilePath);
			logger.info("로그 분석 완료");
		    
			returnStr = "KCP작업완료되었습니다.";
		} catch (Exception e) {
			returnStr = "[에러]" + e.getMessage();
			logger.info(returnStr);
			//new NexenUserDefineException(this.getClass(), e.getMessage(), e);
		}finally {
			try {
				//기존 프로세스 강제 종료
				process.destroy();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return String.valueOf(returnStr);
	}
	
	@RequestMapping("/api/incommingTest.do")
	@ResponseBody
	public String incommingTest() {
		
		try {
			rtreExportClosingService.incommingTest();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		
		return null;
	}
}
