package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class SsdBypassCommentDto extends BaseDto{
	
	private String commentId;
	private String ssdBypassId;
	private String comment;
	private String updatedBy;
	private Date updatedAt;
	/**
	 * @return the commentId
	 */
	public String getCommentId() {
		return commentId;
	}
	/**
	 * @param commentId the commentId to set
	 */
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	
	
	 
	
	/**
	 * @return the ssdBypassId
	 */
	public String getSsdBypassId() {
		return ssdBypassId;
	}
	/**
	 * @param ssdBypassId the ssdBypassId to set
	 */
	public void setSsdBypassId(String ssdBypassId) {
		this.ssdBypassId = ssdBypassId;
	}
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}
	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SsdBypassCommentDto [commentId=" + commentId + ", ssdBypassId=" + ssdBypassId + ", comment=" + comment
				+ ", updatedBy=" + updatedBy + ", updatedAt=" + updatedAt + "]";
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
	
	

}
