package com.nexentire.rental.re.vo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RTREExportClosingVO {
	
	private String rqstDay;
	private String fileNm;
	private String fileTp;
	private String crlfYn;
	private String delYn;
	private String rowSeq;
	private String gubun;
	
	private String fileSeq;
	private String rowData;
	private String regId;
	
	private String httpFullAddress;
	private String httpUrlAddress;
	
	private String filePath;
	private String exportPath;
	private String exportKey;
	
	private String kcpFlag;
	private String kcpReqType;
	private String kcpReqDate;
	private String kcpWorkFile;
	
	private String returnStr;
	
	private List<RTREExportClosingVO> sndRcvList;
	
	private int cnt;
	
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getRqstDay() {
		return rqstDay;
	}
	public void setRqstDay(String rqstDay) {
		this.rqstDay = rqstDay;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getFileTp() {
		return fileTp;
	}
	public void setFileTp(String fileTp) {
		this.fileTp = fileTp;
	}
	public String getCrlfYn() {
		return crlfYn;
	}
	public void setCrlfYn(String crlfYn) {
		this.crlfYn = crlfYn;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	public String getRowSeq() {
		return rowSeq;
	}
	public void setRowSeq(String rowSeq) {
		this.rowSeq = rowSeq;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(String fileSeq) {
		this.fileSeq = fileSeq;
	}
	public String getRowData() {
		return rowData;
	}
	public void setRowData(String rowData) {
		this.rowData = rowData;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public List<RTREExportClosingVO> getSndRcvList() {
		return sndRcvList;
	}
	public void setSndRcvList(List<RTREExportClosingVO> sndRcvList) {
		this.sndRcvList = sndRcvList;
	}
	public String getHttpFullAddress() {
		return httpFullAddress;
	}
	public void setHttpFullAddress(String httpFullAddress) {
		this.httpFullAddress = httpFullAddress;
	}
	public String getHttpUrlAddress() {
		return httpUrlAddress;
	}
	public void setHttpUrlAddress(String httpUrlAddress) {
		this.httpUrlAddress = httpUrlAddress;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getExportPath() {
		return exportPath;
	}
	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}
	public String getExportKey() {
		return exportKey;
	}
	public void setExportKey(String exportKey) {
		this.exportKey = exportKey;
	}
	@Override
	public String toString() {
		return "ExportClosingVO [rqstDay=" + rqstDay + ", fileNm=" + fileNm + ", fileTp=" + fileTp + ", crlfYn="
				+ crlfYn + ", delYn=" + delYn + ", rowSeq=" + rowSeq + ", gubun=" + gubun + "]";
	}
}
