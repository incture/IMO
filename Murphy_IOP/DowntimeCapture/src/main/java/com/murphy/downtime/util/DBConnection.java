package com.murphy.downtime.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {

	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			logger.error("[getConnection] : Error- Exception while loading Drivers " + e);
			return connection;
		}
		try {
			// connection =
			// DriverManager.getConnection("jdbc:sqlserver://10.100.0.123:1433;databaseName=procount_Test",
			// "iop_user", "$i0pu$er$");
			
			try {
				DowntimeServicesUtil.setupSOCKS();
			} catch (Exception e) {
				logger.error("[insertOrUpdateCounts] : ERROR- Exception while setting SOCKS " + e);
			}
			connection = DriverManager.getConnection(DowntimeConstant.MS_DB_CONNECTION_URL, DowntimeConstant.MS_DB_USERNAME,
					DowntimeConstant.MS_DB_PASSWORD);
		} catch (SQLException e) {
			logger.error("[getConnection] : Error- Exception while creating connection " + e);
			return connection;
		}
		return connection;
	}

}
