package com.murphy.taskmgmt.ita;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

public class ITARuleServiceForGasBlowBy extends RuleService {


	private static final Logger logger = LoggerFactory.getLogger(ITARuleServiceForGasBlowBy.class);

	@Override
	public RuleOutputDto getSingleResult(RuleInputDto input) throws ClientProtocolException, IOException {

		JsonNode node = execute(input, MurphyConstant.ITAGasBlowByRulesServiceId);
		if (node.isArray() && node.size() > 1)
			throw new UnsupportedOperationException("The requested search returns multiple results");
		ITAGasBlowByActionDto response = new ITAGasBlowByActionDto();
		if (node.isArray())
			return response.convertFromJSonNode(node.get(0));

		return response.convertFromJSonNode(node);

	}

	@Override
	public List<?> getResultList(RuleInputDto input) throws ClientProtocolException, IOException {

		JsonNode node = execute(input, MurphyConstant.ITAGasBlowByRulesServiceId);
		List<ITAGasBlowByActionDto> responseList = new ArrayList<>();
		if (!ServicesUtil.isEmpty(node)) {
			logger.error("[ITARuleServiceForGasBlowBy][getResultList] " + node);

			if (node.isArray()) {
				logger.error("[ITARuleServiceForGasBlowBy][getResultList] node.isArray() " + node.isArray());
				for (final JsonNode objNode : node) {
					responseList.add(new ITAGasBlowByActionDto().convertFromJSonNode(objNode));
				}
			} else {
				responseList.add(new ITAGasBlowByActionDto().convertFromJSonNode(node));
			}
		}
		return responseList;

	}

}
