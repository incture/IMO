package com.murphy.taskmgmt.dto;

public class CheckListItemDto {
	
	
	private String checkListItem;
	private String dataType;
	private String listId;
	private String value;
	private boolean isEditable;
	private boolean isSelected;
	
	public String getCheckListItem() {
		return checkListItem;
	}
	public void setCheckListItem(String checkListItem) {
		this.checkListItem = checkListItem;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public boolean isEditable() {
		return isEditable;
	}
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	@Override
	public String toString() {
		return "CheckListItemDto [checkListItem=" + checkListItem + ", dataType=" + dataType + "]";
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
	
}
