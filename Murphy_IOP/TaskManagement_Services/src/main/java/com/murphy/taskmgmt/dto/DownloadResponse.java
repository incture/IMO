package com.murphy.taskmgmt.dto;

/**
 * @author INC00718
 *
 */
public class DownloadResponse {

	private String url;
	private String version;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "DownloadResponse [url=" + url + ", version=" + version + "]";
	}

}
