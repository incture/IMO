package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.QueryHint;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.EmpShiftDetailsDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.RouteLocationDto;
import com.murphy.taskmgmt.entity.RouteLocationDo;
import com.murphy.taskmgmt.entity.RouteLocationDoPK;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("RouteLocationDao")
@Transactional
public class RouteLocationDao extends BaseDao<RouteLocationDo, RouteLocationDto> {

	@Autowired
	HierarchyDao hierarchyDao;

	private static final Logger logger = LoggerFactory.getLogger(RouteLocationDao.class);

	@Override
	protected RouteLocationDo importDto(RouteLocationDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		RouteLocationDo entity = new RouteLocationDo();
		RouteLocationDoPK routePK = new RouteLocationDoPK();
		routePK.setEmpId(fromDto.getEmpId());
		routePK.setLocation(fromDto.getLocation());
		entity.setRouteLocationDoPk(routePK);
		entity.setLocationType(fromDto.getType());
		return entity;
	}

	@Override
	protected RouteLocationDto exportDto(RouteLocationDo entity) {
		RouteLocationDto dto = new RouteLocationDto();
		dto.setEmpId(entity.getRouteLocationDoPk().getEmpId());
		dto.setLocation(entity.getRouteLocationDoPk().getLocation());
		dto.setType(entity.getLocationType());
		return dto;
	}

	public String updateRouteLocation(EmpShiftDetailsDto dto) throws Exception {

		if (!ServicesUtil.isEmpty(dto.getRouteLocationDtoList())) {

			// Clear Previous Route Location
			String status = clearRouteLocById(dto.getEmpEmail());

			if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {

				for (RouteLocationDto routeDto : dto.getRouteLocationDtoList()) {
					routeDto.setEmpId(dto.getEmpId());
					this.create(routeDto);
				}
				return MurphyConstant.SUCCESS;
			}
			return MurphyConstant.FAILURE;

		}
		return MurphyConstant.FAILURE;
	}

	private String clearRouteLocById(String emailId) throws Exception {
		try {
			if (!ServicesUtil.isEmpty(emailId)) {

				String queryString = "DELETE FROM ROUTE_LOCATION_MAPPING WHERE EMP_ID ='" + emailId + "'";
				this.getSession().createSQLQuery(queryString).executeUpdate();
				return MurphyConstant.SUCCESS;
			}

		} catch (Exception ex) {
			logger.error("[RouteLocationDao]Exception while deleting Emp Detail : " + ex.getMessage());
			throw ex;
		}
		return MurphyConstant.FAILURE;

	}

	@SuppressWarnings("unchecked")
	public List<RouteLocationDto> fetchMappedLocByEmpId(String empId, Map<String,String> facilityList) throws Exception {
		List<RouteLocationDto> routeLocationDtoList = new ArrayList<>();
		RouteLocationDto dto = null;
		if (!ServicesUtil.isEmpty(empId)) {

			String queryString = "SELECT LOCATION,TYPE FROM ROUTE_LOCATION_MAPPING WHERE EMP_ID='" + empId + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = (List<Object[]>) q.list();
			logger.error("[Murphy][RouteLocationDao][fetchMappedLocByEmpId][queryString]" + queryString);
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					dto = new RouteLocationDto();
					dto.setEmpId(empId);
					String location = ServicesUtil.isEmpty(obj[0]) ? "" : (String) obj[0];
					String type = ServicesUtil.isEmpty(obj[1]) ? "" : (String) obj[1];

					if (type.equalsIgnoreCase(MurphyConstant.FACILITY)) {
						if (!ServicesUtil.isEmpty(facilityList) && facilityList.containsKey(location)) {
							dto.setLocation(location);
							dto.setLocationCode(facilityList.get(location));
							dto.setType(type);
							dto.setSelected(true);
							facilityList.remove(location,facilityList.get(location));
						}
					} else if (type.equalsIgnoreCase(MurphyConstant.WELLPAD)) {
						location= hierarchyDao.getFacilityByName(location);
						dto.setLocation(location);
						dto.setLocationCode(facilityList.get(location));
						dto.setType(MurphyConstant.FACILITY);
						dto.setSelected(true);
						facilityList.remove(location);
					}
					routeLocationDtoList.add(dto);
				}
			}

		}
		if (!ServicesUtil.isEmpty(facilityList)) {
			for (String facility : facilityList.keySet()) {
				dto = new RouteLocationDto();
				dto.setEmpId(empId);
				dto.setLocation(facility);
				dto.setLocationCode(facilityList.get(facility));
				dto.setType(MurphyConstant.FACILITY);
				dto.setSelected(false);
				routeLocationDtoList.add(dto);
			}
		}

		return routeLocationDtoList;
	}

	@SuppressWarnings("unchecked")
	public List<RouteLocationDto> getWellPadMapLocByEmpId(String empId, Map<String,String> wellPadLocList) throws Exception {
		List<RouteLocationDto> routeLocationDtoList = new ArrayList<>();
		RouteLocationDto dto = null;
		if (!ServicesUtil.isEmpty(empId)) {

			String queryString = "SELECT LOCATION,TYPE FROM ROUTE_LOCATION_MAPPING WHERE EMP_ID='" + empId
					+ "' AND TYPE='Well Pad'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = (List<Object[]>) q.list();
			logger.error("[Murphy][RouteLocationDao][getWellPadMapLocByEmpId][queryString]" + queryString);
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					dto = new RouteLocationDto();
					String location = ServicesUtil.isEmpty(obj[0]) ? "" : (String) obj[0];
					String type = ServicesUtil.isEmpty(obj[1]) ? "" : (String) obj[1];
                      
					if (type.equalsIgnoreCase(MurphyConstant.WELLPAD)) {
						if(!ServicesUtil.isEmpty(wellPadLocList) && wellPadLocList.containsKey(location)){
						dto.setEmpId(empId);	
						dto.setLocation(location);
						dto.setLocationCode(wellPadLocList.get(location));
						dto.setType(type);
						dto.setSelected(true);
						wellPadLocList.remove(location);
						routeLocationDtoList.add(dto);

						}
					}
				}
			}
		}
		if (!ServicesUtil.isEmpty(wellPadLocList)) {
			for (String wellPad : wellPadLocList.keySet()) {
				dto = new RouteLocationDto();
				dto.setEmpId(empId);	
				dto.setLocation(wellPad);
				dto.setLocationCode(wellPadLocList.get(wellPad));
				dto.setType(MurphyConstant.WELLPAD);
				dto.setSelected(false);
				routeLocationDtoList.add(dto);
			}
		}
		return routeLocationDtoList;
	}

}
