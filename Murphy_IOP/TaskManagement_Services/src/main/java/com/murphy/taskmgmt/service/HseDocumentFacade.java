package com.murphy.taskmgmt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.util.ApplicationConstant;
import com.murphy.taskmgmt.dao.HseDocumentDao;
import com.murphy.taskmgmt.dao.HseStringDao;
import com.murphy.taskmgmt.dto.DocServiceDto;
import com.murphy.taskmgmt.dto.HseDocumentDto;
import com.murphy.taskmgmt.dto.HseDocumentResponse;
import com.murphy.taskmgmt.dto.HseResponseBodyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.HseDocumentFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("hseDocumentFacade")
public class HseDocumentFacade implements HseDocumentFacadeLocal {
	private static final Logger logger = LoggerFactory.getLogger(HseDocumentFacade.class);

	@Autowired
	HseDocumentDao hseDocumentDao;

	@Autowired
	HseStringDao hseStringDao;

	@Override
	public ResponseMessage createHSEDocument(HseDocumentDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		String url = null;

		responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		Map<String, String> docFileUrlDetails = new HashMap<>();
		try {
			if (!ServicesUtil.isEmpty(dto)) {

				docFileUrlDetails = updateAttachmentToDocServices(dto);

				HseDocumentDto hseDocumentDto = new HseDocumentDto();
				hseDocumentDto.setAttachmentUrl(docFileUrlDetails.get(dto.getDocName()));

				logger.error("[Murphy][HseDocumentFacade][createHSEDocument][URL]" + hseDocumentDto.getAttachmentUrl());

				url = hseDocumentDto.getAttachmentUrl();
				String[] split = url.split("/");
				String documentId = split[split.length - 1];

				hseDocumentDto.setDocumentId(documentId);
				logger.error(
						"[Murphy][HseDocumentFacade][createHSEDocument][DocumentId]" + hseDocumentDto.getDocumentId());
				int version = hseDocumentDao.updateVersion();
				hseDocumentDto.setDocVersion(version);
				logger.error(
						"[Murphy][HseDocumentFacade][createHSEDocument][Version]" + hseDocumentDto.getDocVersion());
				hseDocumentDao.createHSEAttachment(hseDocumentDto);
			}

			responseMessage.setMessage(MurphyConstant.STATUS + " " + MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentFacade][createHSEDocument][error]" + e.getMessage());
		}

		return responseMessage;

	}

	private Map<String, String> updateAttachmentToDocServices(HseDocumentDto dto) {

		// int i = 0;
		// String fileName = "";

		String dstName = "";
		String fileType = "";
		String fileContent = "";
		Map<String, String> mapDocumentDetails = null;
		try {

			System.err.println("inside updateAttachmentToDocServices:" + dto);
			if (!ServicesUtil.isEmpty(dto)) {

				DocServiceDto docServiceDto = new DocServiceDto();
				ArrayList<DocServiceDto> serviceDto = new ArrayList<DocServiceDto>();
				dstName = dto.getDocName();
				fileType = dto.getDocType();
				fileContent = dto.getFileDoc();
				docServiceDto = createDocServiceDto(dstName, fileType, fileContent);
				serviceDto.add(docServiceDto);

				mapDocumentDetails = updateToDocServices(serviceDto);
			} else {
				logger.error("[HseDocumentFacade][updateAttachmentToDocServices][INFO]:Null input found");
			}
		} catch (Exception e) {
			logger.error("[HseDocumentFacade][updateAttachmentToDocServices][ERROR]" + e.getMessage());
		}
		return mapDocumentDetails;
	}

	private Map<String, String> updateToDocServices(ArrayList<DocServiceDto> docList) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failure While Updating to DocumentServices");
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		Map<String, String> mapFileWithUrl = new HashMap<String, String>();
		JSONArray jsonArray = createJsonArrayOfDocServices(docList);
		RestUtil restClass = new RestUtil();
		HttpEntity responseEntity;
		String responseBody = null;
		int statusCode = 0;
		JSONArray responseJson = null;

		String jsonObject = "{" + "\"documentDetailDtoList\": " + jsonArray + "}";
		// System.err.println("JsonObject"+jsonObject);
		try {
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			HttpResponse response = restClass.callDocService(ApplicationConstant.DOCUMENTSERVICES, jsonObject);
			responseEntity = response.getEntity();
			if (responseEntity != null) {
				responseBody = EntityUtils.toString(responseEntity);
			}
			statusCode = response.getStatusLine().getStatusCode();
			logger.error("[Murphy][HseDocumentFacade][updateToDocServices][info]" + statusCode + "   ResponseBody"
					+ responseBody);
			responseJson = new JSONArray(responseBody);
			for (int i = 0; i < responseJson.length(); i++) {
				JSONObject jsonobject = responseJson.getJSONObject(i);
				JSONObject innerJsonObj = jsonobject.getJSONObject("documentDetailDto");
				logger.error("[Murphy][HseDocumentFacade][updateToDocServices][innerJsonObj]" + innerJsonObj);
				if (innerJsonObj != null) {
					String documentUrl = innerJsonObj.getString("documentUrl");
					logger.error("[Murphy][HseDocumentFacade][updateToDocServices][Document url]" + documentUrl);
					String documentName = innerJsonObj.getString("fileName");
					logger.error("[Murphy][HseDocumentFacade][updateToDocServices][File name]" + documentName);
					mapFileWithUrl.put(documentName, documentUrl);
				}

			}

			logger.error("[updateToDocServices] ResponseStatus " + statusCode);

		} catch (Exception e) {
			logger.error("Exception in updateToDocServices" + e.getMessage());
		}
		return mapFileWithUrl;

	}

	private JSONArray createJsonArrayOfDocServices(ArrayList<DocServiceDto> ar) {
		JSONArray aProjects = new JSONArray();
		try {
			for (int i = 0; i < ar.size(); i++) {
				JSONObject requestDto = new JSONObject();
				requestDto.put("folderName", MurphyConstant.HSE_DOCUMENT_FOLDER_NAME);
				requestDto.put("fileType", ar.get(i).getFileType());
				requestDto.put("fileName", ar.get(i).getFileName());
				requestDto.put("fileContent", ar.get(i).getFileContent());
				aProjects.put(requestDto);
			}
		} catch (Exception e) {
			logger.error("Exception while creating Json Array in document log" + e.getMessage());
		}
		return aProjects;

	}

	private DocServiceDto createDocServiceDto(String fileName, String fileType, String fileContent) {

		DocServiceDto docDto = new DocServiceDto();
		try {
			docDto.setFileName(fileName);
			docDto.setFileType(fileType);
			docDto.setFileContent(fileContent);
		} catch (Exception e) {
			logger.error("Exception While create DocService Dto" + e.getMessage());
		}
		return docDto;

	}

	@Override
	public HseDocumentResponse getStringDocument(String text) {
		HseDocumentResponse responseDto = new HseDocumentResponse();

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			if (!ServicesUtil.isEmpty(text)) {
				HseDocumentDto dto = new HseDocumentDto();
				dto.setSearchString(text);
				String s = dto.getSearchString();
				logger.error("[Murphy][HseDocumentFacade][getStringDocument][serach string is] " + s);
				responseDto = hseStringDao.insertIntoTable(dto);
			}
			if (ServicesUtil.isEmpty(responseDto)) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentFacade][getStringList][error]" + e.getMessage());
		}

		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	@Override
	public HseResponseBodyDto getStringList() {
		HseResponseBodyDto responseDto = new HseResponseBodyDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			responseDto = hseStringDao.displayStringList();
			if (ServicesUtil.isEmpty(responseDto)) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentFacade][getStringList][error]" + e.getMessage());
		}

		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	@Override
	public HseResponseBodyDto getHseDocument() {

		HseResponseBodyDto responseDto = new HseResponseBodyDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			responseDto = hseDocumentDao.getHseDocument();
			if (ServicesUtil.isEmpty(responseDto)) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentFacade][getHseDocument][error]" + e.getMessage());
		}

		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	@Override
	public HseDocumentResponse getParagraph(int page, String text, String url) {
		HseDocumentResponse responseDto = new HseDocumentResponse();

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			if (!ServicesUtil.isEmpty(text)) {
				HseDocumentDto dto = new HseDocumentDto();
				dto.setSearchString(text);
				String s = dto.getSearchString();
				logger.error("[Murphy][HseDocumentFacade][getStringDocument][serach string is] " + s);
				responseDto = hseStringDao.getParagraph(page, text, url);
			}
			if (ServicesUtil.isEmpty(responseDto)) {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			} else {

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentFacade][getStringList][error]" + e.getMessage());
		}

		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

}
