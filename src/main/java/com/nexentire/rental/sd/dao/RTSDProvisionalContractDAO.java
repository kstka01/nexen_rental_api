package com.nexentire.rental.sd.dao;

import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexentire.rental.exception.NexenDaoException;
import com.nexentire.rental.sd.vo.RTSDProvisionalContractVO;

@Mapper
public interface RTSDProvisionalContractDAO {

	Logger logger = LoggerFactory.getLogger(RTSDProvisionalContractDAO.class);
	
	void saveProvisionalData(RTSDProvisionalContractVO vo) throws NexenDaoException;
}
