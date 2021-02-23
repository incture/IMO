package com.murphy.datamaintenance.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.murphy.datamaintenance.dto.CellDto;
import com.murphy.datamaintenance.dto.DBMetaDataDTO;
import com.murphy.datamaintenance.dto.DataMaintenanceResponseDto;
import com.murphy.datamaintenance.dto.DataMaintenanceUploadResponseDto;
import com.murphy.datamaintenance.dto.ExcelFormatDto;
import com.murphy.datamaintenance.dto.RequestPayloadDto;
import com.murphy.datamaintenance.dto.TableRowDto;
import com.murphy.datamaintenance.dto.UploadRequestPayloadDto;
import com.murphy.datamaintenance.util.DBServicesUtil;
import com.murphy.datamaintenance.util.DataMaintenanceConstants;
import com.murphy.datamaintenance.util.EnDatabaseTypes;
import com.murphy.datamaintenance.util.ErrorMessageConstants;
import com.murphy.datamaintenance.util.ExcelUtilService;
import com.murphy.datamaintenance.util.FileUtilService;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.mysql.jdbc.Buffer;

@Service("DataMaintenanceService")
public class DataMaintenanceService implements DataMaintenanceFacadeLocal {
	@Autowired
	private Environment environment;

	private static final Logger logger = LoggerFactory.getLogger(DataMaintenanceService.class);

	public DataMaintenanceResponseDto downloadTableData(RequestPayloadDto requestPayloadDto) {
		logger.error("[Murphy][downloadTableData][requestPayloadDto][]" + requestPayloadDto.getModule() + " : "
				+ requestPayloadDto.getSubModule());
		DataMaintenanceResponseDto responseDto = new DataMaintenanceResponseDto();
		ResponseMessage message = new ResponseMessage();
		try {

			Statement statement = getConnection().createStatement();
			StringBuffer selectQueryString = DBServicesUtil.getSelectQueryStringForDownload(requestPayloadDto);
			logger.error("selectQueryString : "+selectQueryString);
			ResultSet rs = statement.executeQuery(selectQueryString.toString());
			ResultSetMetaData rsm = rs.getMetaData();
			int numColumns = rsm.getColumnCount();

			ExcelFormatDto formatDto = new ExcelFormatDto();
			FileUtilService fileUtilService = new FileUtilService();
			ExcelUtilService excelUtilService = new ExcelUtilService();

			List<String> sheetHeaders = new ArrayList<String>();

			for (int columnIndex = 1; columnIndex <= numColumns; columnIndex++) {
				if (!rsm.getColumnName(columnIndex).equals("ACTIVE_FLAG")
						&& !rsm.getColumnName(columnIndex).equals("DEPENDENT_VALUE"))
					sheetHeaders.add(rsm.getColumnName(columnIndex));
			}
			System.err.println("SHEET HEADERS" + sheetHeaders);
			logger.error("[Murphy][downloadTableData][SHEET HEADERS][]" + sheetHeaders);
			fileUtilService.setSheetName(requestPayloadDto.getSubModule(), formatDto);
			formatDto.setSheetHeaders(sheetHeaders);

			List<TableRowDto> rowDtoList = new ArrayList<TableRowDto>();
			while (rs.next()) {
				TableRowDto rowDto = new TableRowDto();
				List<CellDto> cellDtoList = new ArrayList<CellDto>();
				for (int i = 1; i <= numColumns; i++) {
					if (!rsm.getColumnName(i).equals("ACTIVE_FLAG")
							&& !rsm.getColumnName(i).equals("DEPENDENT_VALUE")) {
						CellDto cellDto = new CellDto();
						String columnName = rsm.getColumnName(i);
						String columnType = rsm.getColumnTypeName(i);
						cellDto.setColumnName(columnName);
						cellDto.setColumnType(columnType);
						
						cellDto.setColumnValue(rs.getObject(i) != null ? rs.getObject(i).toString() : null);
						cellDtoList.add(cellDto);
						System.err.println("Column type" +cellDto.getColumnType());
						 System.err.println("DB values" +
						 cellDto.getColumnValue());
						logger.error("[Murphy][downloadTableData][requestPayloadDto][]" + cellDto.getColumnValue());
					}
				}
				rowDto.setCellDtoList(cellDtoList);
				rowDtoList.add(rowDto);
			}
			formatDto.setTableRowDtosList(rowDtoList);
			logger.error("[Murphy][downloadTableData][before excel generation]");
			excelUtilService.generateExcel(formatDto, responseDto, message);
		} catch (Exception e) {
			message.setStatus("FAILURE");
			message.setStatusCode("0");
			message.setMessage("SQL exception while connection");
			logger.error("Exception Message" + e.getMessage());
			responseDto.setMessage(message);
		}
		return responseDto;
	}

	public DataMaintenanceUploadResponseDto uploadExcelToDB(UploadRequestPayloadDto uploadRequestPayloadDto) {
		logger.error("[Murphy][uploadExcelToDB][uploadRequestPayloadDto][]"
				+ uploadRequestPayloadDto.getRequestPayloadDto().getModule() + " : "
				+ uploadRequestPayloadDto.getRequestPayloadDto().getSubModule());
		DataMaintenanceUploadResponseDto responseDto = new DataMaintenanceUploadResponseDto();

		ResponseMessage message = new ResponseMessage();

		Connection connection = null;
		List<String> errorMesssageList = new ArrayList<String>();
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();

			StringBuffer selectQueryString = DBServicesUtil
					.getSelectQueryStringForUpload(uploadRequestPayloadDto.getRequestPayloadDto());

			ResultSet rs = statement.executeQuery(selectQueryString.toString());
			ResultSetMetaData rsm = rs.getMetaData();
			int numColumns = rsm.getColumnCount();

			String tableName = DataMaintenanceConstants
					.valueOf(uploadRequestPayloadDto.getRequestPayloadDto().getSubModule()).getTableName();

			Set<String> primaryKeysSet = new HashSet<String>();
			Map<String, DBMetaDataDTO> dbColumnMetaDataMap = new HashMap<String, DBMetaDataDTO>();
			ResultSet primaryKeyResultSet = connection.getMetaData().getPrimaryKeys(null, null, tableName);
			Set<String> nonNullableColumnsSet = new HashSet<String>();

			while (primaryKeyResultSet.next()) {
				primaryKeysSet.add(primaryKeyResultSet.getString("COLUMN_NAME"));
			}

			// extract DB data to TableRowDTO list

			List<TableRowDto> dbRowsDtosList = new ArrayList<TableRowDto>();
			while (rs.next()) {
				TableRowDto dbRowDto = new TableRowDto();
				List<CellDto> columnDtoList = new ArrayList<CellDto>();
				for (int k = 1; k <= numColumns; k++) {
					CellDto dbColumnDto = new CellDto();
					dbColumnDto.setCellIndex(k);
					dbColumnDto.setColumnName(rsm.getColumnName(k));
					dbColumnDto.setColumnType(rsm.getColumnTypeName(k));
					dbColumnDto.setColumnValue(rs.getObject(k));
					dbColumnDto.setIsPrimaryKey(primaryKeysSet.contains(rsm.getColumnName(k)));
					dbColumnDto.setIsNotNullable(rsm.isNullable(k) == ResultSetMetaData.columnNoNulls);
					columnDtoList.add(dbColumnDto);
					if (rsm.isNullable(k) == ResultSetMetaData.columnNoNulls) {
						nonNullableColumnsSet.add(rsm.getColumnName(k));
					}
				}
				dbRowDto.setCellDtoList(columnDtoList);
				dbRowsDtosList.add(dbRowDto);
			}
			// System.err.println("db rows list size" + dbRowsDtosList.size());
			logger.error("[Murphy][uploadExcelToDB][DB Data Size][]" + dbRowsDtosList.size());

			for (int i = 1; i <= numColumns; i++) {
				DBMetaDataDTO metaDataDTO = new DBMetaDataDTO();
				metaDataDTO.setColumnType(rsm.getColumnTypeName(i));
				metaDataDTO.setIsPrimaryKey(primaryKeysSet.contains(rsm.getColumnName(i)));
				metaDataDTO.setIsNotNullable(rsm.isNullable(i) == ResultSetMetaData.columnNoNulls);
				if (rsm.getColumnTypeName(i).equals(EnDatabaseTypes.VARCHAR.toString())
						|| rsm.getColumnTypeName(i).equals(EnDatabaseTypes.NVARCHAR.toString()) 
						//SOC
						|| rsm.getColumnTypeName(i).equals(EnDatabaseTypes.DECIMAL.toString()))
						//EOC
					metaDataDTO.setColumnLength(rsm.getColumnDisplaySize(i));
				dbColumnMetaDataMap.put(rsm.getColumnName(i), metaDataDTO);

			}
			// }

			// byte[] byteArray =

			// byte[] byteArray=
			// java.util.Base64.getDecoder().decode(uploadRequestPayloadDto.getFileDetailsDto().getBase64());

			 //File file = new File("C:\\Users\\Nivedha Chandhirika\\Desktop\\FRAC - Zone.xlsx");
			// byte[] excdelByteArray = Files.readAllBytes(file.toPath());
			/*
			 * String base64String = new
			 * String(Base64.getEncoder().encode(excdelByteArray));
			 * FileInputStream inputStream = new FileInputStream(file);
			 */

			String base64 = uploadRequestPayloadDto.getFileDetailsDto().getBase64();
			//FileInputStream inputStream = new FileInputStream(file);
			InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = null;
			int noOfSheets = workbook.getNumberOfSheets();
			logger.error("[Murphy][uploadExcelToDB][no of sheets][]" + " " + noOfSheets);

			// convert sheet data to TableRowDto list

			for (int i = 0; i < noOfSheets; i++) {
				sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.rowIterator();
				Map<Integer, CellDto> indexToHeaderCellDetails = new HashMap<Integer, CellDto>();
				List<TableRowDto> sheetRowDtosList = new ArrayList<TableRowDto>();
				Map<String, List<Integer>> primaryKeyCombination = new HashMap<String, List<Integer>>();
				StringBuffer duplicateRowBuffer = null;
				while (rowIterator.hasNext()) {
					StringBuffer primaryKeysBuffer = new StringBuffer();
					Row row = rowIterator.next();
					TableRowDto rowDto = null;
					List<CellDto> cellDtoList = null;
					/*
					 * int primaryKeyColumnsCount = 0; int
					 * notNullableColumnsCount = 0;
					 */
					if (row.getRowNum() != 0) {
						rowDto = new TableRowDto();
						cellDtoList = new ArrayList<CellDto>();
					}
					boolean isValidRow = false;
					Iterator<Cell> cellIterator = row.cellIterator();
					List<String> errorMessagesForValidRow = new ArrayList<String>();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						boolean isValidCell = true;

						if (row.getRowNum() == 0) {

							logger.error("[Murphy][uploadExcelToDB][cell iteration]" + " " + "row 0");
							CellDto headerCellDto = new CellDto();
							headerCellDto.setColumnName(cell.getStringCellValue());
							headerCellDto
									.setColumnType(dbColumnMetaDataMap.get(cell.getStringCellValue()).getColumnType());
							headerCellDto.setIsPrimaryKey(
									dbColumnMetaDataMap.get(cell.getStringCellValue()).getIsPrimaryKey());
							headerCellDto.setColumnLength(
									dbColumnMetaDataMap.get(cell.getStringCellValue()).getColumnLength());
							headerCellDto.setIsNotNullable(
									dbColumnMetaDataMap.get(cell.getStringCellValue()).getIsNotNullable());
							indexToHeaderCellDetails.put(cell.getColumnIndex(), headerCellDto);

						} else {
							CellDto cellDto = new CellDto();
							logger.error("[Murphy][uploadExcelToDB][cell iteration]" + " " + "row number other than 0"
									+ " " + row.getRowNum() + ":" + "cell index" + ": " + cell.getColumnIndex());

							// primary keys validation
							if (indexToHeaderCellDetails.get(cell.getColumnIndex()).getIsPrimaryKey()
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
											.equals(EnDatabaseTypes.TINYINT.toString())
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
											.equals(EnDatabaseTypes.INTEGER.toString())
											//SOC
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
											.equals(EnDatabaseTypes.DECIMAL.toString())
											//EOC
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getIsNotNullable()) {
								if (DBServicesUtil.isCellEmpty(cell)) {
									// System.err.println("cell is empty
									// "+cell.getColumnIndex());
									errorMessagesForValidRow
											.add(ErrorMessageConstants.CELL_BLANK + cell.getAddress().toString());
									// System.err.println("errorMessagesForValidRow"+":"+errorMessagesForValidRow);
									isValidCell = false;
								} else {
									if (!isValidRow) {
										isValidRow = true;
									}
								}

								/*
								 * if (indexToHeaderCellDetails.get(cell.
								 * getColumnIndex()).getIsPrimaryKey()) {
								 * primaryKeyColumnsCount++; } if
								 * (indexToHeaderCellDetails.get(cell.
								 * getColumnIndex()).getIsNotNullable()) {
								 * notNullableColumnsCount++; }
								 */
							}
							if (!DBServicesUtil.isCellEmpty(cell)) {
								logger.error("[Murphy][uploadExcelToDB][cell iteration]" + " " + "cell not empty");

								// data type validation

								if (DBServicesUtil.checkDataType(cell,
										indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType(), cellDto)) {
									logger.error("[Murphy][uploadExcelToDB][cell iteration]" + " "
											+ "Data Type Validation passed");
									// length validation
									if (indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
											.equals(EnDatabaseTypes.VARCHAR.toString())
											|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
													.equals(EnDatabaseTypes.NVARCHAR.toString())) {
										if (!DBServicesUtil.checkLength(cellDto, indexToHeaderCellDetails
												.get(cell.getColumnIndex()).getColumnLength())) {
											logger.error("[Murphy][uploadExcelToDB][cell iteration]" + " "
													+ "length mismatch");
											errorMessagesForValidRow
													.add(ErrorMessageConstants.LENGTH + cell.getAddress().toString());
											isValidCell = false;
										}

									}

								}

								else {
									errorMessagesForValidRow
											.add(ErrorMessageConstants.DATA_TYPE + cell.getAddress().toString());
									isValidCell = false;
								}
								if (!isValidRow) {
									isValidRow = true;
								}
							} else if (!(indexToHeaderCellDetails.get(cell.getColumnIndex()).getIsPrimaryKey()
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
											.equals(EnDatabaseTypes.TINYINT.toString())
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
											.equals(EnDatabaseTypes.INTEGER.toString())
											//SOC
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getColumnType()
											.equals(EnDatabaseTypes.DECIMAL.toString())
											//EOC
									|| indexToHeaderCellDetails.get(cell.getColumnIndex()).getIsNotNullable())) {
								cellDto.setColumnValue(null);

							}

							// }

							if (isValidCell) {
								logger.error("[Murphy][uploadExcelToDB][cell iteration]" + "valid cell");
								int columnIndex = cell.getColumnIndex();
								String columnType = indexToHeaderCellDetails.get(columnIndex).getColumnType();
								String columnName = indexToHeaderCellDetails.get(columnIndex).getColumnName();
								cellDto.setCellIndex(columnIndex);
								cellDto.setColumnName(columnName);
								cellDto.setColumnType(columnType);
								cellDto.setIsPrimaryKey(indexToHeaderCellDetails.get(columnIndex).getIsPrimaryKey());
								cellDto.setIsNotNullable(indexToHeaderCellDetails.get(columnIndex).getIsNotNullable());
								cellDtoList.add(cellDto);
								if (indexToHeaderCellDetails.get(columnIndex).getIsPrimaryKey()) {
									primaryKeysBuffer.append(cellDto.getColumnValue().toString().trim()).append(":");
									logger.error("primaryKeysBuffer : "+ primaryKeysBuffer.toString());
								}

							}

						}

					}
					
					if (row.getRowNum() != 0) {
						if (isValidRow) {
							logger.error("[Murphy][uploadExcelToDB][row iteration]" + "valid row");
							rowDto.setCellDtoList(cellDtoList);
							sheetRowDtosList.add(rowDto);

							//

							for (String rowErroMessgaes : errorMessagesForValidRow) {
								errorMesssageList.add(rowErroMessgaes);
							}
							

						}
						// check if primary keys are duplicated
						if (primaryKeyCombination.containsKey(primaryKeysBuffer.toString())) {
							primaryKeyCombination.get(primaryKeysBuffer.toString()).add(row.getRowNum()+1);
						} else {
							primaryKeyCombination.put(primaryKeysBuffer.toString(), new ArrayList<Integer>());
							primaryKeyCombination.get(primaryKeysBuffer.toString()).add(row.getRowNum()+1);
						}

					}

					/*
					 * if (primaryKeyColumnsCount != primaryKeysSet.size()){
					 * System.err.println("primaryKeyColumnsCount "+" "
					 * +primaryKeyColumnsCount + " primaryKeysSet.size() "+
					 * " "+primaryKeysSet.size()); errorMesssageList.
					 * add("Values are missing for Primary key columns"); } if
					 * (notNullableColumnsCount !=
					 * nonNullableColumnsSet.size()){
					 * System.err.println("notNullableColumnsCount "+" "
					 * +notNullableColumnsCount +
					 * " nonNullableColumnsSet.size() "+
					 * " "+nonNullableColumnsSet.size()); errorMesssageList.
					 * add("Values are missing for Not nullable columns"); }
					 */
				}
				logger.error("[Murphy][uploadExcelToDB][primaryKeyCombination]" + " " + primaryKeyCombination);
				for (Map.Entry<String, List<Integer>> entrySet : primaryKeyCombination.entrySet()) {
					logger.error("map entry: "+entrySet.getKey()+" values "+entrySet.getValue() );
					if (entrySet.getValue().size() > 1) {
						duplicateRowBuffer = new StringBuffer(ErrorMessageConstants.DUPLICATE_ROWS);
						for (Integer rowNumber : entrySet.getValue()) {
							duplicateRowBuffer.append(rowNumber).append(",");
						}
						duplicateRowBuffer.deleteCharAt(duplicateRowBuffer.length() - 1);
						duplicateRowBuffer.append(" at columns ");
						for (Map.Entry<Integer, CellDto> entrySetForPrimaryKey : indexToHeaderCellDetails
								.entrySet()) {
							if (entrySetForPrimaryKey.getValue().getIsPrimaryKey()) {
								duplicateRowBuffer
										.append(entrySetForPrimaryKey.getValue().getColumnName()).append(",");
							}
						}
						duplicateRowBuffer.deleteCharAt(duplicateRowBuffer.length()-1);
						if (!ServicesUtil.isEmpty(duplicateRowBuffer)) {
							duplicateRowBuffer.append(".").append(" Please remove duplicate entries and upload again.");
							errorMesssageList.add(duplicateRowBuffer.toString());
						}
					}
					
				}
				logger.error("[Murphy][uploadExcelToDB][errorMesssageList after duplicate primary keys check list size]" + " " + errorMesssageList.size());
				logger.error("[Murphy][uploadExcelToDB][row dto list size]" + " " + sheetRowDtosList.size());

				// identify insert, update and delete records only if all
				// cells are validated

				logger.error("[Murphy][uploadExcelToDB][errorMesssageList]" + " " + errorMesssageList);

				if (ServicesUtil.isEmpty(errorMesssageList)) {
					List<TableRowDto> insertRowsDtoList = new ArrayList<TableRowDto>();
					List<TableRowDto> updateRowsDtoList = new ArrayList<TableRowDto>();
					List<StringBuffer> deleRowsList = new ArrayList<StringBuffer>();

					// create insert records list, delete records list, update
					// records list

					for (TableRowDto sheetRowDto : sheetRowDtosList) {
						boolean isInsertRecord = true;
						boolean isUpdateRecord = false;

						resultSetLoop: for (TableRowDto dbRowDto : dbRowsDtosList) {
							// resultSetLoop: while (rsIteration.next()) {

							for (CellDto sheetCellDto : sheetRowDto.getCellDtoList()) {
								logger.error("[Murphy][uploadExcelToDB][iterating sheet cells]");
								/*
								 * System.err.println("inside cell dto list" +
								 * "Pk " + sheetCellDto.getIsPrimaryKey() +
								 * "column name " + sheetCellDto.getColumnName()
								 * + "column type " +
								 * sheetCellDto.getColumnType() +
								 * "columnn value " +
								 * sheetCellDto.getColumnValue());
								 */

								if (sheetCellDto.getIsPrimaryKey()) {
									// for (int j = 1; j <= numColumns; j++) {
									for (CellDto dbCellDto : dbRowDto.getCellDtoList()) {
										if (sheetCellDto.getColumnName().equals(dbCellDto.getColumnName())) {
											logger.error(
													"[Murphy][uploadExcelToDB][primary column in dB and sheet matched]");

											if (DBServicesUtil.isVarcharType(sheetCellDto.getColumnType())
													|| DBServicesUtil.isNVarcharType(sheetCellDto.getColumnType())) {
												String columnValue = sheetCellDto.getColumnValue().toString();

												if (columnValue.equals(dbCellDto.getColumnValue().toString())) {
													isUpdateRecord = true;
													isInsertRecord = false;
													logger.error(
															"[Murphy][uploadExcelToDB][primary column in dB and sheet values matched]");
												} else {
													logger.error(
															"[Murphy][uploadExcelToDB][primary column in dB and sheet values not  matched]");
													isUpdateRecord = false;
													isInsertRecord = true;
													continue resultSetLoop;
												}
											}
											if (DBServicesUtil.isIntegerType(sheetCellDto.getColumnType())
													|| DBServicesUtil.isBooleanType(sheetCellDto.getColumnType())) {
												Integer columnValue = Integer
														.parseInt(sheetCellDto.getColumnValue().toString());
												if (columnValue.equals(
														Integer.parseInt(dbCellDto.getColumnValue().toString()))) {
													isUpdateRecord = true;
													isInsertRecord = false;
												} else {
													isUpdateRecord = false;
													isInsertRecord = true;
													continue resultSetLoop;
												}
											}
											//added by ayesha
											
											if (DBServicesUtil.isDecimalType(sheetCellDto.getColumnType())) {
												
												Double columnValue = (Double) sheetCellDto.getColumnValue();
												
												
										
												if (columnValue.equals((Double) dbCellDto.getColumnValue()) ){
													isUpdateRecord = true;
													isInsertRecord = false;
												} else {
													isUpdateRecord = false;
													isInsertRecord = true;
													continue resultSetLoop;
												}
											}

										}
									}
								}
							}
							if (isUpdateRecord) {
								logger.error(
										"[Murphy][uploadExcelToDB][record found in that particular rs hence skipping other rs search]");
								break resultSetLoop;
							}

						}
						if (isInsertRecord) {
							logger.error("[Murphy][uploadExcelToDB][insert record]");
							insertRowsDtoList.add(sheetRowDto);
						} else if (isUpdateRecord) {
							logger.error("[Murphy][uploadExcelToDB][update record]");
							updateRowsDtoList.add(sheetRowDto);
						}
					}

					// insert records:
					logger.error("[Murphy][uploadExcelToDB][insert quertylist size]" + " " + insertRowsDtoList.size());
					if(!ServicesUtil.isEmpty(errorMesssageList))
					{
						System.err.println("errorMesssageList : " +errorMesssageList.toString());
					}
					if (ServicesUtil.isEmpty(errorMesssageList)) {
						String insertQuery = "Insert into" + " " + tableName + " ";
						for (TableRowDto insertRowDto : insertRowsDtoList) {
							StringBuffer columnNamesBuffer = new StringBuffer(insertQuery).append("(");
							StringBuffer columnValuesBuffer = new StringBuffer(" values (");
							for (CellDto cellDto : insertRowDto.getCellDtoList()) {
								columnNamesBuffer.append(cellDto.getColumnName()).append(",");
								//SOC
								if(DBServicesUtil.isDecimalType(cellDto.getColumnType())){
									//columnValuesBuffer.append(cellDto.getColumnValue().toString());
									//added by ayesha
									String value = String.valueOf(cellDto.getColumnValue());
									columnValuesBuffer.append(value);
								} //EOC
								else if (DBServicesUtil.isIntegerType(cellDto.getColumnType())) {
									columnValuesBuffer.append(cellDto.getColumnValue().toString()).append(",");
								} else if (DBServicesUtil.isBooleanType(cellDto.getColumnType())) {
									if (Integer.parseInt(cellDto.getColumnValue().toString()) == 0) {
										columnValuesBuffer.append("FALSE").append(",");
									} else if (Integer.parseInt(cellDto.getColumnValue().toString()) == 1) {
										columnValuesBuffer.append("TRUE").append(",");
									}

								} else if (DBServicesUtil.isNVarcharType(cellDto.getColumnType())
										|| DBServicesUtil.isVarcharType(cellDto.getColumnType())) {
									columnValuesBuffer.append(ServicesUtil.isEmpty(cellDto.getColumnValue()) ? " ' ' "

											: '\'' + cellDto.getColumnValue().toString() + '\'').append(",");
								}

							}
							if(!uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.LOCATION_COORDINATE) 
									&& !uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.WELL_TIER)){
								columnNamesBuffer.append("ACTIVE_FLAG)");
								columnValuesBuffer.append('\'' + "ACTIVE" + '\'').append(")");
							}
							boolean columnValuesBufferEndingWithComma=columnValuesBuffer.toString().endsWith(",");
							if(columnValuesBufferEndingWithComma)
							{
								columnValuesBuffer.deleteCharAt(columnValuesBuffer.length() -1);
								columnValuesBuffer.append(")");
							   // updateQueyBuffer.delete(updateQueyBuffer.lastIndexOf(","),updateQueyBuffer.length());

								
							}
							boolean columnNamesBufferEndingWithComma=columnNamesBuffer.toString().endsWith(",");
							if(columnNamesBufferEndingWithComma)
							{
								columnNamesBuffer.deleteCharAt(columnNamesBuffer.length() -1);
								columnNamesBuffer.append(")");
							   // updateQueyBuffer.delete(updateQueyBuffer.lastIndexOf(","),updateQueyBuffer.length());

								
							}
							columnNamesBuffer.append(columnValuesBuffer);
							logger.error("[Murphy][uploadExcelToDB][insert Quey Buffer]" + " "
									+ columnNamesBuffer.toString());
							statement.addBatch(columnNamesBuffer.toString());

						}

						// update records
						logger.error(
								"[Murphy][uploadExcelToDB][update quertylist size]" + " " + updateRowsDtoList.size());
						String updateQuery = "Update" + " " + tableName + " " + "set ";
						String whereQuery = " where";
						for (TableRowDto updaeteRowDto : updateRowsDtoList) {
							System.err.println("[Murphy][uploadExcelToDB][update quertylist size]" +updateRowsDtoList);
							StringBuffer updateQueyBuffer = new StringBuffer(updateQuery);
							StringBuffer whereQueryBuffer = new StringBuffer(whereQuery);
							for (CellDto cellDto : updaeteRowDto.getCellDtoList()) {
								System.err.println("updaeteRowDto.getCellDtoList() " +updaeteRowDto.getCellDtoList());
								StringBuffer columnValue = new StringBuffer();
								columnValue.append(cellDto.getColumnName()).append(" ").append("=");
								//SOC
								if(DBServicesUtil.isDecimalType(cellDto.getColumnType())){
//									System.prinln("column Type : " +cellDto.getColumnType())
									//added by ayesha
									String value = String.valueOf(cellDto.getColumnValue());
									columnValue.append(value);
									
									
									//
								}//EOC
								else if (DBServicesUtil.isIntegerType(cellDto.getColumnType())) {
									columnValue.append(cellDto.getColumnValue().toString());
								} else if (DBServicesUtil.isBooleanType(cellDto.getColumnType())) {
									if (Integer.parseInt(cellDto.getColumnValue().toString()) == 0) {
										columnValue.append("FALSE");
									} else if (Integer.parseInt(cellDto.getColumnValue().toString()) == 1) {
										columnValue.append("TRUE");
									}
								} else if (DBServicesUtil.isVarcharType(cellDto.getColumnType())
										|| DBServicesUtil.isNVarcharType(cellDto.getColumnType())) {
									columnValue.append(ServicesUtil.isEmpty(cellDto.getColumnValue()) ? " ' ' "
											: '\'' + cellDto.getColumnValue().toString() + '\'');
								}

								if (cellDto.getIsPrimaryKey()) {

									whereQueryBuffer.append(" ").append(columnValue).append(" ").append("and");
								} else {
									
									updateQueyBuffer.append(columnValue).append(",");
									
								}
								
								
							}
							if(!uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.LOCATION_COORDINATE) 
									&& !uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.WELL_TIER)){
							updateQueyBuffer.append("ACTIVE_FLAG='ACTIVE'");
							}
							
							whereQueryBuffer.delete(whereQueryBuffer.lastIndexOf("and"), whereQueryBuffer.length());
							
							boolean isupdateQueyBufferEndingWithComma=updateQueyBuffer.toString().endsWith(",");
							if(isupdateQueyBufferEndingWithComma)
							{
								updateQueyBuffer.deleteCharAt(updateQueyBuffer.length() -1);
							   // updateQueyBuffer.delete(updateQueyBuffer.lastIndexOf(","),updateQueyBuffer.length());

								
							}
							
							updateQueyBuffer.append(whereQueryBuffer);
							logger.error("[Murphy][uploadExcelToDB][update Query Buffer]" + " "
									+ updateQueyBuffer.toString());
							System.err.println("update query :  "+updateQueyBuffer.toString());
							statement.addBatch(updateQueyBuffer.toString());
						}

						// delete records--soft delete- set flag as Delete.
						//SOC : Added the if conditions to by-pass the soft delete extra column
						if(!uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.LOCATION_COORDINATE) 
								&& !uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.WELL_TIER)){
						//EOC
							for (TableRowDto dbRowDto : dbRowsDtosList) {
								boolean isFound = false;
								StringBuffer deleteRowBuffer = new StringBuffer();
								logger.error("[Murphy][uploadExcelToDB][tableRowDtoList size]" + " "
										+ dbRowDto.getCellDtoList().size());
								searchInNextRow: for (TableRowDto deleteSheetRowDto : sheetRowDtosList) {
									for (CellDto dbCellDto : dbRowDto.getCellDtoList()) {
										if (dbCellDto.getIsPrimaryKey()) {
											for (CellDto sheetCellDto : deleteSheetRowDto.getCellDtoList()) {
												if (dbCellDto.getColumnName().equals(sheetCellDto.getColumnName())) {
													if (DBServicesUtil.isVarcharType(sheetCellDto.getColumnType())
															|| DBServicesUtil
																	.isNVarcharType(sheetCellDto.getColumnType())) {
														String columnValue = sheetCellDto.getColumnValue().toString();
														if (columnValue.equals(dbCellDto.getColumnValue().toString())) {
															isFound = true;

														} else {
															isFound = false;
															continue searchInNextRow;
														}
													} else if (DBServicesUtil.isIntegerType(sheetCellDto.getColumnType())
															|| DBServicesUtil.isBooleanType(sheetCellDto.getColumnType())) {
														Integer columnValue = Integer
																.parseInt(sheetCellDto.getColumnValue().toString());
														if (columnValue.equals(
																Integer.parseInt(dbCellDto.getColumnValue().toString()))) {
															isFound = true;
															;
														} else {
															isFound = false;
															continue searchInNextRow;
														}
													}
												}
											}

										}
									}
									if (isFound)
										break searchInNextRow;
								}
								if (!isFound) {
									logger.error("[Murphy][uploadExcelToDB][db cell size]" + " "
											+ dbRowDto.getCellDtoList().size());
									for (CellDto deleteCellDto : dbRowDto.getCellDtoList()) {
										/*
										 * System.err.println("db cell dto" +
										 * deleteCellDto.getColumnName() + " " +
										 * deleteCellDto.getColumnType() + " " +
										 * deleteCellDto.getCellIndex() + " " +
										 * deleteCellDto.getColumnLength() + " " +
										 * deleteCellDto.getIsPrimaryKey() +
										 * deleteCellDto.getColumnValue());
										 */

										if (deleteCellDto.getIsPrimaryKey()) {
											deleteRowBuffer.append(" ").append(deleteCellDto.getColumnName()).append("=");
											if (DBServicesUtil.isIntegerType(deleteCellDto.getColumnType())) {
												deleteRowBuffer.append(deleteCellDto.getColumnValue().toString())
														.append(" ").append("and");
											} else if (DBServicesUtil.isBooleanType(deleteCellDto.getColumnType())) {
												if (Integer.parseInt(deleteCellDto.getColumnValue().toString()) == 0) {
													deleteRowBuffer.append("FALSE").append("and");
												} else {
													deleteRowBuffer.append("TRUE").append("and");
												}
											} else if (DBServicesUtil.isVarcharType(deleteCellDto.getColumnType())
													|| DBServicesUtil.isNVarcharType(deleteCellDto.getColumnType())) {
												deleteRowBuffer
														.append('\'' + deleteCellDto.getColumnValue().toString() + '\'')
														.append("and");
												// System.err.println("delete buffer
												// inside varchar " +
												// deleteRowBuffer);
												logger.error("[Murphy][uploadExcelToDB][delete buffer inside varchar]" + " "
														+ deleteRowBuffer);
											}

										}
									}

									deleteRowBuffer.delete(deleteRowBuffer.lastIndexOf("and"), deleteRowBuffer.length());
									deleRowsList.add(deleteRowBuffer);
								}
							}
							
							
						}
						//Added the if conditions to by-pass the soft delete extra column
						if(!uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.LOCATION_COORDINATE) 
								&& !uploadRequestPayloadDto.getRequestPayloadDto().getSubModule().equalsIgnoreCase(MurphyConstant.WELL_TIER)){
							// delete records:
							String deleteQuery = "Update" + " " + tableName + " set ACTIVE_Flag='DELETE' where";
							logger.error("[Murphy][uploadExcelToDB][delete quertylist size]" + " " + deleRowsList.size());
							for (StringBuffer deleteRow : deleRowsList) {
								StringBuffer deleteRowBuffer = new StringBuffer(deleteQuery).append(deleteRow);
								logger.error("[Murphy][uploadExcelToDB][delete query]" + " " + deleteRowBuffer);
								logger.error("delete query" + deleteRowBuffer);
								statement.addBatch(deleteRowBuffer.toString());
							}
						}

						statement.executeBatch();
						connection.commit();
					}
					
				}
			}
			workbook.close();
			connection.close();

		} catch (IOException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				message.setStatus("FAILURE");
				message.setStatusCode(MurphyConstant.CODE_FAILURE);
				message.setMessage("SQL exception rollback failed" + e1.getMessage());
				responseDto.setMessage(message);
				return responseDto;
			}
			message.setStatus("FAILURE");
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
			message.setMessage("IO Exception " + e.getMessage());
			responseDto.setMessage(message);
			return responseDto;
		} catch (SQLException e) {
			try {
				connection.rollback();
				message.setStatus("FAILURE");
				message.setStatusCode(MurphyConstant.CODE_FAILURE);
				message.setMessage("SQL exception" + e.getMessage());
				responseDto.setMessage(message);
				return responseDto;
			} catch (Exception e1) {
				message.setStatus("FAILURE");
				message.setStatusCode(MurphyConstant.CODE_FAILURE);
				message.setMessage("SQL exception rollback failed" + e1.getMessage());
				responseDto.setMessage(message);
				return responseDto;
			}

		} catch (Exception e) {

			message.setStatus("FAILURE");
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
			message.setMessage("Exception in uploading data" + e.getMessage());
			e.printStackTrace();
			responseDto.setMessage(message);
			return responseDto;
		}
		if (ServicesUtil.isEmpty(errorMesssageList)) {
			message.setStatus("Success");
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			message.setMessage("Data uploaded successfully");
			responseDto.setMessage(message);
		} else {
			message.setStatus("FAILURE");
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
			message.setMessage("Data validations failed");
			responseDto.setErrorMesssage(errorMesssageList);
			responseDto.setMessage(message);
		}
		return responseDto;
	}

	private Connection getConnection() throws SQLException {
		String url = environment.getProperty("jdbc.url");
		String user = environment.getProperty("jdbc.username");
		String password = environment.getProperty("jdbc.password");
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}

}
