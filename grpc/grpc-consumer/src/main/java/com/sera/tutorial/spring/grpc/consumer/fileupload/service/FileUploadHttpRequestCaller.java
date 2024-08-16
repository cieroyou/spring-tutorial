package com.sera.tutorial.spring.grpc.consumer.fileupload.service;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileUploadHttpRequestCaller implements FileUploadRequestCaller {
	@Value("${upload.address}")
	private String serverUrl;

	@Value("${upload.base-url}")
	private String baseUrl;
	private final static String IMAGE_FILE_NAME = "temp.jpg";

	private final String filePath = "/Users/lydia/Developments/sera/tutorial/spring-tutorial/grpc/grpc-consumer/src/main/resources/";

	@Override
	public String request(MultipartFile file) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		Resource resource = file.getResource();
		URI uri = getRequestUri(baseUrl);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
		Resource file1 = new FileSystemResource(filePath + IMAGE_FILE_NAME);


		multipartBodyBuilder.part("file", resource, MediaType.IMAGE_JPEG);
		MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();
		HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri, httpEntity,String.class);
		return responseEntity.getBody();
	}


	URI getRequestUri(String url){
		return  UriComponentsBuilder
			.fromUriString(serverUrl)
			.path(url)
			.encode()
			.build()
			.toUri();
	}
}
