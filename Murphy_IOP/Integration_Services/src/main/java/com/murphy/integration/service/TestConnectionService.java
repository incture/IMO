package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.murphy.integration.util.DBConnections;

public class TestConnectionService {

	public Connection testConnection(String url, String userName, String password)
			throws ClassNotFoundException, SQLException {
		return DBConnections.testOnprimeDbonnection(url, userName, password);
	}

}
