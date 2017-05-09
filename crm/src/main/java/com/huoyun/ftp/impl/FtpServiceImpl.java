package com.huoyun.ftp.impl;

import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.huoyun.exception.BusinessException;
import com.huoyun.ftp.FtpErrorCode;
import com.huoyun.ftp.FtpProperties;
import com.huoyun.ftp.FtpService;

public class FtpServiceImpl implements FtpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpServiceImpl.class);
	private static final int BufferSize = 1 * 1024 * 1024;

	private FtpProperties ftpProperties;
	private FTPClientConfig ftpConfig;

	public FtpServiceImpl(FtpProperties ftpProperties) {
		this.ftpProperties = ftpProperties;
		this.ftpConfig = new FTPClientConfig(ftpProperties.getServerType());
		this.ftpConfig.setServerLanguageCode("zh");
		this.ftpConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
	}

	@Override
	public void uploadFile(MultipartFile sourceFile, String targetFile) throws BusinessException {
		FTPClient ftpClient = this.newFTPClient();
		this.connect(ftpClient);
		this.login(ftpClient);
		this.storeFile(ftpClient, sourceFile, this.ftpProperties.getUploadFolder() + targetFile);
		this.logout(ftpClient);
	}

	@Override
	public void deleteFile(String file) throws BusinessException {
		FTPClient ftpClient = this.newFTPClient();
		this.connect(ftpClient);
		this.login(ftpClient);
		this.deleteFile(ftpClient, this.ftpProperties.getUploadFolder() + file);
		this.logout(ftpClient);
	}

	private void deleteFile(FTPClient ftpClient, String file) throws BusinessException {
		try {
			ftpClient.deleteFile(file);
			this.printFtpReply(ftpClient);
		} catch (IOException ex) {
			this.logout(ftpClient);
			LOGGER.error(String.format("Delete file %s to ftp server failed", file), ex);
			throw new BusinessException(FtpErrorCode.FtpUploadFailed);
		}
	}

	private FTPClient newFTPClient() {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.configure(this.ftpConfig);
		return ftpClient;
	}

	private void connect(FTPClient ftpClient) throws BusinessException {
		try {
			ftpClient.connect(this.ftpProperties.getHost(), this.ftpProperties.getPort());
			this.printFtpReply(ftpClient);
		} catch (IOException ex) {
			this.abortFtpConnection(ftpClient);
			LOGGER.error("Connection to ftp server failed", ex);
			throw new BusinessException(FtpErrorCode.FtpConnectFailed);
		}

		ftpClient.enterLocalPassiveMode();
		this.printFtpReply(ftpClient);
	}

	private void login(FTPClient ftpClient) throws BusinessException {
		try {
			boolean success = ftpClient.login(this.ftpProperties.getUserName(), this.ftpProperties.getPassword());
			this.printFtpReply(ftpClient);
			if (!success) {
				LOGGER.error("Login to ftp server failed");
				throw new BusinessException(FtpErrorCode.FtpLoginFailed);
			}
		} catch (IOException ex) {
			this.abortFtpConnection(ftpClient);
			LOGGER.error("Login to ftp server failed", ex);
			throw new BusinessException(FtpErrorCode.FtpLoginFailed);
		}
	}

	private void storeFile(FTPClient ftpClient, MultipartFile sourceFile, String targetFile) throws BusinessException {
		try {
			ftpClient.setBufferSize(BufferSize);
			this.printFtpReply(ftpClient);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			this.printFtpReply(ftpClient);
			ftpClient.storeFile(targetFile, sourceFile.getInputStream());
			this.printFtpReply(ftpClient);
			sourceFile.getInputStream().close();
		} catch (IOException ex) {
			this.logout(ftpClient);
			LOGGER.error("Upload file to ftp server failed", ex);
			throw new BusinessException(FtpErrorCode.FtpUploadFailed);
		}
	}

	private void logout(FTPClient ftpClient) throws BusinessException {
		try {
			boolean result = ftpClient.logout();
			this.printFtpReply(ftpClient);
			if (!result) {
				LOGGER.error("Logout to ftp server failed");
				throw new BusinessException(FtpErrorCode.FtpLogoutFailed);
			}
		} catch (IOException e) {
			this.abortFtpConnection(ftpClient);
			LOGGER.error("Logout to ftp server failed");
			throw new BusinessException(FtpErrorCode.FtpLogoutFailed);
		}
	}

	private void abortFtpConnection(FTPClient ftpClient) {
		try {
			if (!ftpClient.abort()) {
				this.printFtpReply(ftpClient);
				LOGGER.error("Abort ftp connection failed");
			}
		} catch (IOException ex) {
			LOGGER.error("Abort ftp connection failed", ex);
		}
	}

	private void printFtpReply(FTPClient ftpClient) {
		LOGGER.info(ftpClient.getReplyString());
	}

}
