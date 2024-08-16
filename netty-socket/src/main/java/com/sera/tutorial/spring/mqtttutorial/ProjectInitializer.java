package com.sera.tutorial.spring.mqtttutorial;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProjectInitializer extends ChannelInitializer<SocketChannel> {
	private final ByteDataHandler byteDataHandler;

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		socketChannel.pipeline().addLast(byteDataHandler);
	}
}
