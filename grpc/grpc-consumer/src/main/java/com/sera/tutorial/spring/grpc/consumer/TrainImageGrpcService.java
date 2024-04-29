package com.sera.tutorial.spring.grpc.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.devh.boot.grpc.client.inject.GrpcClient;

import com.google.protobuf.ByteString;
import com.sera.tutorial.spring.grpc.trainimage.Status;
import com.sera.tutorial.spring.grpc.trainimage.TrainImageGrpc;
import com.sera.tutorial.spring.grpc.trainimage.TrainImageResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainImageGrpcService {
	@Value("${root-path}")
	private String baseDirectory;
	@GrpcClient("trainimage")
	TrainImageGrpc.TrainImageBlockingStub blockingStub;

	//
	private Path getDestinationPath(String destinationPath, String destinationFilename) {
		// destinationPath 가 null 이면 "" 처리
		if (destinationPath == null) {
			destinationPath = "";
		}
		String path = baseDirectory + destinationPath;
		return Paths.get(path, destinationFilename).normalize();
	}

	public String sendMessage(MultipartFile file) throws IOException {
		try (InputStream inputStream = file.getInputStream()) {
			// UploadResponse response = blockingStub.uploadFile(
			// 	com.sera.tutorial.spring.grpc.uploadfile.UploadRequest.newBuilder()
			// 		.setFile(ByteString.readFrom(inputStream))
			// 		.build());
			TrainImageResponse response = blockingStub.trainImage(
				com.sera.tutorial.spring.grpc.trainimage.TrainImageRequest.newBuilder()
					.setImage(ByteString.readFrom(inputStream))
					.setImageName(file.getOriginalFilename())
					.build());

			var status = response.getStatus();
			String responseMessage = response.getMessage();

			if (!status.equals(Status.SUCCESS))
				return responseMessage;
			Double quality = response.getImageQuality();
			try (InputStream mapStream = response.getMapImage().newInput();
				 InputStream thumbStream = response.getThumbImage().newInput();) {
				String originalFilenameWithoutExtension = file.getOriginalFilename()
					.substring(0, file.getOriginalFilename().lastIndexOf('.'));
				String mapFileName = originalFilenameWithoutExtension + ".2dmap";
				String thumbFileName = originalFilenameWithoutExtension + "-mini.jpeg";

				Files.copy(mapStream, getDestinationPath("", mapFileName));
				Files.copy(thumbStream, getDestinationPath("", thumbFileName));
			}

			log.info("quality: {}, message: {}", quality, responseMessage);
			// Path destinationPath = getDestinationPath("", "testabresult.png");
			// // Create the destination directory if it doesn't exist
			// Files.createDirectories(destinationPath.getParent());

			// Files.copy(response.getMap().newInput(), destinationPath);

			// float quality = response.getLearnQuality();
		}
		return "uploadComplete";
	}
}
