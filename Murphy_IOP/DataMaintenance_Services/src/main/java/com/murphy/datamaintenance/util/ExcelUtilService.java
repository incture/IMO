package com.murphy.datamaintenance.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.datamaintenance.dto.DataMaintenanceResponseDto;
import com.murphy.datamaintenance.dto.ExcelFormatDto;
import com.murphy.datamaintenance.dto.FileDetailsDto;
import com.murphy.datamaintenance.dto.TableRowDto;
import com.murphy.datamaintenance.service.DataMaintenanceService;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.reports.DownloadReportResponseDto;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

public class ExcelUtilService {
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtilService.class);

	public void generateExcel(ExcelFormatDto formatDto, DataMaintenanceResponseDto responseDto,
			ResponseMessage message) {
		logger.error("[Murphy][generateExcel][requestPayloadDto][]");
		FileDetailsDto fileDetailsDto = new FileDetailsDto();
		if (!ServicesUtil.isEmpty(formatDto)) {
			Workbook workbook = new XSSFWorkbook();
			Sheet detailSheet = null;
			if (!ServicesUtil.isEmpty(formatDto.getSheetName())) {
				detailSheet = workbook.createSheet(formatDto.getSheetName());

			}
			byte[] bytes = null;
			try {
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColorPredefined.CORNFLOWER_BLUE.getIndex());
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				Font headerFont = workbook.createFont();
				headerFont.setBold(true);
				;
				cellStyle.setFont(headerFont);

				CellStyle dataCellStyle = workbook.createCellStyle();
				dataCellStyle.setBorderBottom(BorderStyle.THIN);
				dataCellStyle.setBorderRight(BorderStyle.THIN);
				dataCellStyle.setBorderLeft(BorderStyle.THIN);
				dataCellStyle.setBorderTop(BorderStyle.THIN);

				/**
				 * Printing Header on 1st row of sheet
				 */
				int cellNumber = 0;
				Row row1 = null;
				System.err.println("sheet headers"+formatDto.getSheetHeaders());
				if (!ServicesUtil.isEmpty(formatDto.getSheetHeaders())) {
					row1 = detailSheet.createRow(0);
					for (String header : formatDto.getSheetHeaders()) {
						Cell headerCell = row1.createCell(cellNumber++);
						headerCell.setCellStyle(cellStyle);
						headerCell.setCellValue(header);
					}
					detailSheet.createFreezePane(0, 1);
				}

				/**
				 * Printing Report Data List from 2nd row of sheet till end of
				 * Report Data.
				 */
				int rowNumber = 1;
				 Double cellValue = null ;
				if (!ServicesUtil.isEmpty(formatDto.getTableRowDtosList())) {
					for (TableRowDto rowDto : formatDto.getTableRowDtosList()) {
						Row row = detailSheet.createRow(rowNumber++);
						for (int i = 0; i < rowDto.getCellDtoList().size(); i++) {
							 
							Cell dataCell = row.createCell(i);
							dataCell.setCellStyle(dataCellStyle);
//							dataCell.setCellValue(ServicesUtil.isEmpty(rowDto.getCellDtoList().get(i).getColumnValue())
//									? " " : rowDto.getCellDtoList().get(i).getColumnValue().toString());
							 // added by ayesha
							System.err.println("Column type" +rowDto.getCellDtoList().get(i).getColumnType().toString());
							
							if(rowDto.getCellDtoList().get(i).getColumnType().equals(EnDatabaseTypes.DECIMAL.toString()))  
							 { System.err.println("Its of type double");
							  if(!ServicesUtil.isEmpty(rowDto.getCellDtoList().get(i).getColumnValue()))
							  {
								  
							  String value= rowDto.getCellDtoList().get(i).getColumnValue().toString();
							  cellValue=Double.parseDouble(value);
							   dataCell.setCellValue(cellValue);
							  }
							 }
							else
							{
								dataCell.setCellValue(ServicesUtil.isEmpty(rowDto.getCellDtoList().get(i).getColumnValue())
										? " " : rowDto.getCellDtoList().get(i).getColumnValue().toString());
							}
							
							
							/*
							 * if(rowDto.getCellDtoList().get(i).getColumnType()
							 * .equals(EnDatabaseTypes.INTEGER)) Integer
							 * cellValue=null ;
							 * if(!ServicesUtil.isEmpty(rowDto.getCellDtoList().
							 * get(i).getColumnValue()))
							 * cellValue=(Integer)rowDto.getCellDtoList().get(i)
							 * .getColumnValue();
							 * dataCell.setCellValue(cellValue);
							 * 
							 * dataCell.setCellValue(rowDto.getCellDtoList().get
							 * (i).getColumnValue() == null ? "" :
							 * rowDto.getCellDtoList().get(i).getColumnValue().
							 * toString());
							 * if(rowDto.getCellDtoList().get(i).getColumnType()
							 * .equals(EnDatabaseTypes.VARCHAR))
							 * dataCell.setCellValue(rowDto.getCellDtoList().get
							 * (i).getColumnValue() == null ? "" :
							 * rowDto.getCellDtoList().get(i).getColumnValue().
							 * toString());
							 * if(rowDto.getCellDtoList().get(i).getColumnType()
							 * .equals(EnDatabaseTypes.BOOLEAN))
							 * dataCell.setCellValue(rowDto.getCellDtoList().get
							 * (i).getColumnValue() == null ? "" :
							 * rowDto.getCellDtoList().get(i).getColumnValue().
							 * toString());
							 */

						}
					}
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					workbook.write(bos);
					bytes = bos.toByteArray();
					workbook.close();
					if (!ServicesUtil.isEmpty(bytes)) {
						Base64 base64 = new Base64();
						fileDetailsDto.setFile(bytes);
						fileDetailsDto.setBase64(new String(base64.encode(bytes)));
						fileDetailsDto.setFilename(detailSheet.getSheetName() + "." + MurphyConstant.EXCEL_EXT);
						responseDto.setFileDetailsDto(fileDetailsDto);
						message.setStatus("SUCCESS");
						message.setStatusCode(MurphyConstant.CODE_SUCCESS);
						message.setMessage("EXCEL Bytes Successfully created");
						responseDto.setMessage(message);

					}
				} catch (IOException e) {
					logger.error("IO Exception" + e.getMessage());
					logger.error("[Murphy][exception excel download][requestPayloadDto][]");
					logger.error("[Murphy][exception excel download][requestPayloadDto][]" + e.getStackTrace());
					message.setStatus("FAILURE");
					message.setStatusCode(MurphyConstant.CODE_FAILURE);
					message.setMessage("EXCEL Bytes creation Failed" + e.getMessage());
					responseDto.setMessage(message);
				}

			} catch (Exception e) {
				logger.error("[Murphy][exception excel download][requestPayloadDto][]");
				logger.error("[Murphy][exception excel download][requestPayloadDto][]" + e.getStackTrace());
				message.setStatus("FAILURE");
				message.setStatusCode(MurphyConstant.CODE_FAILURE);
				message.setMessage("EXCEL Bytes creation Failed" + e.getMessage());
				responseDto.setMessage(message);
			}
		}

	}

}
