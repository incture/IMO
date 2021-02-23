package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EI_CONTRACTOR_LIST")
public class EIContractorNameDo implements BaseDo, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "CONTRACTOR_NAME")
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
	public Object getPrimaryKey() {
		return id;
	}
	
}
