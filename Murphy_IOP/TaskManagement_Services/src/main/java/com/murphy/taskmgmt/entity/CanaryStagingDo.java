package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: CanaryStagingDo
 */
@Entity
@Table(name = "TM_CANARY_STAGING_TABLE")
public class CanaryStagingDo implements BaseDo, Serializable {

	private static final long serialVersionUID = -7341365853980611944L;

	@EmbeddedId
	private CanaryStagingPK canaryStagingPK;

	@Column(name = "DATA_VALUE")
	private Double dataValue;

	public CanaryStagingDo() {
		super();
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

	public CanaryStagingPK getCanaryStagingPK() {
		return canaryStagingPK;
	}

	public void setCanaryStagingPK(CanaryStagingPK canaryStagingPK) {
		this.canaryStagingPK = canaryStagingPK;
	}
}