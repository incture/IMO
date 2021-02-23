package com.murphy.taskmgmt.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dao.UserLoginDetailsDao;
import com.murphy.taskmgmt.dto.GreetingResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UserLoginDetailsDto;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.interfaces.GreetingFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("GreetingFacade")
public class GreetingFacade implements GreetingFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(GreetingFacade.class);

	@Autowired
	private UserLoginDetailsDao userLoginDetailsDao;

	@Autowired
	private UserIDPMappingDao userIDPMappingDao;

	@Override
	public GreetingResponseDto checkIfGreeted(String userEmail) {
		GreetingResponseDto greetingResponseDto = new GreetingResponseDto();
		UserIDPMappingDo userDo = userIDPMappingDao.getUserByEmail(userEmail);
		UserLoginDetailsDto dto = new UserLoginDetailsDto();
		dto.setUserEmail(userEmail);
		Boolean isGreeted = false;
		ResponseMessage response = new ResponseMessage();
		if (!ServicesUtil.isEmpty(userDo)) {
			greetingResponseDto.setFirstName(userDo.getUserFirstName());
			greetingResponseDto.setLastName(userDo.getUserLastName());
			ZoneId zone = ZoneId.of( "US/Central" );
			String dayStart = "05:00:00";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			int previousDay = 0;
			if(ZonedDateTime.now(zone).getHour()<5)
				previousDay =1;
			if (userDo.getUserRole().toUpperCase().contains("KAYBOB") || userDo.getUserRole().toUpperCase().contains("MONTNEY")) {
				zone = ZoneId.of( "GMT-7" );
				dayStart = "08:00:00";
				if(ZonedDateTime.now(zone).getHour()<8)
					previousDay =1;
			}
			dayStart = ZonedDateTime.now(zone).minusDays(previousDay).format(formatter) + "T" + dayStart;
			try {
				ZonedDateTime startTime = LocalDateTime.parse(dayStart).atZone(zone);
				dto = userLoginDetailsDao.getByKeys(dto);
				if (!ServicesUtil.isEmpty(dto) && !ServicesUtil.isEmpty(dto.getLastLoginTime())) {
					logger.error("Day Start ["+dayStart+"] startTime ["+startTime+"] Last login at: ["+ dto.getLastLoginTime().toInstant().atZone(zone)+"]");
					if (dto.getLastLoginTime().toInstant().atZone(zone).compareTo(startTime) >= 0)
						isGreeted = true;
				}
				response.setStatus("SUCCESS");
				response.setStatusCode(MurphyConstant.CODE_SUCCESS);
				response.setMessage("User details fecthed successfully");
			}catch(NoResultFault e){
				logger.error("[Murphy][GreetingFacade][checkIfGreeted] error : " + e.getMessage());
				response.setStatus("SUCCESS");
				response.setStatusCode(MurphyConstant.CODE_SUCCESS);
				response.setMessage("User details fecthed successfully");
			}catch (Exception e) {
				logger.error("[Murphy][GreetingFacade][checkIfGreeted] error : " +e.getClass()+" "+e.getMessage());
				response.setStatus("FAILURE");
				response.setStatusCode(MurphyConstant.CODE_FAILURE);
				response.setMessage(e.getMessage());
			}finally{
				dto.setUserEmail(userEmail);
				dto.setLastLoginTime(Calendar.getInstance().getTime());
				userLoginDetailsDao.updateLastLogin(dto);
			}
		}else{
			response.setStatus("ERROR");
			response.setStatusCode(MurphyConstant.CODE_SUCCESS);
			response.setMessage("User does not exist");
		}
		
		greetingResponseDto.setIsGreeted(isGreeted);
		greetingResponseDto.setResponseMessage(response);
		return greetingResponseDto;
	}
}
