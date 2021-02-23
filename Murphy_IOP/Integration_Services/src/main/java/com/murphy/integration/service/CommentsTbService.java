package com.murphy.integration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.CommentsTbDao;
import com.murphy.integration.dto.CommentsTbDto;
import com.murphy.integration.dto.DailyReportCommentsDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.entity.CommentsTbDo;
import com.murphy.integration.entity.ProdViewCommentsDo;
import com.murphy.integration.interfaces.CommentsTbLocal;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ProcountConstant;
import com.murphy.integration.util.ProdViewConstant;
import com.murphy.integration.util.ServicesUtil;

public class CommentsTbService implements CommentsTbLocal {

	private static final Logger logger = LoggerFactory.getLogger(CommentsTbService.class);

	public ResponseMessage insertMerrickIdIntoDB(CommentsTbDto commentsTbDto) {
		logger.info("[insertMerrickIdIntoDB] : INFO- Service Started");
		ResponseMessage responseMessage = new ResponseMessage();
		String countryCode = null;
		if (commentsTbDto != null && commentsTbDto.getEnteredDate() != null && commentsTbDto.getUwiID() != null
				&& commentsTbDto.getUwiID().length() > 0) {

			try {
				countryCode = ServicesUtil.getCountryCodeByMuwi(commentsTbDto.getUwiID());
				logger.error("[countryCode] : INFO- Code " + countryCode);

			} catch (Exception e) {
				logger.info("[Murphy][insertMerrickIdIntoDB][getCountryCodeByMuwi]" + e.getMessage());
			}

			CommentsTbDao commentsTbDao = null;
			CommentsTbDo commentsTbDo = null;
			ProdViewCommentsDo prodViewCommentsDo = null;
			if (!ServicesUtil.isEmpty(countryCode) && countryCode.equalsIgnoreCase(ProcountConstant.EFS_CODE)) {

				

				Connection connection = DBConnections.createConnectionForProcount();
				logger.error("[insertMerrickIdIntoDB] : INFO- Connection " + connection);

				if (connection != null) {
					commentsTbDao = new CommentsTbDao();
					try {
						logger.info("[insertMerrickIdIntoDB] : INFO- Connection to DB successful");

						commentsTbDo = convertCommentsTbDtoToDo(commentsTbDto);

						int merrickId = commentsTbDao.fetchMerrickFromDB(connection, commentsTbDto.getUwiID().trim());
						logger.error("[insertMerrickIdIntoDB] : INFO  - merrickId " + merrickId);

						if (merrickId >= 0) {

							commentsTbDo.setReferenceMerrickItem(merrickId);

							CommentsTbDo commentsTbDoListFromDB = commentsTbDao.getDataFromCommentsDB(connection,
									commentsTbDo.getReferenceMerrickItem(), commentsTbDo.getOriginalDateEntered());

							if (commentsTbDoListFromDB != null) {
								commentsTbDao.updateDataInCommentsDB(connection, commentsTbDo);

							} else {
								commentsTbDao.insertDataInCommentsDB(connection, commentsTbDo);

							}
							connection.commit();
							responseMessage.setMessage("Comment updated successfully");
							responseMessage.setStatus("SUCCESS");
							responseMessage.setStatusCode("0");

						} else {
							responseMessage.setMessage("No merrick Id is maintained for provided uwiId");
							responseMessage.setStatus("FAILURE");
							responseMessage.setStatusCode("1");
						}
					} catch (Exception e) {
						try {
							connection.rollback();
						} catch (SQLException sqlException) {
							logger.error("[insertMerrickIdIntoDB] : ERROR- Rollback transactions because of exception "
									+ sqlException);
						}
						logger.error("[insertMerrickIdIntoDB] : ERROR- Exception while interacting with database " + e);
						responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
						responseMessage.setStatus("FAILURE");
						responseMessage.setStatusCode("1");
					} finally {
						try {
							connection.close();
						} catch (SQLException e) {
							logger.error("[insertMerrickIdIntoDB] : ERROR- Exception while closing Connection " + e);
						}

					}
				} else {
					responseMessage.setMessage("Connection to Database is not possible.");
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");
				}
			} else {

				Connection connection =DBConnections.createConnectionForProdView();
				logger.error("[ProdView] : INFO- Connection " + connection);

				if (connection != null) {
					commentsTbDao = new CommentsTbDao();
					try {
						logger.info("[ProdView] : INFO- Connection to DB successful");

						prodViewCommentsDo = convertCommentsTbDtoToProdViewDo(commentsTbDto);

						Map<String,String> pvUnitComp = commentsTbDao.fetchIdFromPvtUnitComp(connection,
								commentsTbDto.getUwiID().trim());

						if (!ServicesUtil.isEmpty(pvUnitComp) && !ServicesUtil.isEmpty(pvUnitComp.get("idflownet")) && !ServicesUtil.isEmpty(pvUnitComp.get("idrecparent"))) {


							prodViewCommentsDo.setIdflownet(pvUnitComp.get("idflownet"));
							prodViewCommentsDo.setIdrecParent(pvUnitComp.get("idrecparent"));

							ProdViewCommentsDo prodCommentsDoList = commentsTbDao.getDataFromProdViewCommentsDB(connection,
									prodViewCommentsDo);

							if (prodCommentsDoList != null) {
								logger.info("[ProdView] Data Exists");
								prodViewCommentsDo.setIdflownet(prodCommentsDoList.getIdflownet());
								prodViewCommentsDo.setIdrec(prodCommentsDoList.getIdrec());
								prodViewCommentsDo.setSysCreateDate(prodCommentsDoList.getSysCreateDate());
								commentsTbDao.updateDataInProdViewCommentsDB(connection, prodViewCommentsDo);

							} else {
								commentsTbDao.insertDataInProdViewCommentsDB(connection, prodViewCommentsDo);

							}
							connection.commit();
							responseMessage.setMessage("Comment updated successfully");
							responseMessage.setStatus("SUCCESS");
							responseMessage.setStatusCode("0");

						} else {
							responseMessage.setMessage("No Mapping Id is maintained for provided uwiId");
							responseMessage.setStatus("FAILURE");
							responseMessage.setStatusCode("1");
						}
					} catch (Exception e) {
						try {
							connection.rollback();
						} catch (SQLException sqlException) {
							logger.error("[insertMerrickIdIntoDB] : ERROR- Rollback transactions because of exception "
									+ sqlException);
						}
						logger.error("[insertMerrickIdIntoDB] : ERROR- Exception while interacting with database " + e);
						responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
						responseMessage.setStatus("FAILURE");
						responseMessage.setStatusCode("1");
					} finally {
						try {
							connection.close();
						} catch (SQLException e) {
							logger.error("[insertMerrickIdIntoDB] : ERROR- Exception while closing Connection " + e);
						}

					}
				} else {
					responseMessage.setMessage("Connection to Database is not possible.");
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");
				}
				
				
			}
		} else {
			responseMessage.setMessage("Please provide all the necessary values.");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");
		}
		return responseMessage;
	}

	private ProdViewCommentsDo convertCommentsTbDtoToProdViewDo(CommentsTbDto commentsTbDto) {
		ProdViewCommentsDo prodViewCommentsDo = new ProdViewCommentsDo();
		String prodDate = null;
		
		String CommentsEnteredDate=ServicesUtil.convertFromZoneToZoneString(new Date(),null,ProdViewConstant.UTC_ZONE,ProdViewConstant.MST_ZONE, ProdViewConstant.DATEFORMAT_T,
				ProdViewConstant.DATE_DB_FORMATE);
		Date prodDateEntered = commentsTbDto.getProdDate();
		String hstyOfComments = getAllComments(commentsTbDto);

		
		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
        prodDate = dateformatter.format(prodDateEntered);

		
		prodViewCommentsDo.setDttm(prodDate);
		prodViewCommentsDo.setCom(hstyOfComments);
		prodViewCommentsDo.setIdrecItemtk("pvunitcomp");
		prodViewCommentsDo.setSysCreateDate(CommentsEnteredDate);
		prodViewCommentsDo.setSysLockDate(CommentsEnteredDate);
		prodViewCommentsDo.setSysModDate(CommentsEnteredDate);
		prodViewCommentsDo.setSysModUser(ProcountConstant.SVC_IOP);
		prodViewCommentsDo.setSysCreateUser(ProcountConstant.SVC_IOP);

		logger.error("[convertCommentsTbDtoToProdViewDo] : INFO  - ProdViewDo" + prodViewCommentsDo);
		return prodViewCommentsDo;
	}

	private CommentsTbDo convertCommentsTbDtoToDo(CommentsTbDto commentsDto) {
		CommentsTbDo commentsTbDo = new CommentsTbDo();

		Date prodDateEntered = commentsDto.getProdDate();
		String hstyOfComments = getAllComments(commentsDto);

		String prodDate = null;
		String prodTime = null;
		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");

		prodDate = dateformatter.format(prodDateEntered);
		prodTime = timeformatter.format(prodDateEntered);

		commentsTbDo.setReferenceMerrickType(ProcountConstant.COM_REFERENCE_MERRICK_TYPE);
		commentsTbDo.setOriginalDateEntered(prodDate);
		commentsTbDo.setOriginalTimeEntered(prodTime);
		commentsTbDo.setCommentType(ProcountConstant.COM_COMMENT_TYPE);
		commentsTbDo.setCommentPurpose(ProcountConstant.COM_COMMENT_PURPOSE);
		commentsTbDo.setCommentGeneral(hstyOfComments);
		commentsTbDo.setRioCommentCode(ProcountConstant.COM_RIO_COMMENTCODE);
		commentsTbDo.setPriorityType(ProcountConstant.COM_PRIORITY_TYPE);
		commentsTbDo.setMessageSendFlag(ProcountConstant.COM_MESSAGE_SENDFLAG);
		commentsTbDo.setDestinationPerson(ProcountConstant.COM_DESTINATION_PERSON);
		commentsTbDo.setTempInteger(ProcountConstant.COM_TEMP_INTEGER);
		commentsTbDo.setLastTransmission(ProcountConstant.COM_LAST_TRANSMISSION);
		commentsTbDo.setLastLoadDate(ProcountConstant.COM_LAST_LOADDATE);
		commentsTbDo.setLastLoadTime(ProcountConstant.COM_LAST_LOADTIME);
		commentsTbDo.setTransmitFlag(ProcountConstant.COM_TRANSMIT_FLAG);
		commentsTbDo.setDateTimeStamp(prodDate);
		commentsTbDo.setUserDateStamp(prodDate);
		commentsTbDo.setUserTimeStamp(ProcountConstant.COM_USER_TIMESTAMP);
		commentsTbDo.setUserID(ProcountConstant.COM_USERID);
		commentsTbDo.setRowUID(ProcountConstant.COM_ROWUID);
		commentsTbDo.setCommentServiceID(ProcountConstant.COM_COMMENT_SERVICEID);

		logger.error("[convertCommentsTbDtoToDo] : INFO  - CommentsTbDo" + commentsTbDo);
		return commentsTbDo;

	}

	public String getAllComments(CommentsTbDto commentsDto) {
		String[] arrSplit = null;
		String concatenateComment = null;
		String finalComment = null;
		String originalDateTime = null;
		try {
			String comments = commentsDto.getComments();
			String userName = commentsDto.getUserName();
			Date originalDateEntered = commentsDto.getEnteredDate();
//			originalDateTime = ServicesUtil.parseDateTimeFormatString(originalDateEntered, null, "", "",
//					ProcountConstant.COM_DATE_FORMAT, ProcountConstant.Extract);
			originalDateTime = ServicesUtil.convertFromZoneToZoneString(originalDateEntered, null, "", "",
					ProcountConstant.COM_DATE_FORMAT, ProcountConstant.EXTRACT_DATE_TIME_FORMAT);
			arrSplit = comments.split("\\#\\$\\@\\$\\#");

			for (int i = 0; i < arrSplit.length; i++) {

				if (i == 0 && arrSplit.length == 1) {

					concatenateComment = userName + " [ " + originalDateTime + " ] : " + arrSplit[i];

				} else if (i == 0 && arrSplit.length > 1) {
					concatenateComment = arrSplit[i] + ProcountConstant.COMMENT_IDENTIFIER;
				} else if (i == arrSplit.length - 1) {
					concatenateComment = finalComment + userName + " [ " + originalDateTime + " ] : " + arrSplit[i];
				} else {
					concatenateComment = finalComment + arrSplit[i] + ProcountConstant.COMMENT_IDENTIFIER;

				}

				finalComment = concatenateComment;
			}

			logger.error(finalComment);

		} catch (Exception e) {
			logger.error("[Murphy][CommentsTbService] : Error  - getAllComments" + e.getMessage());
		}
		return finalComment;
	}

	@Override
	public UIResponseDto fetchDataFromCommentsDB(String uWID, Date originalDateEntered) {

		logger.info("[fetchDataFromCommentsDB] : INFO- Service Started");
		UIResponseDto uiResponseDto = new UIResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		CommentsTbDao commentsTbDao = null;
		Connection connection = null;
		Connection connection2 = null;
		ProdViewCommentsDo prodCommentsDo=null;
		String countryCode = null;
		if (uWID != null && uWID.length() > 0 && originalDateEntered != null) {

			try {
				countryCode = ServicesUtil.getCountryCodeByMuwi(uWID);

				if (!ServicesUtil.isEmpty(countryCode) && ServicesUtil.EFS_CODE.equalsIgnoreCase(countryCode)) {

					connection = DBConnections.createConnectionForProcount();

					logger.error("[fetchDataFromCommentsDB] : INFO- new connection is created " + connection);

					if (connection != null) {
						commentsTbDao = new CommentsTbDao();

						try {
							logger.info("[fetchDataFromCommentsDB] : INFO- Connection to DB successful");

							SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
							String originalDateInDB = dateformatter.format(originalDateEntered);
							logger.error("[fetchDataFromCommentsDB] : INFO  - originalDateInDB " + originalDateInDB);

							int merrickId = commentsTbDao.fetchMerrickFromDB(connection, uWID.trim());
							logger.error("[fetchDataFromCommentsDB] : INFO  - merrickId " + merrickId);
							if (merrickId > 0) {

								CommentsTbDo commentsTbDoListFromDB = commentsTbDao.getDataFromCommentsDB(connection,
										merrickId, originalDateInDB);

								if (commentsTbDoListFromDB != null) {
									CommentsTbDto commentsTbDto = convertCommentsDoToDTo(commentsTbDoListFromDB);
									commentsTbDto.setUwiId(uWID.trim());
									uiResponseDto.setCommentsTbDto(commentsTbDto);
									responseMessage.setMessage("Successful");
									responseMessage.setStatus("SUCCESS");
									responseMessage.setStatusCode("0");

								} else {
									responseMessage.setMessage("No record is present for provided values.");
									responseMessage.setStatus("false");
								}
							} else {
								responseMessage.setMessage("No merrick Id is maintained for prvided uwiId.");
								responseMessage.setStatus("false");
							}
						} catch (Exception e) {
							logger.error("[fetchDataFromCommentsDB] : ERROR- Exception while interacting with database "
									+ e);
							responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
							responseMessage.setStatus("FAILURE");
							responseMessage.setStatusCode("1");
						} finally {
							try {
								connection.close();
							} catch (SQLException e) {
								logger.error(
										"[fetchDataFromCommentsDB] : ERROR- Exception while closing Connection " + e);
							}
						}
					} else {
						responseMessage.setMessage("Connection to Database is not possible");
						responseMessage.setStatus("FAILURE");
						responseMessage.setStatusCode("1");
					}
				} else if (!ServicesUtil.isEmpty(countryCode) && ServicesUtil.CA_CODE.equalsIgnoreCase(countryCode)) {

//					// Pointing to PROD since its only get Data service
//					connection = DBConnections.createConnectionForDORADB2();
//
//					logger.error("Prod View connection is created " + connection);
//
//					if (connection != null) {
//						commentsTbDao = new CommentsTbDao();
//
//						try {
//							logger.error("[fetchDataFromCommentsDB] : INFO- Connection to DB successful");
//
//							SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
//							SimpleDateFormat dateformatterInTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							String originalDateInDB = dateformatter.format(originalDateEntered);
//							String originalDateInTime = dateformatterInTime.format(originalDateEntered);
//							logger.error("[fetchDataFromCommentsDB] : INFO  - originalDateInDB " + originalDateInDB);
//
//							String wellName = commentsTbDao.fetchWellNameByMuwi(connection, uWID.trim());
//
//							connection2 = DBConnections.createConnectionForDORADB(ProcountConstant.DAILY_REPORTS);
//
//							CommentsTbDto commentsTbDoListFromDB = commentsTbDao
//									.getProcountCommentsForCanada(connection2, wellName, originalDateInDB);
//
//							if (commentsTbDoListFromDB != null) {
//
//								commentsTbDoListFromDB.setUwiId(uWID.trim());
//								commentsTbDoListFromDB.setEnteredDate(dateformatterInTime.parse(originalDateInTime));
//								uiResponseDto.setCommentsTbDto(commentsTbDoListFromDB);
//								responseMessage.setMessage("Successful");
//								responseMessage.setStatus("SUCCESS");
//								responseMessage.setStatusCode("0");
//
//							} else {
//								responseMessage.setMessage("No record is present for provided values.");
//								responseMessage.setStatus("false");
//							}
//
//						} catch (Exception e) {
//							logger.error("[fetchDataFromCommentsDB] : ERROR- Exception while interacting with database "
//									+ e);
//							responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
//							responseMessage.setStatus("FAILURE");
//							responseMessage.setStatusCode("1");
//						} finally {
//							try {
//								connection.close();
//								connection2.close();
//							} catch (SQLException e) {
//								logger.error("[fetchDataFromCommentsDB] : ERROR- Exception while closing Connection "
//										+ e.getMessage());
//							}
//						}
//					} else {
//						responseMessage.setMessage("Connection to Database is not possible");
//						responseMessage.setStatus("FAILURE");
//						responseMessage.setStatusCode("1");
//					}

					connection = DBConnections.createConnectionForProdView();

					logger.error("[ProdViewConnection] : INFO- new connection is created " + connection);

					if (connection != null) {
						commentsTbDao = new CommentsTbDao();
						prodCommentsDo=new ProdViewCommentsDo();
						try {
							logger.info("[fetchDataFromCommentsDB] : INFO- Connection to DB successful");

							SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat dateTimeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
							String originalDateInDB = dateformatter.format(originalDateEntered);
							String originalDateTimeInDb=dateTimeformatter.format(originalDateEntered);
							logger.error("[fetchDataFromCommentsDB] : INFO  - originalDateInDB " + originalDateInDB);

							Map<String,String> pvUnitComp = commentsTbDao.fetchIdFromPvtUnitComp(connection,
									uWID.trim());

							if (!ServicesUtil.isEmpty(pvUnitComp) && !ServicesUtil.isEmpty(pvUnitComp.get("idflownet")) && !ServicesUtil.isEmpty(pvUnitComp.get("idrecparent"))) {

								prodCommentsDo.setIdflownet(pvUnitComp.get("idflownet"));
								prodCommentsDo.setIdrecParent(pvUnitComp.get("idrecparent"));
								prodCommentsDo.setSysCreateDate(originalDateTimeInDb);

								ProdViewCommentsDo prodCommentsDoList = commentsTbDao.getDataFromProdViewCommentsDB(connection,
										prodCommentsDo);

								if (prodCommentsDoList != null) {
									CommentsTbDto commentsTbDto = convertProdDoToDTo(prodCommentsDoList);
									commentsTbDto.setUwiId(uWID.trim());
									uiResponseDto.setCommentsTbDto(commentsTbDto);
									responseMessage.setMessage("Successful");
									responseMessage.setStatus("SUCCESS");
									responseMessage.setStatusCode("0");

								} else {
									responseMessage.setMessage("No record is present for provided values.");
									responseMessage.setStatus("false");
								}
								connection.commit();
								responseMessage.setMessage("Comment updated successfully");
								responseMessage.setStatus("SUCCESS");
								responseMessage.setStatusCode("0");

							} else {
								responseMessage.setMessage("No Mapping Id is maintained for provided uwiId");
								responseMessage.setStatus("FAILURE");
								responseMessage.setStatusCode("1");
							}
						} catch (Exception e) {
							logger.error("[fetchDataFromCommentsDB] : ERROR- Exception while interacting with database "
									+ e);
							responseMessage.setMessage("Server Internal Error. Facing difficulties interacting to DB.");
							responseMessage.setStatus("FAILURE");
							responseMessage.setStatusCode("1");
						} finally {
							try {
								connection.close();
							} catch (SQLException e) {
								logger.error(
										"[fetchDataFromCommentsDB] : ERROR- Exception while closing Connection " + e);
							}
						}
					} else {
						responseMessage.setMessage("Connection to Database is not possible");
						responseMessage.setStatus("FAILURE");
						responseMessage.setStatusCode("1");
					}
					
					
					
					
				} else {
					responseMessage.setMessage("Failed to Identify Country Code");
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");
				}

			} catch (Exception e) {
				logger.error("[fetchDataFromCommentsDB] : ERROR- Exception while fetching Comments " + e.getMessage());
			}
		} else {
			responseMessage.setMessage("Please provide all the necessary values");
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("1");
		}
		logger.info("[fetchDataFromCommentsDB] : UIResponseDto " + uiResponseDto);
		uiResponseDto.setResponseMessage(responseMessage);
		return uiResponseDto;
	}

	private CommentsTbDto convertProdDoToDTo(ProdViewCommentsDo prodCommentsDoList) throws ParseException {
		CommentsTbDto commentsTbDto = new CommentsTbDto();
		Date originalDateEntered = null;

		String comments = prodCommentsDoList.getCom();

		SimpleDateFormat dateFormatterWithTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		
		try {
			originalDateEntered = dateFormatterWithTimestamp
					.parse(prodCommentsDoList.getDttm());
			logger.error("EnteredDate--" + originalDateEntered);

		} catch (ParseException e) {
			logger.error("[convertCommentsDoToDTo] : ERROR- Exception while Converting from String to Date " + e);
			throw e;
		}

		commentsTbDto.setEnteredDate(originalDateEntered);
		commentsTbDto.setComments(comments);

		logger.error("[convertCommentsDoToDTo] : INFO commentsTbDto " + commentsTbDto);
		return commentsTbDto;
	}

	private CommentsTbDto convertCommentsDoToDTo(CommentsTbDo commentsTbDo) throws ParseException {

		CommentsTbDto commentsTbDto = new CommentsTbDto();
		Date originalDateEntered = null;

		String originalDateInDB = commentsTbDo.getOriginalDateEntered();
		String originalTimeInDB = commentsTbDo.getOriginalTimeEntered();
		String comments = commentsTbDo.getCommentGeneral();

		SimpleDateFormat dateFormatterWithTimestamp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

		try {
			originalDateEntered = dateFormatterWithTimestamp
					.parse(originalDateInDB.trim().substring(0, 10) + originalTimeInDB.trim());
			logger.error("[convertCommentsDoToDTo] : INFO  " + originalDateInDB.trim().substring(0, 10) + "   "
					+ originalTimeInDB.trim() + "   " + originalDateEntered);

			logger.error("EnteredDate--" + originalDateEntered);

		} catch (ParseException e) {
			logger.error("[convertCommentsDoToDTo] : ERROR- Exception while Converting from String to Date " + e);
			throw e;
		}

		commentsTbDto.setEnteredDate(originalDateEntered);
		commentsTbDto.setComments(comments);

		logger.error("[convertCommentsDoToDTo] : INFO commentsTbDto " + commentsTbDto);
		return commentsTbDto;
	}

}
