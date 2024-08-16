package com.sera.tutorial.spring.grpc.consumer.fileupload.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadRequestCaller {
	String request(MultipartFile file) throws IOException;
}
