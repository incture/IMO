package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.geotab.HierarchyDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.FracAlertMessageDto;
import com.murphy.taskmgmt.dto.FracNotificationDto;
import com.murphy.taskmgmt.dto.NotificationDto;
import com.murphy.taskmgmt.entity.FracNotificationDo;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.websocket.FracMessage;
import com.murphy.taskmgmt.websocket.FracServiceEndPoint;

@Repository("FracNotificationDao")
@Transactional
public class FracNotificationDao extends BaseDao<FracNotificationDo, FracNotificationDto> {

	private static final Logger logger = LoggerFactory.getLogger(FracNotificationDao.class);

	@Autowired
	OffsetFracPackDao fracPackDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected FracNotificationDo importDto(FracNotificationDto fromDto) {
		FracNotificationDo entity = new FracNotificationDo();
		entity.setSerialId(fromDto.getSerialId());
		entity.setFracId(fromDto.getFracId());
		entity.setMuwi(fromDto.getMuwi());
		entity.setAcknowledgedAt(fromDto.getAcknowledgedAt());
		entity.setUserGroup(fromDto.getUserGroup());
		entity.setUserId(fromDto.getUserId());
		entity.setIsAcknowledged(fromDto.getIsAcknowledged());
		entity.setMaxTubePressure(fromDto.getMaxTubePressure());
		entity.setMaxCasePressure(fromDto.getMaxCasePressure());
		entity.setActiveTubePressure(fromDto.getActiveTubePressure());
		entity.setActiveCasePressure(fromDto.getActiveCasePressure());
		entity.setWellStatus(fromDto.getWellStatus());

		return entity;
	}

	@Override
	protected FracNotificationDto exportDto(FracNotificationDo entity) {
		FracNotificationDto dto = new FracNotificationDto();
		dto.setSerialId(entity.getSerialId());
		dto.setFracId(entity.getFracId());
		dto.setMuwi(entity.getMuwi());
		dto.setAcknowledgedAt(entity.getAcknowledgedAt());
		dto.setUserGroup(entity.getUserGroup());
		dto.setUserId(entity.getUserId());
		dto.setIsAcknowledged(entity.getIsAcknowledged());
		dto.setMaxTubePressure(entity.getMaxTubePressure());
		dto.setMaxCasePressure(entity.getMaxCasePressure());
		dto.setActiveTubePressure(entity.getActiveTubePressure());
		dto.setActiveCasePressure(entity.getActiveCasePressure());
		dto.setWellStatus(entity.getWellStatus());
		return dto;
	}

	

	@SuppressWarnings({ "unused", "unchecked" })
	public FracNotificationDto getFracHitDetails(String userEmailId) {
		List<FracAlertMessageDto> fracAlertList = new ArrayList();
		List<FracNotificationDto> fracDetailDto = new ArrayList();
		FracAlertMessageDto alertMsgDto = new FracAlertMessageDto();
		FracNotificationDto notifyDto = new FracNotificationDto();
		Session session = null;
		Transaction tx = null;
		int existingFracCount = 0;
		String fetchQuery = null;
		double activeTubePressure = 0;
		double activeCasePressure = 0;
		NotificationDto dto = null;

		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			updateCurrentStatusOfWell();

			fetchQuery = "SELECT FRAC_ID,WELL_CODE,MAX_TUBE_PRESSURE,MAX_CASE_PRESSURE,ACT_TUBE_PRESSURE,ACT_CASE_PRESSURE FROM OFFSET_FRAC_PACK "
					+ "WHERE STATUS='IN PROGRESS' EXCEPT "
					+ "SELECT FRAC_ID,MUWI,MAX_TUBE_PRESSURE,MAX_CASE_PRESSURE,ACT_TUBE_PRESSURE,ACT_CASE_PRESSURE FROM FRAC_NOTIFICATION WHERE IS_ACKNOWLEDGED='true' "
					+ "AND USER_ID='" + userEmailId + "' AND WELL_STATUS IN('" + MurphyConstant.FRAC_CRITICAL + "','"
					+ MurphyConstant.FRAC_NORMAL + "') ORDER BY FRAC_ID DESC";

			Query q = session.createSQLQuery(fetchQuery);
		//	logger.error(" FracNotificationQuery----" + fetchQuery);
			List<Object[]> fracListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(fracListResponse)) {
			//	logger.error(" fracListResponse----" + fracListResponse);
				for (Object[] obj : fracListResponse) {
                  //  logger.error("Object"+obj);
                    // dev                 
//                    String fracId = (String) obj[0];
//				    long fracnum = Long.parseLong(fracId);
                    //MCQ/MCP
					int fracId = (Integer) obj[0];
					String wellCode = (String) obj[1];
					Double maxTubePressure = (Double) obj[2];
					Double maxCasePressure = (Double) obj[3];
					activeTubePressure=ServicesUtil.isEmpty(obj[4]) ? 0 : (Double) obj[4];
					activeCasePressure=ServicesUtil.isEmpty(obj[5]) ? 0 : (Double) obj[5];
//					activeTubePressure = fracPackDao.getActiveTubingPressure(wellCode);
//					activeCasePressure = fracPackDao.getActiveCasePressure(wellCode);
					if (!ServicesUtil.isEmpty(maxCasePressure) && (!ServicesUtil.isEmpty(activeCasePressure))) {

						// check CasePressure
						double minC = (double) (maxCasePressure * (50.0f / 100.0f));
						double maxC = (double) (maxCasePressure * (80.0f / 100.0f));

						// Check Tube Pressure
						double minT = (double) (maxTubePressure * (50.0f / 100.0f));
						double maxT = (double) (maxTubePressure * (80.0f / 100.0f));

						// Notifying if Pressure Exceeds 80
						if (activeCasePressure >= maxC || activeTubePressure >= maxT) {
							// Creating dataset To Notify in Web
//							wellName = getWellNameByMuwi(wellCode);
							 dto=new NotificationDto();
							 dto=getWellNameByMuwi(wellCode,"");
							FracAlertMessageDto alertDto = new FracAlertMessageDto();
							alertDto.setFracId(fracId + "");
							alertDto.setMuwi(wellCode);
							alertDto.setWellName(dto.getLocationText());
							alertDto.setActiveCasePressure(activeCasePressure);
							alertDto.setMaxCasePressure(maxCasePressure);
							alertDto.setActiveTubePressure(activeTubePressure);
							alertDto.setMaxTubePressure(maxTubePressure);
							alertDto.setFracAlertMessage("FRAC_HIT");
							alertDto.setLocationCode(dto.getLocationCode());
							alertDto.setLocationType(dto.getLocationType());

							fracAlertList.add(alertDto);

							// insert Data To DB
							FracNotificationDto persistDto = new FracNotificationDto();
							persistDto.setFracId(fracId);
//							persistDto.setFracId(fracnum);
							persistDto.setMuwi(wellCode);
							persistDto.setMaxTubePressure(maxTubePressure);
							persistDto.setMaxCasePressure(maxCasePressure);
							persistDto.setActiveTubePressure(activeTubePressure);
							persistDto.setActiveCasePressure(activeCasePressure);
							persistDto.setUserId(userEmailId);
							persistDto.setIsAcknowledged("false");
							fracDetailDto.add(persistDto);
						}
					}

				}
			}
			insertFracHitWells(fracDetailDto);
			if(fracAlertList.size()>0){
				int fracAlertCount =fracAlertList.size();
				notifyDto.setFracNotificationList(fracAlertList);
				notifyDto.setFracCount(fracAlertCount);
			}
			session.flush();
			session.clear();
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			logger.error("[FracNotificationDao][getFracHitDetails][error]" + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][getFracHitDetails][error] Exception While Closing Session "
						+ e.getMessage());
			}
		}
		
		return notifyDto;
	}

	/*public String getWellNameByMuwi(String wellCode) {
		Session session = null;
		Transaction tx = null;
		String wellName = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
//			String query = "SELECT WELL_NAME FROM OFFSET_FRAC_PACK WHERE WELL_CODE='" + wellCode + "'";
			String query ="SELECT pl.LOCATION_TEXT,pl.LOCATION_CODE,pl.LOCATION_TYPE FROM WELL_MUWI wm JOIN "
					+ "PRODUCTION_LOCATION pl ON wm.LOCATION_CODE=pl.LOCATION_CODE "
					+ "WHERE MUWI='"+wellCode+"'";
			Query q = session.createSQLQuery(query);
			logger.error("queryWellName" + query);
//			List<String> response = (List<String>) q.list();
//			if (!ServicesUtil.isEmpty(response)) {
//				wellName = response.get(0);
//			}
			List<Object[]> listResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(listResponse)) {
				for (Object[] obj : listResponse) {
					
				}
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[FracNotification][getWellNameByMuwi][error]" + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][getWellNameByMuwi][error] Exception While Closing Session "
						+ e.getMessage());
			}
		}
		return wellName;
	}*/
	
	
	public NotificationDto getWellNameByMuwi(String wellCode,String locationCode) {
		NotificationDto dto=null;
		Session session = null;
		Transaction tx = null;
		String wellName = null;
		String query =null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			if(!ServicesUtil.isEmpty(wellCode)){
			 query ="SELECT pl.LOCATION_TEXT,pl.LOCATION_CODE,pl.LOCATION_TYPE FROM WELL_MUWI wm JOIN "
					+ "PRODUCTION_LOCATION pl ON wm.LOCATION_CODE=pl.LOCATION_CODE "
					+ "WHERE wm.MUWI='"+wellCode+"'";
			}else{
			 query ="SELECT pl.LOCATION_TEXT,pl.LOCATION_CODE,pl.LOCATION_TYPE FROM PRODUCTION_LOCATION pl "
			 		+"WHERE pl.LOCATION_CODE='"+locationCode+"'";
			}
			Query q = session.createSQLQuery(query);
		//	logger.error("queryWellName" + query);
			List<Object[]> listResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(listResponse)) {
				for (Object[] obj : listResponse) {
					dto=new NotificationDto();
					dto.setLocationText(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setLocationCode(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
                    dto.setLocationType(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				}
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[FracNotification][getWellNameByMuwi][error]" + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][getWellNameByMuwi][error] Exception While Closing Session "
						+ e.getMessage());
			}
		}
		return dto;
	}

	private void updateCurrentStatusOfWell() {
		Session session = null;
		Transaction tx = null;
		Double activeTubePressure = null;
		Double activeCasePressure = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			String getAllWell = "SELECT FRAC_ID,WELL_CODE,MAX_TUBE_PRESSURE,MAX_CASE_PRESSURE,ACT_TUBE_PRESSURE,ACT_CASE_PRESSURE FROM OFFSET_FRAC_PACK WHERE STATUS='IN PROGRESS'";
			Query q = session.createSQLQuery(getAllWell);
		//	logger.error(" Query----" + getAllWell);
			List<Object[]> fracListResponse = (List<Object[]>) q.list();
			if (!ServicesUtil.isEmpty(fracListResponse)) {
				for (Object[] obj : fracListResponse) {
					//dev
//					String fracnum = (String) obj[0];
//					long fracIdlong = Long.parseLong(fracnum);
//					int fracId=(int)fracIdlong;
					//MCQ,MCP
					int fracId=(Integer)obj[0];
					String wellCode = (String) obj[1];
					Double maxTubePressure = (Double) obj[2];
					Double maxCasePressure = (Double) obj[3];
					activeTubePressure = ServicesUtil.isEmpty(obj[4]) ? 0 : (Double) obj[4];
					activeCasePressure = ServicesUtil.isEmpty(obj[5]) ? 0 : (Double) obj[5];
//					activeTubePressure = fracPackDao.getActiveTubingPressure(wellCode);
//					activeCasePressure = fracPackDao.getActiveCasePressure(wellCode);
					// check CasePressure
					// double minC = (double) (maxCasePressure * (80.0f /
					// 100.0f));
					double maxC = (double) (maxCasePressure * (80.0f / 100.0f));

					// Check Tube Pressure
					// double minT = (double) (maxTubePressure * (50.0f /
					// 100.0f));
					double maxT = (double) (maxTubePressure * (80.0f / 100.0f));

					if (activeCasePressure >= maxC || activeTubePressure >= maxT) {
						updateFracStatus(fracId, wellCode, MurphyConstant.FRAC_CRITICAL, "false");
					} else {
						updateFracStatus(fracId, wellCode, MurphyConstant.FRAC_NORMAL, "true");
					}

				}
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[FracNotificationDao][updateCurrentStatusOfWell][error]" + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][getFracHitDetails][error] Exception While Closing Session "
						+ e.getMessage());
			}
		}

	}

	private void updateFracStatus(int fracnum, String wellCode, String status, String fracFlag) {
		Transaction tx = null;
		Session session = null;
		Date date = null;
		String notifiedAt = null;
		String updateQuery = null;
		Integer updatedCount = null;

		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			if (!ServicesUtil.isEmpty(wellCode) && fracnum > 0) {

				date = new Date();
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				notifiedAt = formatDate.format(date);

				if (status.equalsIgnoreCase(MurphyConstant.FRAC_NORMAL)) {
					updateQuery = "UPDATE FRAC_NOTIFICATION SET IS_ACKNOWLEDGED='" + fracFlag + "',WELL_STATUS='"
							+ MurphyConstant.FRAC_NORMAL + "', ACKNOWLEDGED_AT='" + notifiedAt + "'" + " WHERE FRAC_ID="
							+ fracnum + " AND MUWI='" + wellCode + "' AND WELL_STATUS='" + MurphyConstant.FRAC_CRITICAL
							+ "'";
				} else {
					updateQuery = "UPDATE FRAC_NOTIFICATION SET IS_ACKNOWLEDGED='" + fracFlag + "',WELL_STATUS='"
							+ MurphyConstant.FRAC_CRITICAL + "', ACKNOWLEDGED_AT='" + notifiedAt + "'"
							+ " WHERE FRAC_ID=" + fracnum + " AND MUWI='" + wellCode + "' AND WELL_STATUS='"
							+ MurphyConstant.FRAC_NORMAL + "'";
				}

				Query q = session.createSQLQuery(updateQuery);
			//	logger.error("updateFracWellStatus" + updateQuery);
				updatedCount = q.executeUpdate();
				if (updatedCount > 0) {
					logger.error("Well Status Updated Successfully");
				} 
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[FracNotificationDao][updateFracStatus][error]" + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][getFracHitDetails][error] Exception While Closing Session "
						+ e.getMessage());
			}
		}

	}

	@SuppressWarnings("unchecked")
	private String checkForDuplicates(FracNotificationDto dto) {
		BigInteger count = null;
		String fracCount = null;
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			String query = "Select Count(*) FROM FRAC_NOTIFICATION WHERE FRAC_ID=" + dto.getFracId() + " AND  "
					+ "MUWI='" + dto.getMuwi() + "' AND USER_ID='" + dto.getUserId() + "'";
			Query q = session.createSQLQuery(query);
			// logger.error("CheckDuplicateQuery" + query);
			List<BigInteger> response = (List<BigInteger>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				count = response.get(0);
			}
			fracCount = "" + count;
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[FracNotification][checkForDuplicates][error]" + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][checkForDuplicates][error] Exception While Closing Session "
						+ e.getMessage());
			}
		}
		return fracCount;

	}

	public ResponseMessage insertFracHitWells(List<FracNotificationDto> fracListDto) {
		ResponseMessage message = new ResponseMessage();
		FracNotificationDto notifyDto;
		message.setMessage("Successfully Inserted FracHit Wells");
		message.setStatus(MurphyConstant.SUCCESS);
		message.setStatusCode(MurphyConstant.CODE_SUCCESS);
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			for (FracNotificationDto fracDto : fracListDto) {
				String duplicateCheck = checkForDuplicates(fracDto);
				if (!duplicateCheck.equalsIgnoreCase("1")) {
					notifyDto = new FracNotificationDto();
					String fracTemplateId = UUID.randomUUID().toString().replaceAll("-", "");
					notifyDto.setSerialId(fracTemplateId);
					notifyDto.setFracId(fracDto.getFracId());
					notifyDto.setMuwi(fracDto.getMuwi());
					notifyDto.setMaxCasePressure(fracDto.getMaxCasePressure());
					notifyDto.setMaxTubePressure(fracDto.getMaxTubePressure());
					notifyDto.setActiveCasePressure(fracDto.getActiveCasePressure());
					notifyDto.setActiveTubePressure(fracDto.getActiveTubePressure());
					notifyDto.setUserId(fracDto.getUserId());
					notifyDto.setIsAcknowledged("false");
					notifyDto.setWellStatus(MurphyConstant.FRAC_CRITICAL);
					session.persist(importDto(notifyDto));

				}
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			logger.error("[FracNotification][insertFracHitWells][error] " + ex.getMessage());
			message.setMessage("Failed To Update Notified Wells");
			message.setStatus(MurphyConstant.FAILURE);
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][insertFracHitWells][error] Exception While Closing Session "
						+ e.getMessage());
			}
		}
		return message;
	}

	@SuppressWarnings("unused")
	public ResponseMessage updateFracNotification(List<FracNotificationDto> notifyDtoList) {
		ResponseMessage message = new ResponseMessage();
		String updateQuery = null;
		Integer updatedCount = null;
		Date date = null;
		String notifiedAt = null;
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			if (!ServicesUtil.isEmpty(notifyDtoList)) {
				for (FracNotificationDto updateDto : notifyDtoList) {
					date = new Date();
					SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					notifiedAt = formatDate.format(date);
					updateQuery = "UPDATE FRAC_NOTIFICATION SET IS_ACKNOWLEDGED='" + updateDto.getIsAcknowledged()
							+ "',USER_GROUP='" + updateDto.getUserGroup() + "', ACKNOWLEDGED_AT='" + notifiedAt + "'"
							+ " WHERE FRAC_ID=" + updateDto.getFracId() + " AND MUWI='" + updateDto.getMuwi()
							+ "' AND USER_ID='" + updateDto.getUserId() + "'";

					Query q = session.createSQLQuery(updateQuery);
					// logger.error("UpdateQuery" + updateQuery);
					updatedCount = q.executeUpdate();
					if (updatedCount > 0) {
						message.setMessage("SuccessFully Updated Notified Wells");
						message.setStatus(MurphyConstant.SUCCESS);
						message.setStatusCode(MurphyConstant.CODE_SUCCESS);
					} else {
						message.setMessage("Failed While Updating Notified Wells");
						message.setStatus(MurphyConstant.FAILURE);
						message.setStatusCode(MurphyConstant.CODE_FAILURE);
					}

				}
			} else {
				logger.error("List is Empty,No Notified Wells To Update");
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[FracNotification][updateFracNotification][error]" + e.getMessage());
			message.setMessage("Failed While updating Notified Wells");
			message.setStatus(MurphyConstant.FAILURE);
			message.setStatusCode(MurphyConstant.CODE_FAILURE);
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[FracNotification][updateFracNotification][error]Exception While Closing Session "
						+ e.getMessage());
			}
		}
		return message;
	}

}
