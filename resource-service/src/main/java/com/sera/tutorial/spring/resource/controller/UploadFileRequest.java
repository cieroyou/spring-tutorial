package com.sera.tutorial.spring.resource.controller;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileRequest {
	private MultipartFile file;
}
