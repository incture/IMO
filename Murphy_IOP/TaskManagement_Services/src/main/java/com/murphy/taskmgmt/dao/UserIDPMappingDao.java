package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.geotab.NearestUserDto;
import com.murphy.taskmgmt.dto.GroupsUserDto;
import com.murphy.taskmgmt.dto.UserIDPMappingDto;
import com.murphy.taskmgmt.entity.UserIDPMappingDo;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("userIDPMappingDao")
public class UserIDPMappingDao extends BaseDao<UserIDPMappingDo, UserIDPMappingDto>{

	private static final Logger logger = LoggerFactory.getLogger(UserIDPMappingDao.class);
	
	@Autowired
	private HierarchyDao locDao;

	@Override
	protected UserIDPMappingDo importDto(UserIDPMappingDto fromDto) {
		UserIDPMappingDo mappingDo = null;
		if(!ServicesUtil.isEmpty(fromDto)) {
			mappingDo = new UserIDPMappingDo();
			if(!ServicesUtil.isEmpty(fromDto.getSerialId()))
			mappingDo.setSerialId(fromDto.getSerialId());
			if(!ServicesUtil.isEmpty(fromDto.getBlackLineId()))
			mappingDo.setBlackLineId(fromDto.getBlackLineId());
			mappingDo.setSapId(fromDto.getSapId());
			mappingDo.setUserEmail(fromDto.getUserEmail().toLowerCase());
			mappingDo.setUserFirstName(fromDto.getUserFirstName());
			mappingDo.setUserLastName(fromDto.getUserLastName());
			mappingDo.setUserLoginName(fromDto.getUserLoginName());
			mappingDo.setTaskAssignable(fromDto.getTaskAssignable());
			mappingDo.setUserRole(fromDto.getUserRole());
			mappingDo.setpId(fromDto.getpId());
		}
		return mappingDo;
	}

	@Override
	protected UserIDPMappingDto exportDto(UserIDPMappingDo entity) {
		UserIDPMappingDto mappingDto = null;
		if(!ServicesUtil.isEmpty(entity)) {
			mappingDto = new UserIDPMappingDto();
			if(!ServicesUtil.isEmpty(entity.getSerialId()))
			mappingDto.setSerialId(entity.getSerialId());
			if(!ServicesUtil.isEmpty(entity.getBlackLineId()))
			mappingDto.setBlackLineId(entity.getBlackLineId());
			mappingDto.setUserEmail(entity.getUserEmail());
			mappingDto.setSapId(entity.getSapId());
			mappingDto.setUserFirstName(entity.getUserFirstName());
			mappingDto.setUserLastName(entity.getUserLastName());
			mappingDto.setUserLoginName(entity.getUserLoginName());
			mappingDto.setTaskAssignable(entity.getTaskAssignable());
			mappingDto.setUserRole(entity.getUserRole());
			mappingDto.setpId(entity.getpId());
		}
		return mappingDto;
	}

	public boolean saveOrUpdateUser(UserIDPMappingDto userIDPMappingDto) {
		boolean flag = false;
		try {
			this.getSession().saveOrUpdate(this.importDto(userIDPMappingDto));
			this.getSession().flush();
			flag = true;
		} catch (Exception ex) {
			logger.error("[UserIDPMappingDao]Exception while creating or updating user : "+ex);
		}
		return flag;
	}

	public String delete(String emailId) {
		
		try {
			if (!ServicesUtil.isEmpty(emailId)) {
			
			String queryString = "DELETE FROM TM_USER_IDP_MAPPING WHERE USER_EMAIL IN ('" + emailId + "')";
			int deletedRow = this.getSession().createSQLQuery(queryString).executeUpdate();
			if (deletedRow > 0)
				return MurphyConstant.SUCCESS;
		}
		
			
		} catch (Exception ex) {
			logger.error("[UserIDPMappingDao]Exception while deleting task : "+ex);
		}
		return MurphyConstant.FAILURE;
	}
	
	@SuppressWarnings("unchecked")
	public UserIDPMappingDo getUserBySerialId(String serialId) {
		Criteria criteria = null;
		List<UserIDPMappingDo> userDetails = null;
		UserIDPMappingDo mappingDo = null;
		if(!ServicesUtil.isEmpty(serialId)) {
			try {
				criteria = this.getSession().createCriteria(UserIDPMappingDo.class);
				criteria.add(Restrictions.eq("serialId", serialId));
				userDetails = criteria.list();
			} catch (Exception ex) {
				logger.error("[UserIDPMappingDao]Exception while fetching user by serialId : "+ex);
			}
		}
		if(!ServicesUtil.isEmpty(userDetails))
			mappingDo = userDetails.get(0);
		return mappingDo;
	}

	public UserIDPMappingDo getUserByLoginName(String loginName) {
		UserIDPMappingDo mappingDo = null;
		try {
			mappingDo = new UserIDPMappingDo();
			mappingDo = (UserIDPMappingDo) this.getSession().get(UserIDPMappingDo.class, loginName);
		} catch (Exception ex) {
			logger.error("[UserIDPMappingDao]Exception while fetching user by loginName : "+ex);
		}
		return mappingDo;
	}

	@SuppressWarnings("unchecked")
	public List<UserIDPMappingDo> getAllUserDetails() {
		List<UserIDPMappingDo> mappingDos = null;
		try {
			mappingDos = this.getSession().createCriteria(UserIDPMappingDo.class).list();
		} catch (Exception ex) {
			logger.error("[UserIDPMappingDao]Exception while fetching users : "+ex);
		}
		return mappingDos;
	}

	@SuppressWarnings("unchecked")
	public UserIDPMappingDo getUserByEmail(String userEmail) {
		Criteria criteria = null;
		List<UserIDPMappingDo> userDetails = null;
		UserIDPMappingDo mappingDo = null;
		if(!ServicesUtil.isEmpty(userEmail)) {
			try {
				criteria = this.getSession().createCriteria(UserIDPMappingDo.class);
				criteria.add(Restrictions.eq("userEmail", userEmail));
				userDetails = criteria.list();
			} catch (Exception ex) {
				logger.error("[UserIDPMappingDao]Exception while fetching user by userEmail : "+ex);
			}
		}
		if(!ServicesUtil.isEmpty(userDetails))
			mappingDo = userDetails.get(0);
		return mappingDo;
	}



	@SuppressWarnings("unchecked")
	public List<UserIDPMappingDto> getUserAndTaskCountDetails(List<String> roles, Double longitude, Double latitude) {
		List<UserIDPMappingDto> userDtoList = null;
		UserIDPMappingDto user = null;
		String querString = null;
		try {
			if (!ServicesUtil.isEmpty(roles)) {
				querString = "SELECT SERIAL_ID,USER_EMAIL,TASK_ASSIGNABLE,USER_FIRST_NAME,USER_LAST_NAME,TASK_COUNT,P_ID FROM (SELECT IDP.SERIAL_ID AS SERIAL_ID , IDP.USER_EMAIL AS USER_EMAIL, IDP.TASK_ASSIGNABLE AS TASK_ASSIGNABLE, IDP.USER_FIRST_NAME AS USER_FIRST_NAME, "
						+ "IDP.USER_LAST_NAME AS USER_LAST_NAME,IDP.USER_ROLE AS USER_ROLE , COUNT(TE.TASK_ID) AS TASK_COUNT,IDP.P_ID AS P_ID FROM TM_USER_IDP_MAPPING AS IDP LEFT OUTER JOIN TM_TASK_OWNER AS TO ON IDP.USER_EMAIL = TO.TASK_OWNER "
						+ " LEFT OUTER JOIN TM_TASK_EVNTS AS TE ON TO.TASK_ID = TE.TASK_ID ";
//				if (!ServicesUtil.isEmpty(longitude) && !ServicesUtil.isEmpty(latitude))
				querString += " AND TE.STATUS IN ('" + MurphyConstant.ASSIGN + "','" + MurphyConstant.INPROGRESS
						+ "')" ;
//						+ " AND TE.ORIGIN IN ('" + MurphyConstant.DISPATCH_ORIGIN + "') ";
				
				querString += " GROUP BY IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME,IDP.P_ID,IDP.USER_ROLE ) WHERE ";

				int i = 0;
				for (String role : roles) {
					if (i != 0) {
						querString = querString + " OR ";
					} else {
						i++;
					}
					querString = querString + " USER_ROLE like '%" + role + "%'";
				}
			} else {
				querString = "SELECT IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME, "
						+ " COUNT(TE.TASK_ID) AS TASK_COUNT,IDP.P_ID FROM TM_USER_IDP_MAPPING AS IDP LEFT OUTER JOIN TM_TASK_OWNER AS TO ON IDP.USER_EMAIL = TO.TASK_OWNER "
						+ " LEFT OUTER JOIN TM_TASK_EVNTS AS TE ON TO.TASK_ID = TE.TASK_ID " + " AND TE.STATUS IN ('"
						+ MurphyConstant.ASSIGN + "','" + MurphyConstant.INPROGRESS + "') AND TE.ORIGIN IN ('"
						+ MurphyConstant.DISPATCH_ORIGIN + "') "
						+ " GROUP BY IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME,IDP.P_ID";
			}
			logger.error("[UserIDPMappingDao][getUserAndTaskCountDetails][querString] : " + querString);

			Query query = this.getSession().createSQLQuery(querString);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				userDtoList = new ArrayList<UserIDPMappingDto>();
				for (Object[] object : resultList) {
					user = new UserIDPMappingDto();
					user.setSerialId(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
					user.setUserEmail((String) object[1]);
					user.setTaskAssignable((String) object[2]);
					user.setUserFirstName((String) object[3]);
					user.setUserLastName((String) object[4]);
					user.setTaskCount(ServicesUtil.isEmpty(object[5]) ? null : ((BigInteger) object[5]).intValue());
					user.setpId((String) object[6]);
					/*
					 * if(!ServicesUtil.isEmpty(object[7]))
					 * user.setBlackLineId((String) object[7]);
					 */
					userDtoList.add(user);
				}
			}
			logger.error("[UserIDPMappingDao][getUserAndTaskCountDetails][userDtoList] : " + userDtoList);
		} catch (Exception e) {
			logger.error("[UserIDPMappingDao][getUserAndTaskCountDetails][Exception] : " + e.getMessage());
		}
		return userDtoList;
	}

	@SuppressWarnings("unchecked")
	public NearestUserDto getUserDetailsByUser(String mailId) {
		NearestUserDto user = null;
		try{
			Query query = this.getSession().createSQLQuery("SELECT IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME, "
					+ " IDP.P_ID FROM TM_USER_IDP_MAPPING AS IDP where IDP.USER_EMAIL = '"+mailId+"'");

			List<Object[]> resultList = query.list();
			if(!ServicesUtil.isEmpty(resultList)) {
				for(Object[] object : resultList) {
					user = new NearestUserDto();
					user.setSerialId((String) object[0]);
					user.setEmailId((String) object[1]);
					user.setTaskAssignable(Boolean.valueOf((String) object[2]));
					user.setFirstName((String) object[3]);
					user.setLastName((String) object[4]);
					user.setpId((String) object[5]);
					return user;
					//				logger.error("[UserIDPMappingDao][getUserDetailsFromDB][user] : "+ user);
				}
			}
		}catch(Exception e){
			logger.error("[UserIDPMappingDao][getUserDetailsByUser][userDtoList] : "+ user);
		}
		return null;
	}




	@SuppressWarnings("unchecked")
	public List<GroupsUserDto> getUsersBasedOnRole(List<String> roles) {
		List<GroupsUserDto> userDtoList = null;
		if(!ServicesUtil.isEmpty(roles)){
			try{
				String querString = "select serial_id ,user_first_name ,user_last_name ,user_email,user_role ,user_login_name , task_assignable,p_id from TM_USER_IDP_MAPPING where ";

				int i = 0;
				for(String role : roles){
					if(i != 0){
						querString = querString + 	" OR ";
					}else{
						i++;
					}
					querString = querString + " USER_ROLE like '%"+role+"%'";	
				}

				logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][querString] "+querString);	
				Query query = this.getSession().createSQLQuery(querString);
                
				List<Object[]> resultList = query.list();
				if(!ServicesUtil.isEmpty(resultList)) {
					GroupsUserDto user = null;
					userDtoList = new ArrayList<GroupsUserDto>();
					for(Object[] object : resultList) {
						user = new GroupsUserDto();
						//				user.setSerialId(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
						user.setFirstName(ServicesUtil.isEmpty(object[1]) ? "" :(String) object[1]);
						user.setLastName(ServicesUtil.isEmpty(object[2]) ? "" :(String) object[2]);
						user.setUserId(ServicesUtil.isEmpty(object[3]) ? null :(String) object[3]);
						//				user.setUserRole(ServicesUtil.isEmpty(object[4]) ? null :(String) object[4]);
						user.setUserName(user.getFirstName() + " " +user.getLastName()) ;
						//				user.setTaskAssignable(ServicesUtil.isEmpty(object[6])  ? null :(String) object[6]);
						user.setpId(ServicesUtil.isEmpty(object[7]) ? "" :(String) object[7]);

						userDtoList.add(user);
					}
				}
			}
			catch(Exception e){
				logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][error] "+e);
			}
		}
		return userDtoList;
	}


	@SuppressWarnings("unchecked")
	public List<String> getRolesForNDVTasks(String role,String userType){
		List<String> resultList =  null;
		try{
			role = ServicesUtil.getStringForInQuery(role);
			userType = ServicesUtil.getStringForInQuery(userType);
			String querString = "select TECHNICALROLE from TM_ROLE_MAPPING where FIELD in ("+userType+") and BUSINESSEROLE in ("+role+")";
			//			logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][querString] "+querString);
			Query query = this.getSession().createSQLQuery(querString);
			resultList = (List<String>) query.list();
			//			logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][resultList] "+resultList);

		}
		catch(Exception e){
			logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][error] "+e);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, ArrayList<String>> getOBXRolesWithFieldMap(String field,String businessRole){
		HashMap<String, ArrayList<String>> roleFieldMap=new HashMap<String, ArrayList<String>>();
		ArrayList<String> fieldList =  new ArrayList<String>();
		String role="";
		try{
			businessRole = ServicesUtil.getStringForInQuery(businessRole);
			field = ServicesUtil.getStringForInQuery(field.trim());
			String querString = "select TECHNICALROLE,FIELD from TM_ROLE_MAPPING where FIELD in ("+field+") and BUSINESSEROLE in ("+businessRole+")";
			Query query = this.getSession().createSQLQuery(querString);
			List<Object[]> response = (List<Object[]>) query.list();
			if(!ServicesUtil.isEmpty(response)){
				for(Object[] obj: response){
					role=(String)obj[0];
					if(roleFieldMap.containsKey(role)){
						roleFieldMap.get(role).add(((String)obj[1]).trim());
					}
					else{
						fieldList =  new ArrayList<String>();
						fieldList.add(((String)obj[1]).trim());
						roleFieldMap.put(role, fieldList);
					}
				}
			}

		}
		catch(Exception e){
			logger.error("[Murphy][UserIDPMappingDao][getOBXRolesWithFieldMap][error] "+e.getMessage());
		}
		return roleFieldMap;
	}
	@SuppressWarnings("unchecked")
	public List<String> getOBXRoles(String field,String businessRole){
		List<String> response =null;
		try{
			businessRole = ServicesUtil.getStringForInQuery(businessRole);
			field = ServicesUtil.getStringForInQuery(field);
			String querString = "select DISTINCT TECHNICALROLE from TM_ROLE_MAPPING where FIELD in ("+field+") and BUSINESSEROLE in ("+businessRole+")";
			Query query = this.getSession().createSQLQuery(querString);
			response = query.list();
					}
		catch(Exception e){
			logger.error("[Murphy][UserIDPMappingDao][getOBXRoles][error] "+e.getMessage());
		}
		return response;
	}
	public List<GroupsUserDto> getUsersBasedOnPOTRole(String role,String userType) {
		return getUsersBasedOnRole(getRolesForNDVTasks( role, userType));
	}
	
	// Start-CHG0037344-Inquiry to a field seat.
	public List<GroupsUserDto> getUsersBasedOnGroupRole(String role) {
		List<String> roles = new ArrayList<>();
		roles.add(role);

		return getUsersBasedOnRole(roles);
	}
	// End-CHG0037344-Inquiry to a field seat.
	
	//For Fetching field Name alloted to different technical roles
	@SuppressWarnings("unchecked")
	public HashMap<String,List<String>> getFieldNameFromTechRole(String techRoles, String businessRoles){
		techRoles = ServicesUtil.getStringForInQuery(techRoles);
		businessRoles = ServicesUtil.getStringForInQuery(businessRoles);
		String fieldName = "";
		HashMap<String,List<String>> response = null;
		try{
			String queryString = "select FIELD,BUSINESSEROLE, TECHNICALROLE from TM_ROLE_MAPPING where TECHNICALROLE in ("+techRoles+") and BUSINESSEROLE in ("+businessRoles+")" ;
			logger.error("[Murphy][UserIDPMappingDao][getFieldFromTechRole][queryString] "+queryString);
			Query query = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = (List<Object[]>) query.list();
			if(resultList !=null){
				response=new HashMap<>();
				for(Object[] obj:resultList){
					//String arr[] = ServicesUtil.isEmpty(obj[1])?null:((String)obj[1]).trim().split("_");
					String techRole = ServicesUtil.isEmpty(obj[1])?null:((String)obj[2]);
					if (response.containsKey(techRole.trim())){
						fieldName = locDao.getLocationByLocCode((String) obj[0]);
						if(!ServicesUtil.isEmpty(fieldName))
							response.get(techRole.trim()).add(fieldName.trim());
					}
					else{
						//String arr[] = ServicesUtil.isEmpty(obj[1])?null:((String)obj[1]).trim().split("_");
						response.put(techRole.trim(), new ArrayList<>());
						fieldName = locDao.getLocationByLocCode((String) obj[0]);
						if(!ServicesUtil.isEmpty(fieldName))
							response.get(techRole.trim()).add(fieldName.trim());
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("[Murphy][UserIDPMappingDao][getFieldFromTechRole][error]"+e.getMessage());
		}
		logger.error("Response for getFieldNameFromTechRole "+response);
		return response;
	}
	
	public List<GroupsUserDto> getUsersBasedOnRole(String role,String userType) {
		return getUsersBasedOnRole(getRolesOfType( role, userType));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getRolesOfType(String role,String userType){
		List<String> resultList =  null;
		try{
			role = ServicesUtil.getStringForInQuery(role);
			userType = ServicesUtil.getStringForInQuery(userType);
			String querString = "select TECHNICALROLE from TM_ROLE_MAPPING where FIELD Like ("+userType+") and BUSINESSEROLE in ("+role+")";
			//			logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][querString] "+querString);
			Query query = this.getSession().createSQLQuery(querString);
			resultList = (List<String>) query.list();
			//			logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][resultList] "+resultList);

		}
		catch(Exception e){
			logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][error] "+e);
		}
		return resultList;
	}
	
	
	//For Fetching field code alloted to different technical roles
	@SuppressWarnings("unchecked")
	public List<String> getFieldCode(String techRoles, String businessRoles){
		techRoles = ServicesUtil.getStringForInQuery(techRoles);
		businessRoles = ServicesUtil.getStringForInQuery(businessRoles);
		List<String> resultList =  null;
		try{
			String queryString = "select distinct FIELD from TM_ROLE_MAPPING where TECHNICALROLE in ("+techRoles+") and BUSINESSEROLE in ("+businessRoles+")" ;
			Query query = this.getSession().createSQLQuery(queryString);
			resultList = (List<String>) query.list();
			logger.error("getFieldCode "+resultList);
			if(!ServicesUtil.isEmpty(resultList))
				return resultList;
		}
		catch (Exception e) {
			logger.error("[Murphy][UserIDPMappingDao][getFieldCode][error]"+e.getMessage());
		}
		logger.error("resultList :"+resultList);
		return resultList;
	}
	@SuppressWarnings("unchecked")
	public List<GroupsUserDto> getUsersBasedOnRoleForOBX(List<String> roles) {
		List<GroupsUserDto> userDtoList = null;
		if(!ServicesUtil.isEmpty(roles)){
			try{
				//String querString = "select serial_id ,user_first_name ,user_last_name ,user_email,user_role ,user_login_name , task_assignable,p_id from TM_USER_IDP_MAPPING where ";
				String querString = "select m.serial_id ,m.user_first_name ,m.user_last_name ,m.user_email,m.user_role ,m.user_login_name , m.task_assignable,m.p_id, e.designation from TM_USER_IDP_MAPPING m join EMP_INFO e on m.user_email =e.EMP_EMAIL where ";
				int i = 0;
				for(String role : roles){
					if(i != 0){
						querString = querString + 	" OR ";
					}else{
						i++;
					}
					querString = querString + " USER_ROLE like '%"+role+"%'";	
				}

				logger.error("[Murphy][UserIDPMappingDao][getUsersBasedOnRoleForOBX][querString] "+querString);	
				Query query = this.getSession().createSQLQuery(querString);
                
				List<Object[]> resultList = query.list();
				if(!ServicesUtil.isEmpty(resultList)) {
					GroupsUserDto user = null;
					userDtoList = new ArrayList<GroupsUserDto>();
					for(Object[] object : resultList) {
						user = new GroupsUserDto();
						//				user.setSerialId(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
						user.setFirstName(ServicesUtil.isEmpty(object[1]) ? "" :(String) object[1]);
						user.setLastName(ServicesUtil.isEmpty(object[2]) ? "" :(String) object[2]);
						user.setUserId(ServicesUtil.isEmpty(object[3]) ? null :(String) object[3]);
						//				user.setUserRole(ServicesUtil.isEmpty(object[4]) ? null :(String) object[4]);
						user.setUserName(user.getFirstName() + " " +user.getLastName()) ;
						//				user.setTaskAssignable(ServicesUtil.isEmpty(object[6])  ? null :(String) object[6]);
						user.setpId(ServicesUtil.isEmpty(object[7]) ? "" :(String) object[7]);
					//	user.setObxDesignation(ServicesUtil.isEmpty(object[8]) ? "" :(String) object[8]);

						userDtoList.add(user);
					}
				}
			}
			catch(Exception e){
				logger.error("[Murphy][UserIDPMappingDao][getRolesForNDVTasks][error] "+e);
			}
		}
		return userDtoList;
	}

	@SuppressWarnings("unchecked")
	public List<UserIDPMappingDo> getIopTrainers() {
		List<UserIDPMappingDo>  UserIDPMappingDoList=null;
		UserIDPMappingDo UserIDPMappingDo =null;
		try{
			String querString= "select user_first_name ,user_last_name ,user_email,user_login_name ,p_id from TM_USER_IDP_MAPPING where user_role like '%IOP_TRAINER%'";
			Query query = this.getSession().createSQLQuery(querString);
            
			List<Object[]> resultList = query.list();
			if(!ServicesUtil.isEmpty(resultList)) {
				UserIDPMappingDoList = new ArrayList<UserIDPMappingDo>();
				for(Object[] obj:resultList){
					UserIDPMappingDo = new UserIDPMappingDo();
					UserIDPMappingDo.setUserFirstName(ServicesUtil.isEmpty(obj[0]) ? "" :(String) obj[0]);
					UserIDPMappingDo.setUserLastName(ServicesUtil.isEmpty(obj[1]) ? "" :(String) obj[1]);
					UserIDPMappingDo.setUserEmail(ServicesUtil.isEmpty(obj[2]) ? "" :(String) obj[2]);
					UserIDPMappingDo.setUserLoginName(ServicesUtil.isEmpty(obj[3]) ? "" :(String) obj[3]);
					UserIDPMappingDo.setpId(ServicesUtil.isEmpty(obj[4]) ? "" :(String) obj[4]);
					UserIDPMappingDoList.add(UserIDPMappingDo);
				}
			}
		}catch(Exception e){
			logger.error("[Murphy][UserIDPMappingDao][getIopTrainers][Exception] "+e.getMessage());
		}
		return UserIDPMappingDoList;
	}

}