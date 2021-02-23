package com.murphy.taskmgmt.ita;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.util.MurphyConstant;

@Service("ITARulesServiceFacade")

public class ITARulesServiceFacade implements ITARulesServiceFacadeLocal {
	public List<?> getITARulesByType(String type) throws ClientProtocolException, IOException {
		if (MurphyConstant.ITATaskType.equals(type)) {
			ITATaskRulesDto itaTaskRulesDto = new ITATaskRulesDto();
			itaTaskRulesDto.setType(type);
			return new ITARuleServiceForTask().getResultList(itaTaskRulesDto);
		} else if (MurphyConstant.ITADOPType.equals(type)) {
			ITADOPRulesDto itadopRulesDto = new ITADOPRulesDto();
			itadopRulesDto.setType(type);
			return new ITARuleServiceForDOP().getResultList(itadopRulesDto);
		}
		else if (MurphyConstant.ITAWaterOilCarryOver.equals(type)) {
			ITAWaterOilRulesDto itaWaterOilRulesDto = new ITAWaterOilRulesDto();
			itaWaterOilRulesDto.setType(type);
			return new ITARuleServiceForWaterOil().getResultList(itaWaterOilRulesDto);
		}
		else if (MurphyConstant.ITAGasBlowBy.equals(type)) {
			ITAGasBlowByRulesDto itaGasBlowByRulesDto = new ITAGasBlowByRulesDto();
			itaGasBlowByRulesDto.setType(type);
			return new ITARuleServiceForGasBlowBy().getResultList(itaGasBlowByRulesDto);
		}
		return null;
	}

	/*public Map<String, String> getByDestination() throws ClientProtocolException, IOException {
		return new ITARuleServiceForTask().getByDestination();
	}*/
}
