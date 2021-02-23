package com.murphy.geotab;

import java.util.List;

public class DeviceResponse {

	private List<Device> devices;
	private String responseMessage;
	private String responseCode;

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public String toString() {
		return "DeviceResponse [devices=" + devices + ", responseMessage=" + responseMessage + ", responseCode="
				+ responseCode + "]";
	}

}
