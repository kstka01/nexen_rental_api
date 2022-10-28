package com.nexentire.rental.sd.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexentire.rental.exception.NexenServiceException;
import com.nexentire.rental.sd.dao.RTSDProvisionalContractDAO;
import com.nexentire.rental.sd.vo.RTSDProvisionalContractVO;

@Service
public class RTSDProvisionalContractServiceImpl implements RTSDProvisionalContractService {

	@Autowired
	RTSDProvisionalContractDAO rtsdProvisionalContractDAO;
	
	public RTSDProvisionalContractServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveProvisionalData(RTSDProvisionalContractVO vo) throws NexenServiceException {
		// TODO Auto-generated method stub
		Map result;
		try {
			result = new HashMap();
			
			rtsdProvisionalContractDAO.saveProvisionalData(vo);
			
			if(vo.getSuccessCode() == -1) {
				throw new NexenServiceException(vo.getReturnMessage());
			}
		}catch(NexenServiceException e) {
			throw new NexenServiceException(e.getMessage());
		}
		
	}

}
