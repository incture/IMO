package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: CustomAttrInstancesDo
 *
 */

@Entity
@Table(name = "TM_ATTR_INSTS")
public class CustomAttrInstancesDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public CustomAttrInstancesDo() {
		super();
	}
	private static final long serialVersionUID = -7341365853980611944L;	

	@Id
	@Column(name = "ATTR_ID", length = 32)
	private String attrId = UUID.randomUUID().toString().replaceAll("-", "");	

	@Column(name = "TASK_ID", length = 32)
	private String taskId;

	@Column(name = "ATTR_TEMP_ID", length = 32)
	private String clId;

	@Column(name = "INS_VALUE", length = 1000)
	private String value;



	public String getClId() {
		return clId;
	}



	public void setClId(String clId) {
		this.clId = clId;
	}



	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getAttrId() {
		return attrId;
	}



	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Object getPrimaryKey() {
		return clId;
	}

	@Override
	public String toString() {
		return "CustomAttrInstancesDo [attrId=" + attrId + ", taskId=" + taskId + ", clId=" + clId + ", value="
				+ value + "]";
	}




}
