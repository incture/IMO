package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.PBITokenResponse;

public interface PBITokenGenFacadeLocal {

	PBITokenResponse generateToken(String type);

}
