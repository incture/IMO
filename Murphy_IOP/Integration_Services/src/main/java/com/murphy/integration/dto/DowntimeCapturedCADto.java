package com.murphy.integration.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DowntimeCapturedCADto{

	private String muwi;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private String sysCreateDate;
	private int durationInHours;
	private int durationInMinutes;
	private String idFlownet;
	private String idecParent;
	private String idrec;
	private String typDownTime;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private String downtmeStart;
	private Float durationDownTmeStartDay;
	private String codeDownTime1;
	private String codeDownTime2;
	private String codeDownTime3;
	private String downtmeEnd;
	private String durationDownEndDay;
	private Float durationDownCalc;
	private String downTmePlannedEnd;
	private String durationDownPlanEnd;
	private String comments;
	private int sysLockMeUi;
	private int sysLockChildrenUi;
	private int sysLockMe;
	private int sysLockChildren;
	private String sysLockDate;
	private String sysModDate;
	private String sysModUser;
	private String sysCreateUser;
	private String sysTag;
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	public String getSysCreateDate() {
		return sysCreateDate;
	}
	public void setSysCreateDate(String sysCreateDate) {
		this.sysCreateDate = sysCreateDate;
	}
	public int getDurationInHours() {
		return durationInHours;
	}
	public void setDurationInHours(int durationInHours) {
		this.durationInHours = durationInHours;
	}
	public int getDurationInMinutes() {
		return durationInMinutes;
	}
	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	public String getIdFlownet() {
		return idFlownet;
	}
	public void setIdFlownet(String idFlownet) {
		this.idFlownet = idFlownet;
	}
	public String getIdecParent() {
		return idecParent;
	}
	public void setIdecParent(String idecParent) {
		this.idecParent = idecParent;
	}
	public String getIdrec() {
		return idrec;
	}
	public void setIdrec(String idrec) {
		this.idrec = idrec;
	}
	public String getTypDownTime() {
		return typDownTime;
	}
	public void setTypDownTime(String typDownTime) {
		this.typDownTime = typDownTime;
	}
	public String getDowntmeStart() {
		return downtmeStart;
	}
	public void setDowntmeStart(String downtmeStart) {
		this.downtmeStart = downtmeStart;
	}
	public Float getDurationDownTmeStartDay() {
		return durationDownTmeStartDay;
	}
	public void setDurationDownTmeStartDay(Float durationDownTmeStartDay) {
		this.durationDownTmeStartDay = durationDownTmeStartDay;
	}
	public String getCodeDownTime1() {
		return codeDownTime1;
	}
	public void setCodeDownTime1(String codeDownTime1) {
		this.codeDownTime1 = codeDownTime1;
	}
	public String getCodeDownTime2() {
		return codeDownTime2;
	}
	public void setCodeDownTime2(String codeDownTime2) {
		this.codeDownTime2 = codeDownTime2;
	}
	public String getCodeDownTime3() {
		return codeDownTime3;
	}
	public void setCodeDownTime3(String codeDownTime3) {
		this.codeDownTime3 = codeDownTime3;
	}
	public String getDowntmeEnd() {
		return downtmeEnd;
	}
	public void setDowntmeEnd(String downtmeEnd) {
		this.downtmeEnd = downtmeEnd;
	}
	public String getDurationDownEndDay() {
		return durationDownEndDay;
	}
	public void setDurationDownEndDay(String durationDownEndDay) {
		this.durationDownEndDay = durationDownEndDay;
	}
	public Float getDurationDownCalc() {
		return durationDownCalc;
	}
	public void setDurationDownCalc(Float durationDownCalc) {
		this.durationDownCalc = durationDownCalc;
	}
	public String getDownTmePlannedEnd() {
		return downTmePlannedEnd;
	}
	public void setDownTmePlannedEnd(String downTmePlannedEnd) {
		this.downTmePlannedEnd = downTmePlannedEnd;
	}
	public String getDurationDownPlanEnd() {
		return durationDownPlanEnd;
	}
	public void setDurationDownPlanEnd(String durationDownPlanEnd) {
		this.durationDownPlanEnd = durationDownPlanEnd;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getSysLockMeUi() {
		return sysLockMeUi;
	}
	public void setSysLockMeUi(int sysLockMeUi) {
		this.sysLockMeUi = sysLockMeUi;
	}
	public int getSysLockChildrenUi() {
		return sysLockChildrenUi;
	}
	public void setSysLockChildrenUi(int sysLockChildrenUi) {
		this.sysLockChildrenUi = sysLockChildrenUi;
	}
	public int getSysLockMe() {
		return sysLockMe;
	}
	public void setSysLockMe(int sysLockMe) {
		this.sysLockMe = sysLockMe;
	}
	public int getSysLockChildren() {
		return sysLockChildren;
	}
	public void setSysLockChildren(int sysLockChildren) {
		this.sysLockChildren = sysLockChildren;
	}
	public String getSysLockDate() {
		return sysLockDate;
	}
	public void setSysLockDate(String sysLockDate) {
		this.sysLockDate = sysLockDate;
	}
	public String getSysModDate() {
		return sysModDate;
	}
	public void setSysModDate(String sysModDate) {
		this.sysModDate = sysModDate;
	}
	public String getSysModUser() {
		return sysModUser;
	}
	public void setSysModUser(String sysModUser) {
		this.sysModUser = sysModUser;
	}
	public String getSysCreateUser() {
		return sysCreateUser;
	}
	public void setSysCreateUser(String sysCreateUser) {
		this.sysCreateUser = sysCreateUser;
	}
	public String getSysTag() {
		return sysTag;
	}
	public void setSysTag(String sysTag) {
		this.sysTag = sysTag;
	}
	@Override
	public String toString() {
		return "DowntimeCapturedCADto [muwi=" + muwi + ", sysCreateDate=" + sysCreateDate + ", durationInHours="
				+ durationInHours + ", durationInMinutes=" + durationInMinutes + ", idFlownet=" + idFlownet
				+ ", idecParent=" + idecParent + ", idrec=" + idrec + ", typDownTime=" + typDownTime + ", downtmeStart="
				+ downtmeStart + ", durationDownTmeStartDay=" + durationDownTmeStartDay + ", codeDownTime1="
				+ codeDownTime1 + ", codeDownTime2=" + codeDownTime2 + ", codeDownTime3=" + codeDownTime3
				+ ", downtmeEnd=" + downtmeEnd + ", durationDownEndDay=" + durationDownEndDay + ", durationDownCalc="
				+ durationDownCalc + ", downTmePlannedEnd=" + downTmePlannedEnd + ", durationDownPlanEnd="
				+ durationDownPlanEnd + ", comments=" + comments + ", sysLockMeUi=" + sysLockMeUi
				+ ", sysLockChildrenUi=" + sysLockChildrenUi + ", sysLockMe=" + sysLockMe + ", sysLockChildren="
				+ sysLockChildren + ", sysLockDate=" + sysLockDate + ", sysModDate=" + sysModDate + ", sysModUser="
				+ sysModUser + ", sysCreateUser=" + sysCreateUser + ", sysTag=" + sysTag + "]";
	}
	
	
	
	}
