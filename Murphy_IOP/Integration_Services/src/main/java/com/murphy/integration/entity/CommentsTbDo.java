package com.murphy.integration.entity;

public class CommentsTbDo {


	private int referenceMerrickItem;
	private int referenceMerrickType;
	private String originalDateEntered;
	private String originalTimeEntered;
	private int commentType;
	private String commentPurpose;
	private String commentGeneral;
	private int rioCommentCode;
	private String rioProdCommentCode;
	private int priorityType;
	private int messageSendFlag;
	private int destinationPerson;
	private int tempInteger;
	private int lastTransmission;
	private String lastLoadDate;
	private String lastLoadTime;
	private int transmitFlag;
    private String dateTimeStamp;
    private String userDateStamp;
    private String userTimeStamp;
    private int userID;
    private String rowUID;
    private int commentServiceID;
   
	public int getReferenceMerrickItem() {
		return referenceMerrickItem;
	}
	public void setReferenceMerrickItem(int referenceMerrickItem) {
		this.referenceMerrickItem = referenceMerrickItem;
	}
	public int getReferenceMerrickType() {
		return referenceMerrickType;
	}
	public void setReferenceMerrickType(int referenceMerrickType) {
		this.referenceMerrickType = referenceMerrickType;
	}
	public String getOriginalDateEntered() {
		return originalDateEntered;
	}
	public void setOriginalDateEntered(String originalDateEntered) {
		this.originalDateEntered = originalDateEntered;
	}
	public String getOriginalTimeEntered() {
		return originalTimeEntered;
	}
	public void setOriginalTimeEntered(String originalTimeEntered) {
		this.originalTimeEntered = originalTimeEntered;
	}
	public int getCommentType() {
		return commentType;
	}
	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}
	public String getCommentPurpose() {
		return commentPurpose;
	}
	public void setCommentPurpose(String commentPurpose) {
		this.commentPurpose = commentPurpose;
	}
	public String getCommentGeneral() {
		return commentGeneral;
	}
	public void setCommentGeneral(String commentGeneral) {
		this.commentGeneral = commentGeneral;
	}
	public int getRioCommentCode() {
		return rioCommentCode;
	}
	public void setRioCommentCode(int rioCommentCode) {
		this.rioCommentCode = rioCommentCode;
	}
	public String getRioProdCommentCode() {
		return rioProdCommentCode;
	}
	public void setRioProdCommentCode(String rioProdCommentCode) {
		this.rioProdCommentCode = rioProdCommentCode;
	}
	public int getPriorityType() {
		return priorityType;
	}
	public void setPriorityType(int priorityType) {
		this.priorityType = priorityType;
	}
	public int getMessageSendFlag() {
		return messageSendFlag;
	}
	public void setMessageSendFlag(int messageSendFlag) {
		this.messageSendFlag = messageSendFlag;
	}
	public int getDestinationPerson() {
		return destinationPerson;
	}
	public void setDestinationPerson(int destinationPerson) {
		this.destinationPerson = destinationPerson;
	}
	public int getTempInteger() {
		return tempInteger;
	}
	public void setTempInteger(int tempInteger) {
		this.tempInteger = tempInteger;
	}
	public int getLastTransmission() {
		return lastTransmission;
	}
	public void setLastTransmission(int lastTransmission) {
		this.lastTransmission = lastTransmission;
	}
	public String getLastLoadDate() {
		return lastLoadDate;
	}
	public void setLastLoadDate(String lastLoadDate) {
		this.lastLoadDate = lastLoadDate;
	}
	public String getLastLoadTime() {
		return lastLoadTime;
	}
	public void setLastLoadTime(String lastLoadTime) {
		this.lastLoadTime = lastLoadTime;
	}
	public int getTransmitFlag() {
		return transmitFlag;
	}
	public void setTransmitFlag(int transmitFlag) {
		this.transmitFlag = transmitFlag;
	}
	public String getDateTimeStamp() {
		return dateTimeStamp;
	}
	public void setDateTimeStamp(String dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}
	public String getUserDateStamp() {
		return userDateStamp;
	}
	public void setUserDateStamp(String userDateStamp) {
		this.userDateStamp = userDateStamp;
	}
	public String getUserTimeStamp() {
		return userTimeStamp;
	}
	public void setUserTimeStamp(String userTimeStamp) {
		this.userTimeStamp = userTimeStamp;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getRowUID() {
		return rowUID;
	}
	public void setRowUID(String rowUID) {
		this.rowUID = rowUID;
	}
	public int getCommentServiceID() {
		return commentServiceID;
	}
	public void setCommentServiceID(int commentServiceID) {
		this.commentServiceID = commentServiceID;
	}

	@Override
	public String toString() {
		return "CommentsTbDo [referenceMerrickItem=" + referenceMerrickItem + ", referenceMerrickType="
				+ referenceMerrickType + ", originalDateEntered=" + originalDateEntered + ", originalTimeEntered="
				+ originalTimeEntered + ", commentType=" + commentType + ", commentPurpose=" + commentPurpose
				+ ", commentGeneral=" + commentGeneral + ", rioCommentCode=" + rioCommentCode + ", rioProdCommentCode="
				+ rioProdCommentCode + ", priorityType=" + priorityType + ", messageSendFlag=" + messageSendFlag
				+ ", destinationPerson=" + destinationPerson + ", tempInteger=" + tempInteger + ", lastTransmission="
				+ lastTransmission + ", lastLoadDate=" + lastLoadDate + ", lastLoadTime=" + lastLoadTime
				+ ", transmitFlag=" + transmitFlag + ", dateTimeStamp=" + dateTimeStamp + ", userDateStamp="
				+ userDateStamp + ", userTimeStamp=" + userTimeStamp + ", userID=" + userID + ", rowUID=" + rowUID
				+ ", commentServiceID=" + commentServiceID + "]";
	}
	
   
}
