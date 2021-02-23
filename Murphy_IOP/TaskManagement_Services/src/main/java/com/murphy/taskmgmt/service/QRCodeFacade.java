package com.murphy.taskmgmt.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.parser.JSONParser;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.murphy.taskmgmt.dao.ConfigDao;
import com.murphy.taskmgmt.dao.QRCodeDao;
import com.murphy.taskmgmt.dto.QRCodeDetailsDto;
import com.murphy.taskmgmt.dto.QRCodeDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.QRCodeDo;
import com.murphy.taskmgmt.scheduler.CanaryStagingScheduler;
import com.murphy.taskmgmt.service.interfaces.QRCodeFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

import edu.emory.mathcs.backport.java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service("QRCodeFacade")
public class QRCodeFacade implements QRCodeFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(QRCodeFacade.class);

	@Autowired
	QRCodeDao qrCodeDao;

	@Autowired
	private ConfigDao configDao;

	@Autowired
	CanaryStagingScheduler canaryStagingScheduler;

	@Override
	public void addQRCodeDetail(QRCodeDo qrCodeDo) {
		qrCodeDao.addQRCodeDetail(qrCodeDo);

	}

	@Override
	public void deleteQRCodeDetail(String qrCode) {
		qrCodeDao.deleteQRCodeDetail(qrCode);

	}

	@Override
	public List<QRCodeDo> QRCodeDetailList() {
		return qrCodeDao.getQRCodeDetailsList();

	}

	@Override
	public QRCodeDto GenerateQRCode(String id, int width, int height) {

		QRCodeDto dto = new QRCodeDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			QRCodeWriter writer = new QRCodeWriter();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			BitMatrix matrix = writer.encode(id, BarcodeFormat.QR_CODE, width, height);
			MatrixToImageWriter.writeToStream(matrix, "PNG", stream);
			stream.flush();
			byte[] data = stream.toByteArray();
			String qrCode = new String(Base64.encodeBase64(stream.toByteArray()), "utf-8");
			if (!qrCode.isEmpty()) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				dto.setQrCode(qrCode);
				dto.setMessage(responseMessage);
			}
		} catch (Exception e) {
			logger.error("[QRCodeFacade][GenerateQRCode] Exception " + e.getMessage());
		}
		return dto;
	}

	@Override
	public QRCodeDetailsDto getDetails(String qRNumber) {
		QRCodeDetailsDto qRCodeDetailsDto = new QRCodeDetailsDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		qRCodeDetailsDto.setResponseMessage(responseMessage);
		/*
		 * 
			
		 * */
		try {

			// JSONObject qrDetailJson = qrCodeDao.getCanaryDataForQR(qRNumber);
			org.json.JSONObject qrDetailJson = null;
			String userToken = canaryStagingScheduler.getUserToken();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -15);
			DateFormat sdf = new SimpleDateFormat(MurphyConstant.DATEFORMAT_FOR_CANARY);
			// String startTime = sdf.format(cal.getTime());
			// String endTime = sdf.format(new Date());
			String canaryUrl = "api/v1/getTagData";
			/*String startTime = ServicesUtil.convertFromZoneToZoneString(null, sdf.format(cal.getTime()),
					MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATEFORMAT_FOR_CANARY,
					MurphyConstant.DATEFORMAT_FOR_CANARY);
			String endTime = ServicesUtil.convertFromZoneToZoneString(null, sdf.format(new Date()),
					MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, MurphyConstant.DATEFORMAT_FOR_CANARY,
					MurphyConstant.DATEFORMAT_FOR_CANARY);*/
			String startTime = "2018-11-23T08:00:00.0000000-05";
			String endTime = "2018-11-23T09:30:00.0000000-05";
			qRCodeDetailsDto = qrCodeDao.getQRCodedeatils(qRNumber);
			// ring strDate = dateFormat.format(date);
			/*
			 * List<String> wells = new ArrayList<String>();
			 * wells.add("9264212700000000KON0038H1"); String startTime =
			 * "2018-11-23T08:00:00.0000000-05"; String endTime =
			 * "2018-11-23T09:30:00.0000000-05";
			 * 
			 * String[] canaryParam =
			 * {"PRCASXIN","PRTUBXIN","PRSTAXIN","QTGASD","QTOILD","QTH2OD"};
			 */
			List<String> payloadList = Arrays.asList(qRCodeDetailsDto.getCanaryTag());
			/*
			 * for (String well : wells) { for (String param : canaryParam) {
			 * payloadList.add("MUWI_Prod." + well + "." + param); } }
			 */
			String payload = "";
			HashMap<String , String> tagPressureMap = new HashMap<>();
			for (String st : payloadList) {
				payload = payload + st + ",";
			}
			payload = payload.substring(0, payload.length() - 1);
			payload = "[" + payload + "]";
			// String payload = canaryStagingScheduler.getPayloadInString(wells,
			// canaryParam);
			String canaryPayload = "{" + "\"userToken\": \"" + userToken + "\"," + "\"startTime\": \"" + startTime
					+ ":00\"," + "\"endTime\": \"" + endTime + ":00\"," + "\"aggregateName\": \"" + "\","
					+ "\"includeQuality\": false," + " \"MaxSize\": 4000000," + "\"continuation\": null," + "\"tags\": "
					+ payload + "" + "}";

			logger.error("CanaryStagingScheduler.getCanaryData()[canaryPayload]" + canaryPayload);

			String userName =MurphyConstant.CANARY_USERNAME;// configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF);
			String password = MurphyConstant.CANARY_PASSWORD;//configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF);
//			qrDetailJson = RestUtil.callRest(MurphyConstant.CANARY_API_HOST + canaryUrl, canaryPayload,
//					MurphyConstant.HTTP_METHOD_POST, userName, password);
//			// System.err.println(qrDetail);
//			JsonNode node = null;
//			if (!ServicesUtil.isEmpty(qrDetailJson) && qrDetailJson.toString().contains("data")) {
//				JSONParser parser = new JSONParser();
//				org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(qrDetailJson.toString());
//				node = new ObjectMapper().readTree(json.toString());
//				Iterator<String> fieldNamesIterator = node.get("data").fieldNames();
//				
//				while (fieldNamesIterator.hasNext()) {
//
//					String fieldName = fieldNamesIterator.next();
//					String canaryTag = fieldName.split("\\.")[2];
//					String pressure = "";
//					int arraySize = node.get("data").get(fieldName).size();
//					if (!ServicesUtil.isEmpty(node.get("data").get(fieldName)) && arraySize > 0) {
//						pressure = node.get("data").get(fieldName).get(arraySize - 1).get(1).asText();
//					}
//					/*qRCodeDetailsDto.setTagName(canaryTag);
//					qRCodeDetailsDto.setPressure(pressure);*/
//					
//					/*PRSTAXIN":
//					"PRCASXIN": Casing Pressure
//					"QTGASD": Gas Daily
//					"QTOILD": Oil Daily
//					"QTH2OD": Water Daily
//					"PRTUBXIN" :Tubing Pressure */
//					String tagDesc= "";
//					switch(canaryTag)
//					{
//					case "PRSTAXIN" :
//						/*tagDesc = "PRSTAXIN";
//						pressure = pressure + " psi";*/
//						tagDesc = "";
//						pressure = "";
//						break;
//						
//					case "PRCASXIN" :
//						tagDesc = "Casing Pressure";
//						pressure = pressure + " psi";
//						break;
//						
//					case "QTGASD" :
//						tagDesc = "Gas Daily";
//						pressure = pressure + " MCF";
//						break;
//						
//					case "QTH2OD" :
//						tagDesc = "Water Daily";
//						pressure = pressure + " bbls";
//						break;
//						
//					case "PRTUBXIN" :
//						tagDesc = "Tubing Pressure";
//						break;
//						
//					case "QTOILD" :
//						tagDesc = "Oil Daily";
//						pressure = pressure + " bbls";
//						break;
//					
//					}
//					
//					tagPressureMap.put(tagDesc, pressure);
//					
//					
//				}
//				
//				//qRCodeDetailsDto.setTagPressureMap(tagPressureMap); 
//				
//
//			}
//			
			tagPressureMap.put("Oil Daily", "102.3 bbls");
			tagPressureMap.put("Tubing Pressure", "");
			tagPressureMap.put("Water Daily", "206.3 bbls");
			tagPressureMap.put("Casing Pressure", "66 psi");
			tagPressureMap.put("Gas Daily", "38.9 MCF");
			qRCodeDetailsDto.setTagPressureMap(tagPressureMap); 
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			qRCodeDetailsDto.setResponseMessage(responseMessage);
		} catch (Exception e) {
			logger.error("[Murphy][QRCodeFacade][getDetails][error]" + e.getMessage());
			e.printStackTrace();
		}
		return qRCodeDetailsDto;
	}

}
