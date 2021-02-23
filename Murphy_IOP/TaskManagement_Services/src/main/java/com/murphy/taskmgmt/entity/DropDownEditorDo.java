package com.murphy.taskmgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TM_ATTR_VALUES")
public class DropDownEditorDo implements BaseDo{
	
	@Id
	@Column(name="VALUE_ID")
	private String valueId;
	
	@Column(name="ITEM_ID")
	private String itemId;
	
	@Column(name="ATTR_VALUE")
	private String attrValue;
	
	@Column(name="DEPENDENT_VALUE")
	private String dependentValue;
	
	@Column(name="EST_RESOLVE_TIME")
	private float estResolveTime;

	@Column(name="ACTIVE_FLAG")
	private String activeFlag;
	
	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getDependentValue() {
		return dependentValue;
	}

	public void setDependentValue(String dependentValue) {
		this.dependentValue = dependentValue;
	}

	public float getEstResolveTime() {
		return estResolveTime;
	}

	public void setEstResolveTime(float estResolveTime) {
		this.estResolveTime = estResolveTime;
	}

	@Override
	public Object getPrimaryKey() {
		return valueId;
	}

	@Override
	public String toString() {
		return "DropDownEditorDo [valueId=" + valueId + ", itemId=" + itemId + ", attrValue=" + attrValue
				+ ", dependentValue=" + dependentValue + ", estResolveTime=" + estResolveTime + ", activeFlag="
				+ activeFlag + "]";
	}

	
	
	
}
