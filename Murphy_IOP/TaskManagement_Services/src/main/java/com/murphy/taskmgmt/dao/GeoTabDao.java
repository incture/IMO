package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.geotab.Coordinates;
import com.murphy.geotab.HierarchyDto;
import com.murphy.geotab.NearestUserDto;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.AutoSignInDto;
import com.murphy.taskmgmt.dto.BaseDto;
import com.murphy.taskmgmt.dto.DeviceStatusInfoDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.BaseDo;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.ArcGISUtil;
import com.murphy.taskmgmt.util.GeoTabUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("geoTabDao")
public class GeoTabDao extends BaseDao<BaseDo, BaseDto> {

	private static final Logger logger = LoggerFactory.getLogger(GeoTabDao.class);

	@Override
	protected BaseDo importDto(BaseDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseDto exportDto(BaseDo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	private AutoSignInDao autoSignInDao;

	@Autowired
	private HierarchyDao hierarchyDao;

	@Autowired
	private UserIDPMappingDao userIdpMappingDao;

	@SuppressWarnings("unchecked")
	public List<HierarchyDto> getHierarchy(String query) {
		// ResultSet resultSet = null;
		HierarchyDto hierarchyDto = null;
		List<HierarchyDto> listHierarchy = null;
		if (!ServicesUtil.isEmpty(query)) {
			listHierarchy = new ArrayList<HierarchyDto>();
			// resultSet = DBConnect.getDbCon().query(query);
			Query q = this.getSession().createSQLQuery(query.trim());
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					hierarchyDto = new HierarchyDto();
					// hierarchyDto.setBusinessEntity(ServicesUtil.isEmpty(obj[0])?null:(String)
					// obj[0]);
					// hierarchyDto.setBusinessUnit(ServicesUtil.isEmpty(obj[1])?null:(String)
					// obj[1]);
					// hierarchyDto.setField(ServicesUtil.isEmpty(obj[2])?null:(String)
					// obj[2]);
					// hierarchyDto.setFacility(ServicesUtil.isEmpty(obj[3])?null:(String)
					// obj[3]);
					// hierarchyDto.setWellpad(ServicesUtil.isEmpty(obj[4])?null:(String)
					// obj[4]);
					// hierarchyDto.setWell(ServicesUtil.isEmpty(obj[5])?null:(String)
					// obj[5]);
					// hierarchyDto.setMuwi(ServicesUtil.isEmpty(obj[6])?null:(String)
					// obj[6]);
					hierarchyDto.setLatitude(
							ServicesUtil.isEmpty(obj[0]) ? null : ServicesUtil.getBigDecimal(obj[0]).doubleValue());
					hierarchyDto.setLongitude(
							ServicesUtil.isEmpty(obj[1]) ? null : ServicesUtil.getBigDecimal(obj[1]).doubleValue());

					listHierarchy.add(hierarchyDto);
				}
			}
		}
		return listHierarchy;
	}

	@SuppressWarnings("unchecked")
	public List<NearestUserDto> getUsersFromDB() {
		List<NearestUserDto> usersDto = null;
		NearestUserDto user = null;
		Query query = this.getSession()
				.createSQLQuery("SELECT ID, LOGIN_NAME, FIRST_NAME, LAST_NAME FROM GEOTAB_MANUAL_USERS");
		List<Object[]> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			usersDto = new ArrayList<NearestUserDto>();
			for (Object[] object : resultList) {
				user = new NearestUserDto();
				user.setFirstName((String) object[2]);
				user.setLastName((String) object[3]);
				usersDto.add(user);
				// logger.error("[geotab][userFromDB][user] : "+user);
			}
		}
		// logger.error("[geotab][userFromDB][usersDto] : "+usersDto);
		return usersDto;
	}

	public List<String> getUsersSignedIn() {
		return autoSignInDao.getUsersSignedIn();
	}

	public void updateParkEndTime(DeviceStatusInfoDto deviceDto) {
		autoSignInDao.updateParkEndTime(deviceDto);
	}

	public ResponseMessage saveOrUpdateAutoSignIn(DeviceStatusInfoDto deviceStatusInfoDto) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		AutoSignInDto autoSignInDto = new AutoSignInDto();
		autoSignInDto.setDriverId(deviceStatusInfoDto.getDeviceId());
		autoSignInDto.setDriverLat(deviceStatusInfoDto.getLatitude());
		autoSignInDto.setDriverLon(deviceStatusInfoDto.getLongitude());
		autoSignInDto.setMuwi(deviceStatusInfoDto.getMuwi());
		autoSignInDto.setWellName(deviceStatusInfoDto.getWellName());
		autoSignInDto.setRoadDistance(deviceStatusInfoDto.getRoadDistance());
		autoSignInDto.setCrowFlyDistance(deviceStatusInfoDto.getCrowFlyDistance());
		autoSignInDto.setWellLat(deviceStatusInfoDto.getWellLat());
		autoSignInDto.setWellLon(deviceStatusInfoDto.getWellLon());
		// Date currentDate = new Date();
		try {
			autoSignInDto.setSignInStart(dateFormat.parse(deviceStatusInfoDto.getParkedDateTime()));
		} catch (ParseException e) {
			System.err.println("Exception while setting Signed In Time : " + e);
		}
		return autoSignInDao.saveOrUpdateIntoSignInTable(autoSignInDto);
	}

	@SuppressWarnings("unchecked")
	public List<LocationHierarchyDto> getAllWellDetails() {
		List<LocationHierarchyDto> locations = null;
		String allWellQueryString = "SELECT PL.LOCATION_CODE, LC.LATITUDE, LC.LONGITUDE, WM.MUWI, PL.LOCATION_TEXT FROM PRODUCTION_LOCATION PL JOIN WELL_MUWI WM ON PL.LOCATION_CODE = WM.LOCATION_CODE JOIN LOCATION_COORDINATE LC ON PL.LOCATION_CODE = LC.LOCATION_CODE WHERE PL.LOCATION_TYPE = 'Well'";
		Query query = this.getSession().createSQLQuery(allWellQueryString);
		List<Object[]> result = query.list();

		if (!ServicesUtil.isEmpty(result) && result.size() > 0) {
			locations = new ArrayList<LocationHierarchyDto>();
			for (Object[] obj : result) {
				LocationHierarchyDto hierarchyDto = new LocationHierarchyDto();
				hierarchyDto.setMuwi((String) obj[3]);
				hierarchyDto.setLatValue(ServicesUtil.isEmpty(obj[1]) ? null : ServicesUtil.getBigDecimal(obj[1]));
				hierarchyDto.setLongValue((BigDecimal) (obj[2]));
				hierarchyDto.setLocation((String) obj[0]);
				hierarchyDto.setWell((String) obj[4]);
//				hierarchyDto.setBusinessEntity((String) obj[3]);
//				hierarchyDto.setBusinessUnit((String) obj[4]);
//				hierarchyDto.setField((String) obj[5]);
//				hierarchyDto.setFacility((String) obj[6]);
//				hierarchyDto.setWellpad((String) obj[7]);

				locations.add(hierarchyDto);
			}
		}
		return locations;
	}

	public ArcGISResponseDto getRoadDistance(String locationCodeOne, String locationCodeTwo, String userId) {
		logger.error("ArcGis  getRoadDistance called started at "+ new Date());
		ArcGISResponseDto arcGISResponseDto = new ArcGISResponseDto(0.00, 0.00, 0.00);
		Coordinates coordOne = null;
		Coordinates coordTwo = null;
		Coordinates user = null;
		UserIDPMappingDo idpMappingDo = null;
		if (!ServicesUtil.isEmpty(locationCodeOne) && (!ServicesUtil.isEmpty(locationCodeTwo))) {
			coordOne = hierarchyDao.getCoordByCode(locationCodeOne);
			coordTwo = hierarchyDao.getCoordByCode(locationCodeTwo);
			if (!ServicesUtil.isEmpty(coordOne) && !ServicesUtil.isEmpty(coordTwo)) {
				
				try {
					// Invoking ArcGIS only if location is in US - Prakash Kumar
					if (locationCodeOne.startsWith("MUR-US")) {
						arcGISResponseDto = ArcGISUtil.getRoadDistance(coordOne, coordTwo);
					} else if (locationCodeOne.startsWith("MUR-CA")) {
						arcGISResponseDto = ArcGISUtil.getCanadaRoadDistance(coordOne, coordTwo);
					}
					logger.error("ArcGis arcGISResponseDto " + arcGISResponseDto.toString());
					logger.error("ArcGis  getRoadDistance called ended at " + new Date());
				} catch (Exception ex) {
						logger.error("Exception while fetching ArcGIS Response : " + ex.getMessage());
						logger.debug("Exception while fetching ArcGIS Response : " + ex);
					}
				
			} else {
				logger.error("ArcGis Else ");
				arcGISResponseDto = new ArcGISResponseDto(-1.00, -1.00, -1.00);
			}
		} /*
			 * else if ((!ServicesUtil.isEmpty(userId)) &&
			 * (!ServicesUtil.isEmpty(locationCodeOne))){ coordOne =
			 * hierarchyDao.getCoordByCode(locationCodeOne); idpMappingDo =
			 * userIdpMappingDao.getUserByEmail(userId);
			 * if(!ServicesUtil.isEmpty(idpMappingDo)) { user =
			 * GeoTabUtil.getLocBySerialId(idpMappingDo.getSerialId()); try {
			 * arcGISResponseDto = ArcGISUtil.getRoadDistance(coordOne, user); }
			 * catch (Exception ex) {
			 * logger.error("Exception while fetching ArcGIS Response : "+ex.
			 * getMessage());
			 * logger.debug("Exception while fetching ArcGIS Response : "+ex); }
			 * } }
			 */
		else if ((!ServicesUtil.isEmpty(userId)) && (!ServicesUtil.isEmpty(locationCodeTwo))) {
			coordTwo = hierarchyDao.getCoordByCode(locationCodeTwo);
			idpMappingDo = userIdpMappingDao.getUserByEmail(userId);
			if (!ServicesUtil.isEmpty(idpMappingDo) && !ServicesUtil.isEmpty(coordTwo)) {
				user = GeoTabUtil.getLocBySerialId(idpMappingDo.getSerialId());
				try {
					if (locationCodeTwo.startsWith("MUR-US")) {
						arcGISResponseDto = ArcGISUtil.getRoadDistance(coordTwo, user);
					} else if (locationCodeTwo.startsWith("MUR-CA")) {
						arcGISResponseDto = ArcGISUtil.getCanadaRoadDistance(coordTwo, user);
					}
				} catch (Exception ex) {
					logger.error("Exception while fetching ArcGIS Response : " + ex.getMessage());
					logger.debug("Exception while fetching ArcGIS Response : " + ex);
				}
			} else {
				arcGISResponseDto = new ArcGISResponseDto(-1.00, -1.00, -1.00);
			}
		}
		return arcGISResponseDto;
	}

	@SuppressWarnings("unchecked")
	public Coordinates getLatLongByLocationCode(String locationCode) {
		Coordinates coordinates = null;
		if (!ServicesUtil.isEmpty(locationCode)) {
			Query query = this.getSession().createSQLQuery(
					"SELECT LATITUDE, LONGITUDE FROM LOCATION_COORDINATE WHERE LOCATION_CODE = '" + locationCode + "'");
			List<Object[]> result = query.list();
			if (!ServicesUtil.isEmpty(result) && result.size() > 0) {
				Object[] object = result.get(0);
				if (!ServicesUtil.isEmpty(object)) {
					coordinates = new Coordinates(
							ServicesUtil.isEmpty(object[0]) ? null : ServicesUtil.getBigDecimal(object[0]),
							ServicesUtil.isEmpty(object[1]) ? null : ServicesUtil.getBigDecimal(object[1]));
				}
			}
		}
		return coordinates;
	}

	@SuppressWarnings("unchecked")
	public LocationHierarchyResponseDto getWellUsingEmail(String emailId) {
		LocationHierarchyResponseDto responseDtpo = new LocationHierarchyResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Data fetch failed");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		LocationHierarchyDto dto = new LocationHierarchyDto();
		String queryString = "select DRIVER_LAT,DRIVER_LON,MUWI,WELL_NAME,WELL_LAT,WELL_LON"
				+ " from  tm_geotab_auto_signin gtb left outer join  TM_USER_IDP_MAPPING AS IDP"
				+ " on idp.serial_id=gtb.serial_id where idp.USER_EMAIL='" + emailId
				+ "' and gtb.SIGNIN_END_TIME is null " + " order by gtb.well_name limit 1";
		Query query = this.getSession().createSQLQuery(queryString);
		List<Object[]> result = query.list();
		if (!ServicesUtil.isEmpty(result) && result.size() > 0) {
			for (Object[] object : result) {
				dto.setMuwi(ServicesUtil.isEmpty((String) object[2]) ? null : (String) object[2]);
				dto.setWell(ServicesUtil.isEmpty((String) object[3]) ? null : (String) object[3]);
				dto.setLatValue(ServicesUtil.isEmpty(object[4]) ? null : ServicesUtil.getBigDecimal(object[4]));
				dto.setLongValue(ServicesUtil.isEmpty(object[5]) ? null : ServicesUtil.getBigDecimal(object[5]));
			}
			responseMessage.setMessage("Data Fetched Successful");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDtpo.setResponseDto(dto);
			responseDtpo.setMessage(responseMessage);
		}
		return responseDtpo;

	}

	@SuppressWarnings("unchecked")
	public String findApiDevice(String fieldLocCode) {

		try {
			String queryString = "SELECT DEVICE FROM GEO_LOCATION_CONFIG WHERE LOCATION_CODE ='" + fieldLocCode + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][HierarchyDao][getLocationTypeByLocCode][error]" + e.getMessage());
		}
		return null;
	}

}	
