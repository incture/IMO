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
@Table(name = "FRAC_HIT_TABLE")
public class FracHitDo implements BaseDo,Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7318165884730641958L;
	@Id
	@Column(name = "FRAC_ID", length = 100)
	private long fracId;
    @Id
	@Column(name = "MUWI", length = 70)
	private String muwi;
    @Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FRAC_HIT_TIME")
	private Date fracHitTime;

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
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
	
}
