package com.murphy.taskmgmt.dao;

import java.nio.charset.StandardCharsets;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.AttachmentDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.AttachmentDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("attachmentDao")
public class AttachmentDao extends BaseDao<AttachmentDo, AttachmentDto> {

	private static final Logger logger = LoggerFactory.getLogger(AttachmentDao.class);

	public AttachmentDao() {
	}

	@Autowired
	ImageProcessingDao imageDao;
	
	@Override
	protected AttachmentDo importDto(AttachmentDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {

		AttachmentDo entity = new AttachmentDo();
		if (!ServicesUtil.isEmpty(fromDto.getFileId()))
			entity.setFileId(fromDto.getFileId());
		if (!ServicesUtil.isEmpty(fromDto.getFileName()))
			entity.setFileName(fromDto.getFileName());
		if (!ServicesUtil.isEmpty(fromDto.getFileType()))
			entity.setFileType(fromDto.getFileType());
		if (!ServicesUtil.isEmpty(fromDto.getMappingId()))
			entity.setMappingId(fromDto.getMappingId());
		if (!ServicesUtil.isEmpty(fromDto.getFileDoc()))
			entity.setFileDoc(fromDto.getFileDoc().trim().getBytes(StandardCharsets.UTF_8));
		if (!ServicesUtil.isEmpty(fromDto.getCompressedFile()))
			entity.setCompressedFile(fromDto.getCompressedFile().trim().getBytes(StandardCharsets.UTF_8));
		if (!ServicesUtil.isEmpty(fromDto.getDocumentId()))
			entity.setDocumentId(fromDto.getDocumentId());
		return entity;
	}

	@Override
	protected AttachmentDto exportDto(AttachmentDo entity) {

		AttachmentDto dto = new AttachmentDto();
		if (!ServicesUtil.isEmpty(entity.getFileId()))
			dto.setFileId(entity.getFileId());
		if (!ServicesUtil.isEmpty(entity.getFileName()))
			dto.setFileName(entity.getFileName());
		if (!ServicesUtil.isEmpty(entity.getFileType()))
			dto.setFileType(entity.getFileType());
		if (!ServicesUtil.isEmpty(entity.getMappingId()))
			dto.setMappingId(entity.getMappingId());
		if (!ServicesUtil.isEmpty(entity.getFileDoc()))
			dto.setFileDoc(new String(entity.getFileDoc(), StandardCharsets.UTF_8));
		if (!ServicesUtil.isEmpty(entity.getCompressedFile()))
			dto.setCompressedFile(new String(entity.getCompressedFile(), StandardCharsets.UTF_8));
		if (!ServicesUtil.isEmpty(entity.getDocumentId()))
			dto.setDocumentId(entity.getDocumentId());

		return dto;
	}

	public ResponseMessage createAttachment(AttachmentDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(dto.getFileDoc())) {
			try {
				dto.setCompressedFile(imageDao.getCompressedImage(dto.getFileDoc(),dto.getFileType()));
				create(dto);
				responseDto.setMessage(MurphyConstant.CREATED_SUCCESS);
				responseDto.setStatus(MurphyConstant.SUCCESS);
				responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} catch (Exception e) {
				logger.error("[Murphy][AttachmentFacade][createFile][error]" + e.getMessage());
				responseDto.setMessage(MurphyConstant.CREATE_FAILURE);
			}
		}else
			responseDto.setMessage(MurphyConstant.MAND_MISS);


		return responseDto;
	}

	public AttachmentDto getFile(String fileId) {
		String queryString = "Select p from AttachmentDo p where p.fileId = '" + fileId + "'";
		Query query = this.getSession().createQuery(queryString);

		AttachmentDo resultSet = (AttachmentDo) query.uniqueResult();
		AttachmentDto dto = exportDto(resultSet);
		return dto;
	}
	
	public String removeAttachment(String fileId) {
		try {
			String deleteQuery = "delete from tm_attachment where file_id='" + fileId + "'";
			int result = this.getSession().createSQLQuery(deleteQuery).executeUpdate();

			if (result > 0)
				return MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][AttachmentFacade][delete][error]" + e.getLocalizedMessage());
		}
		return MurphyConstant.FAILURE;
	}

	/*	@SuppressWarnings("unchecked")
	public Map<String ,AttachmentDto> getAttachByTaskId(String taskId) {
		Map<String ,AttachmentDto> locMap = new HashMap<String ,AttachmentDto>();
		String queryString	= "Select * from TM_ATTACHMENT att where att.MAPPING_ID IN ( Select coll.MESSAGE_ID from TM_COLLABORATION coll where coll.TASK_ID = '"+taskId+"' and coll.MESSAGE is null)";
		Query query = this.getSession().createSQLQuery(queryString);
		List<AttachmentDo> resultSet = (List<AttachmentDo>) query.list();
		for(AttachmentDo entity : resultSet){
			locMap.put(entity.getFileId(),exportDto(entity));
		}
		return locMap;
	}*/


}