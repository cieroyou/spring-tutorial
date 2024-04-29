package com.sera.tutorial.spring.grpc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;

import net.devh.boot.grpc.server.service.GrpcService;

import com.google.protobuf.ByteString;
import com.sera.tutorial.spring.grpc.uploadfile.UploadFileServiceGrpc;
import com.sera.tutorial.spring.grpc.uploadfile.UploadRequest;
import com.sera.tutorial.spring.grpc.uploadfile.UploadResponse;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcService
public class UploadController extends UploadFileServiceGrpc.UploadFileServiceImplBase {
	@Value("${root-path}")
	private String baseDirectory;

	// '/' 를 각 시스템 운영체제에 맞는 경로로 변환
	// todo: destinationPath "/" 로 시작하는지 체크 필요
	private Path getDestinationPath(String destinationPath, String destinationFilename) {
		// destinationPath 가 null 이면 "" 처리
		if (destinationPath == null) {
			destinationPath = "";
		}
		String path = baseDirectory + destinationPath;
		return Paths.get(path, destinationFilename).normalize();
	}

	@Override
	public void uploadFile(UploadRequest request, StreamObserver<UploadResponse> responseObserver) {
		// super.uploadFile(request, responseObserver);
		// UploadResponse response = UploadResponse.newBuilder().setMessage("UploadComplete").build();
		try (InputStream stream = request.getFile().newInput()) {
			// Path destinationPath = getDestinationPath("", "testab.png");
			// // Create the destination directory if it doesn't exist
			// Files.createDirectories(destinationPath.getParent());
			// Files.copy(stream, destinationPath);
			UploadResponse response = UploadResponse.newBuilder().setMap(ByteString.readFrom(stream))
				.setLearnQuality(1.4f).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			log.info("Server - sayHello");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
