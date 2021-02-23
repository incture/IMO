package com.murphy.taskmgmt.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.service.TaskManagementCanadaFacade;
import com.murphy.taskmgmt.service.TaskManagementFacade;
import com.murphy.taskmgmt.service.TaskSchedulingCalFacade;
import com.murphy.taskmgmt.service.TaskSchedulingCanadaCalFacade;
import com.murphy.taskmgmt.service.interfaces.TaskManagementInterface;
import com.murphy.taskmgmt.service.interfaces.TaskSchedulingInterface;
/*
 * @author Prakash Kumar
 * Version 1.0
 * Since Sprint 6
 * */
@Service("TaskSchedulingFactory")
public class TaskSchedulingFactory {

	@Autowired
	private TaskSchedulingCalFacade taskSchedulingCalFacade;
	
	@Autowired
	private TaskSchedulingCanadaCalFacade taskSchedulingCanadaCalFacade;
	
	@Autowired
	private TaskManagementFacade taskManagementFacade;
	
	@Autowired
	private TaskManagementCanadaFacade taskManagementCanadaFacade;

	public TaskSchedulingInterface getSchedulingFacade(String loc) {
		if (loc.startsWith("MUR-US"))
			 return taskSchedulingCalFacade;
		else if (loc.startsWith("MUR-CA"))
			return taskSchedulingCanadaCalFacade;

		return null;
	}

	public TaskManagementInterface getTaskManagementFacade(String loc) {
		if (loc.startsWith("MUR-US"))
			return taskManagementFacade;
		if (loc.startsWith("MUR-CA"))
			return taskManagementCanadaFacade;

		return null;
	}
}
