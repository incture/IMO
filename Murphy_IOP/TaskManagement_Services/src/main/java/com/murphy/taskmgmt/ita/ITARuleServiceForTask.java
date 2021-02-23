package com.murphy.taskmgmt.ita;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;

public class ITARuleServiceForTask extends RuleService {
	
	private static final Logger logger = LoggerFactory.getLogger(ITARuleServiceForTask.class);

	@Override
	public RuleOutputDto getSingleResult(RuleInputDto input) throws ClientProtocolException, IOException {

		JsonNode node = execute(input, MurphyConstant.ITATaskRuleServiceId);
		if (node.isArray() && node.size() > 1)
			throw new UnsupportedOperationException("The requested search returns multiple results");
		ITATaskActionDto response = new ITATaskActionDto();
		if (node.isArray())
			return response.convertFromJSonNode(node.get(0));

		return response.convertFromJSonNode(node);

	}

	@Override
	public List<?> getResultList(RuleInputDto input) throws ClientProtocolException, IOException {

		JsonNode node = execute(input, MurphyConstant.ITATaskRuleServiceId);
		List<ITATaskActionDto> responseList = new ArrayList<>();
		if (!ServicesUtil.isEmpty(node)) {
			logger.error("[ITARuleServiceForTask][getResultList] " + node);

			if (node.isArray()) {
				logger.error("[ITARuleServiceForTask][getResultList] node.isArray() " + node.isArray());
				for (final JsonNode objNode : node) {
					responseList.add(new ITATaskActionDto().convertFromJSonNode(objNode));
				}
			} else {
				responseList.add(new ITATaskActionDto().convertFromJSonNode(node));
			}
		}
		return responseList;

	}

	/*public Map<String, String> getByDestination()
			throws ClientProtocolException, IOException {
		return getDest("bpmrulesruntime_ITA");

	}*/

}
