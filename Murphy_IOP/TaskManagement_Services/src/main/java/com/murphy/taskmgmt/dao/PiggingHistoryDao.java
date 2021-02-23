package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.PiggingHistoryDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.PiggingHistoryDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * @author Ritwik.Jain
 *
 */
@Repository("PiggingHistoryDao")
@Transactional(rollbackOn = Exception.class)
public class PiggingHistoryDao extends BaseDao<PiggingHistoryDo, PiggingHistoryDto> {

	private static final Logger logger = LoggerFactory.getLogger(PipelineMeasurementDao.class);

	@Override
	protected PiggingHistoryDo importDto(PiggingHistoryDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		PiggingHistoryDo dos = new PiggingHistoryDo();
		if (!ServicesUtil.isEmpty(fromDto.getEquipmentId()))
			dos.setEquipmentId(fromDto.getEquipmentId());

		if (!ServicesUtil.isEmpty(fromDto.getLastCompletedOn()))
			dos.setLastCompletedOn(fromDto.getLastCompletedOn());

		if (!ServicesUtil.isEmpty(fromDto.getDueDate()))
			dos.setDueDate(fromDto.getDueDate());

		return dos;
	}

	@Override
	protected PiggingHistoryDto exportDto(PiggingHistoryDo entity) {
		PiggingHistoryDto dto = new PiggingHistoryDto();
		if (!ServicesUtil.isEmpty(entity.getEquipmentId()))
			dto.setEquipmentId(entity.getEquipmentId());

		if (!ServicesUtil.isEmpty(entity.getLastCompletedOn()))
			dto.setLastCompletedOn(entity.getLastCompletedOn());

		if (!ServicesUtil.isEmpty(entity.getDueDate()))
			dto.setDueDate(entity.getDueDate());

		return dto;
	}

	public ResponseMessage addPiggingHistory(List<PiggingHistoryDto> dtos) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			for (PiggingHistoryDto dto : dtos)
				persist(importDto(dto));
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseMessage.setMessage("History saved ");

		} catch (Exception e) {
			responseMessage.setMessage("Failed: " + e.getMessage());
			logger.error("[Murphy][PiggingHistoryDao][addPiggingHistory][error]" + e.getMessage());
			
		}
		return responseMessage;

	}

	@SuppressWarnings("unchecked")
	public List<PiggingHistoryDto> getHistoryByDueDate(Date dueDate){
		
		List<PiggingHistoryDto> dtos= new ArrayList<>();
		   dueDate=new LocalDate(dueDate).toDate();
		try{
			Criteria criteria=getSession().createCriteria(PiggingHistoryDo.class);
			    criteria.add(Restrictions.eq("dueDate",dueDate));
			    dtos= exportDtoList(criteria.list());
		}
		catch(Exception e){
			logger.error("[Murphy][PiggingHistoryDao][getHistoryByDueDate][error]" + e.getMessage());
		}
		
		return dtos;
	}

}
