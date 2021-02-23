package com.murphy.taskmgmt.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.AppFileDto;
import com.murphy.taskmgmt.dto.FileVersionDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.AppFileDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("AppFileDao")
public class AppFileDao extends BaseDao<AppFileDo, AppFileDto>{
	
	protected AppFileDo importDto(AppFileDto fromDto) {
		AppFileDo entity = new AppFileDo();
		entity.setFileName(fromDto.getFileName());
		entity.setFileType(fromDto.getFileType());
		entity.setFileUrl(fromDto.getFileUrl());
		entity.setFileVersion(fromDto.getFileVersion());
		entity.setFile(fromDto.getFile());
		entity.setApplication(fromDto.getApplication());
		entity.setOsVersion(fromDto.getOsVersion());
		return entity;
	}

	protected AppFileDto exportDto(AppFileDo entity) {
		AppFileDto dto = new AppFileDto();
		dto.setFileName(entity.getFileName());
		dto.setFileType(entity.getFileType());
		dto.setFileUrl(entity.getFileUrl());
		dto.setFileVersion(entity.getFileVersion());
		dto.setFile(entity.getFile());
		dto.setApplication(entity.getApplication());
		dto.setOsVersion(entity.getOsVersion());
		return dto;
	}

	public ResponseMessage insertFile(AppFileDto dto) throws ExecutionFault {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failed to Save File");
		responseMessage.setStatus("FAILURE");
		responseMessage.setStatusCode("1");
		
		if(!ServicesUtil.isEmpty(dto)) {
			this.getSession().persist(importDto(dto));
			responseMessage.setMessage("Sucessfully Inserted File");
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("0");
		}
		
		return responseMessage;
	}

	public AppFileDto getFile(String fileType, String application,Double osVersion) {
		AppFileDo appFileDo = null;
		Query query = null;
		String osVersionInDB = null;
		FileVersionDto versionDto = this.getVersionList(fileType, application,osVersion);
		// Checking os_version for IOP app
		if(!ServicesUtil.isEmpty(osVersion)){
			if(osVersion >= 12.1 && osVersion < 13.0)
				osVersionInDB = "12";
			else if(osVersion >= 13.0)
				osVersionInDB = "14";
		}
		// // Find max os_version if parameter osVersion is not passed from mobile for IPA
		if(ServicesUtil.isEmpty(osVersion) && fileType.equalsIgnoreCase("IPA")){
			String queryString = "SELECT MAX(OS_VERSION) AS VERSION FROM APP_FILE WHERE APPLICATION = '"
					+ application + "' AND FILE_TYPE = '" + fileType +"'";
			Object obj = this.getSession().createSQLQuery(queryString).uniqueResult();
			if(!ServicesUtil.isEmpty(obj))
				osVersionInDB = obj.toString();
		}
		// Considering os_version
		if(!ServicesUtil.isEmpty(osVersionInDB)){
			query = getSession().createQuery(
					"from AppFileDo where fileType = :fileType and fileVersion = :fileVersion and application = :application "
					+ "and osVersion = :osVersion");
			query.setParameter("fileType", fileType);
			query.setParameter("application", application);
			query.setParameter("fileVersion", versionDto.getFileVersion());
			query.setParameter("osVersion", osVersionInDB);
		}
		// Without os_version consideration
		else{
			query = getSession().createQuery(
					"from AppFileDo where fileType = :fileType and fileVersion = :fileVersion and application = :application");
			query.setParameter("fileType", fileType);
			query.setParameter("application", application);
			query.setParameter("fileVersion", versionDto.getFileVersion());
		}
		appFileDo = (AppFileDo) query.uniqueResult();
		if(!ServicesUtil.isEmpty(appFileDo))
			return exportDto(appFileDo);
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	public FileVersionDto getVersionList(String fileType, String application,Double osVersion) {
		FileVersionDto versionDto = new FileVersionDto();
		String osVersionInDB = null;
		// Checking os_version for IOP app
		if(!ServicesUtil.isEmpty(osVersion)){
			if(osVersion >= 12.1 && osVersion < 13.0)
				osVersionInDB = "12";
			else if(osVersion >= 13.0)
				osVersionInDB = "14";
		}
		// Find max os_version if parameter osVersion is not passed from mobile for IPA
		if(ServicesUtil.isEmpty(osVersion) && fileType.equalsIgnoreCase("IPA")){
			String queryString = "SELECT MAX(OS_VERSION) AS VERSION FROM APP_FILE WHERE APPLICATION = '"
					+ application + "' AND FILE_TYPE = '" + fileType +"'";
			Object obj = this.getSession().createSQLQuery(queryString).uniqueResult();
			if(!ServicesUtil.isEmpty(obj))
				osVersionInDB = obj.toString();
		}
		String queryString = null;
		// Considering os_version
		if(!ServicesUtil.isEmpty(osVersionInDB)){
			queryString = "SELECT FILE_TYPE AS FILE_TYPE, MAX(FILE_VERSION) AS VERSION FROM APP_FILE WHERE APPLICATION = '"
					+ application + "' AND FILE_TYPE = '" + fileType + "' AND OS_VERSION = '" +osVersionInDB + "' GROUP BY(FILE_TYPE)";
		}
		// Without os_version consideration
		else{
			queryString = "SELECT FILE_TYPE AS FILE_TYPE, MAX(FILE_VERSION) AS VERSION FROM APP_FILE WHERE APPLICATION = '"
					+ application + "' AND FILE_TYPE = '" + fileType + "' GROUP BY(FILE_TYPE)";
		}
		List<Object[]> result = getSession().createSQLQuery(queryString)
				.list();
		if (!ServicesUtil.isEmpty(result)) {
			for (Object[] obj : result) {
				versionDto.setFileType((String) obj[0]);
				versionDto.setFileVersion((String) obj[1]);
				versionDto.setApplication(application);
			}
		}
		return versionDto;
	}
}
