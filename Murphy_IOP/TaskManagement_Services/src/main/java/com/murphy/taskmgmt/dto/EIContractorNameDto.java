package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class EIContractorNameDto extends BaseDto {
	
	private String id;
	private String contractorName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	
	@Override
	public String toString() {
		return "EIContractorNameDto [id=" + id + ", contractorName=" + contractorName + "]";
	}
	
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	
}
