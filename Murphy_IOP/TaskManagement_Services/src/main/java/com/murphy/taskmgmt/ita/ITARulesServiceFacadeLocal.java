package com.murphy.taskmgmt.ita;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

public interface ITARulesServiceFacadeLocal {
	public List<?> getITARulesByType(String type) throws ClientProtocolException, IOException;
}
