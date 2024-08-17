package com.sera.tutorial.spring.grpc.consumer.fileupload.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sera.tutorial.spring.grpc.upload.UploadFileServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.devh.boot.grpc.client.inject.GrpcClient;

import com.google.protobuf.ByteString;
import com.sera.tutorial.spring.grpc.upload.UploadResponse;

@Service
public class FileUploadGrpcCaller implements FileUploadRequestCaller {
	private final static String IMAGE_FILE_NAME = "temp.jpg";
	private final static String VIDEO_FILE_NAME = "largefile.mp4";
	@GrpcClient("upload")
	UploadFileServiceGrpc.UploadFileServiceBlockingStub blockingStub;


	@Value("${root-path}")
	private String baseDirectory;

	private Path getDestinationPath(String destinationPath, String destinationFilename) {
		// destinationPath 가 null 이면 "" 처리
		if (destinationPath == null) {
			destinationPath = "";
		}
		String path = baseDirectory + destinationPath;
		return Paths.get(path, destinationFilename).normalize();
	}
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
//			Path destinationPath = getDestinationPath("", "testabresult.png");
			// Create the destination directory if it doesn't exist
//			Files.createDirectories(destinationPath.getParent());
			// Files.copy(response.getMap().newInput(), destinationPath);

			return response.getMessage();

		}
	}
}
