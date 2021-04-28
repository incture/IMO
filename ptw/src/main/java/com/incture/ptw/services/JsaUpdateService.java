package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.incture.ptw.dto.CreateRequestDto;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class JsaUpdateService {
	public ResponseDto updateJsaService(CreateRequestDto createRequestDto){
		return null;
	}

}
