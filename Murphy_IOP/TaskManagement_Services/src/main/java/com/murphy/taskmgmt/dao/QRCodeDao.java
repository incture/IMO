package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.QRCodeDetailsDto;
import com.murphy.taskmgmt.entity.QRCodeDo;
import com.murphy.taskmgmt.scheduler.CanaryStagingScheduler;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("QRCodeDao")
@Transactional
public class QRCodeDao {

	private static final Logger logger = LoggerFactory.getLogger(QRCodeDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void addQRCodeDetail(QRCodeDo qrCodeDo) {

		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(qrCodeDo);
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[QRCodeDao][addQRCodeDetail] Exception " + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error(
						"[QRCodeDao][addQRCodeDetail][Exception] Exception While Closing Session " + e.getMessage());
			}
		}
	}

	public void deleteQRCodeDetail(String qrCode) {

		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(getQRCodeDetailById(qrCode));
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[QRCodeDao][deleteQRCodeDetail] Exception " + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error(
						"[QRCodeDao][deleteQRCodeDetail][Exception] Exception While Closing Session " + e.getMessage());
			}
		}
	}

	public QRCodeDo getQRCodeDetailById(String id) {

		Transaction tx = null;
		Session session = null;
		QRCodeDo qrCodeDo = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			qrCodeDo = (QRCodeDo) session.get(QRCodeDo.class, id);
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[QRCodeDao][getQRCodeDetailById] Exception " + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[QRCodeDao][getQRCodeDetailById][Exception] Exception While Closing Session "
						+ e.getMessage());
			}
		}

		return qrCodeDo;
	}

	public List<QRCodeDo> getQRCodeDetailsList() {

		Transaction tx = null;
		Session session = null;
		List<QRCodeDo> QRCodelist = new ArrayList<QRCodeDo>();
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from com.murphy.taskmgmt.entity.QRCodeDo");
			QRCodelist = (List<QRCodeDo>) query.list();
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[QRCodeDao][getQRCodeDetailsList] Exception " + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[QRCodeDao][getQRCodeDetailsList][Exception] Exception While Closing Session "
						+ e.getMessage());
			}
		}

		return QRCodelist;
	}

	/*public JSONObject getCanaryDataForQR(String qRNumber) {
		org.json.JSONObject canaryResponseObject = null;
		try {
			String userToken = canaryStagingScheduler.getUserToken();
			List<String> wells = new ArrayList<String>();
			wells.add("9264212700000000KON0038H1");
			String startTime = "2018-11-23T08:00:00.0000000-05";
			String endTime = "2018-11-23T09:30:00.0000000-05";
			String canaryUrl = "api/v1/getTagData";
			String[] canaryParam = {"PRCASXIN","PRTUBXIN","PRSTAXIN","QTGASD","QTOILD","QTH2OD"};
			List<String> payloadList = new ArrayList<String>();
			for (String well : wells) {
				for (String param : canaryParam) {
					payloadList.add("MUWI_Prod." + well + "." + param);
				}
			}
			String payload = "";
			for (String st : payloadList) {
				payload = payload + "\"" + st + "\",";
			}
			payload = payload.substring(0, payload.length() - 1);
			payload = "[" + payload + "]";
			//String payload = canaryStagingScheduler.getPayloadInString(wells, canaryParam);
			String canaryPayload = "{" + "\"userToken\": \"" + userToken + "\"," + "\"startTime\": \"" + startTime
					+ ":00\"," + "\"endTime\": \"" + endTime + ":00\"," + "\"aggregateName\": \"" + "\"," + "\"includeQuality\": false,"
					+ " \"MaxSize\": 4000000," + "\"continuation\": null," + "\"tags\": " + payload + "" + "}";

			logger.error("CanaryStagingScheduler.getCanaryData()[canaryPayload]" + canaryPayload);

			String userName = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_USERID_REF);
			String password = configDao.getConfigurationByRef(MurphyConstant.CANARY_API_PASSWORD_REF);
			canaryResponseObject = RestUtil.callRest(MurphyConstant.CANARY_API_HOST + canaryUrl,
					canaryPayload, MurphyConstant.HTTP_METHOD_POST, userName, password);
			System.err.println(canaryResponseObject);
		} catch (Exception e) {
			logger.error("[QRCodeDao][getCanaryDataForQR] Exception " + e.getMessage());
		}
		return canaryResponseObject;

	}*/
	
	public QRCodeDetailsDto getQRCodedeatils(String qrCode){
		QRCodeDetailsDto qRCodeDetailsDto = new QRCodeDetailsDto();
		try{
			String query = "select QR_CODE , LOCATION_CODE , LOCATION_TEXT , EQUIPMENT_CODE , EQUIPMENT_TEXT , EQUIPMENT_TYPE , CANARY_TAG from qrcode_equipment_detail where QR_CODE = '"+qrCode+"'";
			Session session = sessionFactory.openSession();
			List<Object[]> objects = session.createSQLQuery(query).list();
			for(Object[] obj:objects){
				qRCodeDetailsDto.setQrCode((String) obj[0]);
				qRCodeDetailsDto.setLocationCode((String) obj[1]);
				qRCodeDetailsDto.setLocationText((String) obj[2]);
				qRCodeDetailsDto.setEquipmentId((String) obj[3]);
				qRCodeDetailsDto.setEquipmentText((String) obj[4]);
				qRCodeDetailsDto.setEquipmentType((String) obj[5]);
				qRCodeDetailsDto.setCanaryTag(((String) obj[6]).split(","));
				
			}
		} catch (Exception e) {
			logger.error("[QRCodeDao][getQRCodedeatils] Exception " + e.getMessage());
		}
		return qRCodeDetailsDto;
	}
	
}
