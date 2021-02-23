package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.AttachmentResponseDto;
import com.murphy.taskmgmt.dto.CollaborationDto;
import com.murphy.taskmgmt.dto.CollaborationResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface CollaborationFacadeLocal {

	ResponseMessage createCollaboration(CollaborationDto dto);

	CollaborationResponseDto getCollaboration(String processId, String userType);

	AttachmentResponseDto getAttachmentById(String fileId);

	ResponseMessage removeAttachment(String fileId);

}
