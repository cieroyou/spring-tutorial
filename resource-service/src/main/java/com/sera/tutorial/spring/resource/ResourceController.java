package com.sera.tutorial.spring.resource;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resource")
public class ResourceController {

	@Value("${resource.storage.local.root-path}")
	private String uploadDir;

	@PostMapping("/upload")
	public String uploadResources(@RequestPart(name = "file") MultipartFile file) {
		if (file.isEmpty()) {
			return "File is empty";
		}
		try {
			file.transferTo(Paths.get(uploadDir, file.getOriginalFilename()));
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed to upload file: " + e.getMessage();
		}
		return "success";
	}
}
