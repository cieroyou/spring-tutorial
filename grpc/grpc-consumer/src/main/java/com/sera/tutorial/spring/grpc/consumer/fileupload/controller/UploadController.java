package com.sera.tutorial.spring.grpc.consumer.fileupload.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sera.tutorial.spring.grpc.consumer.TrainImageGrpcService;
import com.sera.tutorial.spring.grpc.consumer.fileupload.service.FileUploadRequestCaller;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UploadController {

	private final FileUploadRequestCaller fileUploadHttpRequestCaller;
	private final FileUploadRequestCaller fileUploadGrpcCaller;
	private final TrainImageGrpcService trainImageGrpcService;

	@PostMapping("/upload/http")
	// public String uploadHttp() {
		public String uploadHttp(UploadResource request) {
		try {
			return fileUploadHttpRequestCaller.request(request.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@PostMapping("/upload/grpc")
	// public String uploadGrpc() {
		public String uploadGrpc(UploadResource request) {

		try {
			return fileUploadGrpcCaller.request(request.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
