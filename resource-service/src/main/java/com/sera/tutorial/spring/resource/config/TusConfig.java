package com.sera.tutorial.spring.resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.desair.tus.server.TusFileUploadService;

/**
 * https://github.com/tomdesair/tus-java-server?tab=readme-ov-file#1-setup
 * TusFileUploadService 빈 등록을 위한 설정 클래스
 */
@Configuration
public class TusConfig {

	@Value("${tus.storage-path}")
	private String storagePath;

	@Value("${tus.upload-expiration-period}")
	private Long uploadExpirationPeriod;

	@Bean
	public TusFileUploadService tus() {
		return new TusFileUploadService()
			.withStoragePath(storagePath)
			.withUploadUri("/tus/upload")
			.withDownloadFeature()
			.withUploadExpirationPeriod(uploadExpirationPeriod)
			.withThreadLocalCache(true);
	}
}