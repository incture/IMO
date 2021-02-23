package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.ReasonForBypassDto;
import com.murphy.taskmgmt.entity.ReasonForBypassDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("reasonForBypassDao")
public class ReasonForBypassDao extends BaseDao<ReasonForBypassDo, ReasonForBypassDto> {

	private static final Logger logger = LoggerFactory.getLogger(ReasonForBypassDao.class);

	@Override
	protected ReasonForBypassDo importDto(ReasonForBypassDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ReasonForBypassDo entity = new ReasonForBypassDo();
		

		if (!ServicesUtil.isEmpty(fromDto.getReasonForBypass())) {
			entity.setReasonForBypass(fromDto.getReasonForBypass());
		}
		if (!ServicesUtil.isEmpty(fromDto.getActiveFlag())) {
			entity.setActiveFlag(fromDto.getActiveFlag());
		}
		return entity;
	}

	@Override
	protected ReasonForBypassDto exportDto(ReasonForBypassDo entity) {
		ReasonForBypassDto dto = new ReasonForBypassDto();

		

		if (!ServicesUtil.isEmpty(entity.getReasonForBypass())) {
			dto.setReasonForBypass(entity.getReasonForBypass());
		}
		if (!ServicesUtil.isEmpty(entity.getActiveFlag())) {
			dto.setActiveFlag(entity.getActiveFlag());
		}
		return dto;
	}

	public String createReasonForBypass(ReasonForBypassDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			// dto.setReasonForBypassId(getSeqVal());
			create(dto);
			reponse = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][ReasonForBypassDao][createReasonForBypass][error]" + e.getMessage());
		}
		return reponse;
	}

	public List<ReasonForBypassDto> getReasonForBypassList() throws Exception {
		List<ReasonForBypassDto> reasonForBypassDtoList = new ArrayList<ReasonForBypassDto>();
		String activeFlag=MurphyConstant.ACTIVE;
		String query = "from ReasonForBypassDo p where p.activeFlag='" + activeFlag + "'";
		List<Object> ReasonForBypassList = this.getSession().createQuery(query).list();
		for (Object reason : ReasonForBypassList) {
			ReasonForBypassDto reasonForBypassDto = new ReasonForBypassDto();
			reasonForBypassDto.setReasonForBypass(((ReasonForBypassDo) reason).getReasonForBypass());
			reasonForBypassDto.setActiveFlag(((ReasonForBypassDo) reason).getActiveFlag());
			reasonForBypassDtoList.add(reasonForBypassDto);
		}

		return reasonForBypassDtoList;
	}

	/*
	 * public String getSeqVal() { String reasonForBypassId = ""; try { String
	 * query = "select max(REASON_FOR_BYPASS_ID) from ReasonForBypassDo";
	 * reasonForBypassId = (String)
	 * this.getSession().createQuery(query).uniqueResult(); if
	 * (!ServicesUtil.isEmpty(reasonForBypassId)) { StringBuffer num = new
	 * StringBuffer(); StringBuffer alpha = new StringBuffer(); for (int i = 0;
	 * i < reasonForBypassId.length(); i++) { if
	 * (Character.isDigit(reasonForBypassId.charAt(i))) {
	 * num.append(reasonForBypassId.charAt(i)); } if
	 * (Character.isAlphabetic(reasonForBypassId.charAt(i))) {
	 * alpha.append(reasonForBypassId.charAt(i)); } }
	 * 
	 * int id = Integer.parseInt(num.toString()); id++; reasonForBypassId =
	 * alpha.toString() + id; } else { reasonForBypassId = "R1"; } } catch
	 * (Exception e) { System.err.println(e.getMessage()); e.printStackTrace();
	 * } System.err.println("reasonForBypassId:" + reasonForBypassId); return
	 * reasonForBypassId; }
	 */

}
