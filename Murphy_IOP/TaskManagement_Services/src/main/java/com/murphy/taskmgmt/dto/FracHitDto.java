package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class FracHitDto extends BaseDto{

	private long fracId;
	private String muwi;
	private Date fracHitTime;
	private String wellStatus;
	

	public String getWellStatus() {
		return wellStatus;
	}

	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
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

	public Date getFracHitTime() {
		return fracHitTime;
	}

	public void setFracHitTime(Date fracHitTime) {
		this.fracHitTime = fracHitTime;
	}

	@Override
	public String toString() {
		return "FracHitDo [fracId=" + fracId + ", muwi=" + muwi + ", fracHitTime=" + fracHitTime + "]";
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
