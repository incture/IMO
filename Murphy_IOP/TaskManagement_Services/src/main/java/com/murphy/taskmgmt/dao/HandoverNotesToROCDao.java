package com.murphy.taskmgmt.dao;

import java.sql.Clob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.HandoverNotesDto;
import com.murphy.taskmgmt.dto.HandoverNotesRequestDto;
import com.murphy.taskmgmt.dto.HandoverNotesToROCResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.HandoverNotesDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;


@Repository("HandoverNotesDao")
@Transactional
public class HandoverNotesToROCDao extends BaseDao<HandoverNotesDo, HandoverNotesDto> {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(HandoverNotesToROCDao.class);

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][HandoverNotesToROCDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	@Override
	protected HandoverNotesDo importDto(HandoverNotesDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		HandoverNotesDo entity = new HandoverNotesDo();
		// entity.setNoteId(fromDto.getNoteId());
		Date date = null;
		try {
			date = ServicesUtil.getDate(fromDto.getDate());

		} catch (Exception e) {
			throw new InvalidInputFault("HandoverNotesToROCDao.importDto() " + e.getMessage());
		}
		entity.setDate(date);
		entity.setField(fromDto.getField());
		entity.setShift(fromDto.getShift());
		entity.setUserId(fromDto.getUserId());
		entity.setUserName(fromDto.getUserName());
		return entity;
	}

	@Override
	protected HandoverNotesDto exportDto(HandoverNotesDo entity) {
		HandoverNotesDto toDto = new HandoverNotesDto();
		toDto.setNoteId(entity.getNoteId());
		toDto.setDate(ServicesUtil.getDateString(entity.getDate()));
		toDto.setField(entity.getField());
		toDto.setShift(entity.getShift());
		toDto.setUserId(entity.getUserId());
		toDto.setUserName(entity.getUserName());
		return toDto;
	}

	public Integer saveHandoverNote(HandoverNotesDto dto) {
		Integer primaryKey = null;
		Integer val = null;
		try {
			HandoverNotesDo entity = new HandoverNotesDo();

			if (ServicesUtil.isEmpty(dto.getNoteId())) {
				val = checkDataAvailable(dto);
				if (ServicesUtil.isEmpty(val)) {
					entity = importDto(dto);
					primaryKey = (Integer) this.getSession().save(entity);
				} else {
					HandoverNotesDo entityForUpdate = new HandoverNotesDo();
					entityForUpdate.setNoteId(val);
					entityForUpdate.setDate(ServicesUtil.getDate(dto.getDate()));
					entityForUpdate.setField(dto.getField());
					entityForUpdate.setShift(dto.getShift());
					entityForUpdate.setUserId(dto.getUserId());
					entityForUpdate.setUserName(dto.getUserName());
					this.getSession().update(entityForUpdate);
					primaryKey = val;

				}
			} else {
				HandoverNotesDo entityForUpdate = new HandoverNotesDo();
				entityForUpdate.setNoteId(dto.getNoteId());
				entityForUpdate.setDate(ServicesUtil.getDate(dto.getDate()));
				entityForUpdate.setField(dto.getField());
				entityForUpdate.setShift(dto.getShift());
				entityForUpdate.setUserId(dto.getUserId());
				entityForUpdate.setUserName(dto.getUserName());
				this.getSession().update(entityForUpdate);

				HandoverNotesDto d = exportDto(entityForUpdate);
				System.err.println("dto : " + d.toString());

				primaryKey = d.getNoteId();
			}
			System.err.println("success in session.save");

		} catch (Exception e) {
			logger.error("[Murphy][HandoverNotesDao][saveIntoHandoverNotesTable][Exception]  : " + e.getMessage());
			e.printStackTrace();

		}
		return primaryKey;
	}

	@SuppressWarnings("unchecked")
	private Integer checkDataAvailable(HandoverNotesDto dto) throws ParseException {
//		Date date = ServicesUtil.getDate(dto.getDate());
		Integer primaryKey = null;
		try {

			String queryString = "SELECT NOTE_ID,USER_ID FROM HANDOVER_NOTES WHERE SHIFT='" + dto.getShift() + "' and "
					+ "FIELD='" + dto.getField() + "' and USER_ID='" + dto.getUserId() + "' and DATE='" + dto.getDate() + "' ";
			Query query = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {

					primaryKey = (Integer) obj[0];
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][HandoverNotesDao][checkDataAvailable][Exception]  : " + e.getMessage());
			e.printStackTrace();

		}
		return primaryKey;

	}

	/// end of save notes method

	// get note by note Id start
	@SuppressWarnings("unchecked")
	public List<HandoverNotesDto> getNotesForTheNoteIds(Integer noteId){
		List<HandoverNotesDto> handoverNotesDtoList = new ArrayList<HandoverNotesDto>();

		try {

			String queryString = "SELECT T1.NOTE_ID, T1.DATE, T1.SHIFT, T1.FIELD,"
					+ "T1.USER_ID, T1.USER_NAME, T2.NOTE_CAT_ID, "
					+ "cast(BINTOSTR(cast(T2.NOTE as binary)) as varchar) as NOTE from HANDOVER_NOTES T1 join "
					+ "HANDOVER_NOTES_DATA T2 on T1.NOTE_ID=T2.NOTE_ID where T1.NOTE_ID=" + noteId + " ";
			Query query = this.getSession().createSQLQuery(queryString);
			System.err.println("query : " + queryString);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					HandoverNotesDto dto = new HandoverNotesDto();
					if (!ServicesUtil.isEmpty(obj[0])) {
						dto.setNoteId((Integer) obj[0]);
					}
					if (!ServicesUtil.isEmpty(obj[1])) {
						Date date = (Date) obj[1];

						dto.setDate(ServicesUtil.getDateString(date));
					}
					if (!ServicesUtil.isEmpty(obj[2])) {
						dto.setShift((String) obj[2]);
					}
					if (!ServicesUtil.isEmpty(obj[3])) {
						dto.setField((String) obj[3]);
					}
					if (!ServicesUtil.isEmpty(obj[4])) {
						dto.setUserId((String) obj[4]);
					}
					if (!ServicesUtil.isEmpty(obj[5])) {
						dto.setUserName((String) obj[5]);
					}
					if (!ServicesUtil.isEmpty(obj[6])) {
						dto.setNoteCategoryId((String) obj[6]);
					}

					if (!ServicesUtil.isEmpty(obj[7])) {
						//Clob clob= (Clob) obj[7];
						
						//String clobString = clob.getSubString(Integer.MAX_VALUE,  (int) clob.length());

						dto.setNote((String) obj[7]);
					}

					handoverNotesDtoList.add(dto);
				}
			}
		} catch (Exception e) {
			logger.error("[Murphy][HandoverNotesDao][getNotesForTheNoteIds][Exception]  : " + e.getMessage());
			e.printStackTrace();
		}
		return handoverNotesDtoList;

	}

	// end of get node by noteId method

	// start of MyNotes/SearchNotes Method

	@SuppressWarnings("unchecked")
	public HandoverNotesToROCResponseDto getNoteByNoteType(HandoverNotesRequestDto requesDto) {
		List<HandoverNotesDto> handoverNotesDtoList = new ArrayList<HandoverNotesDto>();
		HandoverNotesToROCResponseDto responseDto = new HandoverNotesToROCResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		String queryString = "";
		String toDate = "";
		String fromDate = "";
		String shift = "";
		String field = "";
		String userId = "";

		try {
			userId = requesDto.getUserId();
			if (!ServicesUtil.isEmpty(requesDto.getNoteType())
					&& requesDto.getNoteType().equalsIgnoreCase("My Notes")) {
				if (ServicesUtil.isEmpty(requesDto.getFromDate()) && ServicesUtil.isEmpty(requesDto.getToDate())) {
					// current date
					java.util.Date today = ServicesUtil.getDate(0);
					SimpleDateFormat toDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					toDate = toDateFormatter.format(today);
					System.err.println("today : " + toDate);

					// finding out current date- 30days date
					java.util.Date fromdate = ServicesUtil.getDate(30);
					SimpleDateFormat fromDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					fromDate = fromDateFormatter.format(fromdate);
					System.err.println("fromDate : " + fromDate);
				} else if (!ServicesUtil.isEmpty(requesDto.getFromDate())
						&& !ServicesUtil.isEmpty(requesDto.getToDate())) {
					toDate = requesDto.getToDate();
					fromDate = requesDto.getFromDate();
				}

				if (!ServicesUtil.isEmpty(requesDto.getField()) && !ServicesUtil.isEmpty(requesDto.getShift())) {

					field = getStringForInQuery(requesDto.getField());
					shift = getStringForInQuery(requesDto.getShift());
					queryString = "SELECT NOTE_ID, DATE, SHIFT, FIELD," + "USER_ID, USER_NAME from HANDOVER_NOTES "
							+ " where DATE BETWEEN '" + fromDate + "' AND '" + toDate + "' " + "and SHIFT IN(" + shift
							+ ") and FIELD IN(" + field + ") and USER_ID ='" + userId + "' order by DATE desc";
				} 
				if (ServicesUtil.isEmpty(requesDto.getField()) && ServicesUtil.isEmpty(requesDto.getShift())) {
					queryString = "SELECT NOTE_ID, DATE, SHIFT, FIELD," + "USER_ID,USER_NAME from HANDOVER_NOTES "
							+ " where DATE BETWEEN '" + fromDate + "' AND '" + toDate + "' " + "and USER_ID ='" + userId
							+ "' order by DATE desc";
				}

				if(!ServicesUtil.isEmpty(requesDto.getField()) && ServicesUtil.isEmpty(requesDto.getShift()))
				{
				field =getStringForInQuery(requesDto.getField());

				queryString = "SELECT NOTE_ID, DATE, SHIFT, FIELD,"
							  + "USER_ID, USER_NAME from HANDOVER_NOTES "
							  + " where DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							  + "and FIELD IN("+field+") and USER_ID ='"+userId+"' order by DATE desc" ;
				}

				if(!ServicesUtil.isEmpty(requesDto.getShift()) && ServicesUtil.isEmpty(requesDto.getField()))
				{
				shift=getStringForInQuery(requesDto.getShift());

				queryString = "SELECT NOTE_ID, DATE, SHIFT, FIELD,"
							  + "USER_ID, USER_NAME from HANDOVER_NOTES "
							  + " where DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							  + "and SHIFT IN("+shift+") and USER_ID ='"+userId+"' order by DATE desc" ;
				}
				if(!ServicesUtil.isEmpty(queryString))
				{
				Query query = this.getSession().createSQLQuery(queryString);
				System.err.println("query : " + queryString);
				List<Object[]> resultList = query.list();
				if (!ServicesUtil.isEmpty(resultList)) {
					for (Object[] obj : resultList) {
						HandoverNotesDto dto = new HandoverNotesDto();
						if (!ServicesUtil.isEmpty(obj[0])) {
							dto.setNoteId((Integer) obj[0]);
						}
						if (!ServicesUtil.isEmpty(obj[1])) {
							Date date = (Date) obj[1];

							dto.setDate(ServicesUtil.getDateString(date));
						}
						if (!ServicesUtil.isEmpty(obj[2])) {
							dto.setShift((String) obj[2]);
						}
						if (!ServicesUtil.isEmpty(obj[3])) {
							dto.setField((String) obj[3]);
						}
						if (!ServicesUtil.isEmpty(obj[4])) {
							dto.setUserId((String) obj[4]);
						}
						if (!ServicesUtil.isEmpty(obj[5])) {
							dto.setUserName((String) obj[5]);
						}

						handoverNotesDtoList.add(dto);
					}
				}
				}
				responseDto.setHandoverNotesDtoList(handoverNotesDtoList);
			}

			if (!ServicesUtil.isEmpty(requesDto.getNoteType())
					&& requesDto.getNoteType().equalsIgnoreCase("Search Notes")) {
				fromDate = requesDto.getFromDate();
				toDate = requesDto.getToDate();
				//toDate = String.valueOf(ServicesUtil.getDate(requesDto.getToDate()));
				//fromDate = String.valueOf(ServicesUtil.getDate(requesDto.getFromDate()));
				if (!ServicesUtil.isEmpty(fromDate) && !ServicesUtil.isEmpty(toDate)) {
					if (!ServicesUtil.isEmpty(requesDto.getField()) && !ServicesUtil.isEmpty(requesDto.getShift())) {
						field = getStringForInQuery(requesDto.getField());
						shift = getStringForInQuery(requesDto.getShift());

						queryString = "SELECT NOTE_ID, DATE,SHIFT, FIELD," + "USER_ID, USER_NAME from HANDOVER_NOTES "
								+ " where DATE BETWEEN '" + fromDate + "' AND '" + toDate + "' " + "and SHIFT IN("
								+ shift + ") and FIELD IN(" + field + ") and USER_ID NOT IN('" + userId + "') "
								+ "order by DATE desc";
					} 
					
					if (ServicesUtil.isEmpty(requesDto.getField()) && ServicesUtil.isEmpty(requesDto.getShift())) {
						queryString = "SELECT NOTE_ID, DATE, SHIFT, FIELD," + "USER_ID, USER_NAME from HANDOVER_NOTES "
								+ "where DATE BETWEEN '" + fromDate + "' AND '" + toDate + "' and USER_ID NOT IN('"
								+ userId + "')" + " order by DATE desc ";
					}
					
					if(!ServicesUtil.isEmpty(requesDto.getField()) && ServicesUtil.isEmpty(requesDto.getShift()))
					{
					field =getStringForInQuery(requesDto.getField());

								    
					queryString = "SELECT NOTE_ID, DATE,SHIFT, FIELD,"
								+ "USER_ID, USER_NAME from HANDOVER_NOTES "
								+ " where DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' "
								+ "and FIELD IN("+field+") and USER_ID NOT IN('"+userId+"') "
								+ "order by DATE desc" ;
					}

					if(!ServicesUtil.isEmpty(requesDto.getShift()) && ServicesUtil.isEmpty(requesDto.getField()))
					{
					shift=getStringForInQuery(requesDto.getShift());
					queryString = "SELECT NOTE_ID, DATE,SHIFT, FIELD,"
								+ "USER_ID, USER_NAME from HANDOVER_NOTES "
								+ " where DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' "
								+ "and SHIFT IN("+shift+") and USER_ID NOT IN('"+userId+"') "
								+ "order by DATE desc" ;
					}
					
					if(!ServicesUtil.isEmpty(queryString))
					{
					Query query = this.getSession().createSQLQuery(queryString);
					System.err.println("query : " + queryString);
					List<Object[]> resultList = query.list();
					if (!ServicesUtil.isEmpty(resultList)) {
						for (Object[] obj : resultList) {
							HandoverNotesDto dto = new HandoverNotesDto();
							if (!ServicesUtil.isEmpty(obj[0])) {
								dto.setNoteId((Integer) obj[0]);
							}
							if (!ServicesUtil.isEmpty(obj[1])) {
								Date date = (Date) obj[1];

								dto.setDate(ServicesUtil.getDateString(date));
							}
							if (!ServicesUtil.isEmpty(obj[2])) {
								dto.setShift((String) obj[2]);
							}
							if (!ServicesUtil.isEmpty(obj[3])) {
								dto.setField((String) obj[3]);
							}
							if (!ServicesUtil.isEmpty(obj[4])) {
								dto.setUserId((String) obj[4]);
							}
							if (!ServicesUtil.isEmpty(obj[5])) {
								dto.setUserName((String) obj[5]);
							}

							handoverNotesDtoList.add(dto);
						}
					}
					
					
					}
					responseDto.setHandoverNotesDtoList(handoverNotesDtoList);

				}
			}
			responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseDto.setResponseMessage(responseMessage);

		} catch (Exception e) {
			logger.error("[Murphy][HandoverNotesDao][getNoteByNoteType  ][Exception]  : " + e.getMessage());
			e.printStackTrace();
			responseDto.setHandoverNotesDtoList(handoverNotesDtoList);
			responseMessage.setMessage(MurphyConstant.READ_FAILURE);
			responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
			responseMessage.setStatus(MurphyConstant.FAILURE);
			responseDto.setResponseMessage(responseMessage);
		}
		return responseDto;

	}

	private String getStringForInQuery(String inputString) {
		String returnString = "";
		if (!ServicesUtil.isEmpty(inputString)) {
			if (inputString.contains(",")) {
				returnString = getStringFromList(inputString.split(","));
			} else {
				returnString = "'" + inputString.trim() + "'";
			}
		}
		return returnString;
	}

	private String getStringFromList(String[] stringList) {
		String returnString = "";
		for (String st : stringList) {
			returnString = returnString + "'" + st.trim() + "',";
		}
		return returnString.substring(0, returnString.length() - 1);
	}

	public HandoverNotesDto getHandOverNotesDetailsById(Integer noteId){
		HandoverNotesDto handOverDto=null;
		String fetchQuery=null;
		try{
			fetchQuery="SELECT DATE,SHIFT,FIELD,USER_ID,USER_NAME FROM HANDOVER_NOTES WHERE NOTE_ID="+noteId;
			Query query = this.getSession().createSQLQuery(fetchQuery);
			System.err.println("query : " + fetchQuery);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					handOverDto=new HandoverNotesDto();
//					DateFormat dateFormat = new SimpleDateFormat(MurphyConstant.DATE_STANDARD);
//					Date createdAt = ServicesUtil.isEmpty(obj[0]) ? null : (Date)obj[0];
					
					handOverDto.setDate(ServicesUtil.isEmpty(obj[0]) ? null : ServicesUtil.getDateString((java.util.Date) obj[0]));
//					handOverDto.setNoteCreatedAt(createdAt);
					handOverDto.setShift(ServicesUtil.isEmpty(obj[1]) ? null : (String)obj[1]);
					handOverDto.setField(ServicesUtil.isEmpty(obj[2]) ? null : (String)obj[2]);
					handOverDto.setUserId(ServicesUtil.isEmpty(obj[3]) ? null : (String)obj[3]);
					handOverDto.setUserName(ServicesUtil.isEmpty(obj[4]) ? null : (String)obj[4]);
				}
			}
		}
		catch(Exception e){
			logger.error("[Murphy][HandoverNotesDao][getHandOverNotesDetailsById][Exception]  : " + e.getMessage());
			e.printStackTrace();
		}
		return handOverDto;
	}

	// end of MyNotes and SearchNotes Method

}