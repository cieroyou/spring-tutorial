package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 1. 응답메세지 처리하는 ChannelInboundHandlerAdapter 정의하기
// 2. Server main 메소드 작성
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SocketNettyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketNettyApplication.class, args);
	}

}
