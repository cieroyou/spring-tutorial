package com.sera.tutorial.spring.grpc.consumer;

import com.google.protobuf.ByteString;
import com.sera.tutorial.spring.grpc.upload.UploadFileServiceGrpc;
import com.sera.tutorial.spring.grpc.upload.UploadRequest;
import com.sera.tutorial.spring.grpc.upload.UploadResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadGrpcService {
    @Value("${root-path}")
    private String baseDirectory;
    @GrpcClient("upload")
    UploadFileServiceGrpc.UploadFileServiceBlockingStub blockingStub;
    // HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub;

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
            UploadResponse response = blockingStub.uploadFile(
                    UploadRequest.newBuilder()
                            .setFile(ByteString.readFrom(inputStream))
                            .build());

            Path destinationPath = getDestinationPath("", "testabresult.png");
            // Create the destination directory if it doesn't exist
            Files.createDirectories(destinationPath.getParent());
//            Files.copy(response.getMap().newInput(), destinationPath);

            float quality = response.getLearnQuality();
            String bbb = "";
        }
        return "uploadComplete";
    }
}
