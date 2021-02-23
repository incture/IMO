package com.murphy.taskmgmt.service;

import java.util.Comparator;

import com.murphy.taskmgmt.dto.GroupsUserDto;

public class OBXCompetencyComparator implements Comparator<GroupsUserDto> {

	@Override
	public int compare(GroupsUserDto o1, GroupsUserDto o2) {
		//return o2.getObxDesignation().compareTo(o1.getObxDesignation());
		return 1;
	}

	
}
