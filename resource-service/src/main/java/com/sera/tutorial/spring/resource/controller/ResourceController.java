package com.sera.tutorial.spring.resource.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resource")
public class ResourceController {

	@Value("${resource.storage.local.root-path}")
	private String uploadDir;

	@PostMapping("/upload")
	public String uploadResources(UploadFileRequest resourceFileRequest) {
		// if (resourceFileRequest.isEmpty()) {
		// 	return "File is empty";
		// }
		// try {
		// 	file.transferTo(Paths.get(uploadDir, file.getOriginalFilename()));
		// } catch (IOException e) {
		// 	e.printStackTrace();
		// 	return "Failed to upload file: " + e.getMessage();
		// }
		log.info("Server - UploadCompleted! size: {}", resourceFileRequest.getFile().getSize());
		return resourceFileRequest.getFile().getOriginalFilename();
	}

}
