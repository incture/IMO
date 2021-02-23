package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.DeviceDto;
import com.murphy.taskmgmt.entity.DeviceDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("deviceDao")
public class DeviceDao extends BaseDao<DeviceDo, DeviceDto> {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDao.class);

	@Override
	protected DeviceDo importDto(DeviceDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		DeviceDo entity = new DeviceDo();

		if (!ServicesUtil.isEmpty(fromDto.getSeverity())) {
			entity.setSeverity(fromDto.getSeverity());
		}

		if (!ServicesUtil.isEmpty(fromDto.getDeviceName())) {
			entity.setDeviceName(fromDto.getDeviceName());
		}
		if (!ServicesUtil.isEmpty(fromDto.getActiveFlag())) {
			entity.setActiveFlag(fromDto.getActiveFlag());
		}
		return entity;
	}

	@Override
	protected DeviceDto exportDto(DeviceDo entity) {
		DeviceDto dto = new DeviceDto();

		if (!ServicesUtil.isEmpty(entity.getSeverity())) {
			dto.setSeverity(entity.getSeverity());
		}

		if (!ServicesUtil.isEmpty(entity.getDeviceName())) {
			dto.setDeviceName(entity.getDeviceName());
		}
		if (!ServicesUtil.isEmpty(entity.getActiveFlag())) {
			dto.setActiveFlag(entity.getActiveFlag());
		}
		return dto;
	}

	public String createDeviceRecord(DeviceDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			create(dto);
			reponse = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][createDeviceRecord][error]" + e.getMessage());
		}
		return reponse;
	}

	/*
	 * public String getSeqVal() { String deviceId = ""; try { String query =
	 * "select max(deviceId) from DeviceDo"; deviceId = (String)
	 * this.getSession().createQuery(query).uniqueResult(); if
	 * (!ServicesUtil.isEmpty(deviceId)) { // deviceId=(String)object;
	 * StringBuffer num = new StringBuffer(); StringBuffer alpha = new
	 * StringBuffer(); for (int i = 0; i < deviceId.length(); i++) { if
	 * (Character.isDigit(deviceId.charAt(i))) { num.append(deviceId.charAt(i));
	 * } if (Character.isAlphabetic(deviceId.charAt(i))) {
	 * alpha.append(deviceId.charAt(i)); } }
	 * 
	 * int id = Integer.parseInt(num.toString()); id++; deviceId =
	 * alpha.toString() + id; } else { deviceId = "D1"; } } catch (Exception e)
	 * { System.err.println(e.getMessage()); e.printStackTrace(); }
	 * System.err.println("Device id:" + deviceId); return deviceId; }
	 */

	public List<DeviceDto> getDeviceList() {
		List<DeviceDto> deviceDtoList = new ArrayList<>();
		try {
			String activeFlag=MurphyConstant.ACTIVE;
			String query = "from DeviceDo p where p.activeFlag='" + activeFlag + "'";
			List<Object> deviceList = this.getSession().createQuery(query).list();
			for (Object device : deviceList) {
				DeviceDto deviceDto = new DeviceDto();
				deviceDto.setSeverity(((DeviceDo) device).getSeverity());
				deviceDto.setDeviceName(((DeviceDo) device).getDeviceName());
				deviceDto.setActiveFlag(((DeviceDo) device).getActiveFlag());
				deviceDtoList.add(deviceDto);

			}
		} catch (Exception e) {
			logger.error("[Murphy][DeviceDao][getDeviceList][error]" + e.getMessage());
		}
		return deviceDtoList;
	}

}
