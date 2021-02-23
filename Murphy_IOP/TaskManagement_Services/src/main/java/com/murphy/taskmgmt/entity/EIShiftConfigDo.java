package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EI_SHIFT_CONFIG")
public class EIShiftConfigDo implements BaseDo, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "SHIFT_TYPE", length = 40)
	private String shiftType;

	@Override
	public Object getPrimaryKey() {
		return this.id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShiftType() {
		return shiftType;
	}

	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	
}
