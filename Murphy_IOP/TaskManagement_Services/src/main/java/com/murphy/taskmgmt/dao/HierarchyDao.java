package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.geotab.Coordinates;
import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.HierarchyRequestDto;
import com.murphy.taskmgmt.dto.HierarchyResponseDto;
import com.murphy.taskmgmt.dto.LocationDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.LocationHierarchyDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("HierarchyDao")
public class HierarchyDao extends BaseDao<LocationHierarchyDo, LocationHierarchyDto> {

	private static final Logger logger = LoggerFactory.getLogger(HierarchyDao.class);

	public HierarchyDao() {
	}

	public LocationHierarchyDto getWellDetailsForMuwi(String muwi) {
		LocationHierarchyDto hierarchyDto = null;
		if (!ServicesUtil.isEmpty(muwi)) {
			String queryForWellDetails = "select p1.location_code As location_code,p1.location_text As well,"
					+ "p2.location_text as wellpad,p3.location_text as facility,p4.location_text as field,"
					+ "r.latitude,r.longitude"
					+ " from well_muwi wm join production_location p1 on p1.location_code=wm.location_code "
					+ "inner join production_location p2 on p1.parent_code=p2.location_code " + "inner join "
					+ "production_location p3  on p2.parent_code=p3.location_code " + "inner join "
					+ "production_location p4 on p3.parent_code=p4.location_code " + "left join "
					+ "location_coordinate r on r.location_code=p1.location_code" + " where wm.muwi='" + muwi + "'";
			// logger.error(
			// "[Murphy][HierarchyDao][getWellDetailsForMuwi][queryForWellDetails]"
			// + queryForWellDetails);

			Query query = this.getSession().createSQLQuery(queryForWellDetails);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.list();
			if (!ServicesUtil.isEmpty(result) && result.size() > 0) {
				hierarchyDto = new LocationHierarchyDto();
				Object[] object = result.get(0);
				if (!ServicesUtil.isEmpty(object)) {
					hierarchyDto.setMuwi((String) object[0]);
					hierarchyDto.setWell((String) object[1]);
					hierarchyDto.setWellpad((String) object[2]);
					hierarchyDto.setFacility((String) object[3]);
					hierarchyDto.setField((String) object[4]);
					// hierarchyDto.setBusinessUnit((String) object[5]);
					// hierarchyDto.setBusinessEntity((String) object[6]);
					hierarchyDto.setLatValue((BigDecimal) object[5]);
					hierarchyDto.setLongValue((BigDecimal) object[6]);
				}
			}
		}
		return hierarchyDto;
	}

	@SuppressWarnings("unchecked")
	public HierarchyResponseDto getHierarchy(HierarchyRequestDto hierarchyRequestDto) {
		HierarchyResponseDto responseDto = null;
		String locationType = hierarchyRequestDto.getLocationType();
		String responseLocationType = null, responseCompressor = null, responseFlare = null;
		String navigate = hierarchyRequestDto.getNavigate();
		String location = hierarchyRequestDto.getLocation();
		List<String> locationList = new ArrayList<>();
		
		String role = hierarchyRequestDto.getRole();
		String orderByQuery = " order by p1.location_text ";
		if (!ServicesUtil.isEmpty(role) && ServicesUtil.isEmpty(location)) {
			String getLocationString = "select distinct(rm.field) as RESULT from TM_ROLE_MAPPING rm where rm.BUSINESSEROLE='"
					+ role + "'";
			Query q = this.getSession().createSQLQuery(getLocationString);
			List<String> response = q.list();
			if (!ServicesUtil.isEmpty(response)) {
				//location = response.get(0);
				locationList.addAll(response);

			}
		}
		if(!ServicesUtil.isEmpty(locationList))
			location = ServicesUtil.getStringFromList(locationList);
		else if(!ServicesUtil.isEmpty(location) && ServicesUtil.isEmpty(locationList)){
			locationList.add(location);
			location = ServicesUtil.getStringFromList(locationList);
		}
		
		String queryString = "", queryStringCompressor = "", queryStringFlare = "";
		
		//SOC : For Canada adding one more top layer
		if ((ServicesUtil.isEmpty(locationType) && ServicesUtil.isEmpty(location) && ServicesUtil.isEmpty(navigate))
				|| MurphyConstant.BASE.equalsIgnoreCase(locationType) && ServicesUtil.isEmpty(location) && ServicesUtil.isEmpty(navigate) ||
				MurphyConstant.PARENT.equalsIgnoreCase(navigate) && MurphyConstant.FIELD.equalsIgnoreCase(locationType)) 
		{

			responseLocationType = MurphyConstant.BASE;
			queryString = "select distinct p1.location_text as field,p1.location_code as field_name from "
					+ "production_location p1 where upper(p1.location_type)=upper('" + MurphyConstant.BASE + "')";
			
		} else if(MurphyConstant.BASE.equalsIgnoreCase(locationType) && MurphyConstant.CHILD.equalsIgnoreCase(navigate) && !ServicesUtil.isEmpty(location) ){
			
			responseLocationType = MurphyConstant.FIELD;
			queryString +=  "select distinct p1.location_text as field,p1.location_code as field_name "
			+ "from production_location p1 inner join production_location j1 "
			+ "on p1.location_code=p1.location_code where p1.parent_code in (" + location + ")";
			
		} //For going up the order from Facility to Field
		else if ( MurphyConstant.FIELD.equalsIgnoreCase(locationType) && ServicesUtil.isEmpty(location)
				&& ServicesUtil.isEmpty(navigate) 
				|| (MurphyConstant.FACILITY.equalsIgnoreCase(locationType) || MurphyConstant.FACILITY_DB.equalsIgnoreCase(locationType)) 
				&& MurphyConstant.PARENT.equalsIgnoreCase(navigate)) {

			responseLocationType = MurphyConstant.FIELD;
			
			queryString = "select distinct p1.location_text as field,p1.location_code as field_name from "
					+ "production_location p1 where upper(p1.location_type)=upper('" + MurphyConstant.FIELD + "')";
			if(!ServicesUtil.isEmpty(location))
				queryString+= " and p1.parent_code in ( select distinct (p3.location_code ) from	production_location pa, production_location p2"
					+ ",production_location p3 where pa.parent_code=p2.location_code and "
					+ "p2.parent_code=p3.location_code and pa.location_code in ("+location+"))";
			
		}
		
		//EOC : : For Canada adding one more top layer
		/*else if (ServicesUtil.isEmpty(locationType) && ServicesUtil.isEmpty(location) && ServicesUtil.isEmpty(navigate)
				|| MurphyConstant.FIELD.equalsIgnoreCase(locationType) && ServicesUtil.isEmpty(location)
						&& ServicesUtil.isEmpty(navigate)
				|| (MurphyConstant.FACILITY.equalsIgnoreCase(locationType) || MurphyConstant.FACILITY_DB.equalsIgnoreCase(locationType))
						&& MurphyConstant.PARENT.equalsIgnoreCase(navigate)
				|| location.equals("KARNES")) {

			responseLocationType = MurphyConstant.FIELD;

			queryString = "select distinct p1.location_text as field,p1.location_code as field_name from "
					+ "production_location p1 where upper(p1.location_type)=upper('" + MurphyConstant.FIELD + "')";
		}*/

		else if (MurphyConstant.FIELD.equalsIgnoreCase(locationType) && MurphyConstant.CHILD.equalsIgnoreCase(navigate)
				|| MurphyConstant.WELLPAD.equalsIgnoreCase(locationType) && MurphyConstant.PARENT.equalsIgnoreCase(navigate)
				|| MurphyConstant.COMPRESSOR.equalsIgnoreCase(locationType) && MurphyConstant.PARENT.equalsIgnoreCase(navigate)
				|| (MurphyConstant.FACILITY.equalsIgnoreCase(locationType) || MurphyConstant.FACILITY_DB.equalsIgnoreCase(locationType))
						&& ServicesUtil.isEmpty(location) && ServicesUtil.isEmpty(navigate)) {

			responseLocationType = MurphyConstant.FACILITY;

			queryString += "select distinct p1.location_text as facility_name,p1.location_code as facility,j1.latitude,j1.longitude "
					+ "from production_location p1 left join location_coordinate j1 on j1.location_code=p1.location_code ";

			if (!ServicesUtil.isEmpty(location)) {
				if (MurphyConstant.CHILD.equals(navigate))
					queryString += " where p1.parent_code in (" + location + ")";
				else {
					if (MurphyConstant.COMPRESSOR.equalsIgnoreCase(locationType)) {
						queryString += " where  p1.parent_code in  (select distinct (p2.location_code) from "
								+ "production_location pa, production_location p2"
								+ " where pa.parent_code=p2.location_code  and pa.location_code in "
								+ "(select distinct parent_code as facility from "
								+ "production_equipment where equipment_code in (" + location + ")))";
					} else {
						queryString += " where p1.parent_code in (select distinct (p3.location_code ) " + "from "
								+ "production_location pa, production_location p2," + "production_location p3 "
								+ "where pa.parent_code=p2.location_code and p2.parent_code=p3.location_code "
								+ "and pa.location_code in (" + location + "))";
					}
				}
			} else if (ServicesUtil.isEmpty(navigate)) {
				queryString += " where upper(p1.location_type) in ('" + MurphyConstant.FACILITY_DB.toUpperCase()
						+ "' ,'" + MurphyConstant.FACILITY.toUpperCase() + "')";
			}

		} else if ((MurphyConstant.FACILITY.equalsIgnoreCase(locationType) || MurphyConstant.FACILITY_DB.equalsIgnoreCase(locationType))
				&& MurphyConstant.CHILD.equalsIgnoreCase(navigate)
				|| MurphyConstant.WELL.equalsIgnoreCase(locationType) && MurphyConstant.PARENT.equalsIgnoreCase(navigate)
				|| MurphyConstant.WELLPAD.equalsIgnoreCase(locationType) && ServicesUtil.isEmpty(location)
						&& ServicesUtil.isEmpty(navigate)) {

			responseLocationType = MurphyConstant.WELLPAD;
			responseCompressor = MurphyConstant.COMPRESSOR;
			responseFlare = MurphyConstant.FLARE;

			queryString += "select distinct p1.location_text as wellpad_compressor,p1.location_code as wellpad_name from "
					+ "production_location p1 ";
			queryStringCompressor = "select p1.equipment_code,p1.equipment_text,jl.latitude,jl.longitude ,em.merrick_id as muwi "
					+ "from production_equipment p1 left outer join location_coordinate  jl "
					+ "on p1.parent_code = jl.location_code "
					+ " left join equipment_merrick em on p1.equipment_code = em.equipment_code "; // added
																									// merrick
																									// id
																									// for
																									// compressors
																									// in
																									// muwid
																									// field
			queryStringFlare = "select p1.MERRICK_ID,p1.METER_NAME,jl.latitude,jl.longitude ,p1.MERRICK_ID as mi "
					+ "from TM_FLARE_METER p1 left outer join location_coordinate  jl "
					+ "on p1.parent_code = jl.location_code ";

			if (!ServicesUtil.isEmpty(location)) {
				if (MurphyConstant.CHILD.equalsIgnoreCase(navigate)) {
					queryString += " where  p1.parent_code in (" + location + ")";
					queryStringCompressor += " where p1.parent_code in (" + location + ")";
					queryStringFlare += " where p1.parent_code in (" + location + ")";

				} else if (MurphyConstant.PARENT.equalsIgnoreCase(navigate)) {
					queryString += " where upper(p1.location_type)=upper('" + MurphyConstant.WELLPAD
							+ "') and p1.parent_code in (select distinct (p3.location_code ) from "
							+ "production_location pa, production_location p2, production_location p3 "
							+ "where pa.parent_code=p2.location_code and p2.parent_code=p3.location_code "
							+ " and pa.location_code in (" + location + "))";

					queryStringCompressor += " where p1.parent_code in "
							+ "(select distinct (p3.location_code ) from  production_location pa,"
							+ " production_location p2,  production_location p3 where pa.parent_code=p2.location_code "
							+ "and p2.parent_code=p3.location_code and pa.location_code in (" + location + "))";

					queryStringFlare += " where p1.parent_code in "
							+ "(select distinct (p3.location_code ) from  production_location pa,"
							+ " production_location p2,  production_location p3 where pa.parent_code=p2.location_code "
							+ "and p2.parent_code=p3.location_code and pa.location_code in (" + location + "))";

				}
			} else if (ServicesUtil.isEmpty(navigate)) {
				queryString += " where upper(p1.location_type)=upper('" + MurphyConstant.WELLPAD + "')";
				queryStringCompressor += "";
				queryStringFlare += "";
			}

		} else if (MurphyConstant.WELLPAD.equalsIgnoreCase(locationType) && MurphyConstant.CHILD.equalsIgnoreCase(navigate)
				|| (locationType.equals(MurphyConstant.WELL) && ServicesUtil.isEmpty(navigate)
						&& ServicesUtil.isEmpty(location))) {

			responseLocationType = MurphyConstant.WELL;

			queryString = "select p1.location_code,p1.location_text,jl.latitude,jl.longitude,wm.muwi from  production_location "
					+ "p1 left outer join  location_coordinate  jl on p1.location_code = jl.location_code"
					+ " left join  well_muwi wm on wm.location_code=p1.location_code where p1.location_type='"
					+ MurphyConstant.WELL + "'";
			if (!ServicesUtil.isEmpty(location)) {
				queryString += " and p1.parent_code in (" + location + ")";
			}
		}

		if (locationType.equals(MurphyConstant.SEARCH) && !ServicesUtil.isEmpty(location)) {
			responseLocationType = MurphyConstant.SEARCH;
			// SOC: For facility Search
			if (!ServicesUtil.isEmpty(hierarchyRequestDto.getForFacility())) {
				if (hierarchyRequestDto.getForFacility().equalsIgnoreCase(MurphyConstant.TRUE)) {
					queryString = "select p1.location_code,p1.location_text,jl.latitude,jl.longitude,wm.muwi from production_location p1 "
							+ "left outer join location_coordinate jl on"
							+ " jl.location_code =p1.location_code left join well_muwi wm on wm.location_code=p1.location_code where";
					queryString = queryString + " (upper(p1.location_type)=upper('" + MurphyConstant.FACILITY + "')"
							+ " OR upper(p1.location_type)=upper('" + MurphyConstant.FACILITY_DB + "'))";
				}

			} // EOC: For facility Search
			else {
				queryString = "select p1.location_code,p1.location_text,jl.latitude,jl.longitude,wm.muwi from "
						+ "production_location p1 left outer join "
						+ "location_coordinate jl on jl.location_code =p1.location_code left join well_muwi wm"
						+ " on wm.location_code=p1.location_code where ";
				queryString = queryString + " upper(p1.location_type)=upper('" + MurphyConstant.WELL + "')";
			}
			if (!("'*'").equals(location)) {
				//
				String locLikeQuery = "";
				if(!ServicesUtil.isEmpty(locationList)){
					locLikeQuery = " lower(p1.location_text)  like '%" + locationList.get(0).toLowerCase() + "%'";
					for (int i = 1; i < locationList.size(); i++) 
						locLikeQuery += " or lower(p1.location_text)  like '%" + locationList.get(i).toLowerCase() + "%'";
				}
				
				//Original Query
				// queryString = queryString + " and lower(p1.location_text)  like '%" + location.toLowerCase() + "%' ";
				
				queryString = queryString + " and " +locLikeQuery;

			}
		}
		// logger.error("[Murphy][HierarchyDao][getHierarchy][queryString to get
		// Hierarchy] && [location_type]"
		// + queryString + " && " + responseLocationType);

		if (!ServicesUtil.isEmpty(responseLocationType)) {
			responseDto = new HierarchyResponseDto();
			responseDto.setLocationType(responseLocationType);
			List<LocationHierarchyDto> dtos = null, compressorDto = null, flareDto = null, dto;
			/*
			 * if (ServicesUtil.isEmpty(responseCompressor)) { queryString +=
			 * orderByQuery; dtos = getDataFromQuery(queryString,
			 * responseLocationType); } else {
			 */
			queryString += orderByQuery;
			dto = getDataFromQuery(queryString, responseLocationType);

			Set<LocationHierarchyDto> newSet = new LinkedHashSet<LocationHierarchyDto>(dto);
			if (!ServicesUtil.isEmpty(responseCompressor)) {
				queryStringCompressor += " order by p1.equipment_text";
				compressorDto = getDataFromQuery(queryStringCompressor, responseCompressor);
				newSet.addAll(compressorDto);
			}
			if (!ServicesUtil.isEmpty(responseFlare)) {
				queryStringFlare += " order by p1.METER_NAME";
				flareDto = getDataFromQuery(queryStringFlare, responseFlare);
				newSet.addAll(flareDto);
			}
			dtos = new ArrayList<LocationHierarchyDto>(newSet);

			// }
			responseDto.setLocationHierarchy(dtos);

		}
		return responseDto;
	}

	@SuppressWarnings("unchecked")
	private List<LocationHierarchyDto> getDataFromQuery(String queryString, String responseLocationType) {
		List<LocationHierarchyDto> locationList = new ArrayList<LocationHierarchyDto>();
		try {
			Query q = this.getSession().createSQLQuery(queryString);
			logger.error("getLoaction query Q " + queryString);

			LocationHierarchyDto dto = null;
			// logger.error("[Murphy][HierarchyDao][getHierarchy][getDataQuery][locationType]"
			// + responseLocationType);
			if (MurphyConstant.WELL.equals(responseLocationType) || MurphyConstant.SEARCH.equals(responseLocationType)
					|| MurphyConstant.COMPRESSOR.equals(responseLocationType)
					|| MurphyConstant.FLARE.equals(responseLocationType)) {

				List<Object[]> resultListAll = q.list();
				if (!ServicesUtil.isEmpty(resultListAll)) {
					for (Object[] obj : resultListAll) {
						dto = new LocationHierarchyDto();

						// if
						// (!MurphyConstant.COMPRESSOR.equals(responseLocationType))
						// //commented for compressors merrick id

						dto.setMuwi(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);

						dto.setLocationText(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						dto.setLocation(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						dto.setChildExist("FALSE");
						dto.setLongValue(ServicesUtil.isEmpty(obj[3]) ? null : (BigDecimal) obj[3]);
						dto.setLatValue(ServicesUtil.isEmpty(obj[2]) ? null : (BigDecimal) obj[2]);
						dto.setLocationType(responseLocationType);
						locationList.add(dto);
					}

				}

			} else {
				List<Object[]> resultList = null;
				resultList = q.list();
				if (!ServicesUtil.isEmpty(resultList)) {
					for (Object[] obj : resultList) {
						dto = new LocationHierarchyDto();
						dto.setLocation(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						dto.setLocationText(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						if ((MurphyConstant.FACILITY.equals(responseLocationType)
								|| MurphyConstant.FACILITY_DB.equals(responseLocationType))) {
							dto.setLatValue(ServicesUtil.isEmpty(obj[2]) ? null : (BigDecimal) obj[2]);
							dto.setLongValue(ServicesUtil.isEmpty(obj[3]) ? null : (BigDecimal) obj[3]);
						}
						String childString = "select to_char(count(*)) from production_location where parent_code='"
								+ (String) obj[1] + "'";
						String equipmentChildString = "select to_char(count(*)) from production_equipment "
								+ "where parent_code='" + (String) obj[1] + "'";

						Query countQuery = this.getSession().createSQLQuery(childString);
						Query countEquipQuery = this.getSession().createSQLQuery(equipmentChildString);

						List<String> response = countQuery.list();
						List<String> responseEquip = countEquipQuery.list();

						String result = "";
						if (!ServicesUtil.isEmpty(response)) {
							if (!ServicesUtil.isEmpty(responseEquip))
								result = responseEquip.get(0) + response.get(0);
							else
								result = response.get(0);

						}
						Integer res = Integer.valueOf(result);
						if (!ServicesUtil.isEmpty(result) && res > 0)
							dto.setChildExist("TRUE");
						else
							dto.setChildExist("FALSE");
						dto.setLocationType(responseLocationType);
						locationList.add(dto);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getDataFromQuery][error]" + e.getMessage());
		}
		return locationList;

	}

	@SuppressWarnings("unchecked")
	public List<LocationDto> getHierarchy(String loc, String locType) {

		List<LocationDto> dtoList = new ArrayList<LocationDto>();
		LocationDto dto = null;
		String queryString = "";
		if (locType.equals(MurphyConstant.FIELD)) {
			dto = new LocationDto();
			dto.setLoc(loc);
			dtoList.add(dto);
		} else {
			if (MurphyConstant.FACILITY.equals(locType) || MurphyConstant.FACILITY_DB.equals(locType)) {
				queryString = "select p2.location_text as field, p1.location_text as facility from "
						+ "production_location p1,"
						+ "production_location p2 where p1.parent_code=p2.location_code and  lower(p1.location_text)='"
						+ loc.toLowerCase() + "'";
			} else if (MurphyConstant.WELLPAD.equals(locType) || MurphyConstant.COMPRESSOR.equals(locType)) {
				queryString = "select p3.location_text as field, p2.location_text as facility ,p1.location_text as wellpad from "
						+ "production_location p1, production_location p2,"
						+ "production_location p3 where p1.parent_code=p2.location_code and p2.parent_code=p3.location_code and  lower(p1.location_text)='"
						+ loc.toLowerCase() + "'";
			} else if (MurphyConstant.WELL.equals(locType)) {
				queryString = "select p4.location_text as field, p3.location_text as facility ,p2.location_text as wellpad, p1.location_text as well from "
						+ "production_location p1, production_location p2," + "production_location p3,"
						+ "production_location p4 where p1.parent_code=p2.location_code and p2.parent_code=p3.location_code and p3.parent_code=p4.location_code and  lower(p1.location_text)='"
						+ loc.toLowerCase() + "'";
			}

			if (!ServicesUtil.isEmpty(queryString)) {
				// logger.error("[Murphy][HierarchyDao][getHierarchy][queryString
				// using loc and locType]" + queryString
				// + "[locType]" + locType);

				Query q = this.getSession().createSQLQuery(queryString);
				List<Object[]> resultListAll = q.list();
				if (!ServicesUtil.isEmpty(resultListAll)) {
					Object[] obj = resultListAll.get(0);
					for (Object objct : obj) {
						dto = new LocationDto();
						dto.setLoc(ServicesUtil.isEmpty(objct) ? null : (String) objct);
						dtoList.add(dto);
					}
				}
			}
		}
		return dtoList;
	}

	@SuppressWarnings("unchecked")
	public FieldResponseDto getFeild(String loc, String locType, boolean queryCheckForLocCode) {

		FieldResponseDto responseDto = new FieldResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Field fetch failed");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String queryString = "";
		if (locType.equals(MurphyConstant.FIELD)) {
			responseDto.setField(loc);
			responseMessage.setMessage("Field fetched successfully");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

		} else {
			if (MurphyConstant.FACILITY.equals(locType) || MurphyConstant.FACILITY_DB.equals(locType)) {
				queryString = "select distinct p2.location_text as field from  production_location p1,"
						+ " production_location p2 " + "where p1.parent_code=p2.location_code ";

			} else if (MurphyConstant.WELLPAD.equals(locType)) {
				queryString = "select distinct p3.location_text as field from  production_location p1,"
						+ " production_location p2," + "production_location p3 where p1.parent_code=p2.location_code"
						+ " and p2.parent_code=p3.location_code ";
			} else {
				queryString = "select distinct p4.location_text as field from  production_location p1,"
						+ " production_location p2, production_location p3, production_location p4 "
						+ "where p1.parent_code=p2.location_code and p2.parent_code=p3.location_code and"
						+ " p3.parent_code=p4.location_code ";
			}

			if (queryCheckForLocCode)
				queryString = queryString + " and p1.location_code='" + loc + "'";
			else
				queryString = queryString + " and lower(p1.location_text)='" + loc.toLowerCase() + "'";
			// logger.error("[Murphy][HierarchyDao][getFeild][queryString]" +
			// queryString);

			logger.error("query to get field----- " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = q.list();
			if (!ServicesUtil.isEmpty(response)) {
				logger.error("field from query " + response.get(0));
				responseDto.setField(response.get(0));
				responseMessage.setMessage("Field fetched successfully");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllWells() {
		try {
			String queryString = "select distinct(wm.muwi) from  production_location p1 , well_muwi wm where wm.location_code = p1.location_code";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getAllWells][response]" +
			// response);
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getAllWells][error]" + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllWellsLocCode() {
		try {
			String queryString = "select distinct(p1.Location_code) from  production_location p1 , well_muwi wm,LOCATION_COORDINATE lc where wm.location_code = p1.location_code "
					+ "and lc.Location_code=p1.Location_code";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getAllWells][response]" +
			// response);
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getAllWellsLocCode][error]" + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> getAllWellsLocationCodeAndTier() {
		HashMap<String, List<String>> response = new HashMap<String, List<String>>();
		try {
			String queryString = "select distinct(p1.location_code),tier from  production_location p1 , well_muwi wm ,well_tier wt where wm.location_code = p1.location_code and wt.location_code=p1.location_code";
			Query q = this.getSession().createSQLQuery(queryString);
			// logger.error("[Murphy][HierarchyDao][getAllWells][response]" +
			// response);
			List<Object[]> resulList = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(resulList)) {
				for (Object[] obj : resulList) {
					String key = (String) obj[1];
					if (response.containsKey(key)) {
						response.get(key).add((String) obj[0]);
					} else {
						List<String> locationCodes = new ArrayList<String>();
						locationCodes.add((String) obj[0]);
						response.put(key, locationCodes);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getAllWellsLocationCodeAndTier][error]" + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public List<LocationHierarchyDto> getAllWellsWithName() {
		List<LocationHierarchyDto> responseList = new ArrayList<LocationHierarchyDto>();
		try {
			String queryString = "select wm.muwi, p1.location_text from  production_location p1 , well_muwi wm where wm.location_code = p1.location_code";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = (List<Object[]>) q.list();
			// logger.error("[Murphy][HierarchyDao][getAllWells][response]" +
			// response);
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					LocationHierarchyDto dto = new LocationHierarchyDto();
					dto.setMuwi(ServicesUtil.isEmpty(obj[0]) ? "" : (String) obj[0]);
					dto.setLocationText(ServicesUtil.isEmpty(obj[1]) ? "" : (String) obj[1]);
					responseList.add(dto);
				}

			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getAllWells][error]" + e.getMessage());
		}
		return responseList;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getAllWellsWithNameMap() {
		Map<String, String> responseList = new HashMap<String, String>();
		try {
			String queryString = "select wm.muwi, p1.location_text from  production_location p1 , well_muwi wm where wm.location_code = p1.location_code";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = (List<Object[]>) q.list();
			// logger.error("[Murphy][HierarchyDao][getAllWells][response]" +
			// response);
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
					// LocationHierarchyDto dto = new LocationHierarchyDto();
					// dto.setMuwi(ServicesUtil.isEmpty(obj[0])? "":(String)
					// obj[0]);
					// dto.setLocationText(ServicesUtil.isEmpty(obj[1])?
					// "":(String) obj[1]);
					responseList.put((String) obj[0], (String) obj[1]);
				}

			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getAllWells][error]" + e.getMessage());
		}
		return responseList;
	}

	//
	@SuppressWarnings("unchecked")
	public String getLocationByMuwi(String muwi) {

		try {
			String queryString = "select p1.location_text from "
					+ "production_location p1 join well_muwi wm on wm.location_code=p1.location_code "
					+ "where wm.muwi = '" + muwi + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getLocationByMuwi][response]"
			// + response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationByMuwi][error]" + e.getMessage());
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public String getLocationCodeByMuwi(String muwi) {

		try {
			String queryString = "select p1.location_code from "
					+ "production_location p1 join well_muwi wm on wm.location_code=p1.location_code "
					+ "where p1.location_code = '" + muwi + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getLocationByMuwi][response]"
			// + response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationByMuwi][error]" + e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public String getMuwiByTaskId(String taskId, String origin) {

		try {
			String queryString = "SELECT wm.muwi AS a FROM tm_task_evnts AS te, well_muwi AS wm , tm_proc_evnts pe WHERE pe.process_id = te.process_id and wm.location_code = pe.loc_code AND te.TASK_ID = '"
					+ taskId + "'";
			// logger.error("[Murphy][HierarchyDao][getMuwiByTaskId][queryString]"
			// + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getMuwiByLocation][response]"+response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getMuwiByLocation][error]" + e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public String getLocationByLocCode(String locationCode) {

		try {
			String queryString = "select p1.location_text from " + "production_location p1 "
					+ "where p1.location_code = '" + locationCode + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getLocationByMuwi][response]"
			// + response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationByMuwi][error]" + e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public String getLocationtypeByLocCode(String locationCode) {

		try {
			String queryString = "select p1.LOCATION_TYPE from " + "production_location p1 "
					+ "where p1.location_code = '" + locationCode + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getLocationByMuwi][response]"
			// + response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationByMuwi][error]" + e.getMessage());
		}
		return null;

	}

	@Override
	protected LocationHierarchyDo importDto(LocationHierarchyDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LocationHierarchyDto exportDto(LocationHierarchyDo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getMuwiByLocationTypeAndCode(String locationType, List<String> locationCodeList) {
		String locationCodeString = null;
		try {
			for (int i = 0; i < locationCodeList.size(); i++) {
				if (i == 0) {
					locationCodeString = "'" + locationCodeList.get(i) + "'";
				} else
					locationCodeString = locationCodeString + ", '" + locationCodeList.get(i) + "'";
			}
			StringBuilder stringBuilder = new StringBuilder("select wm.muwi as well from production_location p1 ");

			if (MurphyConstant.WELL.equalsIgnoreCase(locationType)) {
				stringBuilder.append(" join well_muwi wm on wm.location_code=p1.location_code where p1.location_code ");
			} else if (MurphyConstant.WELLPAD.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join well_muwi wm  on wm.location_code=p1.location_code where "
								+ " p2.location_code ");
			} else if (MurphyConstant.FACILITY.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join production_location p3 on p2.parent_code=p3.location_code "
								+ " join well_muwi wm on wm.location_code=p1.location_code where p3.location_code ");
			} else if (MurphyConstant.FIELD.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join production_location p3 on "
								+ " p2.parent_code=p3.location_code join production_location p4 on p3.parent_code=p4.location_code join well_muwi wm "
								+ " on wm.location_code=p1.location_code where p4.location_code ");
			}

			stringBuilder.append(" in (" + locationCodeString + ")");

			// logger.error("Query---" + stringBuilder.toString());
			Query q = this.getSession().createSQLQuery(stringBuilder.toString());
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][LocationHierarchyDao][getMuwiByLocation][response]"+response);
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][LocationHierarchyDao][getMuwiByLocation][error]" + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getLocCodeByLocationTypeAndCode(String locationType, List<String> locationCodeList) {
		String locationCodeString = null;
		try {
			for (int i = 0; i < locationCodeList.size(); i++) {
				if (i == 0) {
					locationCodeString = "'" + locationCodeList.get(i) + "'";
				} else
					locationCodeString = locationCodeString + ", '" + locationCodeList.get(i) + "'";
			}
			StringBuilder stringBuilder = new StringBuilder(
					"select p1.location_code as well from production_location p1 ");

			if (MurphyConstant.WELL.equalsIgnoreCase(locationType)) {
				stringBuilder.append(" join well_muwi wm on wm.location_code=p1.location_code where p1.location_code ");
			} else if (MurphyConstant.WELLPAD.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join well_muwi wm  on wm.location_code=p1.location_code where "
								+ " p2.location_code ");
			} else if (MurphyConstant.FACILITY.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join production_location p3 on p2.parent_code=p3.location_code "
								+ " join well_muwi wm on wm.location_code=p1.location_code where p3.location_code ");
			} else if (MurphyConstant.FIELD.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p2 on p1.parent_code=p2.location_code join production_location p3 on "
								+ " p2.parent_code=p3.location_code join production_location p4 on p3.parent_code=p4.location_code join well_muwi wm "
								+ " on wm.location_code=p1.location_code where p4.location_code ");
			}

			stringBuilder.append(" in (" + locationCodeString.trim() + ")");

			// logger.error("Query---" + stringBuilder.toString());
			Query q = this.getSession().createSQLQuery(stringBuilder.toString());
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][LocationHierarchyDao][getMuwiByLocation][response]"+response);
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][LocationHierarchyDao][getMuwiByLocation][error]" + e.getMessage());
		}
		return null;
	}

	// soumya
	public String getMeterUsinglocationType(List<LocationHierarchyDto> dtoList, String locationType) {
		String locations = "";

		List<String> locationList = new ArrayList<String>(), locationCodeList = new ArrayList<String>(),
				meterMerrickList = new ArrayList<String>();

		for (LocationHierarchyDto locationDto : dtoList) {
			if (MurphyConstant.FLARE.equalsIgnoreCase(locationDto.getLocationType())) {
				meterMerrickList.add(locationDto.getLocation());
				logger.error("[Murphy][HierarchyDao][getMeterUsinglocationType][meterMerrickList]" + meterMerrickList);
			} else if (MurphyConstant.COMPRESSOR.equalsIgnoreCase(locationDto.getLocationType())) {
				return null;
			} else {
				locationCodeList.add(locationDto.getLocation());
				logger.error("[Murphy][HierarchyDao][getMeterUsinglocationType][locationCodeList]" + locationCodeList);
			}
			logger.error("[Murphy][HierarchyDao][getMeterUsinglocationType][meterMerrickList]" + meterMerrickList);
			logger.error("[Murphy][HierarchyDao][getMeterUsinglocationType][locationCodeList]" + locationCodeList);
			logger.error("[Murphy][HierarchyDao][getMeterUsinglocationType][locationList]" + locationList);
		}
		if (!ServicesUtil.isEmpty(locationCodeList)) {
			locationList = getMeterByLocationTypeAndCode(locationType, locationCodeList);
		}
		if (!ServicesUtil.isEmpty(meterMerrickList)) {
			locationList.addAll(meterMerrickList);
		}
		if (!ServicesUtil.isEmpty(locationList)) {
			locations = ServicesUtil.getStringFromList(locationList);
		}
		logger.error("[Murphy][HierarchyDao][getMeterUsinglocationType][error]" + locations);

		return locations;
	}

	// soumya
	@SuppressWarnings("unchecked")
	public List<String> getchildByParentCode(List<String> locationCodeList) {

		logger.error("[Murphy][LocationHierarchyDao][getchildByParentCode][locationCodeList]" + locationCodeList);
		String locationCodeString = null;
		try {
			for (int i = 0; i < locationCodeList.size(); i++) {
				if (i == 0) {
					locationCodeString = "'" + locationCodeList.get(i) + "'";
				} else
					locationCodeString = locationCodeString + ", '" + locationCodeList.get(i) + "'";
			}
			logger.error(
					"[Murphy][LocationHierarchyDao][getchildByParentCode][locationCodeString]" + locationCodeString);
			StringBuilder locationQuery = new StringBuilder("");

			locationQuery.append(
					"select p1.location_code from production_location p1 join production_location p2 on p2.location_code = p1.parent_code WHERE p2.location_code ");
			locationQuery.append(" in (" + locationCodeString + ")");

			Query q = this.getSession().createSQLQuery(locationQuery.toString());
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][LocationHierarchyDao][getchildByParentCode][error]" + e.getMessage());
		}
		return null;
	}

	// soumya
	@SuppressWarnings("unchecked")
	public List<String> getMeterByLocationTypeAndCode(String locationType, List<String> locationCodeList) {
		logger.error(
				"[Murphy][LocationHierarchyDao][getMeterByLocationTypeAndCode][locationCodeList]" + locationCodeList);
		String locationCodeString = null;
		try {

			if (MurphyConstant.FIELD.equalsIgnoreCase(locationType)) {
				List<String> facilityList = getchildByParentCode(locationCodeList);
				List<String> wellPadList = getchildByParentCode(facilityList);
				List<String> wellList = getchildByParentCode(wellPadList);
				if (!ServicesUtil.isEmpty(facilityList)) {
					locationCodeList.addAll(facilityList);
				}
				if (!ServicesUtil.isEmpty(wellPadList)) {
					locationCodeList.addAll(wellPadList);
				}
				if (!ServicesUtil.isEmpty(wellList) && wellList != null) {
					locationCodeList.addAll(wellList);
				}
			} else if (MurphyConstant.FACILITY.equalsIgnoreCase(locationType)) {
				List<String> wellPadList = getchildByParentCode(locationCodeList);
				List<String> wellList = getchildByParentCode(wellPadList);
				if (!ServicesUtil.isEmpty(wellPadList)) {
					locationCodeList.addAll(wellPadList);
				}
				if (!ServicesUtil.isEmpty(wellList) && wellList != null) {
					locationCodeList.addAll(wellList);
				}
				logger.error("[Murphy][LocationHierarchyDao][getMeterByLocationTypeAndCode][FACILITY]" + wellPadList);
			} else if (MurphyConstant.WELLPAD.equalsIgnoreCase(locationType)) {

				List<String> wellList = getchildByParentCode(locationCodeList);
				logger.error("[Murphy][LocationHierarchyDao][getMeterByLocationTypeAndCode][WELLPAD]" + wellList);
				if (!ServicesUtil.isEmpty(wellList) && wellList != null) {
					locationCodeList.addAll(wellList);
				}
			}
			for (int i = 0; i < locationCodeList.size(); i++) {
				if (i == 0) {
					locationCodeString = "'" + locationCodeList.get(i) + "'";
				} else
					locationCodeString = locationCodeString + ", '" + locationCodeList.get(i) + "'";
			}

			StringBuilder stringBuilder = new StringBuilder(
					"select tm.merrick_id from tm_flare_meter tm where tm.parent_code");

			stringBuilder.append(" in (" + locationCodeString + ")");

			// logger.error("Query---" + stringBuilder.toString());
			Query q = this.getSession().createSQLQuery(stringBuilder.toString());
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][LocationHierarchyDao][getMuwiByLocation][response]"+response);
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][LocationHierarchyDao][getMeterByLocationTypeAndCode][error]" + e.getMessage());
		}
		return new ArrayList<String>();
	}

	@SuppressWarnings("unchecked")
	public List<String> getMerrickByLocationTypeAndCode(String locationType, List<String> locationCodeList) {
		String locationCodeString = null;
		try {
			for (int i = 0; i < locationCodeList.size(); i++) {
				if (i == 0) {
					locationCodeString = "'" + locationCodeList.get(i) + "'";
				} else
					locationCodeString = locationCodeString + ", '" + locationCodeList.get(i) + "'";
			}
			logger.error("locationType " + locationType);

			StringBuilder stringBuilder = new StringBuilder("select wm.merrick_id from production_equipment pe ");

			if (MurphyConstant.WELL.equalsIgnoreCase(locationType)) {
				stringBuilder.append(
						" join equipment_merrick wm on wm.equipment_code = pe.equipment_code where pe.parent_code ");
			} else if (MurphyConstant.WELLPAD.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p1 on pe.parent_code=p1.location_code join equipment_merrick wm on wm.equipment_code = pe.equipment_code where "
								+ " pe.equipment_code ");
			} else if (MurphyConstant.FACILITY.equalsIgnoreCase(locationType)) {
				stringBuilder
						.append(" join production_location p1 on pe.parent_code=p1.location_code join production_location p2 on p1.parent_code=p2.location_code "
								+ " join equipment_merrick wm on wm.equipment_code = pe.equipment_code where pe.parent_code ");
			} else if (MurphyConstant.FIELD.equalsIgnoreCase(locationType)) {
				stringBuilder.append(" join production_location as p1 on pe.parent_code = p1.location_code"
						+ "  join production_location as p2 on p1.parent_code = p2.location_code join equipment_merrick as wm on"
						+ "  wm.equipment_code = pe.equipment_code WHERE p2.location_code ");
			}

			/*
			 * StringBuilder stringBuilder = new
			 * StringBuilder("select wm.merrick_id from production_equipment p1 "
			 * );
			 * 
			 * if (MurphyConstant.COMPRESSOR.equalsIgnoreCase(locationType)) {
			 * stringBuilder.
			 * append(" join equipment_merrick wm on wm.equipment_code = p1.equipment_code where p1.equipment_code "
			 * ); }
			 */
			stringBuilder.append(" in (" + locationCodeString + ")");
			logger.error("Query" + stringBuilder.toString());

			Query q = this.getSession().createSQLQuery(stringBuilder.toString());
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][LocationHierarchyDao][getMerrickByLocationTypeAndCode][error]" + e.getMessage());
		}
		return null;
	}

	public String getCompressorLocationUsinglocationType(List<LocationHierarchyDto> dtoList, String locationType) {
		String locations = "";

		List<String> locationList = new ArrayList<String>(), locationCodeList = new ArrayList<String>();

		for (LocationHierarchyDto locationDto : dtoList) {
			locationCodeList.add(locationDto.getLocation());
		}
		locationList = getMerrickByLocationTypeAndCode(locationType, locationCodeList);
		if (!ServicesUtil.isEmpty(locationList)) {
			locations = ServicesUtil.getStringFromList(locationList);
		}
		logger.error("[Murphy][HierarchyDao][getCompressorLocationUsinglocationType][]" + locations);

		return locations;
	}

	public String getLocationUsinglocationType(List<LocationHierarchyDto> dtoList, String locationType) {
		String locations = "";

		List<String> locationList = new ArrayList<String>(), locationCodeList = new ArrayList<String>();

		for (LocationHierarchyDto locationDto : dtoList) {
			locationCodeList.add(locationDto.getLocation());
		}
		locationList = getMuwiByLocationTypeAndCode(locationType, locationCodeList);
		if (!ServicesUtil.isEmpty(locationList)) {
			locations = ServicesUtil.getStringFromList(locationList);
		}
		logger.error("[Murphy][HierarchyDao][getLocationUsinglocationType][]" + locations);

		return locations;
	}

	@SuppressWarnings("unchecked")
	public Coordinates getCoordByCode(String locationCode) {
		Coordinates coordinates = null;
		Object[] resultRow = null;
		String qry = "SELECT LATITUDE, LONGITUDE FROM LOCATION_COORDINATE WHERE LOCATION_CODE = '" + locationCode + "'";
		List<Object[]> result = this.getSession().createSQLQuery(qry).list();
		if (!ServicesUtil.isEmpty(result)) {
			resultRow = result.get(0);
			coordinates = new Coordinates(ServicesUtil.getBigDecimal(resultRow[0]),
					ServicesUtil.getBigDecimal(resultRow[1]));
		}
		return coordinates;
	}

	// @SuppressWarnings("unchecked")
	public String getTierByCode(String locationCode) {
		String result = "";
		try {
			String qry = "SELECT MAX(TIER) FROM WELL_TIER WHERE LOCATION_CODE = '" + locationCode + "'";
			// logger.error("[Murphy][HierarchyDao][getTierByCode][qry]" +
			// qry+"[locationCode]"+locationCode);
			result = (String) this.getSession().createSQLQuery(qry).uniqueResult();
			// logger.error("[Murphy][HierarchyDao][getTierByCode][result]" +
			// result);
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getTierByCode][error]" + e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public String getMuwiByLocationCode(String locationCode) {

		try {
			String queryString = "select wm.muwi from "
					+ "production_location p1 join well_muwi wm on wm.location_code=p1.location_code "
					+ "where p1.location_code = '" + locationCode + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getLocationByMuwi][response]"
			// + response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationByMuwi][error]" + e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public LocationHierarchyResponseDto getParentOfCompressor(String compressorCode) {
		LocationHierarchyResponseDto hierarchyResponseDto = new LocationHierarchyResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Parent Fetch Success");
		responseMessage.setStatus(MurphyConstant.SUCCESS);
		responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		LocationHierarchyDto locationHierarchyDto = null;
		List<Object[]> result = null;
		Object[] resultRow = null;
		String responseLocationType = null;
		try {
			String getParentQuery = "SELECT PL.LOCATION_CODE, PL.LOCATION_TEXT, PL.LOCATION_TYPE, PL.PARENT_CODE, PE.EQUIPMENT_CODE, PE.EQUIPMENT_TEXT "
					+ "FROM PRODUCTION_EQUIPMENT PE JOIN PRODUCTION_LOCATION PL ON PE.PARENT_CODE = PL.LOCATION_CODE "
					+ "WHERE PE.EQUIPMENT_CODE = '" + compressorCode + "'";
			result = this.getSession().createSQLQuery(getParentQuery).list();
			if (!ServicesUtil.isEmpty(result)) {
				locationHierarchyDto = new LocationHierarchyDto();
				resultRow = result.get(0);
				locationHierarchyDto.setLocation(ServicesUtil.isEmpty(resultRow[0]) ? null : (String) resultRow[0]);
				responseLocationType = ServicesUtil.isEmpty(resultRow[2]) ? null : (String) resultRow[2];
				if (MurphyConstant.FACILITY_DB.equals(responseLocationType)) {
					responseLocationType = MurphyConstant.FACILITY;
				}
				locationHierarchyDto.setLocationText(ServicesUtil.isEmpty(resultRow[1]) ? null : (String) resultRow[1]);
				locationHierarchyDto.setLocationType(responseLocationType);
			} else {
				responseMessage.setMessage("No Parent for given compressor");
			}
		} catch (Exception ex) {
			logger.error("[Murphy][HierarchyDao][getParentOfCompressor][error] : " + ex.getMessage());
			// logger.debug("[Murphy][HierarchyDao][getParentOfCompressor][error]
			// : "+ex);
			responseMessage.setMessage("Parent Fetch Failed");
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		hierarchyResponseDto.setMessage(responseMessage);
		hierarchyResponseDto.setResponseDto(locationHierarchyDto);
		return hierarchyResponseDto;
	}

	@SuppressWarnings("unchecked")
	public String getLocationTypeByLocCode(String locationCode) {

		try {
			String queryString = "select p1.location_type from " + "production_location p1 "
					+ "where p1.location_code = '" + locationCode + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getLocationTypeByLocCode][response]"
			// + response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationTypeByLocCode][error]" + e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public String getLocationCodeByLocText(String locationText) {

		try {
			String queryString = "select p1.location_code from " + "production_location p1 "
					+ "where p1.location_text = '" + locationText + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getLocationTypeByLocCode][response]"
			// + response);
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationTypeByLocCode][error]" + e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getLocationByLocType(String locationType) {
		HashMap<String, String> response = null;
		try {
			String queryString = "select p1.location_code,p1.location_text from " + "production_location p1 "
					+ "where p1.location_type = '" + locationType + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> result = (List<Object[]>) q.list();
			if (result != null) {
				response = new HashMap<>();
				for (Object[] obj : result) {
					response.put(ServicesUtil.isEmpty(obj[1]) ? null : ((String) obj[1]).trim(),
							ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationByLocType][error]" + e.getMessage());
		}
		return response;

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> getTierMap(List<String> locationCodes) {
		HashMap<String, List<String>> response = null;
		StringJoiner joiner = new StringJoiner("','");
		for (String location : locationCodes)
			joiner.add(location.trim());
		String commaSeparatedLocation = joiner.toString();
		try {
			String queryString = "SELECT * FROM WELL_TIER WHERE LOCATION_CODE IN('" + commaSeparatedLocation + "')";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> result = (List<Object[]>) q.list();
			if (result != null) {
				response = new HashMap<>();
				for (Object[] obj : result) {
					if (response.containsKey((String) obj[1])) {
						response.get((String) obj[1]).add(((String) obj[0]).trim());
					} else {
						response.put(ServicesUtil.isEmpty(obj[1]) ? null : ((String) obj[1]).trim(), new ArrayList<>());
						response.get((String) obj[1]).add(((String) obj[0]).trim());
					}
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getTier][error]" + e.getMessage());
		}
		return response;

	}

	@SuppressWarnings("unchecked")
	public List<Coordinates> getCoordsByCodes(List<String> locationCodes) {
		List<Coordinates> coordinates = new ArrayList<>();
		StringJoiner joiner = new StringJoiner("','");
		for (String location : locationCodes)
			joiner.add(location);
		String commaSeparatedLocation = joiner.toString();
		String qry = "SELECT LATITUDE, LONGITUDE FROM LOCATION_COORDINATE WHERE LOCATION_CODE in( '"
				+ commaSeparatedLocation + "')";
		List<Object[]> result = this.getSession().createSQLQuery(qry).list();
		if (!ServicesUtil.isEmpty(result)) {
			for (Object[] obj : result) {
				coordinates
						.add(new Coordinates(ServicesUtil.getBigDecimal(obj[0]), ServicesUtil.getBigDecimal(obj[1])));
			}
		}
		return coordinates;
	}

	@SuppressWarnings("unchecked")
	public FieldResponseDto getFieldText(List<String> locationCodes, String locType) {

		FieldResponseDto responseDto = new FieldResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Field fetch failed");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		String queryString = "";
		String field = "";
		String loc = locationCodes.get(0);
		for (int i = 1; i < locationCodes.size(); i++) {
			loc += "','" + locationCodes.get(i);
		}
		if (MurphyConstant.FIELD.equals(locType)) {
			queryString = "select distinct p1.location_text as field from  production_location p1 " + "where ";

		} else if (MurphyConstant.FACILITY.equals(locType) || MurphyConstant.FACILITY_DB.equals(locType)) {
			queryString = "select distinct p2.location_text as field from  production_location p1,"
					+ " production_location p2 " + "where p1.parent_code=p2.location_code and ";

		} else if (MurphyConstant.WELLPAD.equals(locType)) {
			queryString = "select distinct p3.location_text as field from  production_location p1,"
					+ " production_location p2," + "production_location p3 where p1.parent_code=p2.location_code"
					+ " and p2.parent_code=p3.location_code and ";
		} else {
			queryString = "select distinct p4.location_text as field from  production_location p1,"
					+ " production_location p2, production_location p3, production_location p4 "
					+ "where p1.parent_code=p2.location_code and p2.parent_code=p3.location_code and"
					+ " p3.parent_code=p4.location_code and ";
		}
		queryString = queryString + " p1.location_code in ('" + loc + "')";

		Query q = this.getSession().createSQLQuery(queryString);
		List<String> response = q.list();
		if (!ServicesUtil.isEmpty(response)) {
			field = response.get(0);
			for (int i = 1; i < response.size(); i++) {
				field += "," + response.get(i);
			}
			responseDto.setField(field);
			responseDto.setLocationText(getLocationByLocCode(locationCodes.get(0)));
			responseMessage.setMessage("Field fetched successfully");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		responseDto.setResponseMessage(responseMessage);

		return responseDto;
	}

	@SuppressWarnings("unchecked")
	public List<LocationHierarchyDto> getCoordWithLocationCode(List<String> locationCodes) {
		List<LocationHierarchyDto> response = new ArrayList<LocationHierarchyDto>();
		StringJoiner joiner = new StringJoiner("','");
		for (String location : locationCodes)
			joiner.add(location);
		String commaSeparatedLocation = joiner.toString();
		String qry = "SELECT LATITUDE, LONGITUDE,LOCATION_CODE FROM LOCATION_COORDINATE WHERE LOCATION_CODE in( '"
				+ commaSeparatedLocation + "')";
		List<Object[]> result = this.getSession().createSQLQuery(qry).list();
		if (!ServicesUtil.isEmpty(result)) {
			for (Object[] obj : result) {
				LocationHierarchyDto dto = new LocationHierarchyDto();
				dto.setLatValue((BigDecimal) obj[0]);
				dto.setLongValue((BigDecimal) obj[1]);
				dto.setLocation((String) obj[2]);
				response.add(dto);
			}
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllFields() {
		List<String> fields = new ArrayList<String>();
		String query = "SELECT LOCATION_TEXT FROM PRODUCTION_LOCATION WHERE LOCATION_TYPE='" + MurphyConstant.FIELD
				+ "'";
		List<String> response = this.getSession().createSQLQuery(query).list();
		if (!ServicesUtil.isEmpty(response)) {
			for (String field : response) {
				fields.add(field);
			}
		}
		return fields;
	}

	@SuppressWarnings("unchecked")
	public List<LocationHierarchyDto> getWellListForFieldCode(List<String> fieldCode) {
		List<LocationHierarchyDto> locationList = null;
		LocationHierarchyDto dto = null;
		try {
			String queryString = "select p1.location_code,p1.location_text,jl.latitude,jl.longitude,wm.muwi from "
					+ "production_location p1 left outer join "
					+ "location_coordinate jl on jl.location_code =p1.location_code left join well_muwi wm"
					+ " on wm.location_code=p1.location_code where " + " upper(p1.location_type)=upper('"
					+ MurphyConstant.WELL + "')";

			String locLikeQuery = " p1.location_code LIKE '" + fieldCode.get(0) + "%'";
			for (int i = 1; i < fieldCode.size(); i++) {
				locLikeQuery += " or p1.location_code LIKE '" + fieldCode.get(i) + "%'";
			}
			String orderQuery = "order by p1.location_text";
			queryString = queryString + "and (" + locLikeQuery + ") " + orderQuery;
			logger.error("[Murphy][HierarchyDao][getWellListForFieldCode][Query]" + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultListAll = q.list();
			if (!ServicesUtil.isEmpty(resultListAll)) {
				locationList = new ArrayList<LocationHierarchyDto>();
				for (Object[] obj : resultListAll) {
					dto = new LocationHierarchyDto();
					dto.setMuwi(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setLocationText(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					dto.setLocation(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setChildExist("FALSE");
					dto.setLongValue(ServicesUtil.isEmpty(obj[3]) ? null : (BigDecimal) obj[3]);
					dto.setLatValue(ServicesUtil.isEmpty(obj[2]) ? null : (BigDecimal) obj[2]);
					dto.setLocationType(MurphyConstant.SEARCH);
					locationList.add(dto);
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getWellListForFieldCode][error]" + e.getMessage());
		}
		logger.error("LocationList " + locationList);
		return locationList;
	}

	// SOC: For ProCount List of Muwi
	public List<String> getMuwiUsingByLocCodeProCount(List<LocationHierarchyDto> dtoList, String locationType) {
		List<String> locationList = new ArrayList<String>(), locationCodeList = new ArrayList<String>();
		try {
			for (LocationHierarchyDto locationDto : dtoList) {
				locationCodeList.add(locationDto.getLocation());
			}
			locationList = getMuwiByLocationTypeAndCode(locationType, locationCodeList);
			if (!ServicesUtil.isEmpty(locationList)) {
				logger.error("[Murphy][HierarchyDao][getMuwiUsingByLocCodeProCount][locationList][0] "
						+ locationList.get(0));
				return locationList;
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getMuwiUsingByLocCodeProCount][error]" + e.getMessage());
		}
		return locationList;
	}
	// EOC : For ProCount List of Muwi

	@SuppressWarnings("unchecked")
	public LocationHierarchyDto getFacilityDetailsforMuwi(String muwi) {
		LocationHierarchyDto hierarchyDto = null;
		if (!ServicesUtil.isEmpty(muwi)) {
			String queryForWellDetails = "select p3.location_code,p3.location_text as facility,r.latitude,r.longitude "
					+ "from well_muwi wm join production_location p1 on p1.location_code=wm.location_code "
					+ "inner join production_location p2 on p1.parent_code=p2.location_code inner join "
					+ "production_location p3  on p2.parent_code=p3.location_code inner join "
					+ "production_location p4 on p3.parent_code=p4.location_code left join "
					+ "location_coordinate r on r.location_code=p3.location_code where wm.muwi='" + muwi + "'";
			// logger.error(
			// "[Murphy][HierarchyDao][getWellDetailsForMuwi][queryForWellDetails]"
			// + queryForWellDetails);

			Query query = this.getSession().createSQLQuery(queryForWellDetails);
			List<Object[]> result = query.list();
			if (!ServicesUtil.isEmpty(result)) {
				for (Object[] obj : result) {
					hierarchyDto = new LocationHierarchyDto();
					hierarchyDto.setLocation(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					hierarchyDto.setFacility(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					hierarchyDto.setLatValue(ServicesUtil.isEmpty(obj[2]) ? null : (BigDecimal) obj[2]);
					hierarchyDto.setLongValue(ServicesUtil.isEmpty(obj[3]) ? null : (BigDecimal) obj[3]);
				}
			}
		}
		return hierarchyDto;
	}

	public List<String> getHierarchyLocCodes(String locationType, List<String> locationCodes) {
		List<String> locationCodeList = new ArrayList<>();
		try {
			if (!ServicesUtil.isEmpty(locationCodes)) {
				locationCodeList.addAll(locationCodes);
			}
			if (MurphyConstant.FIELD.equalsIgnoreCase(locationType)) {
				List<String> facilityList = getchildByParentCode(locationCodeList);
				List<String> wellPadList = getchildByParentCode(facilityList);
				List<String> wellList = getchildByParentCode(wellPadList);
				if (!ServicesUtil.isEmpty(facilityList)) {
					locationCodeList.addAll(facilityList);
				}
				if (!ServicesUtil.isEmpty(wellPadList)) {
					locationCodeList.addAll(wellPadList);
				}
				if (!ServicesUtil.isEmpty(wellList) && wellList != null) {
					locationCodeList.addAll(wellList);
				}
			} else if (MurphyConstant.FACILITY.equalsIgnoreCase(locationType)) {
				List<String> wellPadList = getchildByParentCode(locationCodeList);
				List<String> wellList = getchildByParentCode(wellPadList);
				if (!ServicesUtil.isEmpty(wellPadList)) {
					locationCodeList.addAll(wellPadList);
				}
				if (!ServicesUtil.isEmpty(wellList) && wellList != null) {
					locationCodeList.addAll(wellList);
				}
				logger.error("[Murphy][HierarchyDao][getHierarchyLocCodes][FACILITY]" + wellPadList);
			} else if (MurphyConstant.WELLPAD.equalsIgnoreCase(locationType)) {

				List<String> wellList = getchildByParentCode(locationCodeList);
				logger.error("[Murphy][HierarchyDao][getHierarchyLocCodes][WELLPAD]" + wellList);
				if (!ServicesUtil.isEmpty(wellList) && wellList != null) {
					locationCodeList.addAll(wellList);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getHierarchyLocCodes][error]" + e.getMessage());
		}
		return locationCodeList;
	}

	@SuppressWarnings("unchecked")
	public String getLocationCodeByCoord(Double latitude, Double longitude) {
		try {
			String queryString = "select l1.location_code from " + "LOCATION_COORDINATE l1 " + "where l1.latitude = '"
					+ latitude + "' and l1.longitude ='" + longitude + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationCodeByCoord][error]" + e.getMessage());
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getFacilityByField(String fieldName) {
		List<Map<String,String>> locationMap=null;
		Map<String,String> locationList = null;
		try {
			
			if(!ServicesUtil.isEmpty(fieldName)){
			String queryString = "SELECT TRIM(LOCATION_TEXT),TRIM(LOCATION_CODE) FROM PRODUCTION_LOCATION WHERE PARENT_CODE IN("
					+ ServicesUtil.getStringFromList(fieldName.split(",")) + ")";
			
			
			logger.error("[Murphy][HierarchyDao][getFacilityByField][QueryString]" + queryString);
			Query q = this.getSession().createSQLQuery(queryString);

			List<Object[]> response = q.list();
			if (!ServicesUtil.isEmpty(response)) {
				locationMap=new ArrayList<>();
				for (Object[] obj : response) {
					locationList = new HashMap<>();
                    locationList.put("locationText",ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
                    locationList.put("locationCode",ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
                    locationMap.add(locationList);
				}
			}
			}

		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getFacilityByField][error]" + e.getMessage());

		}

		return locationMap;

	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getWellPadLocationByFacility(String facility) throws Exception {
		Map<String,String> locationList = new HashMap<>();
		try {


			
			String queryString = "SELECT TRIM(LOCATION_TEXT),TRIM(LOCATION_CODE) FROM PRODUCTION_LOCATION WHERE PARENT_CODE ='"+facility+"'";

			logger.error("[Murphy][HierarchyDao][getWellPadLocationByFacility][QueryString]" + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> response = q.list();
			if (!ServicesUtil.isEmpty(response)) {
				for (Object[] obj : response) {
                    locationList.put(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0],ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getFacilityByField][error]" + e.getMessage());
			throw e;

		}
		return locationList;
	}

	public String getFacilityByName(String wellPadName) throws Exception {
		String facilityName = "";
		try {

			String queryString = "SELECT TRIM(LOCATION_TEXT) FROM PRODUCTION_LOCATION WHERE "
					+ "LOCATION_CODE=(SELECT PARENT_CODE FROM PRODUCTION_LOCATION WHERE LOCATION_TEXT='" + wellPadName
					+ "' AND LOCATION_TYPE='Well Pad')";

			logger.error("[Murphy][HierarchyDao][getFacilityByName][QueryString]" + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			facilityName = (String) q.uniqueResult();
			if (!ServicesUtil.isEmpty(facilityName)) {
				return facilityName;
			}

		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getFacilityByField][error]" + e.getMessage());
			throw e;

		}
		return facilityName;
	}
	
	public List<String> getAllWells(boolean isEFS, boolean isCA) {
		String queryString ="";
		try {
			if(isEFS)
			{
			queryString = "select distinct(wm.muwi) from  production_location p1 , well_muwi wm "
					+ " where wm.location_code = p1.location_code and p1.location_code like 'MUR-US-EFS%'";
			}
			if(isCA)
			{
			queryString = "select distinct(wm.muwi) from  production_location p1 , well_muwi wm "
					+ " where wm.location_code = p1.location_code and p1.location_code like 'MUR-CA%'";
			}
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			// logger.error("[Murphy][HierarchyDao][getAllWells][response]" +
			// response);
			if (!ServicesUtil.isEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getAllWellsfor dopdgp][error]" + e.getMessage());
		}
		return null;
	}
}