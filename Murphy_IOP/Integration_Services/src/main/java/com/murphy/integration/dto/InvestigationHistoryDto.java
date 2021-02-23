package com.murphy.integration.dto;

import java.time.Instant;


public class InvestigationHistoryDto implements Comparable<InvestigationHistoryDto>{
	
	private String shutInDate;
	private String shutInReason;
	private String rtpDate;
	private String rtpComment;
	private String pwrDate;
	private String suspectedFailure;
	private String jobCompleteionDate;
	private String jobType;
	private String jobSummary;
	private String productionPeriod;
	private String source;
	private String jobStartDate;
	private String jobSubtype;
	private String globalSummary;
	private String operationsSummary;
	private String  twentyFourhrForecast;
	private String remarks;
	
	public String getGlobalSummary() {
		return globalSummary;
	}
	public void setGlobalSummary(String globalSummary) {
		this.globalSummary = globalSummary;
	}
	public String getOperationsSummary() {
		return operationsSummary;
	}
	public void setOperationsSummary(String operationsSummary) {
		this.operationsSummary = operationsSummary;
	}
	public String getTwentyFourhrForecast() {
		return twentyFourhrForecast;
	}
	public void setTwentyFourhrForecast(String twentyFourhrForecast) {
		this.twentyFourhrForecast = twentyFourhrForecast;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getShutInDate() {
		return shutInDate;
	}
	public void setShutInDate(String shutInDate) {
		this.shutInDate = shutInDate;
	}
	public String getShutInReason() {
		return shutInReason;
	}
	public void setShutInReason(String shutInReason) {
		this.shutInReason = shutInReason;
	}
	public String getRtpDate() {
		return rtpDate;
	}
	public void setRtpDate(String rtpDate) {
		this.rtpDate = rtpDate;
	}
	public String getRtpComment() {
		return rtpComment;
	}
	public void setRtpComment(String rtpComment) {
		this.rtpComment = rtpComment;
	}
	public String getPwrDate() {
		return pwrDate;
	}
	public void setPwrDate(String pwrDate) {
		this.pwrDate = pwrDate;
	}
	public String getSuspectedFailure() {
		return suspectedFailure;
	}
	public void setSuspectedFailure(String suspectedFailure) {
		this.suspectedFailure = suspectedFailure;
	}
	public String getJobCompleteionDate() {
		return jobCompleteionDate;
	}
	public void setJobCompleteionDate(String jobCompleteionDate) {
		this.jobCompleteionDate = jobCompleteionDate;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJobSummary() {
		return jobSummary;
	}
	public void setJobSummary(String jobSummary) {
		this.jobSummary = jobSummary;
	}
	public String getProductionPeriod() {
		return productionPeriod;
	}
	public void setProductionPeriod(String productionPeriod) {
		this.productionPeriod = productionPeriod;
	}

	public String getJobStartDate() {
		return jobStartDate;
	}
	public void setJobStartDate(String jobStartDate) {
		this.jobStartDate = jobStartDate;
	}
	public String getJobSubtype() {
		return jobSubtype;
	}
	public void setJobSubtype(String jobSubtype) {
		this.jobSubtype = jobSubtype;
	}
	
	@Override
	public String toString() {
		return "InvestigationHistoryDto [shutInDate=" + shutInDate + ", shutInReason=" + shutInReason + ", rtpDate="
				+ rtpDate + ", rtpComment=" + rtpComment + ", pwrDate=" + pwrDate + ", suspectedFailure="
				+ suspectedFailure + ", jobCompleteionDate=" + jobCompleteionDate + ", jobType=" + jobType
				+ ", jobSummary=" + jobSummary + ", productionPeriod=" + productionPeriod + ", source=" + source
				+ ", jobStartDate=" + jobStartDate + ", jobSubtype=" + jobSubtype + ", globalSummary=" + globalSummary
				+ ", operationsSummary=" + operationsSummary + ", twentyFourhrForecast=" + twentyFourhrForecast
				+ ", remarks=" + remarks + "]";
	}
	@Override
	public int compareTo(InvestigationHistoryDto o) {
		Instant d1 = null;
		Instant d2 = null;
		int value = 0;
		if(o.jobCompleteionDate==null && this.jobCompleteionDate==null){
			value=0;
		}
		else if(o.jobCompleteionDate==null){
			value =  -1;
		}
		else if(this.jobCompleteionDate==null)
		{
			value= 1;
		}
		else
		{
		d1 = Instant.ofEpochMilli(Long.parseLong(o.jobCompleteionDate));
		d2 = Instant.ofEpochMilli(Long.parseLong(this.jobCompleteionDate));
		value = d1.compareTo(d2); 
		}
		return value;
	}
	
}
