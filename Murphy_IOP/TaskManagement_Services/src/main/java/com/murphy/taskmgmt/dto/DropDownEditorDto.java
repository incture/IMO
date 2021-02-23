package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class DropDownEditorDto extends BaseDto{
	
	private String valueId;
	private String itemId;
	private String attrValue;
	private String dependentValue;
	private float estResolveTime;
	private String actionFlag;

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
	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	@Override
	public Boolean getValidForUsage() {
		
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
}

	@Override
	public String toString() {
		return "DropDownEditorDto [valueId=" + valueId + ", itemId=" + itemId + ", attrValue=" + attrValue
				+ ", dependentValue=" + dependentValue + ", estResolveTime=" + estResolveTime + ", actionFlag="
				+ actionFlag + "]";
	}



	
}
