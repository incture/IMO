package com.murphy.taskmgmt.dto;

public class FieldOperatorAvailabilityDto {
	
	
	private String firstName;
	private String lastName;
	private String emailId;
	private String availablePercentage;
	private double availablePercent;
	private String operatorType;
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	public double getAvailablePercent() {
		return availablePercent;
	}
	public void setAvailablePercent(double availablePercent) {
		this.availablePercent = availablePercent;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getAvailablePercentage() {
		return availablePercentage;
	}
	public void setAvailablePercentage(String availablePercentage) {
		this.availablePercentage = availablePercentage;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldOperatorAvailabilityDto other = (FieldOperatorAvailabilityDto) obj;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		return true;
	}
	
}
