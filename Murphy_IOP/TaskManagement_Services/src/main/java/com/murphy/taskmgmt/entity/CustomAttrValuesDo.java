package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: CheckListOptionsDo
 *
 */
@Entity
@Table(name = "TM_ATTR_VALUES")
public class CustomAttrValuesDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public CustomAttrValuesDo() {
		super();
	}
	private static final long serialVersionUID = -7341365853980611944L;	

	@Id
	@Column(name = "VALUE_ID", length = 32)
	private String valueId = UUID.randomUUID().toString().replaceAll("-", "");	

	@Column(name = "ITEM_ID", length = 32)
	private String clItemId;

	@Column(name = "ATTR_VALUE", length = 100)
	private String value;

	@Column(name = "DEPENDENT_VALUE", length = 100)
	private String dependentValue;

	@Column(name = "EST_RESOLVE_TIME")
	private double estResolveTime;

	
	
	
	
	public String getDependentValue() {
		return dependentValue;
	}

	public void setDependentValue(String dependentValue) {
		this.dependentValue = dependentValue;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getClItemId() {
		return clItemId;
	}

	public void setClItemId(String clItemId) {
		this.clItemId = clItemId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	@Override
	public String toString() {
		return "CustomAttrValuesDo [valueId=" + valueId + ", clItemId=" + clItemId + ", value=" + value
				+ ", dependentValue=" + dependentValue + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return valueId;
	}




}
