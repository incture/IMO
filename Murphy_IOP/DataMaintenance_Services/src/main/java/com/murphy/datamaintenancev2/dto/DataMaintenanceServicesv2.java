package com.murphy.datamaintenancev2.dto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.murphy.datamaintenance.dto.DataMaintenanceUploadResponseDto;
import com.murphy.datamaintenance.dto.UploadRequestPayloadDto;
import com.murphy.datamaintenance.util.DataMaintenanceConstants;
import com.murphy.datamaintenance.util.EnDatabaseTypes;
import com.murphy.datamaintenance.util.ErrorMessageConstants;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("dataMaintenanceServicesv2")
public class DataMaintenanceServicesv2 {

	@Autowired
	private Environment environment;
	
	private static final Logger logger = LoggerFactory.getLogger(DataMaintenanceServicesv2.class);

	public DataMaintenanceUploadResponseDto uploadExcelToDB(UploadRequestPayloadDto uploadRequestPayloadDto) {

		logger.error("DataMaintenanceServicesv2.uploadExcelToDB()");
		DataMaintenanceUploadResponseDto dataMaintenanceUploadResponseDto = new DataMaintenanceUploadResponseDto();
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Uploaded Successfully !!");
		message.setStatus("Success");
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		dataMaintenanceUploadResponseDto.setMessage(message);

		try {
			Connection connection = getConnection();
			connection.setAutoCommit(false);
			String tableName = DataMaintenanceConstants
					.valueOf(uploadRequestPayloadDto.getRequestPayloadDto().getSubModule()).getTableName();

			logger.error("tableName " + tableName);

			// STEP 1:get Table MetaData Details
			Map<String, DBMetaDataDTO> tableMetaDataMap = DbMetaDataUtil.getTableMetaDataMap(tableName, connection);

			// STPE 2 : Read Excel File and validate the Data
			String base64 = uploadRequestPayloadDto.getFileDetailsDto().getBase64();
			InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
			Workbook workbook = new XSSFWorkbook(inputStream);

			Sheet sheet = null;
			int noOfSheets = workbook.getNumberOfSheets();
			List<String> errorMesssageList = new ArrayList<String>();
			List<String> primaryKeyCombination = new ArrayList<String>();
			for (int i = 0; i < noOfSheets; i++) {
				sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.rowIterator();

				Map<Integer, CellDto> indexToHeaderCellDetails = getSheetHeaderData(sheet.getRow(0), tableMetaDataMap);
				// String upsertQuery = createUpsertQuery(tableName,
				// indexToHeaderCellDetails);
				// PreparedStatement ps =
				// connection.prepareStatement(upsertQuery.toString());
				Statement statement = connection.createStatement();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					boolean isValid = true;
					if (row.getRowNum() != 0) {

						isValid = validateRow(row, indexToHeaderCellDetails, errorMesssageList, primaryKeyCombination);

						if (isValid) {
							// addtoQuery(indexToHeaderCellDetails, ps, row);
							String sql = createUpsertQuery(tableName, row, indexToHeaderCellDetails);
							System.err.println(sql);
							statement.addBatch(sql);

						}

					}

				}

				if (ServicesUtil.isEmpty(errorMesssageList)) {

					// ps.executeBatch();
					statement.executeBatch();
					connection.commit();
				} else {
					dataMaintenanceUploadResponseDto.setErrorMesssage(errorMesssageList);
				}

			}
			workbook.close();
		} catch (Exception e) {
			logger.error("DataMaintenanceServicesv2.uploadExcelToDB() " + e.getMessage());
			message.setStatus("FAILURE");
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
			message.setMessage("Exception while uploading the file" + e.getMessage());
		}

		return dataMaintenanceUploadResponseDto;

	}

	private String createUpsertQuery(String tableName, Row row, Map<Integer, CellDto> indexToHeaderCellDetails)
			throws SQLException {

		StringBuilder tableFieldsSb = new StringBuilder();
		StringBuilder fieldPlaceHolderSb = new StringBuilder();
		StringBuilder primaryKeyFieldsSb = new StringBuilder();
		for (Integer index : indexToHeaderCellDetails.keySet()) {

			CellDto headerDetailsDto = indexToHeaderCellDetails.get(index);
			tableFieldsSb.append(headerDetailsDto.getColumnName()).append(",");
			Cell dataCell = row.getCell(index);
			setValuetoQuery(headerDetailsDto, dataCell, fieldPlaceHolderSb, primaryKeyFieldsSb);
		}

		tableFieldsSb.deleteCharAt(tableFieldsSb.length() - 1);
		fieldPlaceHolderSb.deleteCharAt(fieldPlaceHolderSb.length() - 1);
		primaryKeyFieldsSb.deleteCharAt(primaryKeyFieldsSb.length() - 1);
		StringBuilder sb = new StringBuilder("UPSERT " + tableName + " (" + tableFieldsSb.toString() + ") VALUES("
				+ fieldPlaceHolderSb.toString() + ") WHERE " + primaryKeyFieldsSb.toString());
		return sb.toString();
	}

	private boolean validateRow(Row row, Map<Integer, CellDto> indexToHeaderCellDetails, List<String> errorMesssageList,
			List<String> primaryKeyCombination) {

		boolean isValid = true;
		Iterator<Cell> cellIterator = row.cellIterator();
		StringBuilder primaryKeysBuffer = new StringBuilder();
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			CellDto headerCell = indexToHeaderCellDetails.get(cell.getColumnIndex());

			// Check Mandatory
			if (headerCell.getIsNotNullable() && ServicesUtil.isEmpty(getCellValue(cell))) {
				errorMesssageList.add(ErrorMessageConstants.CELL_BLANK + cell.getAddress().toString());
				isValid = false;
				continue;
			}

			// Check Data type
			Object object = null;
			try {
				object = checkAndgetCellValue(headerCell, cell);
			} catch (Exception e) {
				errorMesssageList.add(ErrorMessageConstants.DATA_TYPE + cell.getAddress().toString());
				isValid = false;
				continue;
			}

			// check Length
			if (headerCell.getColumnType().equals(EnDatabaseTypes.VARCHAR.toString())
					|| headerCell.getColumnType().equals(EnDatabaseTypes.NVARCHAR.toString())) {

				if (!checkLength(object, headerCell.getColumnLength())) {
					errorMesssageList.add(ErrorMessageConstants.LENGTH + cell.getAddress().toString());
					isValid = false;
					continue;
				}

			}

			//
			if (headerCell.getIsPrimaryKey()) {
				primaryKeysBuffer.append(String.valueOf(object).trim()).append(":");
			}

		}

		System.err.println("primaryKey " + primaryKeysBuffer.toString());
		if (isValid && primaryKeyCombination.contains(primaryKeysBuffer.toString())) {
			errorMesssageList.add(ErrorMessageConstants.DUPLICATE_ROWS + row.getRowNum());
			isValid = false;
		}

		return isValid;
	}

	private Object checkAndgetCellValue(CellDto headerCell, Cell cell) throws InvalidInputFault {
		System.out.println("DataMaintenanceServicesv2.isValidDataType() " + headerCell.getColumnType());
		Object obj = null;
		switch (EnDatabaseTypes.valueOf(headerCell.getColumnType())) {
		case INTEGER:
			obj = (Integer) getCellValue(cell);
			break;
		case DECIMAL:
			obj = (Double) getCellValue(cell);
			break;
		case VARCHAR:
			obj = (String) getCellValue(cell);
			break;
		case NVARCHAR:
			obj = (String) getCellValue(cell);
			break;
		case TINYINT:
			String booleanStr = String.valueOf(getCellValue(cell));
			if (!(booleanStr.equals("0") || booleanStr.equals("1"))) {
				throw new InvalidInputFault("Invalid value for the field type boolean");
			}
			obj = booleanStr;
			break;
		}

		return obj;

	}

	private void addtoQuery(Map<Integer, CellDto> indexToHeaderCellDetails, PreparedStatement ps, Row row)
			throws SQLException {
		Iterator<Cell> cellIterator = row.cellIterator();
		ArrayList<Cell> primaryKeyCells = new ArrayList<Cell>();
		int index = 0;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			index = cell.getColumnIndex() + 1;

			setValuetoPreparedStatement(indexToHeaderCellDetails, cell, ps);

			// Check it is primaryKey
			CellDto cellDto = indexToHeaderCellDetails.get(cell.getColumnIndex());

			if (cellDto.getIsPrimaryKey()) {
				primaryKeyCells.add(cell);
			}

		}

		for (Cell cell : primaryKeyCells) {
			index = index + 1;
			ps.setObject(index, getCellValue(cell));
		}
		ps.addBatch();
	}

	private void setValuetoPreparedStatement(Map<Integer, CellDto> indexToHeaderCellDetails, Cell cell,
			PreparedStatement ps) throws SQLException {

		int index = cell.getColumnIndex();
		switch (EnDatabaseTypes.valueOf(indexToHeaderCellDetails.get(index).getColumnType())) {
		case DECIMAL:
			System.err.println("Inside decimal");
			ps.setDouble(index + 1, getDobuleValueFromCell(cell));
			break;

		case VARCHAR:
			ps.setObject(index + 1, getCellValue(cell));
			break;

		case NVARCHAR:
			ps.setObject(index + 1, getCellValue(cell));
			break;
		default:
			break;
		}

	}

	private void setValuetoQuery(CellDto cellDto, Cell cell, StringBuilder queryString,
			StringBuilder primaryKeyFieldsSb) {
		Object object = null;
		switch (EnDatabaseTypes.valueOf(cellDto.getColumnType())) {
		case DECIMAL:
			queryString.append(getDobuleValueFromCell(cell)).append(",");
			break;

		case VARCHAR:

			object = getCellValue(cell);

			if (ServicesUtil.isEmpty(object)) {
				queryString.append("null").append(",");
			} else {
				queryString.append("'").append(String.valueOf(getCellValue(cell))).append("'").append(",");

				if (cellDto.getIsPrimaryKey()) {

					primaryKeyFieldsSb.append(cellDto.getColumnName()).append("= ").append("'")
							.append(String.valueOf(getCellValue(cell))).append("'").append(",");

				}

			}

			break;

		case NVARCHAR:
			object = getCellValue(cell);

			if (ServicesUtil.isEmpty(object)) {
				queryString.append("null").append(",");
			} else {
				queryString.append("'").append(String.valueOf(getCellValue(cell))).append("'").append(",");

			}

			break;

		case INTEGER:
			// TODO
			break;

		case TINYINT:
			// TODO
			break;

		}

	}

	private double getDobuleValueFromCell(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (!ServicesUtil.isEmpty(cell.getNumericCellValue())) {
				return cell.getNumericCellValue();
			}
		case Cell.CELL_TYPE_STRING:
			if (!ServicesUtil.isEmpty(cell.getStringCellValue())) {
				return Double.parseDouble(cell.getStringCellValue());
			}
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_ERROR:
			break;
		case Cell.CELL_TYPE_FORMULA:
			break;
		}
		return 0;
	}

	private Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			if (!ServicesUtil.isEmpty(cell.getNumericCellValue())) {
				System.err.println(cell.getNumericCellValue());
				return cell.getNumericCellValue();
			}
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_ERROR:
			break;
		case Cell.CELL_TYPE_FORMULA:
			break;
		}
		return null;
	}

	private String createUpsertQuery(String tableName, Map<Integer, CellDto> indexToHeaderCellDetails) {

		StringBuilder tableFieldsSb = new StringBuilder();
		StringBuilder fieldPlaceHolderSb = new StringBuilder();
		StringBuilder primaryKeyFieldsSb = new StringBuilder();

		for (Integer index : indexToHeaderCellDetails.keySet()) {
			CellDto dto = indexToHeaderCellDetails.get(index);
			tableFieldsSb.append(dto.getColumnName()).append(",");
			fieldPlaceHolderSb.append("?").append(",");

			if (dto.getIsPrimaryKey()) {
				primaryKeyFieldsSb.append(dto.getColumnName()).append("= ?").append(",");
			}
		}

		tableFieldsSb.deleteCharAt(tableFieldsSb.length() - 1);
		fieldPlaceHolderSb.deleteCharAt(fieldPlaceHolderSb.length() - 1);
		if (!ServicesUtil.isEmpty(primaryKeyFieldsSb.toString())) {
			primaryKeyFieldsSb.deleteCharAt(primaryKeyFieldsSb.length() - 1);
		}

		StringBuilder sb = new StringBuilder("UPSERT " + tableName + " (" + tableFieldsSb.toString() + ") VALUES("
				+ fieldPlaceHolderSb.toString() + ") WHERE " + primaryKeyFieldsSb.toString());
		return sb.toString();
	}

	private Map<Integer, CellDto> getSheetHeaderData(Row row, Map<String, DBMetaDataDTO> dbColumnMetaDataMap)
			throws InvalidInputFault {
		Map<Integer, CellDto> indexToHeaderCellDetails = new TreeMap<Integer, CellDto>();
		Iterator<Cell> cellIterator = row.cellIterator();

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			CellDto headerCellDto = new CellDto();

			DBMetaDataDTO columnMetaData = dbColumnMetaDataMap.get(cell.getStringCellValue());

			if (!ServicesUtil.isEmpty(columnMetaData)) {
				headerCellDto.setColumnName(cell.getStringCellValue());
				headerCellDto.setColumnType(dbColumnMetaDataMap.get(cell.getStringCellValue()).getColumnType());
				headerCellDto.setIsPrimaryKey(dbColumnMetaDataMap.get(cell.getStringCellValue()).getIsPrimaryKey());
				headerCellDto.setColumnLength(dbColumnMetaDataMap.get(cell.getStringCellValue()).getColumnLength());
				headerCellDto.setIsNotNullable(dbColumnMetaDataMap.get(cell.getStringCellValue()).getIsNotNullable());
				indexToHeaderCellDetails.put(cell.getColumnIndex(), headerCellDto);
			} else {
				throw new InvalidInputFault("Invalid Sheet header" + cell.getStringCellValue());
			}

		}
		return indexToHeaderCellDetails;
	}

	private Connection getConnection() throws SQLException {
		String url = environment.getProperty("jdbc.url");
		String user = environment.getProperty("jdbc.username");
		String password = environment.getProperty("jdbc.password");
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}

	public boolean checkLength(Object obj, int length) {

		if (String.valueOf(obj).length() > length) {
			return false;
		}
		return true;

	}
}
