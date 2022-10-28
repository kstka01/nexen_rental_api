package com.nexentire.rental.re.service;

import java.util.List;
import java.util.Map;

import com.nexentire.rental.re.vo.RTREExportClosingVO;

public interface RTREExportClosingService {

	public String runExportFile(RTREExportClosingVO vo) throws Exception;
	public List<RTREExportClosingVO> getSndRcvList(Map<String, Object> param) throws Exception;
	public int getSndRcvTotCount(RTREExportClosingVO vo) throws Exception;	
	public void makeRowData(List<RTREExportClosingVO> param, RTREExportClosingVO vod) throws Exception;
	public void getProcessResult(RTREExportClosingVO vo, String logFilePath) throws Exception;
	public void incommingTest();

}
