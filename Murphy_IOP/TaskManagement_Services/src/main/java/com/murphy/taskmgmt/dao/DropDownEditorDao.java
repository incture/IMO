package com.murphy.taskmgmt.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.DropDownDataDeleteLogDto;
import com.murphy.taskmgmt.dto.DropDownDataDto;
import com.murphy.taskmgmt.dto.DropDownEditorDto;
import com.murphy.taskmgmt.dto.DropDownEditorRequestDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.DropDownEditorDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("DropDownEditorDao")
@Transactional
public class DropDownEditorDao extends BaseDao<DropDownEditorDo, DropDownEditorDto> {

	@Autowired
	private SessionFactory sessionFactory;

	private Session session;

	private Transaction transaction;

	private static final Logger logger = LoggerFactory.getLogger(DropDownEditorDao.class);

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][DropDownEditorDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	@Override
	protected DropDownEditorDo importDto(DropDownEditorDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		DropDownEditorDo entity = new DropDownEditorDo();
		entity.setValueId(fromDto.getValueId());
		entity.setItemId(fromDto.getItemId());
		entity.setAttrValue(fromDto.getAttrValue());
		entity.setDependentValue(fromDto.getDependentValue());
		entity.setEstResolveTime(fromDto.getEstResolveTime());
		return entity;
	}

	@Override
	protected DropDownEditorDto exportDto(DropDownEditorDo entity) {
		DropDownEditorDto toDto = new DropDownEditorDto();
		toDto.setValueId(entity.getValueId());
		toDto.setItemId(entity.getItemId());
		toDto.setAttrValue(entity.getAttrValue());
		toDto.setDependentValue(entity.getDependentValue());
		toDto.setEstResolveTime(entity.getEstResolveTime());
		return toDto;
	}

	@SuppressWarnings("unchecked")
	public List<DropDownDataDto> getClassificationAndSubClassification() {

		List<DropDownDataDto> dbDataList = new ArrayList<DropDownDataDto>();
		DropDownDataDto dbData = null;

		try {
			String queryString = "SELECT ATTR_VALUE,DEPENDENT_VALUE,VALUE_ID FROM TM_ATTR_VALUES WHERE ITEM_ID='123456'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					dbData = new DropDownDataDto();
					if (ServicesUtil.isEmpty(obj[1])) {
						dbData.setClassification((String) obj[0]);
					} else {
						dbData.setClassification((String) obj[1]);
						dbData.setSubclassification((String) obj[0]);
					}
					if (!ServicesUtil.isEmpty(obj[2]))
						dbData.setValueId((String) obj[2]);
					dbDataList.add(dbData);

				}
			}
			logger.error(
					"[Murphy][DropDownEditorDao][getClassificationAndSubClassification][dbDataList] " + dbDataList);
			return dbDataList;

		} catch (Exception e) {
			logger.error(
					"[Murphy][DropDownEditorDao][getClassificationAndSubClassification][Exception] " + e.getMessage());

		}
		return dbDataList;
	}

	/*
	 * public ResponseMessage dropDownManipulate(DropDownEditorRequestDto
	 * base64) {
	 * 
	 * List<DropDownDataDto> ExcelDataList; List<DropDownDataDto> dbDataList;
	 * InputStream inputStream = new
	 * ByteArrayInputStream(Base64.getDecoder().decode(base64.getBase64().
	 * getBytes())); ExcelDataList = readExcel1(inputStream); dbDataList =
	 * getDbData(); String status; ResponseMessage response = new
	 * ResponseMessage(); int i = 0; boolean isInsert1 = true; boolean isInsert2
	 * = true;
	 * 
	 * for (int j = 0; j < ExcelDataList.size(); j++) { if (i ==
	 * dbDataList.size() && ExcelDataList.get(j).getClassification() != null) {
	 * for (int l = 0; l < dbDataList.size(); l++) {
	 * 
	 * if (ExcelDataList.get(j).getSubclassification() != null &&
	 * dbDataList.get(l).getClassification() != null &&
	 * dbDataList.get(l).getSubclassification() != null &&
	 * ExcelDataList.get(j).getClassification()
	 * .equalsIgnoreCase(dbDataList.get(l).getClassification()) &&
	 * ExcelDataList.get(j).getSubclassification()
	 * .equalsIgnoreCase(dbDataList.get(l).getSubclassification())) { isInsert1
	 * = false; break; } if (ExcelDataList.get(j).getSubclassification() == null
	 * && dbDataList.get(l).getClassification() != null &&
	 * dbDataList.get(l).getSubclassification() == null && ExcelDataList.get(j)
	 * .getClassification().equalsIgnoreCase(dbDataList.get(l).getClassification
	 * ())) { isInsert1 = false; break; } } if (isInsert1) { status =
	 * insertData(ExcelDataList.get(j));
	 * 
	 * if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("Data Inserted Successfully");
	 * 
	 * } else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE);
	 * response.setMessage("Data Insertion Failure");
	 * 
	 * }
	 * 
	 * } else { response.setStatus(MurphyConstant.SUCCESS);
	 * response.setStatus(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("Data Already exists");
	 * 
	 * } } logger.
	 * error("[MURPHY][DropDownDaoEditor][dropDownManipulate][Excel Data] = " +
	 * ExcelDataList.get(j).getClassification() + "subclassification =" +
	 * ExcelDataList.get(j).getSubclassification()); for (int k = i; k <
	 * dbDataList.size(); k++) { logger.
	 * error("[MURPHY][DropDownDaoEditor][dropDownManipulate][Database data] = "
	 * + dbDataList.get(k).getClassification() + "subclassification =" +
	 * dbDataList.get(k).getSubclassification()); if
	 * (dbDataList.get(k).getClassification() != null &&
	 * ExcelDataList.get(j).getClassification() != null &&
	 * ExcelDataList.get(j).getClassification()
	 * .equalsIgnoreCase(dbDataList.get(k).getClassification())) {
	 * 
	 * if ((!(ExcelDataList.get(j).getSubclassification() == null &&
	 * dbDataList.get(k).getSubclassification() != null)) &&
	 * ((ExcelDataList.get(j).getSubclassification() == null &&
	 * dbDataList.get(k).getSubclassification() == null)
	 * 
	 * || (ExcelDataList.get(j).getSubclassification()
	 * .equalsIgnoreCase(dbDataList.get(k).getSubclassification())))) {
	 * 
	 * i++;
	 * 
	 * break;
	 * 
	 * } else if (ExcelDataList.get(j).getSubclassification() != null &&
	 * !ExcelDataList.get(j)
	 * .getSubclassification().equalsIgnoreCase(dbDataList.get(k).
	 * getSubclassification())) { /// update in db i++;
	 * 
	 * if (dbDataList.get(k).getItemId().equalsIgnoreCase("12345")) { for (int l
	 * = 0; l < dbDataList.size(); l++) { logger.error(
	 * "[MURPHY][DropDownEditorDao][dropDownManipulate][entering insertion loop to check for failure]"
	 * );
	 * 
	 * if (dbDataList.get(l).getClassification() != null &&
	 * dbDataList.get(l).getSubclassification() != null &&
	 * ExcelDataList.get(j).getClassification()
	 * .equalsIgnoreCase(dbDataList.get(l).getClassification()) &&
	 * ExcelDataList.get(j).getSubclassification()
	 * .equalsIgnoreCase(dbDataList.get(l).getSubclassification())) {
	 * 
	 * logger.error(
	 * "[MURPHY][DropDownEditorDao][dropDownManipulate][insert fails if already exists]"
	 * );
	 * 
	 * isInsert2 = false; break; }
	 * 
	 * } if (isInsert2) {
	 * 
	 * logger.error(
	 * "[MURPHY][DropDownEditorDao][dropDownManipulate][entering insertion loop to check for success] dbDataList.get(k).getItemId()------>"
	 * + dbDataList.get(k).getItemId());
	 * 
	 * status = insertData(ExcelDataList.get(j)); if
	 * (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("SubClassification Addition Successful"); break; }
	 * else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE);
	 * response.setMessage("SubClassification Addition Failure"); break; } }
	 * else { response.setStatus(MurphyConstant.SUCCESS);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("Data Already Exists"); break; } } else if
	 * (dbDataList.get(k).getItemId().equalsIgnoreCase("123456")) {
	 * 
	 * status = updateOrDelete(ExcelDataList.get(j).getSubclassification(),
	 * dbDataList.get(k).getValueId(), true, false, false,null);
	 * System.err.println(status); if
	 * (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("SubClassisfication Updated Successfully"); break; }
	 * else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE);
	 * response.setMessage("SubClassisfication Updation Failure"); break; }
	 * 
	 * } } else if (dbDataList.get(k).getSubclassification() != null &&
	 * ExcelDataList.get(j).getSubclassification() == null)
	 * 
	 * { // delete subclassifiaction i++; status =
	 * updateOrDelete(dbDataList.get(k).getSubclassification(),
	 * dbDataList.get(k).getValueId(), false, true, false,null); if
	 * (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("SubClassisfication Deleted Successfully"); break; }
	 * else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE);
	 * response.setMessage("SubClassisfication Deletion Failure"); break; } }
	 * 
	 * } else if ((dbDataList.get(k).getSubclassification() == null &&
	 * ExcelDataList.get(j).getSubclassification() == null &&
	 * ExcelDataList.get(j).getClassification() == null) ||
	 * (dbDataList.get(k).getSubclassification() == null &&
	 * ExcelDataList.get(j).getSubclassification() == null &&
	 * !ExcelDataList.get(j)
	 * .getClassification().equalsIgnoreCase(dbDataList.get(k).getClassification
	 * ()))) {
	 * 
	 * if (ExcelDataList.get(j).getClassification() != null) { // Update
	 * calssifiactuon i++; status =
	 * updateOrDelete(ExcelDataList.get(j).getClassification(),
	 * dbDataList.get(k).getValueId(), true, false,
	 * true,dbDataList.get(k).getClassification()); if
	 * (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("Classisfication Updated Successfully"); break; }
	 * else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE);
	 * response.setMessage("Classisfication Updation Failure"); break; } } else
	 * if (ExcelDataList.get(j).getClassification() == null) { // delete
	 * classification with all subclassifiaction i++; status =
	 * updateOrDelete(dbDataList.get(k).getClassification(),
	 * dbDataList.get(k).getValueId(), false, true, true,null);
	 * 
	 * if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("Classisfication Deleted Successfully"); break; }
	 * else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE);
	 * response.setMessage("Classisfication Deletion Failure"); break; } }
	 * 
	 * } else if(ExcelDataList.get(j).getClassification() == null &&
	 * dbDataList.get(k).getClassification() != null &&
	 * ExcelDataList.get(j).getSubclassification() == null) { if
	 * (ExcelDataList.get(j).getClassification() == null &&
	 * dbDataList.get(k).getClassification() != null &&
	 * ExcelDataList.get(j).getSubclassification() == null &&
	 * (dbDataList.get(k).getSubclassification() != null ||
	 * dbDataList.get(k).getSubclassification() == null))
	 * 
	 * //{ if (ExcelDataList.get(j).getClassification() == null &&
	 * dbDataList.get(k).getSubclassification() == null) { // delete
	 * classification with all subclassification i++; status =
	 * updateOrDelete(dbDataList.get(k).getClassification(),
	 * dbDataList.get(k).getValueId(), false, true, true,null);
	 * 
	 * if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("Classisfication Deleted Successfully"); break; }
	 * else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE);
	 * response.setMessage("Classisfication Deletion Failure"); break; } }
	 * if(dbDataList.get(k).getSubclassification() != null){ i++; status =
	 * updateOrDelete(dbDataList.get(k).getClassification(),
	 * dbDataList.get(k).getValueId(), false, true, false,null); if
	 * (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
	 * response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS); response.
	 * setMessage("Classisfication and SubClassisfication Pair Deleted Successfully"
	 * ); break; } else { response.setStatus(status);
	 * response.setStatusCode(MurphyConstant.CODE_FAILURE); response.
	 * setMessage("Classisfication and SubClassisfication Pair Deletion Failure"
	 * ); break; } }
	 * 
	 * //}
	 * 
	 * }else { response.setStatus(MurphyConstant.INVALID);
	 * response.setStatusCode(MurphyConstant.CODE_SUCCESS);
	 * response.setMessage("Invalid excel manipulation"); break; } }
	 * 
	 * } return response;
	 * 
	 * }
	 * 
	 * 
	 */
	public List<DropDownDataDto> getClassification() {

		List<DropDownDataDto> excelDataList = new ArrayList<DropDownDataDto>();
		DropDownDataDto excelData = null;
		try {

			//String queryString = "SELECT ATTR_VALUE,DEPENDENT_VALUE,VALUE_ID FROM TM_ATTR_VALUES WHERE ITEM_ID='12345'";
			String queryString = "SELECT ATTR_VALUE,VALUE_ID FROM TM_ATTR_VALUES WHERE ITEM_ID='12345'";
			Query query = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					excelData = new DropDownDataDto();
					/*if (ServicesUtil.isEmpty(obj[1].toString())) {
						excelData.setClassification((String) obj[0]);

					} else {
						excelData.setClassification((String) obj[1]);

					}
					if (!ServicesUtil.isEmpty(obj[2])){
						excelData.setValueId((String) obj[2]);
					}*/
					excelData.setClassification(obj[0].toString());
					excelData.setValueId(obj[1].toString());
					excelDataList.add(excelData);
				}
			}
			return excelDataList;

		} catch (Exception e) {
			logger.error("[MURPHY][Test][getClassification][Exception]" + e.getMessage());

		}
		return excelDataList;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getDatabaseDataForClassifications() {

		Map<String, String> dbDataMap = new HashMap<String, String>();

		try {

			List<Object[]> resultList = this.getSession()
					.createSQLQuery("Select VALUE_ID,ATTR_VALUE  from TM_ATTR_VALUES where ITEM_ID='12345'").list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {

					if (!ServicesUtil.isEmpty(obj[0]) && !ServicesUtil.isEmpty(obj[1]))
						dbDataMap.put((String) obj[0], (String) (obj[1]));
				}

			}

			return dbDataMap;

		} catch (Exception e) {
			logger.error("[MURPHY][DropDownDao][getDatabaseDataForClassifications][Exception]" + e.getMessage());
		}
		return dbDataMap;

	}

	@SuppressWarnings("unused")
	private static ResponseMessage readExcel(InputStream inputStream,Map<String, String> excelDataMap,Set<String> headersSet,ResponseMessage response) {
		//Map<String, String> excelDataMap = new HashMap<String, String>();
		String valueId = null;
		String classification = null;
		FileInputStream fis = null;
		int insertRowsNum = 1;
		try {
			// fis = new FileInputStream(filename);
			XSSFWorkbook workbookRead = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbookRead.getSheetAt(0);

			Set<String> excelHeaderSet=new HashSet<>();
			// for excel mismatch check SOC
						for (int i = 0; i < 1; i++) {
							Row row = sheet.getRow(i);
							for (int j = 0; j < row.getLastCellNum(); j++) {
								excelHeaderSet.add(row.getCell(j).getStringCellValue());
								}
							}
						
						if(excelHeaderSet.size()==headersSet.size() && excelHeaderSet.containsAll(headersSet) && headersSet.containsAll(excelHeaderSet)){
							
						
						// EOC
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				boolean insertIntoMap = true;
				Row row = sheet.getRow(i);
				if (row.getCell(0) != null)
					classification = row.getCell(0).getStringCellValue();

				if (row.getCell(1) != null && row.getCell(1).getStringCellValue() != "")
					valueId = row.getCell(1).getStringCellValue();

				if (row.getCell(1) == null || row.getCell(1).getStringCellValue() == "") {
					// System.err.println("row num1 = " +insertRowsNum);
					String insertRow = "insert" + insertRowsNum;
					// System.err.println("row num2 = " +insertRowsNum);
					excelDataMap.put(insertRow, classification);

					logger.error("insertred rows valueIds = " + insertRow);
					insertRowsNum++;
					insertIntoMap = false;

				}
				// System.err.println("row num3 = " +insertRowsNum);

				if (insertIntoMap) {
					excelDataMap.put(valueId, classification);
					// System.err.println("row num4 = " +insertRowsNum);
				}
				logger.error("string value of num =" + String.valueOf(insertRowsNum - 1));
				if (i == sheet.getLastRowNum()) {
					excelDataMap.put("insertRowNum", String.valueOf(insertRowsNum - 1));

					logger.error("excelDataMap1 =" + excelDataMap);
				}
			}
			System.err.println("excelDataMap2 =" + excelDataMap);
						}
						else{
							response.setMessage("Sub Module and Upload File mismatch");
							response.setStatus(MurphyConstant.FAILURE);
							response.setStatusCode(MurphyConstant.CODE_FAILURE);
						}
			return response;
		} catch (Exception e) {
			logger.error("[MURPHY][DropdownEditorDao][readExcel][Exception]" + e.getMessage());

		}

		return response;
	}

	@SuppressWarnings("unused")
	private String updateordelete(String classification, String valueId, String dbValue, boolean isUpdate,
			boolean isDelete) {
		String query = null;

		try {

			if (isUpdate) {
				query = "UPDATE TM_ATTR_VALUES SET ATTR_VALUE = (case when ATTR_VALUE='" + dbValue + "' then '"
						+ classification + "' else ATTR_VALUE end),DEPENDENT_VALUE = (case when DEPENDENT_VALUE='"
						+ dbValue + "' then '" + classification + "' else DEPENDENT_VALUE end)";
			}

			if (isDelete) {
//				
				List<DropDownDataDto> dataList = new ArrayList<DropDownDataDto>();
				DropDownDataDto data = null;
				
				String queryString="SELECT ATTR_VALUE,DEPENDENT_VALUE,VALUE_ID FROM TM_ATTR_VALUES WHERE DEPENDENT_VALUE='"
								+classification+ "'";
				Query queryFetch=this.getSession().createSQLQuery(queryString);
				List<Object[]>resultList1=queryFetch.list();
				if (resultList1 != null) {
					for (Object[] obj : resultList1) {

						data = new DropDownDataDto();
						if (obj[1] != null && obj[0] != null){
							data.setClassification((String) obj[1]);
						data.setSubclassification((String) obj[0]);
						data.setValueId((String) obj[2]);
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
						data.setDeleteAt(calendar.getTime());
						}
						dataList.add(data);
					}
					logger.error("size going to insert method = "+dataList.size());
					insertIntoClassLog(dataList);
				
					

				}
					query=	"DELETE FROM TM_ATTR_VALUES WHERE DEPENDENT_VALUE ='" + classification
						+ "' OR ATTR_VALUE ='" + classification + "'";

			}
			int resultList = this.getSession().createSQLQuery(query).executeUpdate();
			if (resultList > 0) {

				return MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[MURPHY][DropDownEditorDao][updateordelete][Exception]" + e.getMessage());

			return MurphyConstant.FAILURE;
		}

		return null;

	}

	private String insertClassification(String attrValue) {
		try {
			String valueId = getValueId();
			String actionFlag="ACTIVE";
			String query = "INSERT INTO TM_ATTR_VALUES (VALUE_ID,ITEM_ID,ATTR_VALUE,DEPENDENT_VALUE) VALUES ('" + valueId
					+ "','12345','" + attrValue + "','')";
			int resultList = this.getSession().createSQLQuery(query).executeUpdate();
			if (resultList > 0) {

				return MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[MURPHY][DropDownEditorDao][insertClassification][Exception]" + e.getMessage());
			return MurphyConstant.FAILURE;
		}

		return null;
	}

	public ResponseMessage dropDownClassificationManipulate(DropDownEditorRequestDto base64) {
		Map<String, String> ExcelDataMap=new HashMap<>();
		Map<String, String> dbDataMap;

		InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64.getBase64().getBytes()));
		/*File file=new File("C:\\Users\\Ayesha.Syed\\Desktop\\ClassificationReport.xlsx");
		FileInputStream inputStream=null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Set<String> headersSet=new HashSet<>();
		headersSet.add("Classification");
		headersSet.add("ValueId");
		ResponseMessage response = new ResponseMessage();
		dbDataMap = getDatabaseDataForClassifications();
		//ExcelDataMap = readExcel(inputStream,ExcelDataMap,headersList,response);
		 readExcel(inputStream,ExcelDataMap,headersSet,response);
		String status;
		String insertClassification;

		if(ServicesUtil.isEmpty(response.getMessage())){

		for (Map.Entry<String, String> entry : dbDataMap.entrySet()) {

			if (ExcelDataMap.containsKey(entry.getKey())) {

				if (ExcelDataMap.get(entry.getKey()).equalsIgnoreCase(entry.getValue())) {
					status = "data matched";
				} else {
					// update
					status = updateordelete(ExcelDataMap.get(entry.getKey()), entry.getKey(), entry.getValue(), true,
							false);
					if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
						response.setMessage("success updating");
						response.setStatusCode(MurphyConstant.CODE_SUCCESS);
						response.setStatus(status);

					} else {
						response.setMessage("failure");
						response.setStatus(status);
						response.setStatusCode(MurphyConstant.CODE_FAILURE);
					}
				}
			} else {
				// delete
				status = updateordelete(entry.getValue(), entry.getKey(), null, false, true);
				if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
					response.setMessage("success deletion");
					response.setStatusCode(MurphyConstant.CODE_SUCCESS);
					response.setStatus(status);

				} else {
					response.setMessage("failure deletion");
					response.setStatusCode(MurphyConstant.CODE_FAILURE);
					response.setStatus(status);
				}
			}

		}
		logger.error("string value =" + ExcelDataMap.get("insertRowNum"));

		//logger.error("int value of insertrows =" + Integer.parseInt(ExcelDataMap.get("insertRowNum")));
if(ExcelDataMap.get("insertRowNum")!=null){
		int insertRowNum = Integer.parseInt(ExcelDataMap.get("insertRowNum"));

		for (int j = 1; j <= insertRowNum; j++) {
			// insert
			insertClassification = ExcelDataMap.get("insert" + j);
			status = insertClassification(insertClassification);
			if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
				response.setMessage("success inserting");
				response.setStatusCode(MurphyConstant.CODE_SUCCESS);

				response.setStatus(status);
			} else {
				response.setMessage("failure inserting");
				response.setStatusCode(MurphyConstant.CODE_FAILURE);
				response.setStatus(status);

			}
			logger.error("inserted valueId =" + insertClassification);

		}
}
		}

		return response;

	}

	private String updateOrDelete(String tobeUpdatedOrDeleted, String valueId, boolean isUpdate, boolean isDelete,
			boolean isClassification, String dbValue) {
		try {

			String query = null;
			String query1=null;
			if (isUpdate && isClassification) {
				/*
				 * query = "UPDATE TM_ATTR_VALUES SET DEPENDENT_VALUE ='" +
				 * tobeUpdatedOrDeleted + "' WHERE VALUE_ID ='" + valueId + "'";
				 */

				query = "update TM_ATTR_VALUES set ATTR_VALUE = (case when ATTR_VALUE='" + dbValue + "' then '"
						+ tobeUpdatedOrDeleted + "' else ATTR_VALUE end),DEPENDENT_VALUE = (case when DEPENDENT_VALUE='"
						+ dbValue + "' then '" + tobeUpdatedOrDeleted + "' else DEPENDENT_VALUE end)";
				logger.error("[MURPHY][DropDownDaoEditor][updateOrDelete][updating classification] " + query);
			}
			if (isUpdate && !isClassification) {
				query = "UPDATE TM_ATTR_VALUES SET ATTR_VALUE ='" + tobeUpdatedOrDeleted + "' WHERE VALUE_ID ='"
						+ valueId + "'";
				logger.error("[MURPHY][DropDownDaoEditor][updateOrDelete][updating subclassification] " + query);
			}

			if (isDelete && !isClassification) {
				//insertIntoClassLog();
				//query = " UPDATE TM_ATTR_VALUES SET ACTIVE_FLAG='DELETE' WHERE VALUE_ID ='" + valueId + "'";
				DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				query1 = "INSERT INTO TM_CLASS_SUBCLASS_DELETE_LOG  VALUES('" + valueId + "','"
						+ dbValue+ "','" + tobeUpdatedOrDeleted + "','"+sdf.format(cal.getTime())+"')";
			this.getSession().createSQLQuery(query1).executeUpdate();
			

				//query = "DELETE FROM TM_ATTR_VALUES WHERE VALUE_ID='" + valueId + "'";
				query = "DELETE FROM TM_ATTR_VALUES WHERE VALUE_ID ='" + valueId + "'";
				logger.error("[MURPHY][DropDownDaoEditor][updateOrDelete][deleting subclassification] " + query);
			}

			if (isDelete && isClassification) {
//				query = "UPDATE TM_ATTR_VALUES SET ACTIVE_FLAG='DELETE' WHERE VALUE_ID ='"
//						+ valueId + "'  OR DEPENDENT_VALUE='"+tobeUpdatedOrDeleted+"'";
				List<DropDownDataDto> dataList = new ArrayList<DropDownDataDto>();
				DropDownDataDto data = null;
				/*List<Object[]> resultList1 = s
						.createQuery("SELECT ATTR_VALUE,DEPENDENT_VALUE FROM TM_ATTR_VALUES WHERE DEPENDENT_VALUE='"
								+ tobeUpdatedOrDeleted + "'")
						.list();*/
				String queryString="SELECT ATTR_VALUE,DEPENDENT_VALUE,VALUE_ID FROM TM_ATTR_VALUES WHERE DEPENDENT_VALUE='"
								+ tobeUpdatedOrDeleted + "'";
				Query queryFetch=this.getSession().createSQLQuery(queryString);
				List<Object[]>resultList1=queryFetch.list();
				if (resultList1 != null) {
					for (Object[] obj : resultList1) {

						data = new DropDownDataDto();
						if (obj[1] != null && obj[0] != null){
							data.setClassification((String) obj[1]);
						data.setSubclassification((String) obj[0]);
						data.setValueId((String) obj[2]);
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
						data.setDeleteAt(calendar.getTime());
						}
						dataList.add(data);
					}
					logger.error("size going to insert method = "+dataList.size());
					insertIntoClassLog(dataList);
				
					

				}
				
				//query = "DELETE FROM TM_ATTR_VALUES WHERE DEPENDENT_VALUE ='" + tobeUpdatedOrDeleted
						//+ "' OR ATTR_VALUE ='" + tobeUpdatedOrDeleted + "'";
				query="DELETE FROM TM_ATTR_VALUES WHERE DEPENDENT_VALUE ='" + tobeUpdatedOrDeleted
						+ "' OR ATTR_VALUE ='" + tobeUpdatedOrDeleted + "'";

				logger.error("[MURPHY][DropDownDaoEditor][updateOrDelete][deleting classification] " + query);
			}
			int resultList = this.getSession().createSQLQuery(query).executeUpdate();
			if (resultList > 0) {
				return MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[MURPHY][DropDownEditorDao][updateOrDelete][Exception]" + e.getMessage());
			return MurphyConstant.FAILURE;
		}

		return MurphyConstant.FAILURE;
	}

	private String insertIntoClassLog(List<DropDownDataDto> dataList) throws ExecutionFault, InvalidInputFault {
		/*for (DropDownDataDto dropDownDataDto : dataList) {
			DropDownDataDeleteLogDo entity = new DropDownDataDeleteLogDo();
			entity.setClassification(dropDownDataDto.getClassification());
			entity.setSubClassification(dropDownDataDto.getSubclassification());
			entity.setDeletedAt(dropDownDataDto.getDeleteAt());
			entity.setDeleteId(dropDownDataDto.getValueId());
			DropDownDataDeleteLogDo xyz = (DropDownDataDeleteLogDo) this.getSession().save(entity);
			System.out.println(xyz);
		}*/
		logger.error("size of dataList" + dataList.size());
		try{
		for (DropDownDataDto dropDownDataDto : dataList) {
			logger.error("DropDownDataDto before create" + " " + dropDownDataDto.getClassification() + " "
					+ dropDownDataDto.getSubclassification() + " " + dropDownDataDto.getDeleteAt().toString());
		}
		DropDownDataDeleteLogDao dropDownDataDeleteLogDao=new DropDownDataDeleteLogDao();
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		for(DropDownDataDto dropDownDataDto:dataList){
			DropDownDataDeleteLogDto dropDownDataDeleteLogDto=new DropDownDataDeleteLogDto();
			dropDownDataDeleteLogDto.setClassification(dropDownDataDto.getClassification());
			dropDownDataDeleteLogDto.setSubclassification(dropDownDataDto.getSubclassification());
			//dropDownDataDeleteLogDto.setDeletedAt(new Date());
			dropDownDataDeleteLogDto.setDeleteId(dropDownDataDto.getValueId());
			/*try{
			dropDownDataDeleteLogDao.create(dropDownDataDeleteLogDto);
			}catch (Exception e) {
				System.err.println("exception while creation"+ " "+e.getMessage()+""+e.getStackTrace());			}*/
			//Session session= sessionFactory.openSession();
			
			String workbenchAuditLogUpdateQuery = "INSERT into TM_CLASS_SUBCLASS_DELETE_LOG values('" + dropDownDataDto.getValueId() + "' , '"
					+ dropDownDataDto.getClassification() + "' , '"+dropDownDataDto.getSubclassification()+"' , '"+sdf.format(cal.getTime())+"')";
			this.getSession().createSQLQuery(workbenchAuditLogUpdateQuery).executeUpdate();
			
			
		}
		/*for(int i=0;i<dataList.size();i++)
		{
			String query=null;
			Drop
			
			for (int j = 0; j < dataList.size(); j++) {
				query = "INSERT INTO CLASS_LOG  VALUES('"
						+dataList.get(i).getValueId()+"','"
						+ dataList.get(i).getClassification() + "','" + dataList.get(i).getSubclassification() + "')";
			int resultList= this.getSession().createSQLQuery(query).executeUpdate();
				
			}
		}*/
		}catch(Exception e){
			System.err.println("exception while inserting"+" "+e.getMessage());
		}
		return null;
		
	}

	@SuppressWarnings("unchecked")
	private Map<String, DropDownDataDto> getDbData() {
		Map<String, DropDownDataDto> dbDataMap = new HashMap<String, DropDownDataDto>();
		DropDownDataDto dbData = null;

		try {

			List<Object[]> resultList = this.getSession()
					.createSQLQuery(
							"Select VALUE_ID,ATTR_VALUE,DEPENDENT_VALUE,ITEM_ID from TM_ATTR_VALUES where ITEM_ID='123456' ")
					.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					dbData = new DropDownDataDto();
					if (obj[0] != null)
						dbData.setValueId((String) obj[0]);
					if (obj[1] != null && (obj[2] == null||obj[2].toString().equalsIgnoreCase("")))
						dbData.setClassification((String) obj[1]);
					    //dbData.setSubclassification((String) obj[2]); 
					if (obj[1] != null && (obj[2] != null && !obj[2].toString().equalsIgnoreCase(""))) {
						dbData.setSubclassification((String) obj[1]);
						dbData.setClassification((String) obj[2]);
					}
					if (obj[3] != null)
						dbData.setItemId((String) obj[3]);

					dbDataMap.put((String) obj[0], dbData);
				}
logger.error("dbDataMap = " +dbDataMap);
				return dbDataMap;
			}
		} catch (Exception e) {
			logger.error("[MURPHY][DropDownEditorDao][getDbData][Exception]" + e.getMessage());
		}
		return dbDataMap;

	}

	private static Map<String, DropDownDataDto> readExcel1(InputStream inputStream) {
		Map<String, DropDownDataDto> excelMap = new HashMap<String, DropDownDataDto>();
		DropDownDataDto dto = null;
		FileInputStream fis = null;

		try {

			XSSFWorkbook workbookRead = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbookRead.getSheetAt(0);

			int insertRowsNum = 1;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				dto = new DropDownDataDto();
				Row row = sheet.getRow(i);

				Cell cell = row.getCell(0);
				if (cell != null)
					dto.setClassification(row.getCell(0).getStringCellValue());

				if (row.getCell(1) != null && row.getCell(1).getStringCellValue() != "")
					dto.setSubclassification(row.getCell(1).getStringCellValue());

				if (row.getCell(2) != null) {
					dto.setValueId(row.getCell(2).getStringCellValue());

				}

				/*
				 * if(row.getCell(2) == null &&
				 * row.getCell(2).getStringCellValue()=="")
				 */
				else {
					String insertRow = "insert" + insertRowsNum;
					dto.setValueId(insertRow);
					insertRowsNum++;
				}

				excelMap.put(dto.getValueId(), dto);
				if (i == sheet.getLastRowNum()) {
					DropDownDataDto insertRowDto = new DropDownDataDto();
					insertRowDto.setInsertRows(insertRowsNum - 1);
					excelMap.put("insertRowNum", insertRowDto);
				}
			}
			System.out.println("excelMap =" + excelMap);
			return excelMap;
		} catch (Exception e) {
			logger.error("[MURPHY][DropdownEditorDao][readExcel1][Exception]" + e.getMessage());
		}

		return excelMap;
	}

	public ResponseMessage manipulateAllData(DropDownEditorRequestDto base64) {
		Map<String, DropDownDataDto> ExcelDataMap=new HashMap<>();
		Map<String, DropDownDataDto> dbDataMap;
		InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64.getBase64().getBytes()));
		/*File file=new File("C:\\Users\\Ayesha.Syed\\Desktop\\DropDownDataReport.xlsx");
		FileInputStream inputStream=null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		
		
		Set<String> headersSet=new HashSet<>();
		headersSet.add("Classification");
		headersSet.add("SubClassification");
		headersSet.add("ValueId");
		dbDataMap = getDbData();
		ResponseMessage response = new ResponseMessage();
		//ExcelDataMap = readExcelData(inputStream,headersList);
		 readExcelData(inputStream,ExcelDataMap,headersSet,response);
		 if(ServicesUtil.isEmpty(response.getMessage())){
			 
		String status = null;
		int i = 0;

		DropDownDataDto dbDto;
		DropDownDataDto excelDto;
		boolean isUpdate = true;
		boolean isInsert1 = true;
		boolean isInsert2 = true;
		
		for (Map.Entry<String, DropDownDataDto> entry : dbDataMap.entrySet()) {
			logger.error("valueid from the db =" + entry.getKey());
			if (ExcelDataMap.containsKey(entry.getKey())) {
				logger.error("valuid matched =" + entry.getKey());
				dbDto = new DropDownDataDto();
				excelDto = new DropDownDataDto();
				excelDto = ExcelDataMap.get(entry.getKey());
				dbDto = entry.getValue();
				if (excelDto.getClassification() != null && dbDto.getClassification() != null
						&& excelDto.getClassification().equalsIgnoreCase(dbDto.getClassification())
						&& ((excelDto.getSubclassification() != null && dbDto.getSubclassification() != null
								&& excelDto.getSubclassification().equalsIgnoreCase(dbDto.getSubclassification()))
								|| (excelDto.getSubclassification() == null && dbDto.getSubclassification() == null))) {
					logger.error("valuid matched same =" + entry.getKey());
					status = "data matched";
				} else {
					// update
					/*if (excelDto.getClassification() != null && (excelDto.getSubclassification() == null ||excelDto.getSubclassification().equalsIgnoreCase(""))
							&& dbDto.getItemId().equalsIgnoreCase("12345")
							&& !excelDto.getClassification().equalsIgnoreCase(dbDto.getClassification()))
						status = updateOrDelete(excelDto.getClassification(), entry.getKey(), true, false, true,
								dbDto.getClassification());
*/
					if (excelDto.getClassification() != null && excelDto.getSubclassification() != null
							&& dbDto.getItemId().equalsIgnoreCase("123456")
							&& excelDto.getClassification().equalsIgnoreCase(dbDto.getClassification())
							&& !excelDto.getSubclassification().equalsIgnoreCase(dbDto.getSubclassification()))
						status = updateOrDelete(excelDto.getSubclassification(), entry.getKey(), true, false, false,
								null);
					if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
						response.setMessage("Success updating");
						response.setStatusCode(MurphyConstant.CODE_SUCCESS);
						response.setStatus(status);
					} else {
						response.setMessage("Failure updating");
						response.setStatusCode(MurphyConstant.CODE_FAILURE);
						response.setStatus(status);
					}
				}
			} else {
				// delete
				logger.error("valuid didn't matched =" + entry.getKey());
				if (entry.getValue().getSubclassification() != null && !entry.getValue().getSubclassification().equalsIgnoreCase(""))
					status = updateOrDelete(entry.getValue().getSubclassification(), entry.getKey(), false, true, false,
							entry.getValue().getClassification());
				/*if (entry.getValue().getSubclassification() == null||entry.getValue().getSubclassification().equalsIgnoreCase(""))
					status = updateOrDelete(entry.getValue().getClassification(), entry.getKey(), false, true, true,
							null);*/
				if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
					response.setMessage("Data removed successfully");
					response.setStatusCode(MurphyConstant.CODE_SUCCESS);
					response.setStatus(status);
				} else {
					response.setMessage("Error occured while deletion");
					response.setStatusCode(MurphyConstant.CODE_FAILURE);
					response.setStatus(status);
				}
			}

		}

		// insert
	
		System.err.println("val= " + ExcelDataMap.get("insertRowNum").getInsertRows());
		logger.error("inerted rows =" + ExcelDataMap.get("insertRowNum").getInsertRows());
		int insertRowNum = ExcelDataMap.get("insertRowNum").getInsertRows();
		for (int j = 1; j <= insertRowNum; j++) {
			status = insertData(ExcelDataMap.get("insert" + j));
			if (status.equalsIgnoreCase(MurphyConstant.SUCCESS)) {
				response.setMessage("Data inserted successfully");
				response.setStatusCode(MurphyConstant.CODE_SUCCESS);
				response.setStatus(status);
			} else {
				response.setMessage("Failure inserting");
				response.setStatusCode(MurphyConstant.CODE_FAILURE);
				response.setStatus(status);
			}
		}
	
	}
		return response;
	}

	private static ResponseMessage readExcelData(InputStream inputStream,Map<String, DropDownDataDto> excelMap,Set<String> headersList,ResponseMessage response) {
		//Map<String, DropDownDataDto> excelMap = new HashMap<String, DropDownDataDto>();
		DropDownDataDto dto = null;
		FileInputStream fis = null;
		// InputStream inputStream

		try {

			XSSFWorkbook workbookRead = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbookRead.getSheetAt(0);
			
			Set<String> excelHeaderList=new HashSet<>();
			int insertRowsNum = 1;
			// for excel mismatch check SOC
			for (int i = 0; i < 1; i++) {
				Row row = sheet.getRow(i);
				for (int j = 0; j < row.getLastCellNum(); j++) {
					excelHeaderList.add(row.getCell(j).getStringCellValue());
					}
				}
			
			if(headersList.size()==excelHeaderList.size() && excelHeaderList.containsAll(headersList) && headersList.containsAll(excelHeaderList)){
				
			
			// EOC
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				dto = new DropDownDataDto();
				Row row = sheet.getRow(i);

				Cell cell = row.getCell(0);
				if (cell != null)
					dto.setClassification(row.getCell(0).getStringCellValue());

				if (row.getCell(1) != null)
					dto.setSubclassification(row.getCell(1).getStringCellValue());

				if (row.getCell(2) != null && row.getCell(2).getStringCellValue() != "") {
					dto.setValueId(row.getCell(2).getStringCellValue());
				}

				logger.error("second row element =" + row.getCell(2));
				if (row.getCell(2) == null || row.getCell(2).getStringCellValue() == "") {
					String insertRow = "insert" + insertRowsNum;
					dto.setValueId(insertRow);
					logger.error("inserted valueid =" + dto.getValueId());
					insertRowsNum++;

				}

				logger.error("insertRowsNum  outside if =" + insertRowsNum);
				logger.error("value id for each map item =" + dto.getValueId());
				excelMap.put(dto.getValueId(), dto);
               logger.error("Sheet last row number = " +sheet.getLastRowNum());
                logger.error("value of i = "+i);


				if (!excelMap.containsKey("insert" + insertRowsNum)) {
					logger.error("sheet last row num =" + sheet.getLastRowNum());
					logger.error("insertRowsNum =" + insertRowsNum);
					DropDownDataDto insertRowDto = new DropDownDataDto();
					insertRowDto.setInsertRows(insertRowsNum - 1);
					excelMap.put("insertRowNum", insertRowDto);
				}
			}
			}else{
				response.setMessage("Headers are not matching. Please upload valid excel");
				response.setStatus(MurphyConstant.FAILURE);
				response.setStatusCode(MurphyConstant.CODE_FAILURE);
			}
			return response;
		} catch (Exception e) {
			logger.error("[MURPHY][DropdownDao][readExcelData][Exception]" + e.getMessage());
		}

		return response;
	}

	private String insertData(DropDownDataDto dropDownDataDto) {

		try {
			String query = null;
			String itemId = null;
			String valueId = null;
			String attrValue = null;
			String dependentValue = null;
			String actionFlag=null;
			valueId = getValueId();
			if (dropDownDataDto.getClassification() != null && dropDownDataDto.getSubclassification() == null) {
				itemId = "12345";
				actionFlag="ACTIVE";
				attrValue = dropDownDataDto.getClassification();
				query = "INSERT INTO TM_ATTR_VALUES (VALUE_ID,ITEM_ID,ATTR_VALUE,DEPENDENT_VALUE) VALUES ('" + valueId + "','" + itemId
						+ "','" + attrValue + "','')";
			} else if (dropDownDataDto.getClassification() != null && dropDownDataDto.getSubclassification() != null) {
				itemId = "123456";
				actionFlag="ACTIVE";
				attrValue = dropDownDataDto.getSubclassification();
				dependentValue = dropDownDataDto.getClassification();
				query = "INSERT INTO TM_ATTR_VALUES (VALUE_ID,ITEM_ID,ATTR_VALUE,DEPENDENT_VALUE) VALUES ('" + valueId
						+ "','" + itemId + "','" + attrValue + "','" + dependentValue + "')";
			}

			/*
			 * valueId = getValueId(); System.err.println("valueId =" +
			 * valueId);
			 */
			/*
			 * DropDownEditorDto dropDownEditorDto = new DropDownEditorDto();
			 * dropDownEditorDto.setItemId(itemId);
			 * dropDownEditorDto.setValueId(valueId);
			 * dropDownEditorDto.setAttrValue(attrValue);
			 * dropDownEditorDto.setDependentValue(dependentValue);
			 * create(dropDownEditorDto);
			 * 
			 */
			/*
			 * query =
			 * "INSERT INTO TM_ATTR_VALUES (VALUE_ID,ITEM_ID,ATTR_VALUE,DEPENDENT_VALUE) VALUES ('"
			 * + valueId + "','" + itemId + "','" + attrValue + "','" +
			 * dependentValue + "')";
			 */
			int resultList = this.getSession().createSQLQuery(query).executeUpdate();

			if (resultList > 0) {
				return MurphyConstant.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[MURPHY][DropDownEditorDao][insertData][Exception]" + e.getMessage());
			return MurphyConstant.FAILURE;
		}
		return null;

	}

	private String getValueId() {
		try {

			String queryString = "select max(CAST(VALUE_ID AS INT)) from TM_ATTR_VALUES where ITEM_ID='12345' OR ITEM_ID='123456'";
			Query query = this.getSession().createSQLQuery(queryString);
			int result = (Integer) query.uniqueResult();

			int value = result + 1;

			return String.valueOf(value);

		} catch (Exception e) {
			logger.error("[MURPHY][DropDownEditorDao][getValueId][Exception]" + e.getMessage());

		}
		return null;
	}

	/*
	 * private List<DropDownDataDto> getDbData() { List<DropDownDataDto>
	 * dbDataList = new ArrayList<DropDownDataDto>(); DropDownDataDto dbData =
	 * null;
	 * 
	 * try {
	 * 
	 * String getDbDataQuery =
	 * "Select VALUE_ID,ATTR_VALUE,DEPENDENT_VALUE,ITEM_ID from TM_ATTR_VALUES WHERE ITEM_ID='12345'OR ITEM_ID='123456'"
	 * ;
	 * 
	 * Query q = this.getSession().createSQLQuery(getDbDataQuery);
	 * List<Object[]> resultList = q.list();
	 * 
	 * if (!ServicesUtil.isEmpty(resultList)) { for (Object[] obj : resultList)
	 * { dbData = new DropDownDataDto();
	 * 
	 * dbData.setValueId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
	 * 
	 * if (!ServicesUtil.isEmpty(obj[1]) && ServicesUtil.isEmpty(obj[2]))
	 * dbData.setClassification((String) obj[1]);
	 * 
	 * if (!ServicesUtil.isEmpty(obj[1]) && !ServicesUtil.isEmpty(obj[2])) {
	 * dbData.setSubclassification((String) obj[1]);
	 * 
	 * dbData.setClassification((String) obj[2]);
	 * 
	 * }
	 * 
	 * dbData.setItemId(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
	 * logger.error("[MURPHY][DropDownDaoEditor][getDbData][each db data] " +
	 * dbData); dbDataList.add(dbData); }
	 * 
	 * return dbDataList; } } catch (Exception e) {
	 * logger.error("[MURPHY][DropDownDaoEditor][getDbData][Exception]" +
	 * e.getMessage()); } return dbDataList;
	 * 
	 * }
	 */
	/*
	 * private List<DropDownDataDto> readExcel1(InputStream inputStream) {
	 * List<DropDownDataDto> exceDataList = new ArrayList<DropDownDataDto>();
	 * DropDownDataDto dto = null; try {
	 * 
	 * XSSFWorkbook workbookRead = new XSSFWorkbook(inputStream); XSSFSheet
	 * sheet = workbookRead.getSheetAt(0);
	 * workbookRead.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL)
	 * ; for (int i = 1; i <= sheet.getLastRowNum(); i++) { dto = new
	 * DropDownDataDto(); Row row = sheet.getRow(i);
	 * 
	 * if (row == null) { dto.setClassification(null);
	 * 
	 * dto.setSubclassification(null);
	 * 
	 * } else { Cell cell = row.getCell(0); if (cell != null) {
	 * dto.setClassification(row.getCell(0).getStringCellValue());
	 * 
	 * }
	 * 
	 * if (row.getCell(1) != null && row.getCell(1).getStringCellValue() != "")
	 * 
	 * dto.setSubclassification(row.getCell(1).getStringCellValue());
	 * 
	 * } logger.error("MURPHY][DropDownEditorDao][readExcel1][each dto]=" +
	 * dto); exceDataList.add(dto);
	 * 
	 * }
	 * 
	 * return exceDataList; } catch (Exception e) {
	 * logger.error("[MURPHY][DropDownEditorDao][readExcel1][Exception]" +
	 * e.getMessage()); }
	 * 
	 * return exceDataList; }
	 */

}
