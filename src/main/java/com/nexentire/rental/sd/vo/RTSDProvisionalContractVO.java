package com.nexentire.rental.sd.vo;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RTSDProvisionalContractVO {

	public RTSDProvisionalContractVO() {
		// TODO Auto-generated constructor stub
	}

	private String provsnTp;
	private String provsnDay;
	private String custNm;
	private String mobNo;
	private String mcNm;
	private String specNm;
	private String carNo;
	private String petternNm;
	private String emailAddr;
	private String regId;
	private String hshopTp;
	private String contactGet;
	private String provsnSt;
	private String provsnStDtl;
	
	private int successCode;
	private String returnMessage;
	private String errorText;
}
