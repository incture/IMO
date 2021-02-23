package com.murphy.taskmgmt.service.interfaces;

import java.util.HashMap;
import java.util.List;

import com.murphy.taskmgmt.dto.QRCodeDetailsDto;
import com.murphy.taskmgmt.dto.QRCodeDto;
import com.murphy.taskmgmt.entity.QRCodeDo;

public interface QRCodeFacadeLocal {
	
	public void addQRCodeDetail(QRCodeDo qrCodeDo);
	public void deleteQRCodeDetail(String qrCode);
	public List<QRCodeDo> QRCodeDetailList();
	public QRCodeDto GenerateQRCode(String id, int width, int height);
	
	public QRCodeDetailsDto getDetails(String qRNumber);

}
