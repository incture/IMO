package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.HseDocumentDto;
import com.murphy.taskmgmt.dto.HseDocumentResponse;
import com.murphy.taskmgmt.dto.HseResponseBodyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface  HseDocumentFacadeLocal {
	HseResponseBodyDto getHseDocument();

	ResponseMessage createHSEDocument(HseDocumentDto dto);
	
	HseResponseBodyDto getStringList();

	HseDocumentResponse getStringDocument(String text);
	
	HseDocumentResponse getParagraph(int page ,String text, String url);
}
