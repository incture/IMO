package com.murphy.taskmgmt.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.integration.dto.InvestigationHistoryDto;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.InvestigationHistoryLocal;
import com.murphy.integration.service.InvestigationHistoryFromWellViewService;
import com.murphy.integration.service.InvestigationHistoryService;
import com.murphy.taskmgmt.dto.AlsInvestigationDto;
import com.murphy.taskmgmt.entity.AlsInvestigationDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("alsInvestigationDao")
public class AlsInvestigationDao extends BaseDao<AlsInvestigationDo, AlsInvestigationDto> {

	private static final Logger logger = LoggerFactory.getLogger(AlsInvestigationDao.class);
	
	@Autowired
	HierarchyDao locdao;

	@Override
	protected AlsInvestigationDo importDto(AlsInvestigationDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		AlsInvestigationDo entity = new AlsInvestigationDo();

		if (!ServicesUtil.isEmpty(fromDto.getAlsId()))
			entity.setAlsId(fromDto.getAlsId());
		if (!ServicesUtil.isEmpty(fromDto.getMuwId()))
			entity.setMuwId(fromDto.getMuwId());
		if (!ServicesUtil.isEmpty(fromDto.getReason()))
			entity.setReason(fromDto.getReason());
		if (!ServicesUtil.isEmpty(fromDto.getDate()))
			entity.setDate(fromDto.getDate());
		if (!ServicesUtil.isEmpty(fromDto.getSource()))
			entity.setSource(fromDto.getSource());

		return entity;
	}

	@Override
	protected AlsInvestigationDto exportDto(AlsInvestigationDo entity) {

		AlsInvestigationDto dto = new AlsInvestigationDto();

		if (!ServicesUtil.isEmpty(entity.getAlsId()))
			dto.setAlsId(entity.getAlsId());
		if (!ServicesUtil.isEmpty(entity.getMuwId()))
			dto.setMuwId(entity.getMuwId());
		if (!ServicesUtil.isEmpty(entity.getReason()))
			dto.setReason(entity.getReason());
		if (!ServicesUtil.isEmpty(entity.getDate()))
			dto.setDate(entity.getDate());
		if (!ServicesUtil.isEmpty(entity.getSource()))
			dto.setSource(entity.getSource());

		return dto;
	}

	public String createRecord(String muwId) {
		String response = MurphyConstant.FAILURE;
		try {
			InvestigationHistoryLocal investigationHistoryLocal = new InvestigationHistoryService();
			UIResponseDto uiResponseDto = new UIResponseDto();
			uiResponseDto = investigationHistoryLocal.fetchInvestigationData(muwId);
//			logger.error("[AlsStagingFacade][StageALSData][uiResponseDto] " + uiResponseDto.toString());

			List<InvestigationHistoryDto> investigationHistoryDtoList = uiResponseDto.getInvestigationHistoryDtoList();
//			logger.error("[AlsStagingFacade][StageALSData][size] " + uiResponseDto.getInvestigationHistoryDtoList().size());
//			logger.error("[AlsStagingFacade][StageALSData][size] " + uiResponseDto.getInvestigationHistoryDtoList().toString());

			if (!ServicesUtil.isEmpty(investigationHistoryDtoList))
				for (InvestigationHistoryDto investigationHistoryDto : investigationHistoryDtoList) {

					if (!ServicesUtil.isEmpty(investigationHistoryDto.getPwrDate()) && !MurphyConstant.HYPHEN.equals(investigationHistoryDto.getPwrDate())) {
						createMethod(muwId,
								ServicesUtil.convertFromZoneToZone(null, investigationHistoryDto.getPwrDate(), "", "",
										MurphyConstant.DATE_REVERSE_FULL, MurphyConstant.DATE_STANDARD),
								MurphyConstant.PWR, MurphyConstant.SOURCE_ALS);
					}
					if (!ServicesUtil.isEmpty(investigationHistoryDto.getRtpDate()) && !MurphyConstant.HYPHEN.equals(investigationHistoryDto.getRtpDate())) {
						createMethod(muwId,
								ServicesUtil.convertFromZoneToZone(null, investigationHistoryDto.getRtpDate(), "", "",
										MurphyConstant.DATE_REVERSE_FULL, MurphyConstant.DATE_STANDARD),
								MurphyConstant.RETURN_TO_PROD, MurphyConstant.SOURCE_ALS);
					}
					if (!ServicesUtil.isEmpty(investigationHistoryDto.getJobCompleteionDate()) && !MurphyConstant.HYPHEN.equals(investigationHistoryDto.getJobCompleteionDate())) {
						createMethod(muwId,
								ServicesUtil.convertFromZoneToZone(null,
										investigationHistoryDto.getJobCompleteionDate(), "", "",
										MurphyConstant.DATE_REVERSE_FULL, MurphyConstant.DATE_STANDARD),
								MurphyConstant.JOB_COMP, MurphyConstant.SOURCE_ALS);
					}
					if (!ServicesUtil.isEmpty(investigationHistoryDto.getShutInDate()) && !MurphyConstant.HYPHEN.equals(investigationHistoryDto.getShutInDate())) {
						createMethod(muwId,
								ServicesUtil.convertFromZoneToZone(null, investigationHistoryDto.getShutInDate(), "",
										"", MurphyConstant.DATE_REVERSE_FULL, MurphyConstant.DATE_STANDARD),
								MurphyConstant.SHUT_IN, MurphyConstant.SOURCE_ALS);
					}
					response = MurphyConstant.SUCCESS;
				}
		} catch (Exception e) {
			logger.error("[Murphy][AlsStagingFacade][StageALSData][error] " + e);
		}
		return response;

	}

	public String createMethod(String muwi, Date date, String reason, String source) {
		String result = MurphyConstant.FAILURE;
		AlsInvestigationDto alsDto = null;
		if (!ServicesUtil.isEmpty(muwi)) {
			alsDto = new AlsInvestigationDto();
			alsDto.setMuwId(muwi);
			alsDto.setDate(date);
			alsDto.setReason(reason);
			alsDto.setSource(source);
			try {
				create(alsDto);
				result = MurphyConstant.SUCCESS;
			} catch (Exception e) {
				logger.error("[Murphy][AlsStagingFacade][alsCreating][error] " + e + " for Date Value" + date);
			}
		}
		return result;
	}

	public String removeRecord(String muwi) {
		if (!ServicesUtil.isEmpty(muwi)) {
			String queryString = "DELETE FROM ALS_INVESTIGATION WHERE MUWI IN ('" + muwi + "') AND SOURCE = 'ALS'";
			int deletedRow = this.getSession().createSQLQuery(queryString).executeUpdate();
			if (deletedRow > 0)
				return MurphyConstant.SUCCESS;
		}
		return MurphyConstant.FAILURE;
	}

	@SuppressWarnings("unchecked")
	public List<InvestigationHistoryDto> getAlsInvestigation(String muwi) {
		logger.error("begining of common method of efs and canada:");
		InvestigationHistoryDto dto = null;
		List<InvestigationHistoryDto> dtos = new ArrayList<InvestigationHistoryDto>();
		String processId = "";
		String queryString = "select pe.process_id,pe.COMPLETED_AT, string_agg(tai.ins_value,'-') as job_type,pe.STARTED_AT "
				+ " from well_muwi wm " + "join tm_proc_evnts pe on wm.location_code=pe.loc_code "
				+ "join tm_attr_insts tai on tai.task_id=pe.process_id " + "where wm.muwi='" + muwi
				+ "' and tai.attr_temp_id in ('NDO5','NDO6')" + " group by pe.process_id,pe.COMPLETED_AT,pe.STARTED_AT  ";

		try {
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			logger.error("[AlsStagingFacade][als Read][queryString]" + queryString + "[len] " + resultList.size());

			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] objects : resultList) {
					processId = (String) objects[0];
					String jobSummaryQuery = "SELECT  col.message,upper(uim.user_role)  FROM well_muwi AS wm,"
							+ " tm_proc_evnts AS pe, tm_collaboration AS col   left join tm_user_idp_mapping uim "
							+ "on uim.user_email=col.user_id WHERE  wm.muwi= '" + muwi + "'"
							+ "and wm.location_code = pe.loc_code and col.process_id = pe.process_id "
							+ "and uim.user_role is not null and  col.message  "
							+ "IS NOT NULL  and pe.process_id='" + processId + "' group by uim.user_role, col.message, col.message_id";
					Query qs = this.getSession().createSQLQuery(jobSummaryQuery);
					List<Object[]> resultListSum = qs.list();
//					logger.error("[AlsStagingFacade][alsCreating][jobSummaryQuery]" + jobSummaryQuery + "[Size]"
//							+ resultListSum.size());
					String summary = "";
					List<String> message = null;
					Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
					if (!ServicesUtil.isEmpty(resultListSum)) {
						for (Object[] obj : resultListSum) {

							String key = (String) obj[1];
							if (key.contains("ALS"))
								key = "ALS";
							else if (key.contains("ENG"))
								key = "ENG";
							else if (key.contains("POT"))
								key = "POT";

							if (map.containsKey(key)) {
								map.get(key).add((String) obj[0]);
							} else {
								message = new ArrayList<String>();
								message.add((String) obj[0]);
								map.put(key, message);
							}
						}
					}
					if (!ServicesUtil.isEmpty(map)) {
						for (Entry<String, List<String>> entry : map.entrySet()) {
							summary += entry.getKey() + " : " + ServicesUtil.getStringFromListForAls(entry.getValue())
									+ "\n";
						}
					}
					if (!ServicesUtil.isEmpty(summary))
						summary = summary.substring(0, summary.length() - 1);

					dto = new InvestigationHistoryDto();
//					dto.setJobCompleteionDate(ServicesUtil.isEmpty(objects[1]) ? null
//							: (ServicesUtil.convertFromZoneToZoneString(null, objects[1], MurphyConstant.UTC_ZONE,
//									MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
//									MurphyConstant.ALS_COMPLETED_DATE_FORMAT)));
					
					dto.setJobCompleteionDate(ServicesUtil.isEmpty(objects[1]) ? null
							: (ServicesUtil.convertToEpoch(objects[1],MurphyConstant.DATE_DB_FORMATE)));
					
					dto.setJobType((String) objects[2]);
					
					dto.setJobStartDate(ServicesUtil.isEmpty(objects[3]) ? null
							: (ServicesUtil.convertToEpoch(objects[3],MurphyConstant.DATE_DB_FORMATE)));
					
					dto.setShutInDate("-");
					dto.setRtpDate("-");
					dto.setPwrDate("-");
					dto.setJobSummary(summary);
					dto.setSource(MurphyConstant.SOURCE_IOP);
					dtos.add(dto);
				}
			}
			InvestigationHistoryService proveLocal = new InvestigationHistoryService();
			InvestigationHistoryFromWellViewService wellViewService = new InvestigationHistoryFromWellViewService();
			
			String countryCode= ServicesUtil.getCountryCodeByMuwi(muwi);
			
			logger.error("Incoming country code for ALS or WELL VIEW" +countryCode );
			if(!ServicesUtil.isEmpty(countryCode) && countryCode.equalsIgnoreCase(MurphyConstant.EFS_CODE))
			{
			List<InvestigationHistoryDto> proveData = proveLocal.fetchInvestigationData(muwi)
					.getInvestigationHistoryDtoList();
			if (!ServicesUtil.isEmpty(proveData)) {
				dtos.addAll(proveData);
			}
			}
			if(!ServicesUtil.isEmpty(countryCode) && countryCode.equalsIgnoreCase(MurphyConstant.CA_CODE))
			{
				logger.error("The country is canada");
				List<InvestigationHistoryDto> proveData = wellViewService.fetchInvestigationDataFromWellView(muwi)
						.getInvestigationHistoryDtoList();
				if (!ServicesUtil.isEmpty(proveData)) {
					dtos.addAll(proveData);
				}
			}
			
		} catch (Exception e) {
			logger.error("[AlsStagingFacade][als][error]" + e.getLocalizedMessage());
		}
		return dtos;
	}

	
}
