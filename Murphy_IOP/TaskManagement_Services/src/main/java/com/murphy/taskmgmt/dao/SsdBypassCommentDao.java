package com.murphy.taskmgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.SsdBypassCommentDto;
import com.murphy.taskmgmt.entity.SsdBypassCommentsDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ssdBypassCommentDao")
public class SsdBypassCommentDao extends BaseDao<SsdBypassCommentsDo, SsdBypassCommentDto> {

	private static final Logger logger = LoggerFactory.getLogger(TaskEventsDao.class);

	@Override
	protected SsdBypassCommentsDo importDto(SsdBypassCommentDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		SsdBypassCommentsDo entity = new SsdBypassCommentsDo();

		if (!ServicesUtil.isEmpty(fromDto.getCommentId()))
			entity.setCommentId(fromDto.getCommentId());

		if (!ServicesUtil.isEmpty(fromDto.getSsdBypassId()))
			entity.setSsdBypassId(fromDto.getSsdBypassId());

		if (!ServicesUtil.isEmpty(fromDto.getComment()))
			entity.setComment(fromDto.getComment());

		if (!ServicesUtil.isEmpty(fromDto.getUpdatedBy()))
			entity.setUpdatedBy(fromDto.getUpdatedBy());

		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(fromDto.getUpdatedAt());

		return entity;

	}

	@Override
	protected SsdBypassCommentDto exportDto(SsdBypassCommentsDo entity) {

		SsdBypassCommentDto dto = new SsdBypassCommentDto();

		if (!ServicesUtil.isEmpty(entity.getCommentId()))
			dto.setCommentId(entity.getCommentId());

		if (!ServicesUtil.isEmpty(entity.getSsdBypassId()))
			dto.setSsdBypassId(entity.getSsdBypassId());

		if (!ServicesUtil.isEmpty(entity.getComment()))
			dto.setComment(entity.getComment());

		if (!ServicesUtil.isEmpty(entity.getUpdatedBy()))
			dto.setUpdatedBy(entity.getUpdatedBy());

		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			dto.setUpdatedAt(entity.getUpdatedAt());

		return dto;
	}

	/*
	 * public List<SsdBypassCommentDto> exportDtoList(List<SsdBypassCommentsDo>
	 * entities) { List<SsdBypassCommentDto> SsdBypassCommentDtoList = new
	 * ArrayList<>(); for (SsdBypassCommentsDo entity : entities) {
	 * SsdBypassCommentDtoList.add(exportDto(entity)); } return
	 * SsdBypassCommentDtoList; }
	 */
	public String createBypassLogComment(SsdBypassCommentDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			create(dto);
			reponse = MurphyConstant.SUCCESS;
			//this.getSession().flush();

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassCommentDao][createBypassLogComment][error]" + e.getMessage());

		}
		return reponse;

	}
}
