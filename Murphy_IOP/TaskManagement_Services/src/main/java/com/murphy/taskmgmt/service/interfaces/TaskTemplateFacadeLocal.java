package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.ProcessTemplateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskTemplatesDto;

public interface TaskTemplateFacadeLocal {

	ResponseMessage createTemplate(ProcessTemplateDto dto);

	ProcessTemplateDto readTemplate(String procTemplateId);

	ResponseMessage createTaskTemplate(TaskTemplatesDto dto);
	
	ResponseMessage createTaskFromTemplate(TaskTemplatesDto dto);
	
}
