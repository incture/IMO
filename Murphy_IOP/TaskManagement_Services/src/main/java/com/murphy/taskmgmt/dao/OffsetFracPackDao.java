package com.murphy.taskmgmt.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.FracHitDto;
import com.murphy.taskmgmt.dto.FracScenarioDto;
import com.murphy.taskmgmt.dto.OffsetFracPackDto;
import com.murphy.taskmgmt.dto.OffsetFracPackResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.FracHitDo;
import com.murphy.taskmgmt.entity.OffsetFracPackDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.SequenceGenerator;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * @author Ritwik.Jain
 *
 */
@Repository("OffsetFrackPackDao")
@Transactional(rollbackOn = Exception.class)
public class OffsetFracPackDao extends BaseDao<OffsetFracPackDo, OffsetFracPackDto> {

	private static final Logger logger = LoggerFactory.getLogger(OffsetFracPackDao.class);

	@Override
	protected OffsetFracPackDo importDto(OffsetFracPackDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		OffsetFracPackDo dos = new OffsetFracPackDo();
		if (!ServicesUtil.isEmpty(fromDto.getFracId()))
			dos.setFracId(fromDto.getFracId());
		if (!ServicesUtil.isEmpty(fromDto.getWellName()))
			dos.setWellName(fromDto.getWellName());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			dos.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getUserRole()))
			dos.setUserRole(fromDto.getUserRole());
		
		if (!ServicesUtil.isEmpty(fromDto.getFracStatus()))
			dos.setFracStatus(fromDto.getFracStatus());

		if (!ServicesUtil.isEmpty(fromDto.getFieldCode()))
			dos.setFieldCode(fromDto.getFieldCode());
		if (!ServicesUtil.isEmpty(fromDto.getWellCode()))
			dos.setWellCode(fromDto.getWellCode());
		if (!ServicesUtil.isEmpty(fromDto.getStartAt()))
			dos.setStartAt(fromDto.getStartAt());
		if (!ServicesUtil.isEmpty(fromDto.getWellStatus()))
			dos.setWellStatus(fromDto.getWellStatus());
		if (!ServicesUtil.isEmpty(fromDto.getEndAt()))
			dos.setEndAt(fromDto.getEndAt());
		if (!ServicesUtil.isEmpty(fromDto.getEstBolDate()))
			dos.setEstBolDate(fromDto.getEstBolDate());
		if (!ServicesUtil.isEmpty(fromDto.getScenario()))
			dos.setScenario(fromDto.getScenario());
		if (!ServicesUtil.isEmpty(fromDto.getProdImpact()))
			dos.setProdImpact(fromDto.getProdImpact());
		if (!ServicesUtil.isEmpty(fromDto.getMaxTubePressure()))
			dos.setMaxTubePressure(fromDto.getMaxTubePressure());
		if (!ServicesUtil.isEmpty(fromDto.getMaxCasePressure()))
			dos.setMaxCasePressure(fromDto.getMaxCasePressure());
//		// Adding for CHG0036988 to hold max Case PSI reached
		if (!ServicesUtil.isEmpty(fromDto.getMaxCasePSI()))
			dos.setMaxCasePSI(fromDto.getMaxCasePSI());
		if (!ServicesUtil.isEmpty(fromDto.getBoed()))
			dos.setBoed(fromDto.getBoed());
		if (!ServicesUtil.isEmpty(fromDto.getDistFrac()))
			dos.setDistFrac(fromDto.getDistFrac());
		if (!ServicesUtil.isEmpty(fromDto.getOrientation()))
			dos.setOrientation(fromDto.getOrientation());
		if (!ServicesUtil.isEmpty(fromDto.getZone()))
			dos.setZone(fromDto.getZone());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			dos.setDescription(fromDto.getDescription());
		
		if (!ServicesUtil.isEmpty(fromDto.getActiveCasePressure()))
			dos.setActiveCasePressure(fromDto.getActiveCasePressure());
		
		if (!ServicesUtil.isEmpty(fromDto.getActiveTubePressure()))
			dos.setActiveTubePressure(fromDto.getActiveTubePressure());
		
		

		return dos;
	}

	@Override
	protected OffsetFracPackDto exportDto(OffsetFracPackDo entity) {
		OffsetFracPackDto dto = new OffsetFracPackDto();
		if (!ServicesUtil.isEmpty(entity.getFracId()))
			dto.setFracId(entity.getFracId());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			dto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getUserRole()))
			dto.setUserRole(entity.getUserRole());
		if (!ServicesUtil.isEmpty(entity.getWellName()))
			dto.setWellName(entity.getWellName());
		if (!ServicesUtil.isEmpty(entity.getFieldCode()))
			dto.setFieldCode(entity.getFieldCode());
		if (!ServicesUtil.isEmpty(entity.getWellCode()))
			dto.setWellCode(entity.getWellCode());
		if (!ServicesUtil.isEmpty(entity.getWellStatus()))
			dto.setWellStatus(entity.getWellStatus());
		if (!ServicesUtil.isEmpty(entity.getStartAt()))
			dto.setStartAt(entity.getStartAt());
		if (!ServicesUtil.isEmpty(entity.getEndAt()))
			dto.setEndAt(entity.getEndAt());
		if (!ServicesUtil.isEmpty(entity.getEstBolDate()))
			dto.setEstBolDate(entity.getEstBolDate());
		if (!ServicesUtil.isEmpty(entity.getFracStatus()))
			dto.setFracStatus(entity.getFracStatus());
		if (!ServicesUtil.isEmpty(entity.getScenario()))
			dto.setScenario(entity.getScenario());
		if (!ServicesUtil.isEmpty(entity.getProdImpact()))
			dto.setProdImpact(entity.getProdImpact());
		if (!ServicesUtil.isEmpty(entity.getMaxTubePressure()))
			dto.setMaxTubePressure(entity.getMaxTubePressure());
		if (!ServicesUtil.isEmpty(entity.getMaxCasePressure()))
			dto.setMaxCasePressure(entity.getMaxCasePressure());
//		// Adding for CHG0036988 to hold max CASE PSI Reached
		if (!ServicesUtil.isEmpty(entity.getMaxCasePSI()))
			dto.setMaxCasePSI(entity.getMaxCasePSI());
		if (!ServicesUtil.isEmpty(entity.getBoed()))
			dto.setBoed(entity.getBoed());
		if (!ServicesUtil.isEmpty(entity.getDistFrac()))
			dto.setDistFrac(entity.getDistFrac());
		if (!ServicesUtil.isEmpty(entity.getOrientation()))
			dto.setOrientation(entity.getOrientation());
		if (!ServicesUtil.isEmpty(entity.getZone()))
			dto.setZone(entity.getZone());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			dto.setDescription(entity.getDescription());
		
		if (!ServicesUtil.isEmpty(entity.getActiveCasePressure()))
			dto.setActiveCasePressure(entity.getActiveCasePressure());
		
		if (!ServicesUtil.isEmpty(entity.getActiveTubePressure()))
			dto.setActiveTubePressure(entity.getActiveTubePressure());
		
		return dto;
	}

	protected FracHitDo importDto(FracHitDto fromDto) {
		FracHitDo dos = new FracHitDo();

		if (!ServicesUtil.isEmpty(fromDto.getFracId()))
			dos.setFracId(fromDto.getFracId());
		if (!ServicesUtil.isEmpty(fromDto.getMuwi()))
			dos.setMuwi(fromDto.getMuwi());
		if (!ServicesUtil.isEmpty(fromDto.getFracHitTime()))
			dos.setFracHitTime(fromDto.getFracHitTime());

		return dos;
	}

	protected FracHitDto exportDto(FracHitDo entity) {
		FracHitDto dto = new FracHitDto();
		if (!ServicesUtil.isEmpty(entity.getFracId()))
			dto.setFracId(entity.getFracId());
		if (!ServicesUtil.isEmpty(entity.getMuwi()))
			dto.setMuwi(entity.getMuwi());
		if (!ServicesUtil.isEmpty(entity.getFracHitTime()))
			dto.setFracHitTime(entity.getFracHitTime());
		return dto;
	}

	protected List<OffsetFracPackDto> exportList(List<OffsetFracPackDo> dos) {
		List<OffsetFracPackDto> dtos = new ArrayList<>();
		for (OffsetFracPackDo d : dos) {
			dtos.add(exportDto(d));
		}
		return dtos;
	}
	
	public long getLatestKey(String table, String column) {

		// String queryString = "Select max( cls."+column+") from
		// "+o.getClass().getSimpleName()+" cls ";
		String queryString = "Select max( cls." + column + ") from " + table + " cls ";
		Query q = this.getSession().createSQLQuery(queryString);
		Integer current = null;
		if (!ServicesUtil.isEmpty(q.uniqueResult())){
			 current = Integer.parseInt(q.uniqueResult().toString());
		}
		
		SequenceGenerator sg = new SequenceGenerator();
		return sg.getNextKey(current);
	}

	public ResponseMessage createFracPack(List<OffsetFracPackDto> dtos)
			throws ExecutionFault, InvalidInputFault, NoResultFault, RuntimeException {
		ResponseMessage responseDto = new ResponseMessage();

		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(dtos)) {
			/*
			 * try {
			 */
			// getSession().getTransaction().begin();
			for (OffsetFracPackDto dto : dtos) {
				create(dto);
			}
			// getSession().getTransaction().commit();
			responseDto.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			/*
			 * } catch (Exception e) {
			 * logger.error("[Murphy][FracPackFaced][createFracPack][error]" +
			 * e.getMessage());
			 * responseDto.setMessage(MurphyConstant.CREATE_FAILURE); }
			 */
		}

		return responseDto;

	}

	// Getting unique FracPack by fracId and FieldCode
	@SuppressWarnings("unchecked")
	public OffsetFracPackResponseDto getFracPack(String fracId, String fieldCode, String wellCode) {
		OffsetFracPackResponseDto response = new OffsetFracPackResponseDto();
		ResponseMessage message = new ResponseMessage();
		List<OffsetFracPackDo> listDo = new ArrayList<>();

		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
//		OffsetFracPackDto responsedto = new OffsetFracPackDto();
		try {
			Criteria criteria = getSession().createCriteria(OffsetFracPackDo.class);
			criteria.add(Restrictions.eq("fracId", fracId));
			if (!ServicesUtil.isEmpty(fieldCode))
				criteria.add(Restrictions.eq("fieldcode", fieldCode));
			if (!ServicesUtil.isEmpty(wellCode))
				criteria.add(Restrictions.eq("wellCode", wellCode));

			listDo = criteria.list();

			response.setFracPacks(exportDtoList(listDo));

			message.setMessage("Successfully fetched");
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][FracPackFaced][getFracPack][error]" + e.getMessage());
			message.setMessage("Failed");
		}

		response.setResponseMessage(message);
		return response;

	}

	@SuppressWarnings("unchecked")
	public List<OffsetFracPackDto> getFracPacks(String userRole) {
		List<OffsetFracPackDo> listDo = new ArrayList<>();
//		List<OffsetFracPackDto> listDto = new ArrayList<>();
		Criteria criteria = getSession().createCriteria(OffsetFracPackDo.class);
		criteria.add(Restrictions.eq("fracStatus", MurphyConstant.INPROGRESS));
		
		/*if (!ServicesUtil.isEmpty(userRole))
			criteria.add(Restrictions.eq("userRole", userRole));*/
		criteria.addOrder(Order.desc("fracId"));
		listDo.addAll(criteria.list());
		return exportDtoList(listDo);
	}

	// Save or update fracPack
	public ResponseMessage updateFracPack(List<OffsetFracPackDto> dtos)throws ExecutionFault, InvalidInputFault, NoResultFault, RuntimeException {
		ResponseMessage responseDto = new ResponseMessage();
		
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		s.setTimeZone(TimeZone.getTimeZone("CST"));
		String startString=null,estBolString=null;
			try {
				if (!ServicesUtil.isEmpty(dtos)) {
				for(OffsetFracPackDto dto : dtos){
					if(dto.getAction().equalsIgnoreCase("update"))
					{
						if (!ServicesUtil.isEmpty(dto.getStartAt())) {
							Date startAt = dto.getStartAt();
							startString =s.format(startAt);
						}
						if (!ServicesUtil.isEmpty(dto.getEstBolDate())) {
							Date estBolDate = dto.getEstBolDate();
							estBolString = s.format(estBolDate);
						}
						String query = "update iop.OFFSET_FRAC_PACK set WELL_NAME='"+dto.getWellName()+
										"',START_DATE=to_timestamp('"+startString+"','yyyy-MM-dd HH24:mi:ss'),"+ 
										"EST_BOL_DATE=to_timestamp('"+estBolString+"','yyyy-MM-dd HH24:mi:ss'),"+
										"SCENARIO='"+dto.getScenario()+"', STATUS='"+dto.getFracStatus()+"', MAX_TUBE_PRESSURE="+dto.getMaxTubePressure()+ 
										",MAX_CASE_PRESSURE="+dto.getMaxCasePressure()+", DISTANCE_FROM_FRAC="+dto.getDistFrac()+", ORIENTATION='"+dto.getOrientation()+
										"',zone ='"+dto.getZone()+"', DESCRIPTION='"+dto.getDescription()+"', USER_ID='"+dto.getUserId()+
										"', USER_ROLE='"+dto.getUserRole()+"' Where FRAC_ID="+dto.getFracId()+" and FIELD_CODE='"+dto.getFieldCode()+"' and WELL_CODE='"+dto.getWellCode()+"'";
						
						Query q = this.getSession().createSQLQuery(query);
						 logger.error("[Murphy][OffsetFracPackDao][updateFracPack][query]"+ query);
						int result = (Integer) q.executeUpdate();
						if(result > 0){
							responseDto.setMessage(MurphyConstant.UPDATE_SUCCESS);
							responseDto.setStatus(MurphyConstant.SUCCESS);
							responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
						}
					}
					else if(dto.getAction().equalsIgnoreCase("create")){
						create(dto);
						responseDto.setMessage(MurphyConstant.UPDATE_SUCCESS);
						responseDto.setStatus(MurphyConstant.SUCCESS);
						responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
					}
				}
			  }
			} 
			catch (Exception e) {
				logger.error("[Murphy][FracPackFaced][updateFracPack][error]" + e.getMessage());
				responseDto.setMessage(MurphyConstant.UPDATE_FAILURE);
				responseDto.setStatus(MurphyConstant.FAILURE);
				responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
			}
		return responseDto;
	}

	@SuppressWarnings("unchecked")
	public OffsetFracPackResponseDto getFracPacksByField(String fieldCode) {
		OffsetFracPackResponseDto responseDto = new OffsetFracPackResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		List<OffsetFracPackDto> dtoList = new ArrayList<>();
		List<OffsetFracPackDo> doList = new ArrayList<>();

		try {
			Criteria criteria = getSession().createCriteria(OffsetFracPackDo.class);

			criteria.add(Restrictions.eq("fieldcode", fieldCode));

			doList = criteria.list();

			if (!ServicesUtil.isEmpty(doList)) {
				for (OffsetFracPackDo dos : doList) {
					dtoList.add(exportDto(dos));
				}

			}

			responseMessage.setMessage("Successfully factched");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error("[Murphy][FracPackFaced][getFracPacksByField][error]" + e.getMessage());
			responseMessage.setMessage("failed to fetch");
		}

		responseDto.setFracPacks(dtoList);
		responseDto.setResponseMessage(responseMessage);

		return responseDto;
	}

	public FracHitDto getFracHit(long fracId, String muwiId) {
		try {
			String hql = "select h1 from FracHitDo h1 where h1.fracId= :fracId and h1.muwi= :muwiId "
					+ " and h1.fracHitTime= (select max(h2.fracHitTime) from FracHitDo h2 where h2.fracId=h1.fracId"
					+ " and h2.muwi=h1.muwi)";

			Query query = getSession().createQuery(hql);
			query.setParameter("fracId", fracId);
			query.setParameter("muwiId", muwiId);
			FracHitDo resdo = (FracHitDo) query.uniqueResult();
			System.err.println("FRAC HIT DATA:------------------------" + resdo);
			if (!ServicesUtil.isEmpty(resdo))
				return exportDto(resdo);
			else
				return new FracHitDto();
		} catch (Exception e) {
			logger.error("[Murphy][FracPackFaced][getFracHit][error]" + e.getMessage());
			return new FracHitDto();

		}
	}

	public void insertFractHit(FracHitDto dto) throws Exception {
		try {

			this.getSession().persist(importDto(dto));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Murphy][FracPackFaced][insertFractHit][error] Error while Saving in DB" + e.getMessage());
			throw e;
		}
	}
	public ResponseMessage markComplete(List<FracHitDto> dtos) {
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			for (FracHitDto dto : dtos) {
               long fracId=dto.getFracId();
               String wellCode=dto.getMuwi();
               String hql="update OffsetFracPackDo c set c.fracStatus = :status where c.fracId= :fracId and c.wellCode= :wellCode";
				
           	Query query = getSession().createQuery(hql);
           	query.setParameter("status",MurphyConstant.COMPLETE);
			query.setParameter("fracId", fracId);
			query.setParameter("wellCode", wellCode);
			query.executeUpdate();
				
			}
			
			message.setMessage("Mark completed successfully");
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Murphy][FracPackFaced][markComplete][error] Error while Updating in DB" + e.getMessage());
            message.setMessage("Failed while Updating DB");
		}
		return message;
	}
	
	@SuppressWarnings("unchecked")
	public double getActiveCasePressure(String muiwId) {
		
		String queryString = "SELECT TOP 1 data_value FROM tm_canary_staging_table WHERE muwi_id ='"+muiwId+"' AND param_type='PRCASXIN'  AND data_value IS NOT NULL ORDER BY created_at DESC;";

		double activeCasePressure= 0;

		try{
			Query q = this.getSession().createSQLQuery(queryString);
			Object result = q.uniqueResult();
			if(!ServicesUtil.isEmpty(result)) {
				activeCasePressure = (double) result;
			}
			
//			List<String> queryResponse = (List<String>) q.list();
//			if(!ServicesUtil.isEmpty(queryResponse)){
//				for(String obj : queryResponse){
//					String value = (String) obj;
//					//logger.error("[ffsetFracPackDao][getActiveCasePressure]" +(String) obj);
//					activeCasePressure = Double.parseDouble(value);
//					//logger.error("[ffsetFracPackDao][getActiveCasePressure]" +activeCasePressure);
//				}
//			}	
		}catch(Exception e){
			logger.error("[error][ffsetFracPackDao][getActiveCasePressure]" + e.getMessage());
		}

		return activeCasePressure;
	
	}
	
	@SuppressWarnings("unchecked")
	public double getActiveTubingPressure(String muiwId) {

		String queryString = "SELECT TOP 1 data_value FROM tm_canary_staging_table WHERE muwi_id ='"+muiwId+"' AND param_type='PRTUBXIN'  AND data_value IS NOT NULL ORDER BY created_at DESC;";

		double activetubePressure= 0;
		try{
			Query q = this.getSession().createSQLQuery(queryString);
			Object result = q.uniqueResult();
			if(!ServicesUtil.isEmpty(result)) {
				activetubePressure = (double) result;
			}
//			List<String> queryResponse = (List<String>) q.list();
//			if(!ServicesUtil.isEmpty(queryResponse)){
//				for(String obj : queryResponse){
//					String value = obj;
//				//	logger.error("[ffsetFracPackDao][getActiveTubingPressure]" +(String) obj);
//					activetubePressure = Double.parseDouble(value);
//				//	logger.error("[ffsetFracPackDao][getActiveTubingPressure]" +activetubePressure);
//				}
//			}	
		}catch(Exception e){
			logger.error("[error][offsetFracPackDao][getActiveTubingPressure]" + e.getMessage());
		}

		return activetubePressure;
	
	}
	
	@SuppressWarnings("unchecked")
	public String getWellCode(String muwi) {
		String wellCode = "";
		try{
			String queryString = "select location_code from WELL_MUWI where muwi = '"+muwi+"'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				logger.error("[error][ffsetFracPackDao][wellCode]********" +  response.get(0));
				wellCode = response.get(0);
				logger.error("[error][ffsetFracPackDao][wellCode]********" + wellCode);
			}
		} catch(Exception e){
			logger.error("[error][ffsetFracPackDao][getWellCode]" + e.getMessage());
		}

		return wellCode;
	}
	
	public ResponseMessage updateWellStatus(List<FracHitDto> dtos) {
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			for (FracHitDto dto : dtos) {
               long fracId=dto.getFracId();
               String wellCode=dto.getMuwi();
               String status=dto.getWellStatus();
               String hql="update OffsetFracPackDo c set c.wellStatus = :wellstatus where c.fracId= :fracId and c.wellCode= :wellCode";
				
           	Query query = getSession().createQuery(hql);
           	query.setParameter("wellstatus",status);
			query.setParameter("fracId", fracId);
			query.setParameter("wellCode", wellCode);
			query.executeUpdate();
				
			}
			
			message.setMessage("Well status updated successfully");
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Murphy][FracPackFaced][updateStatus][error] Error while Updating in DB" + e.getMessage());
            message.setMessage("Failed while Updating DB");
		}
		return message;
	}
	
	//Adding for incident INC0078316
	public String getStatusBasedOnScenario(String scenario){
		String status = null;
		try{
			String queryString = "select status from FRAC_SCENARIO where scenario '"+scenario+"'";
			Query q = this.getSession().createSQLQuery(queryString);
			status = (String) q.uniqueResult();
		}catch(Exception e){
			logger.error("[error][ofsetFracPackDao][getStatusBasedOnScenario]" + e.getMessage());
		}
		return status;
	}

		//Adding for incident INC0078316
		@SuppressWarnings("unchecked")
		public List<FracScenarioDto> getFracScenario(){
			List<FracScenarioDto> dtoList = new ArrayList<>();
			try{
				String queryString = "select SCENARIO, WELL_STATUS from FRAC_SCENARIO";
				Query q = this.getSession().createSQLQuery(queryString);
				List<Object[]> resultList = (List<Object[]>) q.list();
				if(!ServicesUtil.isEmpty(resultList)){
					for(Object[] obj:resultList){
						FracScenarioDto fracDto=new FracScenarioDto();
						fracDto.setScenario(ServicesUtil.isEmpty(obj[0]) ? null :(String)obj[0]);
						fracDto.setWellStatus(ServicesUtil.isEmpty(obj[1]) ? null :(String)obj[1]);
						dtoList.add(fracDto);
					}
				}
			}
			catch(Exception e){
				logger.error("[error][ofsetFracPackDao][getFracScenario]" + e.getMessage());
			}
			return dtoList;
		}
}