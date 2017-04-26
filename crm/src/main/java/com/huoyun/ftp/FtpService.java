package com.huoyun.ftp;

import org.springframework.web.multipart.MultipartFile;

import com.huoyun.exception.BusinessException;

public interface FtpService {

	void uploadFile(MultipartFile sourceFile, String targetFile) throws BusinessException;
}
