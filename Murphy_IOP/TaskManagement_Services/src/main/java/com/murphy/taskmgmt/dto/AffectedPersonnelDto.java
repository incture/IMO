package com.murphy.taskmgmt.dto;


public class AffectedPersonnelDto {

	private String name;
	private String email;
	private String signature;
	private Object signatureImage;
	private String contractorPerformingWork;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getContractorPerformingWork() {
		return contractorPerformingWork;
	}
	public void setContractorPerformingWork(String contractorPerformingWork) {
		this.contractorPerformingWork = contractorPerformingWork;
	}
	
	public Object getSignatureImage() {
		return signatureImage;
	}
	public void setSignatureImage(Object signatureImage) {
		this.signatureImage = signatureImage;
	}
	@Override
	public String toString() {
		return "AffectedPersonnel [name=" + name + ", email=" + email + ", signature=" + signature + ", signatureImage="
				+ signatureImage + ", contractorPerformingWork=" + contractorPerformingWork + "]";
	}
	
	
}
