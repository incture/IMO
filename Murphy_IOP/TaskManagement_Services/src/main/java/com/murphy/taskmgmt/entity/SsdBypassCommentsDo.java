/**
 * 
 */
package com.murphy.taskmgmt.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Kamlesh.Choubey
 *
 */

@Entity
@Table(name = "SSD_BYPASS_COMMENTS")
public class SsdBypassCommentsDo implements BaseDo {

	@Id
	@Column(name = "COMMENT_ID", length = 100)
	private String commentId = UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "SSD_BYPASS_ID", length = 100)
	private String ssdBypassId;

	@Column(name = "COMMENT", length = 1000)
	private String comment;

	@Column(name = "UPDATED_BY", length = 100)
	private String updatedBy;

	@Column(name = "UPDATED_AT", length = 100)
	private Date updatedAt = new Date();

	/**
	 * @return the commentId
	 */
	public String getCommentId() {
		return commentId;
	}

	/**
	 * @param commentId
	 *            the commentId to set
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
	 * @param ssdBypassId
	 *            the ssdBypassId to set
	 */
	public void setSsdBypassId(String ssdBypassId) {
		this.ssdBypassId = ssdBypassId;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
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
	 * @param updatedBy
	 *            the updatedBy to set
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
	 * @param updatedAt
	 *            the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SsdBypassCommentsDo [commentId=" + commentId + ", ssdBypassId=" + ", comment=" + comment
				+ ", updatedBy=" + updatedBy + ", updatedAt=" + updatedAt + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
