package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.integration.util.ApplicationConstant;
import com.murphy.taskmgmt.dto.EiAttachmentDto;
import com.murphy.taskmgmt.entity.EIAttachmentDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("EiAttachmentDao")
@Transactional
public class EiAttachmentDao extends BaseDao<EIAttachmentDo, EiAttachmentDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(EiAttachmentDao.class);

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][EiAttachmentDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}
	}
	
	@Override
	protected EIAttachmentDo importDto(EiAttachmentDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EIAttachmentDo entity = new EIAttachmentDo();
		entity.setAttachmentUrl(fromDto.getAttachmentUrl());
		entity.setCreatedAt(fromDto.getCreatedAt());
		entity.setCreatedBy(fromDto.getCreatedBy());
		entity.setId(fromDto.getId());
		entity.setFileId(fromDto.getFileId());
		entity.setFileName(fromDto.getFileName());
		entity.setFileType(fromDto.getFileType());
		entity.setFormId(fromDto.getFormId());
		entity.setIsolationId(fromDto.getIsolationId());
		return entity;
	}

	@Override
	protected EiAttachmentDto exportDto(EIAttachmentDo entity) {
		EiAttachmentDto toDto = new EiAttachmentDto();
		toDto.setAttachmentUrl(entity.getAttachmentUrl());
		toDto.setCreatedAt(entity.getCreatedAt());
		toDto.setCreatedBy(entity.getCreatedBy());
		toDto.setId(entity.getId());
		toDto.setFileId(entity.getFileId());
		toDto.setFileName(entity.getFileName());
		toDto.setFileType(entity.getFileType());
		toDto.setFormId(entity.getFormId());
		toDto.setIsolationId(entity.getIsolationId());
		return toDto;
	}

	public void createList(List<EiAttachmentDto> list, String formId, String isolationId) {
		if (list != null) {
			uploadAttachmentList(list, formId);
			logger.error("[Murphy][EiAttachmentDao][createList] upload successful");
			for (EiAttachmentDto eiAttachmentDto : list) {
				if (eiAttachmentDto.getAttachmentUrl() == null || eiAttachmentDto.getId() != null) {
					continue;
				}
				try {
					if (isolationId != null) {
						eiAttachmentDto.setIsolationId(isolationId);
					}
					eiAttachmentDto.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					eiAttachmentDto.setFormId(formId);
					eiAttachmentDto.setCreatedAt(new Date());
					create(eiAttachmentDto);
				} catch (ExecutionFault e) {
					e.printStackTrace();
				} catch (InvalidInputFault e) {
					e.printStackTrace();
				} catch (NoResultFault e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void uploadAttachmentList(List<EiAttachmentDto> list, String formId) {
		String uploadResponse = uploadDocument(list);
		if (uploadResponse != null) {
			Map<String, String> docUrlMap = processUploadResponse(uploadResponse);
			
			for (EiAttachmentDto eiAttachmentDto : list) {
				if (eiAttachmentDto.getAttachmentUrl() == null) {
					eiAttachmentDto.setAttachmentUrl(docUrlMap.get(eiAttachmentDto.getFileName()));
				}
			}
		}
		
	}
	
	private String uploadDocument(List<EiAttachmentDto> list) {
		JSONArray jsonArray = createJsonArrayFromAttachments(list);
		if (jsonArray.length() > 0) {
		RestUtil restClass = new RestUtil();
		HttpEntity responseEntity;
		String responseBody = null;
		String jsonObject = "{" + "\"documentDetailDtoList\": " + jsonArray + "}";
		try {
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			HttpResponse response = restClass.callDocService(ApplicationConstant.DOCUMENTSERVICES, jsonObject);
			responseEntity = response.getEntity();
			if (responseEntity != null) {
				responseBody = EntityUtils.toString(responseEntity);
			}
		} catch (Exception e) {
			logger.error("[Murphy][EiAttachmentDao] Exception" + e.getMessage());
		}
		return responseBody;
		} else {
			return null;
		}
	}
	
	private Map<String, String> processUploadResponse(String uploadResponse) {
		Map<String, String> mapFileWithUrl = null;
		mapFileWithUrl = new HashMap<String, String>();
		JSONArray responseJson = new JSONArray(uploadResponse);
		for (int i = 0; i < responseJson.length(); i++) {
			JSONObject jsonobject = responseJson.getJSONObject(i);
			JSONObject innerJsonObj = jsonobject.getJSONObject("documentDetailDto");
			if (innerJsonObj != null) {
				String documentUrl = innerJsonObj.getString("documentUrl");
				String documentName = innerJsonObj.getString("fileName");
				mapFileWithUrl.put(documentName, documentUrl);
			}
		}
		return mapFileWithUrl;
	}
	
	private JSONArray createJsonArrayFromAttachments(List<EiAttachmentDto> list) {
		JSONArray jsonResponse = new JSONArray();
		try {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getAttachmentUrl() == null) {
					JSONObject requestDto = new JSONObject();
					requestDto.put("folderName", MurphyConstant.EI_ATTACHMENT_FOLDER_NAME);
					requestDto.put("fileType", list.get(i).getFileType());
					requestDto.put("fileName", list.get(i).getFileName());
					requestDto.put("fileContent", list.get(i).getFileDoc());
					jsonResponse.put(requestDto);
				}
			}
		} catch (Exception e) {
			logger.error("Exception while creating Json Array in bypass log" + e.getMessage());
		}
		return jsonResponse;
	}
	
	@SuppressWarnings("unchecked")
	public List<EiAttachmentDto> getAttachment(String formId, String isolationId) {
		List<EiAttachmentDto> attachmentList = null;
		StringBuffer query = new StringBuffer("select * from EI_ATTACHMENT where FORM_ID = '" + formId + "'");
		if (isolationId != null) {
			query.append(" and ISOLATION_ID = '" + isolationId + "'");
		} else {
			query.append(" and ISOLATION_ID IS NULL");
		}
//		logger.error("[Murphy][EiAttachmentDao] Query: " + query);
		Query q = this.getSession().createSQLQuery(query.toString());
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			attachmentList = new ArrayList<EiAttachmentDto>();
			EiAttachmentDto attachment = null;
			for (Object[] obj : resultList) {
				attachment = new EiAttachmentDto();
				attachment.setId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				attachment.setFileName(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				attachment.setFileType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				attachment.setFileId(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
				attachment.setCreatedBy(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
				attachment.setCreatedAt(ServicesUtil.isEmpty(obj[5]) ? null : (Date) obj[5]);
				attachment.setAttachmentUrl(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
				attachment.setFormId(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
				attachment.setIsolationId(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
				attachmentList.add(attachment);
			}
		}
		return attachmentList;
	}
	
}
