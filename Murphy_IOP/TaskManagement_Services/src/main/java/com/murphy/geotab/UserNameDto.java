package com.murphy.geotab;

/**
 * @author INC00718
 *
 */
public class UserNameDto {
	
//	private String name;
	private String firstName;
	private String lastName;
	private String truckId;
	private String fullName;
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the truckId
	 */
	public String getTruckId() {
		return truckId;
	}
	/**
	 * @param truckId the truckId to set
	 */
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserNameDto [firstName=" + firstName + ", lastName=" + lastName + ", truckId=" + truckId + ", fullName="
				+ fullName + "]";
	}
	
}
