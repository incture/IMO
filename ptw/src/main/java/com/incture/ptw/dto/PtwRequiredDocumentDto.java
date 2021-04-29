package com.incture.ptw.dto;

public class PtwRequiredDocumentDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCWP;
	private Integer isHWP;
	private Integer isCSE;
	private Integer atmosphericTestRecord;
	private Integer loto;
	private Integer procedure;
	private Integer pAndIdOrDrawing;
	private String certificate;
	private Integer temporaryDefeat;
	private Integer rescuePlan;
	private Integer sds;
	private String otherWorkPermitDocs;
	private Integer fireWatchChecklist;
	private Integer liftPlan;
	private Integer simopDeviation;
	private Integer safeWorkPractice;
	public PtwRequiredDocumentDto() {
		super();
	}
	public PtwRequiredDocumentDto(Integer serialNo, Integer permitNumber, Integer isCWP, Integer isHWP, Integer isCSE,
			Integer atmosphericTestRecord, Integer loto, Integer procedure, Integer pAndIdOrDrawing, String certificate,
			Integer temporaryDefeat, Integer rescuePlan, Integer sds, String otherWorkPermitDocs,
			Integer fireWatchChecklist, Integer liftPlan, Integer simopDeviation, Integer safeWorkPractice) {
		super();
		this.serialNo = serialNo;
		this.permitNumber = permitNumber;
		this.isCWP = isCWP;
		this.isHWP = isHWP;
		this.isCSE = isCSE;
		this.atmosphericTestRecord = atmosphericTestRecord;
		this.loto = loto;
		this.procedure = procedure;
		this.pAndIdOrDrawing = pAndIdOrDrawing;
		this.certificate = certificate;
		this.temporaryDefeat = temporaryDefeat;
		this.rescuePlan = rescuePlan;
		this.sds = sds;
		this.otherWorkPermitDocs = otherWorkPermitDocs;
		this.fireWatchChecklist = fireWatchChecklist;
		this.liftPlan = liftPlan;
		this.simopDeviation = simopDeviation;
		this.safeWorkPractice = safeWorkPractice;
	}
	@Override
	public String toString() {
		return "PtwRequiredDocumentDto [serialNo=" + serialNo + ", permitNumber=" + permitNumber + ", isCWP=" + isCWP
				+ ", isHWP=" + isHWP + ", isCSE=" + isCSE + ", atmosphericTestRecord=" + atmosphericTestRecord
				+ ", loto=" + loto + ", procedure=" + procedure + ", pAndIdOrDrawing=" + pAndIdOrDrawing
				+ ", certificate=" + certificate + ", temporaryDefeat=" + temporaryDefeat + ", rescuePlan=" + rescuePlan
				+ ", sds=" + sds + ", otherWorkPermitDocs=" + otherWorkPermitDocs + ", fireWatchChecklist="
				+ fireWatchChecklist + ", liftPlan=" + liftPlan + ", simopDeviation=" + simopDeviation
				+ ", safeWorkPractice=" + safeWorkPractice + "]";
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public Integer getPermitNumber() {
		return permitNumber;
	}
	public void setPermitNumber(Integer permitNumber) {
		this.permitNumber = permitNumber;
	}
	public Integer getIsCWP() {
		return isCWP;
	}
	public void setIsCWP(Integer isCWP) {
		this.isCWP = isCWP;
	}
	public Integer getIsHWP() {
		return isHWP;
	}
	public void setIsHWP(Integer isHWP) {
		this.isHWP = isHWP;
	}
	public Integer getIsCSE() {
		return isCSE;
	}
	public void setIsCSE(Integer isCSE) {
		this.isCSE = isCSE;
	}
	public Integer getAtmosphericTestRecord() {
		return atmosphericTestRecord;
	}
	public void setAtmosphericTestRecord(Integer atmosphericTestRecord) {
		this.atmosphericTestRecord = atmosphericTestRecord;
	}
	public Integer getLoto() {
		return loto;
	}
	public void setLoto(Integer loto) {
		this.loto = loto;
	}
	public Integer getProcedure() {
		return procedure;
	}
	public void setProcedure(Integer procedure) {
		this.procedure = procedure;
	}
	public Integer getpAndIdOrDrawing() {
		return pAndIdOrDrawing;
	}
	public void setpAndIdOrDrawing(Integer pAndIdOrDrawing) {
		this.pAndIdOrDrawing = pAndIdOrDrawing;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public Integer getTemporaryDefeat() {
		return temporaryDefeat;
	}
	public void setTemporaryDefeat(Integer temporaryDefeat) {
		this.temporaryDefeat = temporaryDefeat;
	}
	public Integer getRescuePlan() {
		return rescuePlan;
	}
	public void setRescuePlan(Integer rescuePlan) {
		this.rescuePlan = rescuePlan;
	}
	public Integer getSds() {
		return sds;
	}
	public void setSds(Integer sds) {
		this.sds = sds;
	}
	public String getOtherWorkPermitDocs() {
		return otherWorkPermitDocs;
	}
	public void setOtherWorkPermitDocs(String otherWorkPermitDocs) {
		this.otherWorkPermitDocs = otherWorkPermitDocs;
	}
	public Integer getFireWatchChecklist() {
		return fireWatchChecklist;
	}
	public void setFireWatchChecklist(Integer fireWatchChecklist) {
		this.fireWatchChecklist = fireWatchChecklist;
	}
	public Integer getLiftPlan() {
		return liftPlan;
	}
	public void setLiftPlan(Integer liftPlan) {
		this.liftPlan = liftPlan;
	}
	public Integer getSimopDeviation() {
		return simopDeviation;
	}
	public void setSimopDeviation(Integer simopDeviation) {
		this.simopDeviation = simopDeviation;
	}
	public Integer getSafeWorkPractice() {
		return safeWorkPractice;
	}
	public void setSafeWorkPractice(Integer safeWorkPractice) {
		this.safeWorkPractice = safeWorkPractice;
	}
	
}
