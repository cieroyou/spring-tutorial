package com.sera.tutorial.spring.grpc.consumer.fileupload.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.devh.boot.grpc.client.inject.GrpcClient;

import com.google.protobuf.ByteString;
import com.sera.tutorial.spring.grpc.upload.UploadResponse;
import com.sera.tutorial.spring.grpc.upload.UploadServiceGrpc;

@Service
public class FileUploadGrpcCaller implements FileUploadRequestCaller {
	private final static String IMAGE_FILE_NAME = "temp.jpg";
	private final static String VIDEO_FILE_NAME = "largefile.mp4";
	@GrpcClient("upload")
	UploadServiceGrpc.UploadServiceBlockingStub blockingStub;
	// @Override
	// public String upload(MultipartFile file) throws IOException {
	// 	UploadResponse response = blockingStub.uploadFile(
	// 		UploadRequest.newBuilder()
	// 			.setFirstName(file.getOriginalFilename())
	// 			.setLastName(file.getOriginalFilename())
	// 			.build());
	// 	return response.getMessage();
	//
	// 	// try (InputStream inputStream = file.getInputStream()) {
	// 	// 	UploadResponse response = blockingStub.uploadFile(
	// 	// 		com.sera.tutorial.spring.grpc.uploadfile.UploadRequest.newBuilder()
	// 	// 			.setFile(file.getOriginalFilename())
	// 	// 			// .setFile(ByteString.readFrom(inputStream))
	// 	// 			.build());
	// 	// 	float aa = response.getLearnQuality();
	// 	//
	// 	// }
	// 	// return "uploadComplete";
	// }

	@Override
	public String request(MultipartFile file) throws IOException {

		// InputStream inputStream = file.getInputStream()
		// InputStream inputStream = FileUploadGrpcCaller.class.getClassLoader()
		// 	.getResource(IMAGE_FILE_NAME)
		// 	.openStream()
		try (InputStream inputStream = file.getInputStream()) {
			UploadResponse response = blockingStub.uploadFile(
				com.sera.tutorial.spring.grpc.upload.UploadRequest.newBuilder()
					.setFile(ByteString.readFrom(inputStream))
					.build());
			return response.getMessage();

		}
	}
}
