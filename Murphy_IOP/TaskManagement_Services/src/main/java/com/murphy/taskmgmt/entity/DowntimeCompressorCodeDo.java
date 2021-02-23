package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="DT_COMPRESSOR_CODE")
public class DowntimeCompressorCodeDo implements Serializable,BaseDo{
	@Id
	@Column(name="COMPRESSOR_CODE",length=100)
private String compressorCode;
	@Column(name="FLARE_CODE_DESCRIPTION",length=100)
private String flareCodeDescription;
	@Column(name="DEPENDENT_VALUE",length=100)
private String dependentValue;
	
	@Column(name="ACTIVE_FLAG",length=100)
	private String activeFlag;

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}


}
