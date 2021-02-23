package com.murphy.taskmgmt.ita;

public class ITAWaterOilRulesDto implements RuleInputDto {
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ITATaskRulesDto [type=" + type + "]";
	}

	@Override
	public String toRuleInputString(String rulesServiceName) {
	return 	"{ \"RuleServiceId\" : \"" + rulesServiceName + "\", \"Vocabulary\" : [ {"
				+ "\"ITACarryOverRules\" : { \"Type\":" + "\"" + this.getType() +"\""
				+ "} } ] }";
	}

}
