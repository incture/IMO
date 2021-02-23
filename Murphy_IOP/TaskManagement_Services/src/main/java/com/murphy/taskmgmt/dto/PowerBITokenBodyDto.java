package com.murphy.taskmgmt.dto;

public class PowerBITokenBodyDto {

	private String grant_type;
	private String client_id;
	private String username;
	private String password;
	private String resource;
	private String groups;
	private String reports;

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getReports() {
		return reports;
	}

	public void setReports(String reports) {
		this.reports = reports;
	}

	@Override
	public String toString() {
		return "PowerBITokenBodyDto [grant_type=" + grant_type + ", client_id=" + client_id + ", username=" + username
				+ ", password=" + password + ", resource=" + resource + ", groups=" + groups + ", reports=" + reports
				+ "]";
	}

}
