package com.sera.tutorial.spring.grpc.consumer.fileupload.controller;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResource {
	private MultipartFile file;
}