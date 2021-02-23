package com.murphy.taskmgmt.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HSE_STRINGS")
public class HseStringsDo  implements BaseDo{
	
	@Id
	@Column(name = "ID", length = 100)
	private String id = UUID.randomUUID().toString().replaceAll("-", "");
	

	@Column(name = "SEARCH_STRING" ,length = 100)
	private String searchString;	
	
	@Column(name = "STRING_COUNT")
	private int count;	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}




	
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}




	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
