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

public class ITARuleServiceForWaterOil extends RuleService {


	private static final Logger logger = LoggerFactory.getLogger(ITARuleServiceForWaterOil.class);

	@Override
	public RuleOutputDto getSingleResult(RuleInputDto input) throws ClientProtocolException, IOException {

		JsonNode node = execute(input, MurphyConstant.ITAWaterOilRulesServiceId);
		if (node.isArray() && node.size() > 1)
			throw new UnsupportedOperationException("The requested search returns multiple results");
		ITAWaterOilActionDto response = new ITAWaterOilActionDto();
		if (node.isArray())
			return response.convertFromJSonNode(node.get(0));

		return response.convertFromJSonNode(node);

	}

	@Override
	public List<?> getResultList(RuleInputDto input) throws ClientProtocolException, IOException {

		JsonNode node = execute(input, MurphyConstant.ITAWaterOilRulesServiceId);
		List<ITAWaterOilActionDto> responseList = new ArrayList<>();
		if (!ServicesUtil.isEmpty(node)) {
			logger.error("[ITARuleServiceForWaterOil][getResultList] " + node);

			if (node.isArray()) {
				logger.error("[ITARuleServiceForWaterOil][getResultList] node.isArray() " + node.isArray());
				for (final JsonNode objNode : node) {
					responseList.add(new ITAWaterOilActionDto().convertFromJSonNode(objNode));
				}
			} else {
				responseList.add(new ITAWaterOilActionDto().convertFromJSonNode(node));
			}
		}
		return responseList;

	}

}
