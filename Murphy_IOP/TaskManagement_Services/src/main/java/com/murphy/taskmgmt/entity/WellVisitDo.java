package com.murphy.taskmgmt.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TM_WELL_VISIT")
public class WellVisitDo implements BaseDo{
	
	@EmbeddedId
	private WellVisitDoPK wellVisitDoPK;
	
	@Column(name = "FIELD", length = 100)
	private String field;


	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	public WellVisitDoPK getWellVisitDoPK() {
		return wellVisitDoPK;
	}

	public void setWellVisitDoPK(WellVisitDoPK wellVisitDoPK) {
		this.wellVisitDoPK = wellVisitDoPK;
	}

	@Override
	public Object getPrimaryKey() {
		return wellVisitDoPK;
	}

	@Override
	public String toString() {
		return "WellVisitDo [wellVisitDoPK=" + wellVisitDoPK + ", field=" + field + "]";
	}


}
