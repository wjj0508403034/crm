package com.huoyun.upload.impl;

import java.util.Random;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.exception.BusinessException;
import com.huoyun.ftp.FtpService;
import com.huoyun.upload.Attachment;
import com.huoyun.upload.ResourceServerProperties;
import com.huoyun.upload.UploadErrorCode;
import com.huoyun.upload.UploadService;

public class UploadServiceImpl implements UploadService {

	private static int MAX = 9999;
	private static int MIN = 1000;

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

		Attachment attachment = this.upload(file);

		bo.setPropertyValue(propertyName, attachment);
		bo.update();
	}

	@Transactional
	@Override
	public void uploadFileForImageList(String boNamespace, String boName, Long boId, String propertyName,
			MultipartFile file) throws BusinessException {
		BusinessObject bo = this.boFacade.getBoRepository(boNamespace, boName).load(boId);
		if (bo == null) {
			throw new BusinessException(UploadErrorCode.NotFoundBusinessObject);
		}

		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(boNamespace, boName);
		PropertyMeta propMeta = boMeta.getPropertyMeta(propertyName);
		BoMeta nodeMeta = this.boFacade.getMetadataRepository().getBoMeta(propMeta.getNodeMeta().getNodeClass());
		String imagePropName = this.getImagePropertyNameOfSubNode(nodeMeta);
		if (propMeta == null || propMeta.getNodeMeta() == null || imagePropName == null) {
			throw new BusinessException(UploadErrorCode.NotFoundBusinessObject);
		}

		Attachment attachment = this.upload(file);

		BusinessObject subNode = this.boFacade.newBo(propMeta.getNodeMeta().getNodeClass());
		subNode.setPropertyValue(imagePropName, attachment);
		subNode.setPropertyValue(propMeta.getNodeMeta().getMappedBy(), bo);
		subNode.create();
	}

	@Transactional
	@Override
	public void deleteFileForImageList(String boNamespace, String boName, Long boId, String propertyName)
			throws BusinessException {
		BusinessObject bo = this.boFacade.getBoRepository(boNamespace, boName).load(boId);
		if (bo == null) {
			throw new BusinessException(UploadErrorCode.NotFoundBusinessObject);
		}
		BoMeta nodeMeta = this.boFacade.getMetadataRepository().getBoMeta(boNamespace, boName);
		String imagePropName = this.getImagePropertyNameOfSubNode(nodeMeta);
		if (nodeMeta == null || imagePropName == null) {
			throw new BusinessException(UploadErrorCode.NotFoundBusinessObject);
		}
		Attachment attachment = (Attachment) bo.getPropertyValue(imagePropName);
		bo.delete();
		if (attachment != null) {
			attachment.setBoFacade(this.boFacade);
			attachment.delete();
			this.ftpService.deleteFile(attachment.getRelativePath());
		}
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

	private Attachment upload(MultipartFile file) throws BusinessException {
		String targetFile = this.generateNo();
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

		return attachment;
	}

	private String getImagePropertyNameOfSubNode(BoMeta boMeta) {
		for (PropertyMeta propMeta : boMeta.getProperties()) {
			if (propMeta.getType() == PropertyType.Image && propMeta.getRuntimeType() == Attachment.class) {
				return propMeta.getName();
			}
		}

		return null;
	}

	private String generateNo() {
		Random random = new Random();
		int randomNum = random.nextInt(MAX) % (MAX - MIN + 1) + MIN;
		return DateTime.now().toString("yyyyMMddHHmmssSSS") + randomNum;
	}
}
