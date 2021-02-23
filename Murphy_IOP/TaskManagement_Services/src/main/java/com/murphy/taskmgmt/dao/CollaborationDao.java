package com.murphy.taskmgmt.dao;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.hibernate.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.util.ApplicationConstant;
import com.murphy.taskmgmt.dto.AttachmentDto;
import com.murphy.taskmgmt.dto.CollaborationDto;
import com.murphy.taskmgmt.dto.DocServiceDto;
import com.murphy.taskmgmt.entity.CollaborationDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.CollaborationFacade;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("collaborationDao")
public class CollaborationDao extends BaseDao<CollaborationDo, CollaborationDto> {

	private static final Logger logger = LoggerFactory.getLogger(CollaborationDao.class);

	public CollaborationDao() {
	}

	@Autowired
	AttachmentDao attachDao;

	@Autowired
	TaskEventsDao taskEventsDao;
	
	@Autowired
    NotificationDao notificationDao;

	// @Autowired
	// CollaborationFacade collaborationFacade;

	@Override
	protected CollaborationDo importDto(CollaborationDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		CollaborationDo entity = new CollaborationDo();
		if (!ServicesUtil.isEmpty(fromDto.getMessageId()))
			entity.setMessageId(fromDto.getMessageId());
		if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
			entity.setProcessId(fromDto.getProcessId());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getUserDisplayName()))
			entity.setUserDisplayName(fromDto.getUserDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getMessage()))
			entity.setMessage(fromDto.getMessage());
		/*
		 * if (!ServicesUtil.isEmpty(fromDto.getChatId()))
		 * entity.setChatId(fromDto.getChatId()); if
		 * (!ServicesUtil.isEmpty(fromDto.getChatDisplayName()))
		 * entity.setChatDisplayName(fromDto.getChatDisplayName()); if
		 * (!ServicesUtil.isEmpty(fromDto.getTaggedUserId()))
		 * entity.setTaggedUserId(fromDto.getTaggedUserId());
		 */

		return entity;
	}

	@Override
	protected CollaborationDto exportDto(CollaborationDo entity) {

		CollaborationDto dto = new CollaborationDto();
		if (!ServicesUtil.isEmpty(entity.getMessageId()))
			dto.setMessageId(entity.getMessageId());
		if (!ServicesUtil.isEmpty(entity.getProcessId()))
			dto.setProcessId(entity.getProcessId());
		if (!ServicesUtil.isEmpty(entity.getTaskId()))
			dto.setTaskId(entity.getTaskId());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			dto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getUserDisplayName()))
			dto.setUserDisplayName(entity.getUserDisplayName());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getMessage()))
			dto.setMessage(entity.getMessage());
		/*
		 * if (!ServicesUtil.isEmpty(entity.getChatId()))
		 * dto.setChatId(entity.getChatId()); if
		 * (!ServicesUtil.isEmpty(entity.getChatDisplayName()))
		 * dto.setChatDisplayName(entity.getChatDisplayName()); if
		 * (!ServicesUtil.isEmpty(entity.getTaggedUserId()))
		 * dto.setTaggedUserId(entity.getTaggedUserId());
		 */
		return dto;
	}

	public String createCollaborationDetail(CollaborationDto dto) {
		String response = MurphyConstant.FAILURE;
		try {
			create(dto);
			notificationDao.createAlertForStatusChange(dto.getTaskId(),MurphyConstant.TASK, null);
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Murphy][CollaborationChatDao][createTaskOwner][failed]" + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public List<CollaborationDto> getCollaborationsById(String processId, String userType) {
		List<CollaborationDto> responseDto = null;
		AttachmentDto attachDto = null;
		// String queryString = "Select coll.MESSAGE_ID AS MESSAGE_ID
		// ,coll.PROCESS_ID AS PROCESS_ID,coll.TASK_ID AS TASK_ID ,coll.USER_ID
		// AS USER_ID"
		// + ",coll.USER_DISPLAY_NAME AS USER_DISPLAY_NAME,coll.CREATED_AT AS
		// CREATED_AT,coll.MESSAGE AS MESSAGE"
		// + ",att.FILE_NAME AS FILE_NAME , att.FILE_TYPE AS
		// FILE_TYPE,att.COMPRESSED_FILE AS COMPRESSED_FILE, att.FILE_ID AS
		// FILE_ID "
		// + " from TM_COLLABORATION coll left outer join TM_ATTACHMENT att on
		// coll.MESSAGE_ID = att.MAPPING_ID ";
		String queryString = "Select coll.MESSAGE_ID AS MESSAGE_ID ,coll.PROCESS_ID AS PROCESS_ID,coll.TASK_ID AS TASK_ID ,coll.USER_ID AS USER_ID"
				+ ",coll.USER_DISPLAY_NAME AS USER_DISPLAY_NAME,coll.CREATED_AT AS CREATED_AT,coll.MESSAGE AS MESSAGE"
				+ ",att.FILE_NAME AS FILE_NAME , att.FILE_TYPE AS FILE_TYPE,att.COMPRESSED_FILE AS COMPRESSED_FILE, att.FILE_ID AS FILE_ID,att.DOCUMENT_ID AS DOCUMENT_ID"
				+ " from TM_COLLABORATION coll left outer join TM_ATTACHMENT att on coll.MESSAGE_ID = att.MAPPING_ID ";

		// if (MurphyConstant.USER_TYPE_POT.equals(userType)) {
		queryString += " where coll.process_id IN ('" + processId + "')";
		// } else if (MurphyConstant.USER_TYPE_ALS.equals(userType) ||
		// MurphyConstant.USER_TYPE_ENG.equals(userType)) {
		// queryString += " where coll.process_id ='" +
		// taskEventsDao.getProcessIdFromTask(taskId) + "'";
		// } else {
		// queryString += " where coll.TASK_ID='" + taskId + "'";
		// }
		queryString += " order by 6 asc ";
		logger.error("[CollaborationDao][getCollaborationsById][QueryString] "+queryString);
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			responseDto = new ArrayList<CollaborationDto>();
			Map<String, Integer> locationMap = new HashMap<String, Integer>();

			for (Object[] obj : resultList) {
				String listItem = (String) obj[0];
				if (locationMap.containsKey(listItem) && !ServicesUtil.isEmpty(obj[0])) {
					if (!ServicesUtil.isEmpty(obj[10])) {

						responseDto.get(locationMap.get(listItem)).getAttachmentDetails().add(getAttachmentDto(obj));
					}
				} else {

					CollaborationDto dto = getCollabDto(obj);
					if (!ServicesUtil.isEmpty(obj[10])) {
						attachDto = getAttachmentDto(obj);
						// if(!ServicesUtil.isEmpty(obj[10])){
						dto.setAttachmentDetails(new ArrayList<AttachmentDto>());
						dto.getAttachmentDetails().add(attachDto);
						// dto.setDocumentUrl(attachDto.getDocumentUrl());

					}
					// dto.setDocumentUrl(attachDto.getDocumentUrl());
					responseDto.add(dto);
					locationMap.put(listItem, responseDto.size() - 1);
				}
			}
		}
		return responseDto;
	}

	private CollaborationDto getCollabDto(Object[] obj) {
		CollaborationDto dto = null;
		String documentId = null;
		try {
			dto = new CollaborationDto();
			dto.setMessageId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
			dto.setProcessId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
			dto.setTaskId(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
			dto.setUserId(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
			dto.setUserDisplayName(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
			dto.setCreatedAt(ServicesUtil.isEmpty(obj[5]) ? null: ((Date)obj[5]));
			
			//Converting to epoch
			Date created_at = ServicesUtil.isEmpty(obj[5]) ? null: (Date)obj[5];
			if(!ServicesUtil.isEmpty(created_at)){
				long epoch = created_at.getTime();
				dto.setCreatedAtDisplay(String.valueOf(epoch));
			}
			else
				dto.setCreatedAtDisplay(null);
				
			
			/*dto.setCreatedAtDisplay(ServicesUtil.isEmpty(obj[5]) ? null
					: ServicesUtil.convertFromZoneToZoneString(null, obj[5], MurphyConstant.UTC_ZONE,
							MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE,
							MurphyConstant.DATE_DISPLAY_FORMAT)); */
			dto.setMessage(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);

			/*
			 * //Dms call for null DocumentId
			 * documentId=ServicesUtil.isEmpty(obj[11]) ? null : (String)
			 * obj[11]; if(ServicesUtil.isEmpty(documentId)){ AttachmentDto
			 * attachDto=getAttachmentDto(obj);
			 * if(!ServicesUtil.isEmpty(attachDto.getDocumentId())){
			 * updateDocId(attachDto.getFileId(),attachDto.getDocumentId());
			 * dto.setDocumentUrl(getDocumentUrlForId(attachDto.getDocumentId())
			 * ); } } else{ dto.setDocumentUrl(getDocumentUrlForId(documentId));
			 * }
			 */
			return dto;
		} catch (Exception ex) {
			logger.error("Exception While fetching CollabDto" + ex.getMessage());
		}
		return dto;
	}

	private String updateDocId(String fileId, String docId) {
		Integer result = null;
		try {
			String updateQuery = "UPDATE TM_ATTACHMENT SET DOCUMENT_ID='" + docId + "' WHERE FILE_ID='" + fileId + "'";
			Query q = this.getSession().createSQLQuery(updateQuery);
			result = q.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception While DocumentId" + e.getMessage());
		}
		if (result > 0)
			return MurphyConstant.SUCCESS;
		else
			return MurphyConstant.NO_RECORD;

	}

	private AttachmentDto getAttachmentDto(Object[] obj) {
		DocServiceDto docServiceDto = new DocServiceDto();
		ArrayList<DocServiceDto> serviceDto = new ArrayList<DocServiceDto>();
		String docUrl = null;
		String documentId = null;
		AttachmentDto dto = null;
		try {
			if (!ServicesUtil.isEmpty(obj[10])) {
				dto = attachDao.getFile((String) obj[10]);
				String filedoc = dto.getFileDoc();
				dto.setFileDoc(null);
				if (ServicesUtil.isEmpty(dto.getDocumentId())) {
					docServiceDto.setFileContent(filedoc);
					docServiceDto.setFileName(dto.getFileName());
					docServiceDto.setFileType(dto.getFileType());
					serviceDto.add(docServiceDto);
					String parentOrigin = getParentOrigin((String) obj[2]);
					Map<String, String> documentsDetail = updateToDocServices(serviceDto, parentOrigin);
					documentId = documentsDetail.get(docServiceDto.getFileName());
					updateDocId(dto.getFileId(), documentId);
					docUrl = getDocumentUrlForId(documentId);
					dto.setDocumentId(documentId);
					dto.setDocumentUrl(docUrl);
				} else if (!ServicesUtil.isEmpty(dto.getDocumentId())) {
					docUrl = getDocumentUrlForId(dto.getDocumentId());
					dto.setDocumentUrl(docUrl);
				}

				return dto;
			}

		} catch (Exception e) {
			logger.error("Exception while Fetching Attachment Details" + e.getMessage());
		}
		// AttachmentDto dto = new AttachmentDto();
		// dto.setFileName(ServicesUtil.isEmpty(obj[7]) ? null: (String)
		// obj[7]);
		// dto.setFileType(ServicesUtil.isEmpty(obj[8]) ? null: (String)
		// obj[8]);
		//
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// try {
		// ObjectOutput out = new ObjectOutputStream(bos);
		// out.writeObject(obj[9]);
		//
		// out.flush();
		// byte[] yourBytes = bos.toByteArray();
		//
		// dto.setFileDoc(new String(yourBytes, StandardCharsets.UTF_8));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// // String fileDoc=Base64.encodeBase64String(out.toByteArray());
		//
		// return dto;
		return null;

		// Reader reader = new InputStreamReader(new
		// ByteArrayInputStream(content));
		//
		// String s = FileCopyUtils.copyToString(reader);
	}

	public String getParentOrigin(String taskId) {
		String parentOrigin = null;
		try {
			logger.error("Inside Get Parent Origin");
			String selectQuery = "SELECT PARENT_ORIGIN FROM TM_TASK_EVNTS WHERE TASK_ID='" + taskId + "'";
			logger.error("Query" + selectQuery);
			Query q = this.getSession().createSQLQuery(selectQuery);
			List<String> parentOriginList = (List<String>) q.list();
			logger.error("parentOrigin" + parentOriginList);
			if (!ServicesUtil.isEmpty(parentOriginList)) {
				for (String str : parentOriginList) {
					parentOrigin = str;
					logger.error("parentOrigin" + parentOrigin);
				}
			}
		} catch (Exception e) {
			logger.error("Exception While Fetching Parent_Id" + e.getMessage());
		}
		return parentOrigin;
	}

	public String getDocumentUrlForId(String docId) {
		String docUrl = null;
		try {
			if (!ServicesUtil.isEmpty(docId)) {

				String selectQuery = "Select DOCUMENT_URL from " + ApplicationConstant.DOCUMENTSERVICE_SCHEMA
						+ ".DOCUMENTS_DETAIL WHERE DOCUMENT_ID='" + docId + "'";

				Query q = this.getSession().createSQLQuery(selectQuery);
				List<String> documentUrl = (List<String>) q.list();
				if (!ServicesUtil.isEmpty(documentUrl)) {
					for (String str : documentUrl) {
						docUrl = str;
					}
				}

			}
		} catch (Exception ex) {
			logger.error("Exception While Fetching URl" + ex.getMessage());
		}
		return docUrl;
	}

	public Map<String, String> updateAttachmentToDocServices(CollaborationDto dto, String parentOrigin) {
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
					if (parentOrigin.equalsIgnoreCase("Pigging")) {
						fileName = attachment.getFileName();
						i = fileName.contains(".") ? fileName.lastIndexOf('.') : fileName.length();
						dstName = fileName.substring(0, i) + dto.getTaskId() + fileName.substring(i);
					} else {
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
	}

	public DocServiceDto createDocServiceDto(String fileName, String fileType, String fileContent) {
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

	public JSONArray createJsonArrayOfDocServices(ArrayList<DocServiceDto> ar, String parentOrigin) {
		JSONArray aProjects = new JSONArray();
		try {
			for (int i = 0; i < ar.size(); i++) {
				JSONObject requestDto = new JSONObject();
				requestDto.put("folderName", "SCPApps/Iop/" + parentOrigin);
				requestDto.put("fileType", ar.get(i).getFileType());
				requestDto.put("fileName", ar.get(i).getFileName());
				requestDto.put("fileContent", ar.get(i).getFileContent());
				aProjects.put(requestDto);
			}
		} catch (Exception e) {
			logger.error("Exception while creating Json Array " + e.getMessage());
		}
		return aProjects;

	}

	public Map<String, String> updateToDocServices(ArrayList<DocServiceDto> docList, String parentOrigin) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failure While Updating to DocumentServices");
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		Map<String, String> mapFileWithUrl = new HashMap<String, String>();
		JSONArray jsonArray = createJsonArrayOfDocServices(docList, parentOrigin);
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

	}

}
