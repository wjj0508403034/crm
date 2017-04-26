package com.huoyun.upload;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.ftp.FtpService;
import com.huoyun.upload.impl.UploadServiceImpl;

@Configuration
public class UploadAutoConfiguration {

	@Bean
	public UploadService UploadService(FtpService ftpService, BusinessObjectFacade boFacade) {
		return new UploadServiceImpl(ftpService, boFacade);
	}
}
