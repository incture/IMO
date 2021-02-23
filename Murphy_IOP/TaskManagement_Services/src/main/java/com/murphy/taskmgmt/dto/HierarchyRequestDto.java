package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class HierarchyRequestDto extends BaseDto{

	private String locationType;
	private String navigate;
	private String location;
	private String role;
	private String forFacility;
	
	
	public String getForFacility() {
		return forFacility;
	}

	public void setForFacility(String forFacility) {
		this.forFacility = forFacility;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getNavigate() {
		return navigate;
	}

	public void setNavigate(String navigate) {
		this.navigate = navigate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "HierarchyRequestDto [locationType=" + locationType + ", navigate=" + navigate + ", location=" + location
				+ ", role=" + role + ", forFacility=" + forFacility + "]";
	}

}
