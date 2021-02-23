package com.murphy.taskmgmt.ita;

public class ITAGasBlowByRulesDto implements RuleInputDto{
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	@Override
	public String toString() {
		return "ITAGasBlowByRulesDto [type=" + type + "]";
	}

	@Override
	public String toRuleInputString(String rulesServiceName) {
	return 	"{ \"RuleServiceId\" : \"" + rulesServiceName + "\", \"Vocabulary\" : [ {"
				+ "\"ITAGasBlowbyRules\" : { \"Type\":" + "\"" + this.getType() +"\""
				+ "} } ] }";
	}

}
