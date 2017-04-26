package com.huoyun.upload.impl;

import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;
import com.huoyun.ftp.FtpService;
import com.huoyun.upload.Attachment;
import com.huoyun.upload.ResourceServerProperties;
import com.huoyun.upload.UploadErrorCode;
import com.huoyun.upload.UploadService;

public class UploadServiceImpl implements UploadService {

	private FtpService ftpService;

	private BusinessObjectFacade boFacade;

	private ResourceServerProperties resourceServer;

	public UploadServiceImpl(FtpService ftpService, BusinessObjectFacade boFacade) {
		this.ftpService = ftpService;
		this.boFacade = boFacade;
		this.resourceServer = boFacade.getBean(ResourceServerProperties.class);
	}

	@Transactional
	@Override
	public void upload(String boNamespace, String boName, Long boId, String propertyName, MultipartFile file)
			throws BusinessException {
		BusinessObject bo = this.boFacade.getBoRepository(boNamespace, boName).load(boId);
		if (bo == null) {
			throw new BusinessException(UploadErrorCode.NotFoundBusinessObject);
		}

		String targetFile = file.getOriginalFilename();
		try {
			this.ftpService.uploadFile(file, targetFile);
		} catch (BusinessException ex) {
			throw new BusinessException(UploadErrorCode.UploadFailed);
		}

		Attachment attachment = this.boFacade.newBo(Attachment.class);
		attachment.setFileName(file.getOriginalFilename());
		attachment.setRelativePath(targetFile);
		attachment.setFileSize(file.getSize());
		attachment.setMineType(file.getContentType());
		attachment.create();

		bo.setPropertyValue(propertyName, attachment);
		bo.update();
	}

	@Override
	public String getFilePath(String boNamespace, String boName, Long boId, String propertyName)
			throws BusinessException {
		BusinessObject bo = this.boFacade.getBoRepository(boNamespace, boName).load(boId);
		if (bo == null) {
			throw new BusinessException(UploadErrorCode.NotFoundBusinessObject);
		}

		Attachment attachment = (Attachment) bo.getPropertyValue(propertyName);
		if (attachment == null) {
			throw new BusinessException(UploadErrorCode.NotFoundBusinessObject);
		}

		return this.resourceServer.getRoot() + attachment.getRelativePath();
	}

}
