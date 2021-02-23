package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "FRAC_NOTIFICATION")
public class FracNotificationDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 7318165884730641958L;
	
	
	@Id
	@Column(name="SERIAL_ID",length=50)
	private String serialId;
	
	@Column(name="FRAC_ID", length=32)
	private long fracId;
	
	
	@Column(name = "MUWI", length = 70)
	private String muwi;
	
	@Column(name = "MAX_TUBE_PRESSURE")
	private Double maxTubePressure;
	
	@Column(name = "MAX_CASE_PRESSURE")
	private Double maxCasePressure;
	
	@Column(name = "USER_GROUP",length = 200)
	private String userGroup;
	
	@Column(name = "USER_ID",length =50)
	private String userId;
	
	@Column(name="ACKNOWLEDGED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date acknowledgedAt;
	
	@Column(name = "IS_ACKNOWLEDGED")
	private String isAcknowledged;
	
	@Column(name ="ACT_TUBE_PRESSURE")
	private Double activeTubePressure;
		
	@Column(name ="ACT_CASE_PRESSURE")
    private Double activeCasePressure;
	

	@Column(name = "WELL_STATUS",length =50)
	private String wellStatus;
	

	public String getSerialId() {
		return serialId;
	}




	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}




	public long getFracId() {
		return fracId;
	}




	public void setFracId(long fracId) {
		this.fracId = fracId;
	}




	public String getMuwi() {
		return muwi;
	}




	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}




	public Double getMaxTubePressure() {
		return maxTubePressure;
	}




	public void setMaxTubePressure(Double maxTubePressure) {
		this.maxTubePressure = maxTubePressure;
	}




	public Double getMaxCasePressure() {
		return maxCasePressure;
	}




	public void setMaxCasePressure(Double maxCasePressure) {
		this.maxCasePressure = maxCasePressure;
	}


	public String getUserGroup() {
		return userGroup;
	}




	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}




	public Date getAcknowledgedAt() {
		return acknowledgedAt;
	}




	public void setAcknowledgedAt(Date acknowledgedAt) {
		this.acknowledgedAt = acknowledgedAt;
	}




	



	public String getIsAcknowledged() {
		return isAcknowledged;
	}




	public void setIsAcknowledged(String isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}






	public Double getActiveTubePressure() {
		return activeTubePressure;
	}




	public void setActiveTubePressure(Double activeTubePressure) {
		this.activeTubePressure = activeTubePressure;
	}




	public Double getActiveCasePressure() {
		return activeCasePressure;
	}




	public void setActiveCasePressure(Double activeCasePressure) {
		this.activeCasePressure = activeCasePressure;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}




	public String getWellStatus() {
		return wellStatus;
	}




	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
	}




	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}




	public String getUserId() {
		return userId;
	}




	public void setUserId(String userId) {
		this.userId = userId;
	}




	@Override
	public String toString() {
		return "FracNotificationDo [serialId=" + serialId + ", fracId=" + fracId + ", muwi=" + muwi
				+ ", maxTubePressure=" + maxTubePressure + ", maxCasePressure=" + maxCasePressure + ", userGroup="
				+ userGroup + ", userId=" + userId + ", acknowledgedAt=" + acknowledgedAt + ", isAcknowledged="
				+ isAcknowledged + ", activeTubePressure=" + activeTubePressure + ", activeCasePressure="
				+ activeCasePressure + ", wellStatus=" + wellStatus + "]";
	}
	
	
	
	
}
