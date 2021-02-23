package com.murphy.taskmgmt.dao;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.OpenTokDto;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("OpenTokDao")
public class OpenTokDao {

	private static final Logger logger = LoggerFactory.getLogger(OpenTokDao.class);

	@Autowired
	SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][OpenTokDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}
	}

	public String getConfigDescById(String configId) {
		String configValue = "";
		try {
			String query = "select CONFIG_DESC_VALUE from TM_CONFIG_VALUES where CONFIG_ID = '" + configId + "'";
			configValue = (String) this.getSession().createSQLQuery(query).uniqueResult();
		} catch (Exception ex) {
			logger.error("[OpenTokDao][getConfigDescById][Exception] : " + ex);
		}
		return configValue;

	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public String createCall(OpenTokDto openTokDto) {
		String response = "";
		try {
			Session session = this.getSession();
			String checkUserAvail = "select STATUS from AR_USER_AVAILABILITY where USER_ID = '"
					+ openTokDto.getSubscriberId() + "'";
			String userId = "";
			String userStatus = (String) session.createSQLQuery(checkUserAvail).uniqueResult();
			if (!ServicesUtil.isEmpty(userStatus) && userStatus.equalsIgnoreCase(MurphyConstant.INCALL)) {
				response = MurphyConstant.BUSY;
			} else {
				String queryForLog = "insert into AR_CALL_LOG values('" + openTokDto.getSessionId() + "' , '"
						+ openTokDto.getTokenId() + "' , '" + openTokDto.getPublisherName() + "' , '"
						+ openTokDto.getPublisherId() + "' , '" + openTokDto.getSubsciberName() + "' , '"
						+ openTokDto.getSubscriberId() + "' , '" + openTokDto.getApiKey() + "' , '"
						+ openTokDto.getApiSecret() + "' , '" + openTokDto.getNotificationType() + "')";
				session.createSQLQuery(queryForLog).executeUpdate();
				String checkUserAvailQuery = "select USER_ID from AR_USER_AVAILABILITY where USER_ID = '"
						+ openTokDto.getPublisherId() + "'";
				userId = (String) this.getSession().createSQLQuery(checkUserAvailQuery).uniqueResult();
				if (!ServicesUtil.isEmpty(userId)) {
					String updateUserAvailabilityQuery = "update AR_USER_AVAILABILITY set SESSION_ID = '"
							+ openTokDto.getSessionId() + "' , STATUS = '" + openTokDto.getPublisherStatus()
							+ "' where USER_ID = '" + openTokDto.getPublisherId() + "'";
					session.createSQLQuery(updateUserAvailabilityQuery).executeUpdate();
					 updateARCallResponse(openTokDto, MurphyConstant.SUBSCRIBER,  "");
				} else {
					// String availEntryQuery = "insert into
					// AR_USER_AVAILABILITY values('" +
					// openTokDto.getSessionId()
					// + "' , '" + openTokDto.getPublisherId() + "' , '" +
					// openTokDto.getPublisherStatus() + "')";
					// session.createSQLQuery(availEntryQuery).executeUpdate();
					createUserAvailabilityEntry(openTokDto, MurphyConstant.PUBLISHER);
				}
				response = MurphyConstant.CONNECTED;
			}

			session.flush();
		} catch (Exception ex) {
			logger.error("[OpenTokDao][createCall][Exception] : " + ex);
		}

		return response;

	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public String updateARCallResponse(OpenTokDto openTokDto, String responseBy, String actionType) {
		String response = "";
		try {
			if (responseBy.equalsIgnoreCase(MurphyConstant.SUBSCRIBER)) {
				if (!ServicesUtil.isEmpty(openTokDto.getSubscriberId())) {
					String userAvailability = checkUserAvailability(openTokDto);
					if (!ServicesUtil.isEmpty(userAvailability)
							&& userAvailability.equalsIgnoreCase(MurphyConstant.INCALL)) {
						String checkUserAvailQuery = "select USER_ID from AR_USER_AVAILABILITY where USER_ID = '"
								+ openTokDto.getSubscriberId() + "'";
						String userId = (String) this.getSession().createSQLQuery(checkUserAvailQuery).uniqueResult();
						if (!ServicesUtil.isEmpty(userId)) {
							String updateUserAvailabilityQuery = "update AR_USER_AVAILABILITY set SESSION_ID = '"
									+ openTokDto.getSessionId() + "' , STATUS = '" + openTokDto.getSubscriberStatus()
									+ "' where USER_ID = '" + openTokDto.getSubscriberId() + "'";
							this.getSession().createSQLQuery(updateUserAvailabilityQuery).executeUpdate();
						} else {
							createUserAvailabilityEntry(openTokDto, MurphyConstant.SUBSCRIBER);
						}

						if (openTokDto.getSubscriberStatus().equalsIgnoreCase(MurphyConstant.OUTCALL)) {
							response = "Call ended by subscriber";
						} else if (openTokDto.getSubscriberStatus().equalsIgnoreCase(MurphyConstant.INCALL)) {
							response = "Call started";
						}

					} else {
						if (!ServicesUtil.isEmpty(actionType)) {
							String checkUserAvailQuery = "select USER_ID from AR_USER_AVAILABILITY where USER_ID = '"
									+ openTokDto.getSubscriberId() + "'";
							String userId = (String) this.getSession().createSQLQuery(checkUserAvailQuery)
									.uniqueResult();
							if (!ServicesUtil.isEmpty(userId)) {
								String updateUserAvailabilityQuery = "update AR_USER_AVAILABILITY set SESSION_ID = '"
										+ openTokDto.getSessionId() + "' , STATUS = '"
										+ openTokDto.getSubscriberStatus() + "' where USER_ID = '"
										+ openTokDto.getSubscriberId() + "'";
								this.getSession().createSQLQuery(updateUserAvailabilityQuery).executeUpdate();
							} else {
								createUserAvailabilityEntry(openTokDto, MurphyConstant.SUBSCRIBER);
							}

							if (openTokDto.getSubscriberStatus().equalsIgnoreCase(MurphyConstant.OUTCALL)) {
								response = "Call ended by subscriber";
							} else if (openTokDto.getSubscriberStatus().equalsIgnoreCase(MurphyConstant.INCALL)) {
								response = "Call started";
							}
						}

						response = "Call has already been ended by publisher";
					}

				}
			} else if (responseBy.equalsIgnoreCase(MurphyConstant.PUBLISHER))
				if (!ServicesUtil.isEmpty(openTokDto.getPublisherId())) {
					String updateUserAvailabilityQuery = "update AR_USER_AVAILABILITY set SESSION_ID = '"
							+ openTokDto.getSessionId() + "' , STATUS = '" + openTokDto.getPublisherStatus()
							+ "' where USER_ID = '" + openTokDto.getPublisherId() + "'";
					this.getSession().createSQLQuery(updateUserAvailabilityQuery).executeUpdate();
					if (openTokDto.getPublisherStatus().equalsIgnoreCase(MurphyConstant.OUTCALL)) {
						response = "Call ended by publisher";
					} else if (openTokDto.getPublisherStatus().equalsIgnoreCase(MurphyConstant.INCALL)) {
						response = "Call started";
					}

				}

		} catch (Exception ex) {
			logger.error("[OpenTokDao][updateARCallResponse][Exception] : " + ex);
		}
		return response;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public String checkUserAvailability(OpenTokDto openTokDto) {
		String response = "";
		try {
			String checkUserAvailQuery = "select STATUS from AR_USER_AVAILABILITY where USER_ID = '"
					+ openTokDto.getPublisherId() + "' and SESSION_ID = '" + openTokDto.getSessionId() + "'";
			response = (String) this.getSession().createSQLQuery(checkUserAvailQuery).uniqueResult();
		} catch (Exception ex) {
			logger.error("[OpenTokDao][updateARCallResponse][Exception] : " + ex);
		}
		return response;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void createUserAvailabilityEntry(OpenTokDto openTokDto, String actionBy) {
		try {
			if (actionBy.equalsIgnoreCase(MurphyConstant.PUBLISHER)) {
				String availEntryQuery = "insert into AR_USER_AVAILABILITY values('" + openTokDto.getSessionId()
						+ "' , '" + openTokDto.getPublisherId() + "' , '" + openTokDto.getPublisherStatus() + "')";
				this.getSession().createSQLQuery(availEntryQuery).executeUpdate();
			} else if (actionBy.equalsIgnoreCase(MurphyConstant.SUBSCRIBER)) {
				String availEntryQuery = "insert into AR_USER_AVAILABILITY values('" + openTokDto.getSessionId()
						+ "' , '" + openTokDto.getSubscriberId() + "' , '" + openTokDto.getSubscriberStatus() + "')";
				this.getSession().createSQLQuery(availEntryQuery).executeUpdate();
			}
		} catch (Exception ex) {
			logger.error("[OpenTokDao][createUserAvailabilityEntry][Exception] : " + ex);
		}

	}

}
