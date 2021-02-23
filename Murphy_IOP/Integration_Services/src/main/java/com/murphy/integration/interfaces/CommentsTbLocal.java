package com.murphy.integration.interfaces;

import java.util.Date;

import com.murphy.integration.dto.CommentsTbDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIResponseDto;

public interface CommentsTbLocal {

	ResponseMessage insertMerrickIdIntoDB(CommentsTbDto commentsDto);

	UIResponseDto fetchDataFromCommentsDB(String uWID, Date originalDateEntered);
}