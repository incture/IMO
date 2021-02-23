package com.murphy.taskmgmt.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.murphy.taskmgmt.dto.AppFileDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.AppFileFacadeLocal;
import com.murphy.taskmgmt.util.ServicesUtil;

@Controller
@RequestMapping(value="/file")
public class AppFileController {

	@Autowired
	private AppFileFacadeLocal fileLocal;
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String showUploadForm(HttpServletRequest request) {
		return "Upload";
	}
	
	@RequestMapping(value="/uploadFile", method=RequestMethod.POST)
	public String saveFile(@RequestParam("appFile") CommonsMultipartFile appFile, @RequestParam("fileVersion") String fileVersion, @RequestParam("fileType") String fileType, @RequestParam("fileName") String fileName, @RequestParam("uploadType") String uploadType, @RequestParam("downloadUrl") String downloadUrl, @RequestParam("application") String application, HttpServletRequest request) {
//		return "Working";
		AppFileDto dto=new AppFileDto();
		try {
			dto.setFile(appFile.getBytes());
			dto.setFileVersion(fileVersion);
			dto.setFileType(fileType.toUpperCase());
			if(ServicesUtil.isEmpty(fileName)) {
				dto.setFileName(FilenameUtils.removeExtension(appFile.getOriginalFilename()));
			} else {
				dto.setFileName(fileName);
			}
			dto.setUploadType(uploadType);
			if(!ServicesUtil.isEmpty(downloadUrl)){
				dto.setFileUrl(downloadUrl);
			} else {
				String serverUrl = ServicesUtil.getServerUrl(request);
				dto.setFileUrl(""+serverUrl+"/murphy/file/downloadFile?fileType="+fileType.toUpperCase()+"&application="+application.toUpperCase()+"");
			}
			if(!ServicesUtil.isEmpty(application))
				dto.setApplication(application.toUpperCase());
		} catch (Exception e) {
			System.err.println("Exception in Getting File : "+e.getMessage());
		}
		ResponseMessage response = fileLocal.saveFile(dto);
		System.err.println("Response From Upload : "+response);
		return "Success";
	}

	@RequestMapping(value="/downloadFile", method=RequestMethod.GET)
	public void downloadFile(HttpServletResponse res, @RequestParam("fileType") String fileType, @RequestParam("application") String application){
		AppFileDto dto = fileLocal.getFile(fileType, application,null);
		if(!ServicesUtil.isEmpty(dto)) {
			res.addHeader("Content-Disposition", "attachment;filename="+dto.getFileName()+"_"+dto.getFileVersion()+"."+dto.getFileType());
			try {
				FileCopyUtils.copy(dto.getFile(), res.getOutputStream());
			} catch (IOException e) {
				System.err.println("IO Exception while fetching File : "+e);
			}
		}
	}
}
