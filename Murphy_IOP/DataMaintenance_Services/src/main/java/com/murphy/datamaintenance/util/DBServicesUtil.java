package com.murphy.datamaintenance.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import com.murphy.datamaintenance.dto.CellDto;
import com.murphy.datamaintenance.dto.RequestPayloadDto;
import com.murphy.taskmgmt.util.MurphyConstant;

public class DBServicesUtil {
	public static StringBuffer getSelectQueryStringForDownload(RequestPayloadDto requestPayloadDto) {
		StringBuffer selectQueryString = new StringBuffer("SELECT * FROM ");
		String tableName = DataMaintenanceConstants.valueOf(requestPayloadDto.getSubModule()).getTableName();
		if(!requestPayloadDto.getSubModule().equalsIgnoreCase(MurphyConstant.LOCATION_COORDINATE) 
				&& !requestPayloadDto.getSubModule().equalsIgnoreCase(MurphyConstant.WELL_TIER))
			selectQueryString.append(tableName).append(" ").append("WHERE ACTIVE_FLAG='ACTIVE'");
		else
			selectQueryString.append(tableName).append(" ");
		return selectQueryString;
	}

	public static StringBuffer getSelectQueryStringForUpload(RequestPayloadDto requestPayloadDto) {
		StringBuffer selectQueryString = new StringBuffer("SELECT * FROM ");
		String tableName = DataMaintenanceConstants.valueOf(requestPayloadDto.getSubModule()).getTableName();
		selectQueryString.append(tableName);
		return selectQueryString;
	}

	public static boolean isCellEmpty(Cell cell) {
		if (cell.getCellTypeEnum() == CellType.BLANK)
			return true;
		else
			return false;

	}

	public static boolean checkDataType(Cell cell, String columnType, CellDto cellDto) {
		try {
			//System.err.println("index in data type check " + cell.getColumnIndex() + " " + "columntype" + columnType);
			for (EnDatabaseTypes databaseType : EnDatabaseTypes.values()) {
				switch (databaseType) {
				case INTEGER: {
					if (columnType.equals(databaseType.toString())) {
						// double d = cell.getNumericCellValue();
						/*
						 * System.err.println( "index in data type check " +
						 * cell.getColumnIndex() + " " +
						 * cell.getStringCellValue());
						 */
						// cellDto.setColumnValue((int)
						// cell.getNumericCellValue());
						if (cell.getCellTypeEnum() == CellType.NUMERIC) {
							//System.err.println("numeric type");
							cellDto.setColumnValue((int) cell.getNumericCellValue());
						} else if (cell.getCellTypeEnum() == CellType.STRING) {
							//System.err.println("string  type");
							cellDto.setColumnValue(Integer.parseInt(cell.getStringCellValue()));
						}

					}
					break;
				}
				//added by ayesha
				case DECIMAL: {
					if (columnType.equals(databaseType.toString())) {
						// double d = cell.getNumericCellValue();
						/*
						 * System.err.println( "index in data type check " +
						 * cell.getColumnIndex() + " " +
						 * cell.getStringCellValue());
						 */
						// cellDto.setColumnValue((int)
						// cell.getNumericCellValue());
						if (cell.getCellTypeEnum() == CellType.NUMERIC) {
							//System.err.println("numeric type");
							cellDto.setColumnValue(cell.getNumericCellValue());
						} else if (cell.getCellTypeEnum() == CellType.STRING) {
							//System.err.println("string  type");
							cellDto.setColumnValue(Integer.parseInt(cell.getStringCellValue()));
						}

					}
					break;
				}
				// end of addition
				case VARCHAR: {
					if (columnType.equals(databaseType.toString())) {
						if(cell.getCellTypeEnum() == CellType.NUMERIC){
							cellDto.setColumnValue((int)cell.getNumericCellValue());
						}
						else if (cell.getCellTypeEnum() == CellType.STRING){
						cellDto.setColumnValue(cell.getStringCellValue());
						}
						// System.err.println("inside varchar block cell value"
						// + cellDto.getColumnValue().toString());
					}
					break;
				}
				case NVARCHAR: {
					if (columnType.equals(databaseType.toString())) {
						cellDto.setColumnValue(cell.getStringCellValue());
						// System.err.println("inside varchar block cell value"
						// + cellDto.getColumnValue().toString());
					}
					break;
				}
				case TINYINT: {
					if (columnType.equals(databaseType.toString())) {
						if (cell.getCellTypeEnum() == CellType.NUMERIC) {
							if ((int) cell.getNumericCellValue() == 0 || (int) cell.getNumericCellValue() == 1) {
								cellDto.setColumnValue((int) cell.getNumericCellValue());
							} else {
								throw new Exception();
							}
						} else if (cell.getCellTypeEnum() == CellType.STRING) {
							if (Integer.parseInt(cell.getStringCellValue()) == 0
									|| Integer.parseInt(cell.getStringCellValue()) == 1) {
								cellDto.setColumnValue(Integer.parseInt(cell.getStringCellValue()));
							} else
								throw new Exception();
						}

					}
				}
					break;
				}
			}

		} catch (Exception e) {

			//System.err.println("data type check exception" + e.getMessage());
			/*
			 * if (columnType.equals(EnDatabaseTypes.VARCHAR.toString()) ||
			 * columnType.equals(EnDatabaseTypes.NVARCHAR.toString())) {
			 * cellDto.setColumnValue((int)cell.getNumericCellValue()); return
			 * true; }
			 */

			return false;
		}
		return true;
	}

	public static boolean checkLength(CellDto cellDto, int length) {

		if (cellDto.getColumnValue().toString().length() > length) {
			return false;
		}
		return true;

	}

	public static boolean isVarcharType(String columnType) {
		if (columnType.equals(EnDatabaseTypes.VARCHAR.toString()))
			return true;
		else
			return false;
	}

	public static boolean isBooleanType(String columnType) {
		if (columnType.equals(EnDatabaseTypes.TINYINT.toString()))
			return true;
		else
			return false;
	}

	public static boolean isIntegerType(String columnType) {
		if (columnType.equals(EnDatabaseTypes.INTEGER.toString()))
			return true;
		else
			return false;
	}
	
	public static boolean isDecimalType(String columnType) {
		if (columnType.equals(EnDatabaseTypes.DECIMAL.toString()))
			return true;
		else
			return false;
	}

	public static boolean isNVarcharType(String columnType) {
		if (columnType.equals(EnDatabaseTypes.NVARCHAR.toString()))
			return true;
		else
			return false;
	}
}
