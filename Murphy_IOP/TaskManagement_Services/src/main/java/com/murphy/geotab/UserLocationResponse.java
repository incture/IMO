package com.murphy.geotab;

import com.murphy.taskmgmt.dto.ResponseMessage;

public class UserLocationResponse {
	private Coordinates coordinates;
	private ResponseMessage response;

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public ResponseMessage getResponse() {
		return response;
	}

	public void setResponse(ResponseMessage response) {
		this.response = response;
	}

}
