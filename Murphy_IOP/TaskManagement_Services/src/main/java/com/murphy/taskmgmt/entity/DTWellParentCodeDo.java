package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nivedha Chandhirika
 *
 */
@Entity
@Table(name = "DT_WELL_PARENT_CODE")
public class DTWellParentCodeDo implements BaseDo, Serializable {

	
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "PARENT_CODE")
	private Integer parentCode;

	@Column(name = "PARENT_CODE_DESCRIPTION")
	private String parentCodeDescription;

	@Column(name = "DEPENDENT_VALUE")
	private String dependentValue;

	@Override
	public Object getPrimaryKey() {
		return parentCode;
	}

}
