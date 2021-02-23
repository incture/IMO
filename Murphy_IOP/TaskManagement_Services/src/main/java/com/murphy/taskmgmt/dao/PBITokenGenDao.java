package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.BaseDto;
import com.murphy.taskmgmt.dto.PBITokenResponse;
import com.murphy.taskmgmt.dto.PowerBITokenBodyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TokenDetailsDto;
import com.murphy.taskmgmt.entity.BaseDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.CacheImplementation;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("PBITokenGenDao")
public class PBITokenGenDao extends BaseDao<BaseDo, BaseDto> {

	private static final Logger logger = LoggerFactory.getLogger(PBITokenGenDao.class);

	@Autowired
	ConfigDao configDao;

	@Override
	protected BaseDo importDto(BaseDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		return null;
	}

	@Override
	protected BaseDto exportDto(BaseDo entity) {
		return null;
	}
	
	private PowerBITokenBodyDto tokenBody = null;
	
	@PostConstruct
	public void initializeTokenBody() {
		tokenBody = new PowerBITokenBodyDto();
		tokenBody.setClient_id(configDao.getConfigurationByRef(MurphyConstant.POWER_BI_CLIENTID_REF));
		tokenBody.setGrant_type(configDao.getConfigurationByRef(MurphyConstant.POWER_BI_GRANTTYPE_REF));
		tokenBody.setUsername(configDao.getConfigurationByRef(MurphyConstant.POWER_BI_USERNAME_REF));
		tokenBody.setPassword(configDao.getConfigurationByRef(MurphyConstant.POWER_BI_PASSWORD_REF));
		tokenBody.setResource(configDao.getConfigurationByRef(MurphyConstant.POWER_BI_RESOURCE_REF));
		tokenBody.setGroups(configDao.getConfigurationByRef(MurphyConstant.POWER_BI_GROUPS_REF));
	}

	public PBITokenResponse generateToken(String type) {
		
		PBITokenResponse tokenResponse = new PBITokenResponse();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Token Fetch Failed");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		if(!ServicesUtil.isEmpty(type)) {
			/* Cache Implementation Change on 19/07/2018 */
			CacheImplementation cacheImplementation = new CacheImplementation();
			TokenDetailsDto embeddedToken = getEmbeddedToken(cacheImplementation, type);
			if(!ServicesUtil.isEmpty(embeddedToken)) {
				tokenResponse.setAccessToken(embeddedToken.getToken());
				tokenResponse.setGroupId(tokenBody.getGroups());
				tokenResponse.setReportId(tokenBody.getReports());
				responseMessage.setMessage("Token Fetch Success");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				tokenResponse.setResponseMessage(responseMessage);
			}
		} else {
			responseMessage.setMessage("Type required");
		}
		return tokenResponse;
	}

	public TokenDetailsDto getApplicationToken(CacheImplementation cacheImplementation) {
		JSONObject applicationTokenObject = null;
		List<NameValuePair> params = null;
		TokenDetailsDto applicationToken = null;
		if(!ServicesUtil.isEmpty(cacheImplementation)) {
			try {
				applicationToken = cacheImplementation.retrieveFromCache(MurphyConstant.APPLICATION_TOKEN_REF_CONSTANT);
				if (applicationToken != null && checkTokenValidity(applicationToken.getExpirationDateTime())) {
					return applicationToken;
				} else {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("GRANT_TYPE", tokenBody.getGrant_type()));
					params.add(new BasicNameValuePair("CLIENT_ID", tokenBody.getClient_id()));
					params.add(new BasicNameValuePair("USERNAME", tokenBody.getUsername()));
					params.add(new BasicNameValuePair("PASSWORD", tokenBody.getPassword()));
					params.add(new BasicNameValuePair("RESOURCE", tokenBody.getResource()));
					applicationTokenObject = RestUtil.callRestParams(MurphyConstant.POWER_BI_BASE_URL, params,
							MurphyConstant.HTTP_METHOD_POST, null, null);
					if(!ServicesUtil.isEmpty(applicationTokenObject) && applicationTokenObject.toString().contains("access_token")) {
						applicationToken = new TokenDetailsDto();
						applicationToken.setToken(applicationTokenObject.getString("access_token"));
						applicationToken
								.setExpirationDateTime(TimeUnit.SECONDS.toMillis(applicationTokenObject.getInt("expires_on")));
						applicationToken.setTokenId(UUID.randomUUID().toString());
						cacheImplementation.putInCache(applicationToken, MurphyConstant.APPLICATION_TOKEN_REF_CONSTANT);
					}
				}
			} catch (Exception ex) {
				logger.error("Exception while fetching application Token : "+ex.getMessage());
				logger.debug("Exception while fetching application Token : "+ex);
			}
		}
		return applicationToken;
	}

	public TokenDetailsDto getEmbeddedToken(CacheImplementation cacheImplementation, String type) {
		JSONObject embeddedTokenObject = null;
		TokenDetailsDto applicationToken = null;
		TokenDetailsDto embeddedToken = null;
		if (!ServicesUtil.isEmpty(cacheImplementation) && !ServicesUtil.isEmpty(type)) {
			try {
				if (MurphyConstant.POWER_BI_ALARM.equals(type)) {
					tokenBody.setReports(configDao.getConfigurationByRef(MurphyConstant.POWERBI_ALARM_REPORTID));
				} else if (MurphyConstant.POWER_BI_INVESTIGATION_7.equals(type)) {
					tokenBody.setReports(
							configDao.getConfigurationByRef(MurphyConstant.POWERBI_INVESTIGATION_REPORTID_7));
				}else if (MurphyConstant.POWER_BI_INVESTIGATION_30.equals(type)) {
					tokenBody.setReports(
							configDao.getConfigurationByRef(MurphyConstant.POWERBI_INVESTIGATION_REPORTID_30));
				}
				else if(MurphyConstant.POWER_BI_VARIENCE.equals(type)){
					tokenBody.setReports(
							configDao.getConfigurationByRef(MurphyConstant.POWERBI_VARIANCE_REPORTID));
				} else if (MurphyConstant.POWER_BI_FRAC.equals(type)){
					tokenBody.setReports(
							configDao.getConfigurationByRef(MurphyConstant.POWERBI_FRAC_REPORTID));
				}
//				System.err.println("[Murphy][PBITokenGenDao][getEmbeddedToken][tokenBody]"+tokenBody +"\n[type]"+type);
				applicationToken = getApplicationToken(cacheImplementation);
				embeddedToken = cacheImplementation.retrieveFromCache(type+MurphyConstant.EMBEDDED_TOKEN_REF_CONSTANT);
				if(!ServicesUtil.isEmpty(embeddedToken) && checkTokenValidity(embeddedToken.getExpirationDateTime())) {
					return embeddedToken;
				} else {
					String tokenServiceURL = MurphyConstant.POWER_BI_TOKEN_BASE_URL + MurphyConstant.POWER_BI_URL_GROUPS
							+ tokenBody.getGroups() + MurphyConstant.POWER_BI_URL_REPORTS + tokenBody.getReports()
							+ MurphyConstant.POWER_BI_URL_GEN_TOKEN;
					if (!ServicesUtil.isEmpty(applicationToken))
						embeddedTokenObject = RestUtil.callRestBearer(tokenServiceURL, MurphyConstant.POWER_BI_TOKEN_GEN_BODY,
								MurphyConstant.HTTP_METHOD_POST, applicationToken.getToken());
					if(!ServicesUtil.isEmpty(embeddedTokenObject) && embeddedTokenObject.toString().contains("token")) {
						embeddedToken = new TokenDetailsDto();
						embeddedToken.setToken(embeddedTokenObject.getString("token"));
						embeddedToken.setTokenId(embeddedTokenObject.getString("tokenId"));
						embeddedToken.setExpirationDateTime(ServicesUtil.convertFromZoneToZone(null, embeddedTokenObject.getString("expiration"), MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss").getTime());
						cacheImplementation.putInCache(embeddedToken, type+MurphyConstant.EMBEDDED_TOKEN_REF_CONSTANT);
					}
				}
			} catch (Exception ex) {
				logger.error("Exception while fetching embedded Token "+type+" : "+ex.getMessage());
				logger.debug("Exception while fetching embedded Token "+type+" : "+ex);
			}
		}
		return embeddedToken;
	}
	
	public static void main(String[] args) {
//		System.out.println(new PBITokenGenDao().generateToken("ALARM"));
		System.out.println(new Date(1531939494000L));
		System.out.println(new Date(1531935764448L));
		System.out.println(System.currentTimeMillis());
	}


	private boolean checkTokenValidity(Long longDate){
		Date expirationDate;
//		logger.error("Long date "+longDate);
		try {

//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//			expirationDate = sdf.parse(stringDate);
			Date currentDate = new Date();
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			
			expirationDate = ServicesUtil.convertFromZoneToZone(new Date(longDate), null, MurphyConstant.UTC_ZONE, MurphyConstant.UTC_ZONE, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss");

//			logger.error("Current date "+currentDate);
//			logger.error("Expiration date "+expirationDate);

			if (currentDate.before(expirationDate)) {
				return true;
			} 
		}
		catch (Exception e) {
			logger.error("[PBITokenGenDao][checkTokenValidity][Exception] : Error while checking expiration time, refetching token from service "+e);
			return false;
		}
		return false;
	}


}
