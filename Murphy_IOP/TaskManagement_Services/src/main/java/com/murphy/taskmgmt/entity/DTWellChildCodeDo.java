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
@Table(name = "DT_WELL_CHILD_CODE")
public class DTWellChildCodeDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CHILD_CODE")
	private Integer childCode;

	@Column(name = "CHILD_CODE_DESCRIPTION")
	private String childCodeDescription;

	@Column(name = "PARENT_CODE")
	private String parentCode;

	@Override
	public Object getPrimaryKey() {
		return childCode;
	}

}
