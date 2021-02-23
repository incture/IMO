package com.murphy.taskmgmt.dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.integration.util.ServicesUtil;
import com.murphy.taskmgmt.dto.AffectedPersonnelDto;
import com.murphy.taskmgmt.dto.EnergyIsolationDto;
import com.murphy.taskmgmt.dto.IsolationDetailsDto;
import com.murphy.taskmgmt.util.MurphyConstant;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Repository
public class EnergyIsolationPdfDao {

	private static final Logger logger = LoggerFactory.getLogger(EnergyIsolationPdfDao.class);

	public File generateEIPdfTemplate(EnergyIsolationDto energyIsolationDto) {
		List<IsolationDetailsDto> finalData=new ArrayList<IsolationDetailsDto>();
		List<AffectedPersonnelDto> affectedPersonnelList=new ArrayList<>();
		AffectedPersonnelDto affectedPersonnelData=new AffectedPersonnelDto();
		IsolationDetailsDto data = new IsolationDetailsDto();
		File tempFile=null;

		try{
			//Load the File Url from by class loading
			URL energyIsolationUrl = EnergyIsolationPdfDao.class.getClassLoader().getResource(MurphyConstant.ENERGY_ISOLATION_TEMPLATE);
			URL LogoURL = EnergyIsolationPdfDao.class.getClassLoader().getResource(MurphyConstant.MURPHY_LOGO);
			URL tickImageURL = EnergyIsolationPdfDao.class.getClassLoader().getResource(MurphyConstant.CHECKBOXTICK);
			if(energyIsolationUrl!=null)
			{
			JasperReport jasperReport = JasperCompileManager.compileReport(energyIsolationUrl.getPath());
			BufferedImage image = ImageIO.read(new File(LogoURL.getPath()));
			BufferedImage tickImage = ImageIO.read(new File(tickImageURL.getPath()));
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reasonForIsolation", energyIsolationDto.getIsolationReason());
			parameters.put("permitNo", energyIsolationDto.getPermitNo());
			parameters.put("plannedDateOfWork", energyIsolationDto.getPlannedDateofWork());
			parameters.put("facility", energyIsolationDto.getFacility());
			parameters.put("permitHolder", energyIsolationDto.getPermitHolder());
			parameters.put("MurphyLogo",image);
			parameters.put("contractorPerforming",energyIsolationDto.getContractorPerformingWork());
			parameters.put("estimationOfCompletion",energyIsolationDto.getEstCompletionTime());
			parameters.put("equipment",energyIsolationDto.getEquipmentTag());
			parameters.put("workOrderNo",energyIsolationDto.getWorkOrderNo());
			parameters.put("PreJobPermitIssuer",energyIsolationDto.getPreJobPermitIssuer());
			parameters.put("PreJobPermitHolder",energyIsolationDto.getPreJobPermitHolder());
			parameters.put("PreJobSafetyDate1",energyIsolationDto.getPreJobSafetyDate1());
			parameters.put("PreJobSafetyDate2",energyIsolationDto.getPreJobSafetyDate2());
			parameters.put("otherHazards",energyIsolationDto.getOtherHazards());
			parameters.put("IsolationPermitHolder",energyIsolationDto.getIsolationPermitHolder());
			parameters.put("IsolationPermitIssuer",energyIsolationDto.getIsolationPermitIssuer());
			parameters.put("PermitHolderDate",energyIsolationDto.getIsolationHolderDate());
			parameters.put("PermitHolderTime",energyIsolationDto.getIsolationHolderTime());
			parameters.put("PermitIssuerDate",energyIsolationDto.getIsolationIssuerDate());
			parameters.put("PermmitIssuerTime",energyIsolationDto.getIsolationIssuerTime());
			parameters.put("ServicePermitHolder",energyIsolationDto.getServicePermitHolder());
			parameters.put("ServicePermitIssuer",energyIsolationDto.getServicePermitIssuer());
			parameters.put("ServiceHolderDate",energyIsolationDto.getServiceHolderDate());
			parameters.put("ServiceIssuerDate",energyIsolationDto.getServiceIssuerDate());
			parameters.put("ServiceHolderTime",energyIsolationDto.getServiceHolderTime());
			parameters.put("ServiceIssuerTime",energyIsolationDto.getServiceIssuerTime());
			parameters.put("energyIsolatedType", energyIsolationDto.getEnergyIsolatedType());

			//Is JSA reviewed
			if(energyIsolationDto.getJsaIsReviewed().equalsIgnoreCase("true")){
				parameters.put("JsaReviewedyes",tickImage);
			}else{
				parameters.put("JsaReviewedNo",tickImage);
			}
			
			//Is LOTO Notified
            if(energyIsolationDto.getIsLOTONotified().equalsIgnoreCase("true")){
				parameters.put("LOTOIntentYes",tickImage);
            }
            else{
				parameters.put("LOTOintentNo",tickImage);
            }
            
            //Is LOTO Removal Notified
            if(energyIsolationDto.getIsLOTORemovalNotified().equalsIgnoreCase("true")){
				parameters.put("LOTORemovalInfoYes",tickImage);
            }
            else{
				parameters.put("LOTORemovalInfoNo",tickImage);
            }
            
            //Is WorkArea Inspected
            if(energyIsolationDto.getIsWorkAreaInspected().equalsIgnoreCase("true")){
				parameters.put("workAreaInspectedYes",tickImage);
            }
            else{
				parameters.put("workAreaInspectedNo",tickImage);
            }
            
            //Is Affected Locks Removed
            if(energyIsolationDto.getIsAffectedLockRemoved().equalsIgnoreCase("true")){
				parameters.put("affectLockRemovedYes",tickImage);
            }
            else{
				parameters.put("affectLockRemovedNo",tickImage);
            }
            
            
            //Isolation Details Table 
            if(!ServicesUtil.isEmpty(energyIsolationDto.getIsolationDetailsDtoList())){
            for(IsolationDetailsDto isolationDto:energyIsolationDto.getIsolationDetailsDtoList()){
				
				data=new IsolationDetailsDto();
				data=createIsolationDetailRow(isolationDto);
				finalData.add(data);
			}
            }
			
			
			//Affected Personnel Table
            if(!ServicesUtil.isEmpty(energyIsolationDto.getAffectedPersonnelList())){
			for(AffectedPersonnelDto affectedPersonnelDto :energyIsolationDto.getAffectedPersonnelList()){
				affectedPersonnelData=new AffectedPersonnelDto();
				affectedPersonnelData=createAffectedPersonnelTableRow(affectedPersonnelDto);
				affectedPersonnelList.add(affectedPersonnelData);
			}
            }
			
			
			JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(finalData);
			JRBeanCollectionDataSource dataSource2=new JRBeanCollectionDataSource(affectedPersonnelList);
			parameters.put("IsolationTable", dataSource);
			parameters.put("AffectedPersonnel", dataSource2);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			tempFile = File.createTempFile("EnergyIsolationTest", ".pdf");
			JasperExportManager.exportReportToPdfFile(jasperPrint, tempFile.getAbsolutePath());
			}
			
		}
		catch (Exception e) {
			logger.error("[Murphy][EnergyIsolationPdfService][convertBase64String][Exception]"+e.getMessage());
			e.printStackTrace();
		}
		
		return tempFile;
	}

	private AffectedPersonnelDto createAffectedPersonnelTableRow(AffectedPersonnelDto dto) {
		AffectedPersonnelDto affectedRowDto=new AffectedPersonnelDto();
		try{
			affectedRowDto.setName(dto.getName());
			affectedRowDto.setEmail(dto.getEmail());
			affectedRowDto.setSignature(dto.getSignature());
			affectedRowDto.setContractorPerformingWork(dto.getContractorPerformingWork());
			return affectedRowDto;
		}
		catch(Exception e){
			logger.error("[Murphy][EnergyIsolationPdfService][createAffectedPersonnelTableRow][Exception]"+e.getMessage());
			e.printStackTrace();
		}
		return affectedRowDto;
	}
	
	private String convertBase64String(File tempFile) {
		String pdfBase64Data = null;
		try {
			byte[] bytfile3 = new byte[(int) tempFile.length()];
			FileInputStream fileReader3 = new FileInputStream(tempFile);
			fileReader3.read(bytfile3);
			byte[] encodedfile3 = Base64.encodeBase64(bytfile3);
			pdfBase64Data = new String(encodedfile3);
			return pdfBase64Data;
		} catch (Exception e) {
			logger.error("[Murphy][EnergyIsolationPdfService][convertBase64String][Exception]" + e.getMessage());
			e.printStackTrace();
		}
		return pdfBase64Data;
	}

	private IsolationDetailsDto createIsolationDetailRow(IsolationDetailsDto dto) {
		IsolationDetailsDto isolationDto = new IsolationDetailsDto();

		isolationDto.setIsolationDetail(ServicesUtil.isEmpty(dto.getIsolationDetail()) ? "" : dto.getIsolationDetail());
		isolationDto.setLocation(ServicesUtil.isEmpty(dto.getLocation()) ? "" : dto.getLocation());
		isolationDto.setIsolationDate(ServicesUtil.isEmpty(dto.getIsolationDate()) ? "" : dto.getIsolationDate());
		isolationDto.setReInstateMentDate(
				ServicesUtil.isEmpty(dto.getReInstateMentDate()) ? "" : dto.getReInstateMentDate());
		if (dto.getIsEnergyStored().equalsIgnoreCase("true")) {
			isolationDto.setIsEnergyStored("Yes");
		} else {
			isolationDto.setIsEnergyNotStored("No");
		}
		if (dto.getIsEquipmentTested().equalsIgnoreCase("true")) {
			isolationDto.setIsEquipmentTested("Yes");
		} else {
			isolationDto.setIsEquipmentNotTested("No");
		}
		return isolationDto;
	}
}
