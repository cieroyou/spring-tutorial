package com.sera.tutorial.spring.grpc.consumer;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UploadController {

	private final FileUploadGrpcService fileUploadGrpcService;
	private final TrainImageGrpcService trainImageGrpcService;

	@PostMapping("/upload")
	public String printMessage(UploadResource request) {
		try {
			// return fileUploadGrpcService.sendMessage(request.getFile());
			return trainImageGrpcService.sendMessage(request.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
