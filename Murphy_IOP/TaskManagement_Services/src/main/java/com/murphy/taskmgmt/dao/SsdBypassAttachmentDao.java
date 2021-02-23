/**
 * 
 */
package com.murphy.taskmgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.SsdBypassAttachementDto;
import com.murphy.taskmgmt.entity.SsdBypassAttachmentsDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * @author Kamlesh.Choubey
 *
 */

@Repository("ssdBypassAttachmentDao")
public class SsdBypassAttachmentDao extends BaseDao<SsdBypassAttachmentsDo, SsdBypassAttachementDto> {

	private static final Logger logger = LoggerFactory.getLogger(TaskEventsDao.class);

	@Override
	protected SsdBypassAttachmentsDo importDto(SsdBypassAttachementDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		SsdBypassAttachmentsDo entity = new SsdBypassAttachmentsDo();

		if (!ServicesUtil.isEmpty(fromDto.getDocumentId()))
			entity.setDocumentId(fromDto.getDocumentId());

		if (!ServicesUtil.isEmpty(fromDto.getFileId()))
			entity.setFileId(fromDto.getFileId());

		if (!ServicesUtil.isEmpty(fromDto.getFileName()))
			entity.setFileName(fromDto.getFileName());

		if (!ServicesUtil.isEmpty(fromDto.getFileType()))
			entity.setFileType(fromDto.getFileType());

		if (!ServicesUtil.isEmpty(fromDto.getBypassId()))
			entity.setBypassId(fromDto.getBypassId());

		if (!ServicesUtil.isEmpty(fromDto.getCreatedBy()))
			entity.setCreatedBy(fromDto.getCreatedBy());

		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getAttachmentUrl()))
			entity.setAttachmentUrl(fromDto.getAttachmentUrl());
		return entity;
	}

	@Override
	protected SsdBypassAttachementDto exportDto(SsdBypassAttachmentsDo entity) {

		SsdBypassAttachementDto dto = new SsdBypassAttachementDto();
		if (!ServicesUtil.isEmpty(entity.getDocumentId()))
			dto.setDocumentId(entity.getDocumentId());

		if (!ServicesUtil.isEmpty(entity.getFileId()))
			dto.setFileId(entity.getFileId());

		if (!ServicesUtil.isEmpty(entity.getFileName()))
			dto.setFileName(entity.getFileName());

		if (!ServicesUtil.isEmpty(entity.getFileType()))
			dto.setFileType(entity.getFileType());

		if (!ServicesUtil.isEmpty(entity.getBypassId()))
			dto.setBypassId(entity.getBypassId());

		if (!ServicesUtil.isEmpty(entity.getCreatedBy()))
			dto.setCreatedBy(entity.getCreatedBy());

		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(entity.getCreatedAt());

		if (!ServicesUtil.isEmpty(entity.getAttachmentUrl()))
			dto.setAttachmentUrl(entity.getAttachmentUrl());

		return dto;
	}

	/*
	 * public List<SsdBypassAttachementDto>
	 * exportDtoList(List<SsdBypassAttachmentsDo> entities) {
	 * List<SsdBypassAttachementDto> ssdBypassAttachementDtoList = new
	 * ArrayList<>(); for (SsdBypassAttachmentsDo entity : entities) {
	 * ssdBypassAttachementDtoList.add(exportDto(entity)); } return
	 * ssdBypassAttachementDtoList; }
	 */

	public String createBypassLogAttachment(SsdBypassAttachementDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			create(dto);
			reponse = MurphyConstant.SUCCESS;
			this.getSession().flush();

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassAttachmentDao][createBypassLogComment][error]" + e.getMessage());
			this.getSession().flush();

		}
		return reponse;

	}

}
