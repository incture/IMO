package com.murphy.taskmgmt.dto;

public class ObxOperatorWorkloadDetailsDto {

	private String obxOperatorfullName;
	private Double workLoad;
	private String clusterId;
	
	public ObxOperatorWorkloadDetailsDto(){
	}
	
	public ObxOperatorWorkloadDetailsDto(String obxOperatorfullName){
		this.workLoad=0.0;
		this.obxOperatorfullName=obxOperatorfullName;
	}
	/**
	 * @return the workLoad
	 */
	public Double getWorkLoad() {
		return workLoad;
	}
	/**
	 * @param workLoad the workLoad to set
	 */
	public void setWorkLoad(Double workLoad) {
		this.workLoad = workLoad;
	}
	/**
	 * @return the obxOperatorfullName
	 */
	public String getObxOperatorfullName() {
		return obxOperatorfullName;
	}
	/**
	 * @param obxOperatorfullName the obxOperatorfullName to set
	 */
	public void setObxOperatorfullName(String obxOperatorfullName) {
		this.obxOperatorfullName = obxOperatorfullName;
	}
	/**
	 * @return the clusterId
	 */
	public String getClusterId() {
		return clusterId;
	}
	/**
	 * @param clusterId the clusterId to set
	 */
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	
}
