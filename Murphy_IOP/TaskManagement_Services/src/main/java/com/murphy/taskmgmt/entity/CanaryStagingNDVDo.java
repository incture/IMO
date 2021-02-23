package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: CanaryStagingDo
 *
 */
@Entity
@Table(name = "TM_CANARY_STAGING_NDV")
public class CanaryStagingNDVDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public CanaryStagingNDVDo() {
		super();
	}

	private static final long serialVersionUID = -7341365853980611944L;
	
	@EmbeddedId
	private CanaryStagingNDVPK canaryStagingPK;

	@Column(name = "STAGING_ID", length = 32)
	private String stagingId = UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "DATA_VALUE")
	private Double dataValue;
	
	
	

	public String getStagingId() {
		return stagingId;
	}




	public void setStagingId(String stagingId) {
		this.stagingId = stagingId;
	}




	public Double getDataValue() {
		return dataValue;
	}




	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}




	@Override
	public Object getPrimaryKey() {
		return canaryStagingPK;
	}




	public CanaryStagingNDVPK getCanaryStagingNDVPK() {
		return canaryStagingPK;
	}




	public void setCanaryStagingNDVPK(CanaryStagingNDVPK canaryStagingPK) {
		this.canaryStagingPK = canaryStagingPK;
	}



}
