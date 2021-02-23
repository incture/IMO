package com.murphy.taskmgmt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.murphy.taskmgmt.dao.AttachmentDao;
import com.murphy.taskmgmt.dao.CollaborationDao;
import com.murphy.taskmgmt.dao.ImageProcessingDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.dto.AttachmentDto;
import com.murphy.taskmgmt.dto.AttachmentResponseDto;
import com.murphy.taskmgmt.dto.CollaborationDto;
import com.murphy.taskmgmt.dto.CollaborationResponseDto;
import com.murphy.taskmgmt.dto.DocServiceDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.CollaborationFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("collaborationFacade")
public class CollaborationFacade implements CollaborationFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(CollaborationFacade.class);

	public CollaborationFacade() {
	}

	@Autowired
	private CollaborationDao collabDao;

	@Autowired
	private AttachmentDao attachDao;

	@Autowired
	private TaskEventsDao taskDao;

	@Autowired
	private ImageProcessingDao imageDao;
	
	//Adding for incident INC0077951
	@Autowired
	private TaskEventsDao taskEventsDao;

	@Override
	public ResponseMessage createCollaboration(CollaborationDto dto) {

		return createCollaboration(dto, null);
	}
	
	
	public ResponseMessage createCollaboration(CollaborationDto dto, String parentOrigin) {
//		logger.error("[createCollaboration][parentOrigin]"+parentOrigin);
		ResponseMessage responseDto = new ResponseMessage();
		Map<String,String> docFileUrlDetails=new HashMap<>();
		String messageId = UUID.randomUUID().toString().replaceAll("-", "");
		dto.setMessageId(messageId);
		dto.setCreatedAt(new Date());
		Boolean runSchdeluer = false;
		String documentName=null;
		taskDao.updateTaskEventStatus(dto.getTaskId(), "", "", dto.getUserDisplayName(), "", null, "",runSchdeluer,null);
		
		

		if (!ServicesUtil.isEmpty(dto.getMessage())) {
			dto.setMessage(dto.getMessage() + " ");
		}
		
		
		if (!ServicesUtil.isEmpty(dto.getAttachmentDetails())) {
			if(parentOrigin==null){
				parentOrigin = collabDao.getParentOrigin(dto.getTaskId());
			}
				docFileUrlDetails=collabDao.updateAttachmentToDocServices(dto,parentOrigin);
				if(docFileUrlDetails.size()==dto.getAttachmentDetails().size())
				{
			   for (AttachmentDto attachDto : dto.getAttachmentDetails()) {
				attachDto.setMappingId(messageId);
				try {
					String fileType = attachDto.getFileType();
					if(parentOrigin.equalsIgnoreCase("Pigging")){
						documentName=attachDto.getFileName();
						int i = documentName.contains(".") ? documentName.lastIndexOf('.') : documentName.length();
						documentName = documentName.substring(0, i) + dto.getTaskId() + documentName.substring(i);
					}
					else{
						documentName=attachDto.getFileName();
					}
					String documentId=docFileUrlDetails.get(documentName);
					attachDto.setDocumentId(documentId);
					if (MurphyConstant.FILE_TYPE_JPEG.equals(fileType) || MurphyConstant.FILE_TYPE_JPG.equals(fileType)
							|| MurphyConstant.FILE_TYPE_PNG.equals(fileType)) {
						attachDto.setCompressedFile(
								imageDao.getCompressedImage(attachDto.getFileDoc(), attachDto.getFileType()));
					} else {
						attachDto.setCompressedFile(attachDto.getFileDoc());
					}
					attachDao.create(attachDto);
				} catch (Exception e) {
					logger.error("[Murphy][CollaborationFacade][createCollaboration][error]" + e.getMessage() + ""
							+ e.getLocalizedMessage());
				}
			}
		}
				}
				if (collabDao.createCollaborationDetail(dto).equals(MurphyConstant.SUCCESS)) {
					responseDto.setMessage(MurphyConstant.CREATED_SUCCESS);
					responseDto.setStatus(MurphyConstant.SUCCESS);
					responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
					return responseDto;
				}
				responseDto.setMessage(MurphyConstant.CREATE_FAILURE);
				responseDto.setStatus(MurphyConstant.FAILURE);
				responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
				
				return responseDto;
	}

	@Override
	public CollaborationResponseDto getCollaboration(String processId, String userType) {
		CollaborationResponseDto responseDto = new CollaborationResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			//Adding for incident INC0077951
			String processIds = "";
			String process = "";
			//Add collaboration if any prev task exist
			if(!ServicesUtil.isEmpty(processId)){
				processIds = taskEventsDao.processIdHavingPrevTask(processId);
				if(!ServicesUtil.isEmpty(processIds)){
					process += processIds +"','"+processId ;
				}else
					process = processId;
			}
			List<CollaborationDto> collList = collabDao.getCollaborationsById(process, userType);
			if (!ServicesUtil.isEmpty(collList)) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				responseDto.setCollaborationDtos(collList);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][CollaborationFacade][getCollaboration][error]" + e.getMessage() + ""
					+ e.getLocalizedMessage());
		}

		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	/*
	 * @Override public AttachmentDto getAttachmentById(String taskId) {
	 * AttachmentDto dto = attachDao.getFile(taskId); return dto; }
	 */

	@Override
	public AttachmentResponseDto getAttachmentById(String fileId) {
		AttachmentResponseDto dto = new AttachmentResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			AttachmentDto attachmentDto = attachDao.getFile(fileId);
			if (!ServicesUtil.isEmpty(attachmentDto)) {
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
				dto.setDto(attachmentDto);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			dto.setMessage(responseMessage);
		} catch (Exception e) {
			logger.error("[Murphy][CollaborationFacade][getAttachmentById][error]" + e.getMessage() + ""
					+ e.getLocalizedMessage());
		}

		return dto;
	}

	@Override
	public ResponseMessage removeAttachment(String fileId) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			String result = attachDao.removeAttachment(fileId);
			if (result.equals(MurphyConstant.SUCCESS)) {
				responseMessage.setMessage(MurphyConstant.DELETE_SUCCESS);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} else
				responseMessage.setMessage(MurphyConstant.DELETE_FAILURE);
		} catch (Exception e) {
			logger.error("[Murphy][CollaborationFacade][removeAttachment][error]" + e.getLocalizedMessage());
		}
		return responseMessage;
	}

	/*public Map<String, String> updateAttachmentToDocServices(CollaborationDto dto, String parentOrigin) {
		int i = 0;
		String fileName = "";
		String dstName = "";
		String fileType = "";
		String fileContent = "";
		Map<String, String> mapDocumentDetails = null;
		try {

			System.err.println("CollaborationDato for  document Service:" + dto);
			if (!ServicesUtil.isEmpty(dto) && !ServicesUtil.isEmpty(dto.getAttachmentDetails())) {
				List<AttachmentDto> attachmentDto = dto.getAttachmentDetails();
				DocServiceDto docServiceDto = new DocServiceDto();
				ArrayList<DocServiceDto> serviceDto = new ArrayList<DocServiceDto>();
				for (AttachmentDto attachment : attachmentDto) {
					if(parentOrigin.equalsIgnoreCase("Pigging")){
					fileName = attachment.getFileName();
					i = fileName.contains(".") ? fileName.lastIndexOf('.') : fileName.length();
					dstName = fileName.substring(0, i) + dto.getTaskId() + fileName.substring(i);
					}
					else{
						dstName = attachment.getFileName();
					}
					fileType = attachment.getFileType();
					fileContent = attachment.getFileDoc();
					docServiceDto = createDocServiceDto(dstName, fileType, fileContent);
					serviceDto.add(docServiceDto);

				}
				mapDocumentDetails = updateToDocServices(serviceDto, parentOrigin);
			} else {
				logger.error("[CollaborationFacade][updateAttachmentToDocServices][INFO]:Null input found");
			}
		} catch (Exception e) {
			logger.error("[CollaborationFacade][updateAttachmentToDocServices][ERROR]" + e.getMessage());
		}
		return mapDocumentDetails;
	}*/

	/*public Map<String, String> updateToDocServices(ArrayList<DocServiceDto> docList, String parentOrigin) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failure While Updating to DocumentServices");
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		Map<String, String> mapFileWithUrl = new HashMap<String, String>();
		JSONArray jsonArray = createJsonArrayOfDocServices(docList,parentOrigin);
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
			logger.error("[Murphy][CollaborationFacade][updateAttachmentToDocServices][info]" + statusCode
					+ "ResponseBody" + responseBody);
			responseJson = new JSONArray(responseBody);
			for (int i = 0; i < responseJson.length(); i++) {
				JSONObject jsonobject = responseJson.getJSONObject(i);
				JSONObject innerJsonObj = jsonobject.getJSONObject("documentDetailDto");
				if (innerJsonObj != null) {
					String documentUrl = innerJsonObj.getString("documentId");
					String documentName = innerJsonObj.getString("fileName");
					mapFileWithUrl.put(documentName, documentUrl);
				}

			}
			
			logger.error("[updateToDocServices] ResponseStatus " + statusCode);

		} catch (Exception e) {
			logger.error("Exception" + e.getMessage());
		}
		return mapFileWithUrl;

	}*/

	/*public DocServiceDto createDocServiceDto(String fileName, String fileType, String fileContent) {
		DocServiceDto docDto = new DocServiceDto();
		try{
		docDto.setFileName(fileName);
		docDto.setFileType(fileType);
		docDto.setFileContent(fileContent);
		}
		catch(Exception e){
			logger.error("Exception While create DocService Dto"+e.getMessage());
		}
		return docDto;
	}

	public JSONArray createJsonArrayOfDocServices(ArrayList<DocServiceDto> ar, String parentOrigin) {
		JSONArray aProjects = new JSONArray();
		try{
		for (int i = 0; i < ar.size(); i++) {
			JSONObject requestDto = new JSONObject();
			requestDto.put("folderName", "SCPApps/Iop/"+parentOrigin);
			requestDto.put("fileType", ar.get(i).getFileType());
			requestDto.put("fileName", ar.get(i).getFileName());
			requestDto.put("fileContent", ar.get(i).getFileContent());
			aProjects.put(requestDto);
		}
		}
		catch(Exception e){
			logger.error("Exception while creating Json Array "+e.getMessage());
		}
		return aProjects;

	}*/
}
