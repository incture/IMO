package com.murphy.taskmgmt.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.murphy.integration.util.ServicesUtil;
import com.murphy.taskmgmt.dao.RouteLocationDao;
import com.murphy.taskmgmt.dao.ShiftAuditLogDao;
import com.murphy.taskmgmt.dao.ShiftRegisterDao;
import com.murphy.taskmgmt.dto.EmpShiftDetailsDto;
import com.murphy.taskmgmt.dto.Response;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RouteLocationDto;
import com.murphy.taskmgmt.dto.ShiftAuditLogDto;
import com.murphy.taskmgmt.dto.ShiftRegisterDto;
import com.murphy.taskmgmt.dto.ShiftRegisterResponseDto;
import com.murphy.taskmgmt.service.interfaces.ShiftRegisterFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;

@Service("ShiftRegisterFacade")
public class ShiftRegisterFacade implements ShiftRegisterFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ShiftRegisterFacade.class);

	@Autowired
	private ShiftRegisterDao shiftRegisterDao;

	@Autowired
	ShiftAuditLogDao shiftAuditLogDao;

	@Autowired
	RouteLocationDao routeLocationDao;

	@Override
	public ResponseMessage createShiftRegister(ShiftRegisterDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Shift Register Insertion " + MurphyConstant.FAILURE);
		try {
			shiftRegisterDao.create(dto);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Shift Register Inserted " + MurphyConstant.SUCCESSFULLY);

		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][createShiftRegister][Exception]" + e.getMessage());
		}

		return responseDto;
	}

	@Override
	public ResponseMessage updateShiftRegister(EmpShiftDetailsDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Shift Register Upadte" + MurphyConstant.FAILURE);

		try {

			// update RouteLocation
			//routeLocationDao.updateRouteLocation(dto);

			String msg = shiftRegisterDao.updateEmpShift(dto);
			if (msg.equalsIgnoreCase(MurphyConstant.SUCCESS) && !ServicesUtil.isEmpty(dto.getShiftAuditDto())){
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
				int currentDate=calendar.get(Calendar.DAY_OF_MONTH);
				String[] dateArr=dto.getDate().split("-");
				int date=Integer.parseInt(dateArr[0]);
				if(currentDate==date)
				dto.getShiftAuditDto().setShiftDay(MurphyConstant.TODAY_SHIFT);
				else
				dto.getShiftAuditDto().setShiftDay(MurphyConstant.TOMORROW);	
				responseDto = shiftAuditLogDao.createInstance(dto.getShiftAuditDto(),true);
				
			}
			if (msg.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
				responseDto.setStatus(MurphyConstant.SUCCESS);
				responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseDto.setMessage("Shift Register Updated " + MurphyConstant.SUCCESSFULLY);
			}

		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][updateShiftRegister][Exception]" + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ResponseMessage deleteShiftRegister(ShiftRegisterDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Shift Register Deletion " + MurphyConstant.FAILURE);

		try {

			shiftRegisterDao.delete(dto);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Shift Register Deleted " + MurphyConstant.SUCCESSFULLY);

		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][deleteShiftRegister][Exception]" + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ShiftRegisterDto getShiftRegister(ShiftRegisterDto dto) {
		ShiftRegisterDto shiftRegisterDto = null;
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Shift Register Fetch " + MurphyConstant.FAILURE);
		try {
			shiftRegisterDto = shiftRegisterDao.getByKeys(dto);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Shift Register Fetched " + MurphyConstant.SUCCESSFULLY);

		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][getShiftRegister][Exception]" + e.getMessage());
		}
		return shiftRegisterDto;
	}

	;

	@Override
	public ResponseMessage shiftRegisterScheduler() {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Shift Register Scheduler Run " + MurphyConstant.FAILURE);
		try {
			String message = shiftRegisterDao.shiftRegisterScheduler();
			if (message.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
				responseDto.setStatus(MurphyConstant.SUCCESS);
				responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseDto.setMessage("Shift Register Scheduler Ran " + MurphyConstant.SUCCESSFULLY);
			}

		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][getShiftRegister][Exception]" + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ShiftRegisterResponseDto empShiftDetails(String date, String Email, List<String> userRole) {
		ShiftRegisterResponseDto shiftRegisterResponseDto = new ShiftRegisterResponseDto();
		List<EmpShiftDetailsDto> empShiftDetailsDto = null;
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseMessage.setMessage("No operators available under logged in User");
		boolean isROC = false;
		try {
			if (!ServicesUtil.isEmpty(userRole)) {
				for (String role : userRole) {
					if (role.contains(MurphyConstant.USER_TYPE_ROC)) {
						isROC = true;
						break;
					}

				}
			}
			if (isROC) {
				empShiftDetailsDto = shiftRegisterDao.empShiftDetailsUnderROC(date, Email,
						userRole);
			} else {
				empShiftDetailsDto = shiftRegisterDao.empShiftDetails(date, Email);
			}

			if (!ServicesUtil.isEmpty(empShiftDetailsDto)) {
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseMessage.setMessage("Employee Details Fetched " + MurphyConstant.SUCCESSFULLY);
				shiftRegisterResponseDto.setEmpShiftDetailsDto(empShiftDetailsDto);
			}
			shiftRegisterResponseDto.setResponseMessage(responseMessage);
			return shiftRegisterResponseDto;
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][empShiftDetails][Exception]" + e.getMessage());
		}
		return shiftRegisterResponseDto;
	}

	@Override
	public boolean getShiftDetailsForEmp(String emp_email) {
		boolean response_inShift = false;
		try {
			response_inShift = shiftRegisterDao.getShiftDetailsForEmp(emp_email);
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][getShiftDetailsForEmp][Exception]" + e.getMessage());
		}
		return response_inShift;
	}

	@Override
	public ResponseEntity<Response<JSONObject>> getAuditDetails(int month, int pageNum) {
		Response<JSONObject> response = new Response<JSONObject>();
		try {

			// Default pageNum to 1 if its 0 or empty
			if (ServicesUtil.isEmpty(pageNum) || pageNum == 0)
				pageNum = 1;

			JSONObject shiftRegisterObj = shiftAuditLogDao.getAllShiftAuditDetails(month, pageNum);
			response.setOutput(shiftRegisterObj);
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][getAuditDetails][Exception]" + e.getMessage());
			response.setStatus(MurphyConstant.FAILURE);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Response<List<RouteLocationDto>>> fetchWellPad(String empId, String facility) {
		Response<List<RouteLocationDto>> response = new Response<List<RouteLocationDto>>();
		try {
			if (!ServicesUtil.isEmpty(facility)) {
				Map<String,String> wellPadNameList = shiftRegisterDao.getAllWellPadByFacility(empId, facility);
				List<RouteLocationDto> wellPadList = routeLocationDao.getWellPadMapLocByEmpId(empId, wellPadNameList);
				response.setOutput(wellPadList);
			}
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterFacade][fetchWellPad][Exception]" + e.getMessage());
			response.setStatus(MurphyConstant.FAILURE);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}

		return ResponseEntity.ok(response);
	}

}
