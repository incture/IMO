package com.murphy.taskmgmt.dto;

public class TokenDetailsDto {

	private String token;
	private Long expirationDateTime;
	private String tokenId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(Long expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public String toString() {
		return "TokenDetailsDto [token=" + token + ", expirationDateTime=" + expirationDateTime + ", tokenId=" + tokenId
				+ "]";
	}

}
