package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_GEOTAB_AUTO_SIGNIN")
public class AutoSignInDo {
	
	public AutoSignInDo() {
		this.autoSignInDoPK = new AutoSignInIdDoPK();
	}

	@EmbeddedId
	private AutoSignInIdDoPK autoSignInDoPK;
	
//	@Column(name = "SIGNIN_ID", length = 100)
//	private String signInId = UUID.randomUUID().toString().replaceAll("-", "");

//	@Column(name = "DRIVER_NAME", length = 200)
//	private String driverName;

	@Column(name = "DRIVER_LAT")
	private Double driverLat;

	@Column(name = "DRIVER_LON")
	private Double driverLon;

	@Column(name = "WELL_NAME", length = 200)
	private String wellName;

	@Column(name = "WELL_LAT")
	private Double wellLat;

	@Column(name = "WELL_LON")
	private Double wellLon;

	@Column(name = "SIGNIN_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date signInEnd;

	@Column(name = "TIME_SIGNEDIN", length = 100)
	private String timeSignedIn;

	@Column(name = "CROW_FLY_DISTANCE")
	private Double crowFlyDistance;

	@Column(name = "ROAD_DISTANCE")
	private Double roadDistance;
	
	@Column(name = "SIGNIN_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date signInStart;

//	@Column(name = "DRIVER_IN_FIELD")
//	private Boolean driverInField;

//	public String getSignInId() {
//		return signInId;
//	}
//
//	public void setSignInId(String signInId) {
//		this.signInId = signInId;
//	}

//	public String getDriverName() {
//		return driverName;
//	}
//
//	public void setDriverName(String driverName) {
//		this.driverName = driverName;
//	}
	
	public Date getSignInStart() {
		return signInStart;
	}

	public void setSignInStart(Date signInStart) {
		this.signInStart = signInStart;
	}

	public Double getDriverLat() {
		return driverLat;
	}

	public void setDriverLat(Double driverLat) {
		this.driverLat = driverLat;
	}

	public Double getDriverLon() {
		return driverLon;
	}

	public void setDriverLon(Double driverLon) {
		this.driverLon = driverLon;
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public Double getWellLat() {
		return wellLat;
	}

	public void setWellLat(Double wellLat) {
		this.wellLat = wellLat;
	}

	public Double getWellLon() {
		return wellLon;
	}

	public void setWellLon(Double wellLon) {
		this.wellLon = wellLon;
	}

	public Date getSignInEnd() {
		return signInEnd;
	}

	public void setSignInEnd(Date signInEnd) {
		this.signInEnd = signInEnd;
	}

	public String getTimeSignedIn() {
		return timeSignedIn;
	}

	public void setTimeSignedIn(String timeSignedIn) {
		this.timeSignedIn = timeSignedIn;
	}

	public Double getCrowFlyDistance() {
		return crowFlyDistance;
	}

	public void setCrowFlyDistance(Double crowFlyDistance) {
		this.crowFlyDistance = crowFlyDistance;
	}

	public Double getRoadDistance() {
		return roadDistance;
	}

	public void setRoadDistance(Double roadDistance) {
		this.roadDistance = roadDistance;
	}

//	public Boolean getDriverInField() {
//		return driverInField;
//	}
//
//	public void setDriverInField(Boolean driverInField) {
//		this.driverInField = driverInField;
//	}

	public AutoSignInIdDoPK getAutoSignInDoPK() {
		return autoSignInDoPK;
	}

	public void setAutoSignInDoPK(AutoSignInIdDoPK autoSignInDoPK) {
		this.autoSignInDoPK = autoSignInDoPK;
	}

	@Override
	public String toString() {
		return "AutoSignInDo [autoSignInDoPK=" + autoSignInDoPK + ", driverLat=" + driverLat + ", driverLon="
				+ driverLon + ", wellName=" + wellName + ", wellLat=" + wellLat + ", wellLon=" + wellLon
				+ ", signInEnd=" + signInEnd + ", timeSignedIn=" + timeSignedIn + ", crowFlyDistance=" + crowFlyDistance
				+ ", roadDistance=" + roadDistance + ", signInStart=" + signInStart + "]";
	}

}
