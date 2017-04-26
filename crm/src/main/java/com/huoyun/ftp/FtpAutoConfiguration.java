package com.huoyun.ftp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.ftp.impl.FtpServiceImpl;

@Configuration
public class FtpAutoConfiguration {

	@Bean
	public FtpService ftpService(FtpProperties ftpProperties){
		return new FtpServiceImpl(ftpProperties);
	}
}
