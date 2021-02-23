package com.murphy.taskmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.PBITokenGenDao;
import com.murphy.taskmgmt.dto.PBITokenResponse;
import com.murphy.taskmgmt.service.interfaces.PBITokenGenFacadeLocal;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("PBITokenGenFacade")
public class PBITokenGenFacade implements PBITokenGenFacadeLocal {
	
	@Autowired
	PBITokenGenDao pbiTokenDao;
	
	@Override
	public PBITokenResponse generateToken(String type) {
		if(!ServicesUtil.isEmpty(pbiTokenDao))
			return pbiTokenDao.generateToken(type);
		return null;
	}

}
