package com.murphy.taskmgmt.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TM_WELL_VISIT_DUMMY")
public class WellVisitDoDummy implements BaseDo{
	
	@EmbeddedId
	private WellVisitDoPKDummy wellVisitDoPK;
	
	@Column(name = "FIELD", length = 100)
	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	public WellVisitDoPKDummy getWellVisitDoPK() {
		return wellVisitDoPK;
	}

	public void setWellVisitDoPK(WellVisitDoPKDummy wellVisitDoPK) {
		this.wellVisitDoPK = wellVisitDoPK;
	}

	@Override
	public Object getPrimaryKey() {
		return wellVisitDoPK;
	}


}
