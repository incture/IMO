package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.murphy.taskmgmt.dto.AutoSignInDto;
import com.murphy.taskmgmt.dto.BaseDto;
import com.murphy.taskmgmt.dto.DeviceStatusInfoDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.AutoSignInDo;
import com.murphy.taskmgmt.entity.BaseDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("autoSignInDao")
@EnableTransactionManagement
public class AutoSignInDao extends BaseDao<BaseDo, BaseDto> {

private static final Logger logger = LoggerFactory.getLogger(AutoSignInDao.class);
	
	protected AutoSignInDo importDto(AutoSignInDto fromDto) {
		AutoSignInDo entity = new AutoSignInDo();
//		if (!ServicesUtil.isEmpty(fromDto.getSignInId()))
//			entity.setSignInId(fromDto.getSignInId());;
		if (!ServicesUtil.isEmpty(fromDto.getDriverId()))
			entity.getAutoSignInDoPK().setDriverId(fromDto.getDriverId());
//		if (!ServicesUtil.isEmpty(fromDto.getDriverName()))
//			entity.setDriverName(fromDto.getDriverName());
		if (!ServicesUtil.isEmpty(fromDto.getDriverLat()))
			entity.setDriverLat(fromDto.getDriverLat());
		if (!ServicesUtil.isEmpty(fromDto.getDriverLon()))
			entity.setDriverLon(fromDto.getDriverLon());
		if (!ServicesUtil.isEmpty(fromDto.getMuwi()))
			entity.getAutoSignInDoPK().setMuwi(fromDto.getMuwi());
		if (!ServicesUtil.isEmpty(fromDto.getWellName()))
			entity.setWellName(fromDto.getWellName());
		if (!ServicesUtil.isEmpty(fromDto.getWellLat()))
			entity.setWellLat(fromDto.getWellLat());
		if (!ServicesUtil.isEmpty(fromDto.getWellLon()))
			entity.setWellLon(fromDto.getWellLon());
		if (!ServicesUtil.isEmpty(fromDto.getSignInStart()))
			entity.setSignInStart(fromDto.getSignInStart());
		if (!ServicesUtil.isEmpty(fromDto.getSignInEnd()))
			entity.setSignInEnd(fromDto.getSignInEnd());
		if (!ServicesUtil.isEmpty(fromDto.getTimeSignedIn()))
			entity.setTimeSignedIn(fromDto.getTimeSignedIn());
		if (!ServicesUtil.isEmpty(fromDto.getCrowFlyDistance()))
			entity.setCrowFlyDistance(fromDto.getCrowFlyDistance());
		if (!ServicesUtil.isEmpty(fromDto.getRoadDistance()))
			entity.setRoadDistance(fromDto.getRoadDistance());
//		if (!ServicesUtil.isEmpty(fromDto.getDriverInField()))
//			entity.setDriverInField(fromDto.getDriverInField());
		return entity;
	}

	protected AutoSignInDto exportDto(AutoSignInDo entity) {
		AutoSignInDto dto = new AutoSignInDto();
//		if (!ServicesUtil.isEmpty(entity.getSignInId()))
//			dto.setSignInId(entity.getSignInId());;
		if (!ServicesUtil.isEmpty(entity.getAutoSignInDoPK().getDriverId()))
			dto.setDriverId(entity.getAutoSignInDoPK().getDriverId());
//		if (!ServicesUtil.isEmpty(entity.getDriverName()))
//			dto.setDriverName(entity.getDriverName());
		if (!ServicesUtil.isEmpty(entity.getDriverLat()))
			dto.setDriverLat(entity.getDriverLat());
		if (!ServicesUtil.isEmpty(entity.getDriverLon()))
			dto.setDriverLon(entity.getDriverLon());
		if (!ServicesUtil.isEmpty(entity.getAutoSignInDoPK().getMuwi()))
			dto.setMuwi(entity.getAutoSignInDoPK().getMuwi());
		if (!ServicesUtil.isEmpty(entity.getWellName()))
			dto.setWellName(entity.getWellName());
		if (!ServicesUtil.isEmpty(entity.getWellLat()))
			dto.setWellLat(entity.getWellLat());
		if (!ServicesUtil.isEmpty(entity.getWellLon()))
			dto.setWellLon(entity.getWellLon());
		if (!ServicesUtil.isEmpty(entity.getSignInStart()))
			dto.setSignInStart(entity.getSignInStart());
		if (!ServicesUtil.isEmpty(entity.getSignInEnd()))
			dto.setSignInEnd(entity.getSignInEnd());
		if (!ServicesUtil.isEmpty(entity.getTimeSignedIn()))
			dto.setTimeSignedIn(entity.getTimeSignedIn());
		if (!ServicesUtil.isEmpty(entity.getCrowFlyDistance()))
			dto.setCrowFlyDistance(entity.getCrowFlyDistance());
		if (!ServicesUtil.isEmpty(entity.getRoadDistance()))
			dto.setRoadDistance(entity.getRoadDistance());
//		if (!ServicesUtil.isEmpty(entity.getDriverInField()))
//			dto.setDriverInField(entity.getDriverInField());
		return dto;
	}
	
	ResponseMessage saveOrUpdateIntoSignInTable(AutoSignInDto autoSignInDto) {
		boolean flag = true;
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Save Or Update Failure");
		message.setStatus("FAILURE");
		message.setStatusCode("1");

		try {
			if(ServicesUtil.isEmpty(autoSignInDto.getSignInEnd())) {
				String sql = "SELECT COUNT(SERIAL_ID) AS COUNT FROM TM_GEOTAB_AUTO_SIGNIN WHERE SERIAL_ID = '"+autoSignInDto.getDriverId()+"' AND MUWI = '"+autoSignInDto.getMuwi()+"' AND SIGNIN_END_TIME IS NULL ";
				Object result = this.getSession().createSQLQuery(sql).uniqueResult();
				if(!ServicesUtil.isEmpty(result)) {
					if(((BigInteger) result).intValue() > 0) {
						flag = false;
					}
				}
			}
			if((flag == true) && (!ServicesUtil.isEmpty(autoSignInDto))) {
				this.getSession().save(AutoSignInDo.class.toString(), importDto(autoSignInDto));
				message.setMessage("Save Or Update Success");
				message.setStatus("SUCCESS");
				message.setStatusCode("0");
			}
		} catch (org.hibernate.exception.ConstraintViolationException ex){
			logger.error("INFO : [Record Exists so not creating another record][exception] " +ex.getLocalizedMessage() +" Dto : " +autoSignInDto);

		} catch (Exception e) {
			logger.error("ERROR : [AutoSignInDao][saveOrUpdateIntoSignInTable][Exception] : "+e.getMessage());
//			e.printStackTrace();
		}
		return message;
	}

	@SuppressWarnings({ "unchecked" })
	public List<String> getUsersSignedIn() {
		String query = "SELECT DISTINCT(X.SERIAL_ID) FROM TM_GEOTAB_AUTO_SIGNIN X WHERE X.SIGNIN_END_TIME IS NULL";
		return this.getSession().createSQLQuery(query).list();
	}

	public void updateParkEndTime(DeviceStatusInfoDto deviceDto) {
		String query = "UPDATE TM_GEOTAB_AUTO_SIGNIN X SET X.SIGNIN_END_TIME = TO_TIMESTAMP('"+ServicesUtil.convertFromZoneToZoneString(deviceDto.getParkEndTime(), null, "", "","", MurphyConstant.DATE_DB_FORMATE_SD)+"', 'yyyy-MM-dd HH24:mi:ss'), TIME_SIGNEDIN = SECONDS_BETWEEN(X.SIGNIN_START_TIME, TO_TIMESTAMP('"+ServicesUtil.convertFromZoneToZoneString(deviceDto.getParkEndTime(), null, "", "","", MurphyConstant.DATE_DB_FORMATE_SD)+"', 'yyyy-MM-dd HH24:mi:ss')) WHERE X.SERIAL_ID = '"+deviceDto.getDeviceId()+"' AND X.SIGNIN_END_TIME IS NULL";
		int update = this.getSession().createSQLQuery(query).executeUpdate();
		System.err.println("Updating SignIn End Time : "+update);
	}
	
	public ResponseMessage deleteRecords() {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Delete Failed");
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		String query = "DELETE FROM TM_GEOTAB_AUTO_SIGNIN WHERE TIME_SIGNEDIN < "+MurphyConstant.AUTOSIGNIN_TIME_PARKED_CONSTANT+"";
		int update = this.getSession().createSQLQuery(query).executeUpdate();
//		System.err.println("Deleting Users who signed in less than 20 min : "+update);
		if(update > 0) {
			message.setMessage("Delete Success");
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} else if(update == 0) {
			message.setMessage("Nothing to Delete");
		}
		return message;
	}

	@Override
	protected BaseDo importDto(BaseDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		return null;
	}

	@Override
	protected BaseDto exportDto(BaseDo entity) {
		return null;
	}
}
