package com.nexentire.rental.re.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexentire.rental.exception.NexenDaoException;
import com.nexentire.rental.re.vo.RTREExportClosingVO;

@Mapper
public interface RTREExportClosingDAO {
	
	Logger logger = LoggerFactory.getLogger(RTREExportClosingDAO.class);
	
	List<RTREExportClosingVO> getSndRcvList(Map param) throws NexenDaoException;

	int getSndRcvTotCount(RTREExportClosingVO vo) throws NexenDaoException;
}
