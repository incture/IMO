package com.murphy.taskmgmt.ita;

import com.fasterxml.jackson.databind.JsonNode;

public interface RuleOutputDto {
	public RuleOutputDto convertFromJSonNode(JsonNode node);
}
