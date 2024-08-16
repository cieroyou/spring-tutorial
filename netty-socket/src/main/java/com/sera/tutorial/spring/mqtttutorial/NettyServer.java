package com.sera.tutorial.spring.mqtttutorial;

import java.net.InetSocketAddress;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NettyServer {
	EventLoopGroup bossGroup;
	EventLoopGroup workGroup;

	private final ProjectInitializer nettyInitializer;

	@PostConstruct
	public void start() throws InterruptedException {
		new Thread(() -> {
			bossGroup = new NioEventLoopGroup();
			workGroup = new NioEventLoopGroup();
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.localAddress(new InetSocketAddress(8888));
			bootstrap.childHandler(nettyInitializer);
			ChannelFuture channelFuture = null;
			try {
				channelFuture = bootstrap.bind().sync();
				log.info("Server started and listen on:{}", channelFuture.channel().localAddress());
				channelFuture.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	@PreDestroy
	public void destroy() throws InterruptedException {
		if (bossGroup != null) {
			bossGroup.shutdownGracefully().sync();
		}
		if (workGroup != null) {
			workGroup.shutdownGracefully().sync();
		}
	}
}
