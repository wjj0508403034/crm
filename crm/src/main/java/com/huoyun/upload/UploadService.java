package com.huoyun.upload;

import org.springframework.web.multipart.MultipartFile;

import com.huoyun.exception.BusinessException;

public interface UploadService {

	void upload(String boNamespace, String boName, Long boId, String propertyName, MultipartFile file)
			throws BusinessException;

	String getFilePath(String boNamespace, String boName, Long boId, String propertyName) throws BusinessException;
}
