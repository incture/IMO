package com.incture.ptw.dto;

public class JsaHazardsToolsDto {
	private Integer permitNumber;
	private Integer EquipmentAndTools;
	private Integer inspectEquipmentTool;
	private Integer brassToolsNecessary;
	private Integer useProtectiveGuards;
	private Integer useCorrectTools;
	private Integer checkForSharpEdges;
	private Integer applyHandSafetyPrinciple;

	public JsaHazardsToolsDto() {
		super();
	}

	public JsaHazardsToolsDto(Integer permitNumber, Integer equipmentAndTools, Integer inspectEquipmentTool,
			Integer brassToolsNecessary, Integer useProtectiveGuards, Integer useCorrectTools,
			Integer checkForSharpEdges, Integer applyHandSafetyPrinciple) {
		super();
		this.permitNumber = permitNumber;
		EquipmentAndTools = equipmentAndTools;
		this.inspectEquipmentTool = inspectEquipmentTool;
		this.brassToolsNecessary = brassToolsNecessary;
		this.useProtectiveGuards = useProtectiveGuards;
		this.useCorrectTools = useCorrectTools;
		this.checkForSharpEdges = checkForSharpEdges;
		this.applyHandSafetyPrinciple = applyHandSafetyPrinciple;
	}

	@Override
	public String toString() {
		return "JsaHazardsToolsDto [permitNumber=" + permitNumber + ", EquipmentAndTools=" + EquipmentAndTools
				+ ", inspectEquipmentTool=" + inspectEquipmentTool + ", brassToolsNecessary=" + brassToolsNecessary
				+ ", useProtectiveGuards=" + useProtectiveGuards + ", useCorrectTools=" + useCorrectTools
				+ ", checkForSharpEdges=" + checkForSharpEdges + ", applyHandSafetyPrinciple="
				+ applyHandSafetyPrinciple + "]";
	}

	public Integer getPermitNumber() {
		return permitNumber;
	}

	public void setPermitNumber(Integer permitNumber) {
		this.permitNumber = permitNumber;
	}

	public Integer getEquipmentAndTools() {
		return EquipmentAndTools;
	}

	public void setEquipmentAndTools(Integer equipmentAndTools) {
		EquipmentAndTools = equipmentAndTools;
	}

	public Integer getInspectEquipmentTool() {
		return inspectEquipmentTool;
	}

	public void setInspectEquipmentTool(Integer inspectEquipmentTool) {
		this.inspectEquipmentTool = inspectEquipmentTool;
	}

	public Integer getBrassToolsNecessary() {
		return brassToolsNecessary;
	}

	public void setBrassToolsNecessary(Integer brassToolsNecessary) {
		this.brassToolsNecessary = brassToolsNecessary;
	}

	public Integer getUseProtectiveGuards() {
		return useProtectiveGuards;
	}

	public void setUseProtectiveGuards(Integer useProtectiveGuards) {
		this.useProtectiveGuards = useProtectiveGuards;
	}

	public Integer getUseCorrectTools() {
		return useCorrectTools;
	}

	public void setUseCorrectTools(Integer useCorrectTools) {
		this.useCorrectTools = useCorrectTools;
	}

	public Integer getCheckForSharpEdges() {
		return checkForSharpEdges;
	}

	public void setCheckForSharpEdges(Integer checkForSharpEdges) {
		this.checkForSharpEdges = checkForSharpEdges;
	}

	public Integer getApplyHandSafetyPrinciple() {
		return applyHandSafetyPrinciple;
	}

	public void setApplyHandSafetyPrinciple(Integer applyHandSafetyPrinciple) {
		this.applyHandSafetyPrinciple = applyHandSafetyPrinciple;
	}

}
