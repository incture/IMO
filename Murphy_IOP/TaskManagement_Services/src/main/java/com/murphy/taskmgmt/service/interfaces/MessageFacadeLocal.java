package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import org.json.simple.JSONObject;

import com.murphy.taskmgmt.dto.MessageDto;
import com.murphy.taskmgmt.dto.MessageUIDetailDto;
import com.murphy.taskmgmt.dto.MessageUIResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskEventsDto;
import com.murphy.taskmgmt.dto.TeamsResponseDto;
import com.murphy.taskmgmt.dto.UpdateRequestDto;

public interface MessageFacadeLocal {

	ResponseMessage update(MessageDto dto);

	MessageUIResponseDto getAllActiveMessagesOwnedBy(String user, List<String> userType, List<String> country, Integer pageNo);

	MessageUIDetailDto getMessage(Long messageId);

	TeamsResponseDto create(JSONObject json, String auth);

	void setMessageInProgress(Long messageId);

	void resolveMessage(Long messageId, String status, TaskEventsDto taskEvent);

}
