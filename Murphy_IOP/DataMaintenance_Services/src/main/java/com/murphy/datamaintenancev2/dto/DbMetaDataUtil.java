package com.murphy.datamaintenancev2.dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.murphy.datamaintenance.dto.DBMetaDataDTO;
import com.murphy.datamaintenance.util.EnDatabaseTypes;

public class DbMetaDataUtil {

	public static Map<String, DBMetaDataDTO> getTableMetaDataMap(String tableName, Connection connection)
			throws SQLException {

		List<String> primaryKeySet = getPrimaryKey(connection, null, null, tableName);

		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, DBMetaDataDTO> dbColumnMetaDataMap = null;
		try {

			StringBuilder sql = new StringBuilder("SELECT TOP 1 * FROM "+tableName);
			ps = connection.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			dbColumnMetaDataMap = new HashMap<String, DBMetaDataDTO>();
			int numColumns = rsm.getColumnCount();

			for (int i = 1; i <=numColumns; i++) {
				DBMetaDataDTO dto = new DBMetaDataDTO();
				dto.setColumnType(rsm.getColumnTypeName(i));
				dto.setIsPrimaryKey(primaryKeySet.contains(rsm.getColumnName(i)));
				dto.setIsNotNullable(rsm.isNullable(i) == ResultSetMetaData.columnNoNulls);
				if (rsm.getColumnTypeName(i).equals(EnDatabaseTypes.VARCHAR.toString())
						|| rsm.getColumnTypeName(i).equals(EnDatabaseTypes.NVARCHAR.toString())
						|| rsm.getColumnTypeName(i).equals(EnDatabaseTypes.DECIMAL.toString())) {
					dto.setColumnLength(rsm.getColumnDisplaySize(i));
				}
				dbColumnMetaDataMap.put(rsm.getColumnName(i), dto);
			}

		} finally {
			rs.close();
			ps.close();
		}
		return dbColumnMetaDataMap;
	}

	public static List<String> getPrimaryKey(Connection connection, String catalog, String schema, String tableName)
			throws SQLException {
		ResultSet rs = null;
		try {

			rs = connection.getMetaData().getPrimaryKeys(catalog, schema, tableName);
			List<String> primaryKeysSet = new ArrayList<String>();
			while (rs.next()) {
				primaryKeysSet.add(rs.getString("COLUMN_NAME"));
			}

			rs.close();
			return primaryKeysSet;
		} finally {
			rs.close();
		}
	}

}
