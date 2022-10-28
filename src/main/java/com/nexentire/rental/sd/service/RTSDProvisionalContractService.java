package com.nexentire.rental.sd.service;

import java.util.Map;

import com.nexentire.rental.exception.NexenServiceException;
import com.nexentire.rental.sd.vo.RTSDProvisionalContractVO;

public interface RTSDProvisionalContractService {

	public void saveProvisionalData(RTSDProvisionalContractVO vo) throws NexenServiceException;
}
