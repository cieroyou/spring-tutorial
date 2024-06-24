package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.example.client.codec.RequestDataEncoder;
import org.example.client.codec.ResponseDataDecoder;
import org.example.message.RequestData;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;

// 1. 응답메세지 처리하는 ChannelInboundHandlerAdapter 정의하기
// 2. Server main 메소드 작성
@RequiredArgsConstructor
public class SocketClientApplication {

	public static void main(String[] args) throws InterruptedException {
		String host = "localhost";
		int port = 8888;
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				public void initChannel(SocketChannel ch)
					throws Exception {
					ch.pipeline().addLast(new RequestDataEncoder(),
						new ResponseDataDecoder(), new ClientHandler());
				}
			});

			ChannelFuture future = b.connect(host, port).sync();
			Channel channel = future.channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				String line = in.readLine();
				StringTokenizer st = new StringTokenizer(line);
				Integer intValue = Integer.parseInt(st.nextToken());
				String stringValue = st.nextToken();

				RequestData msg = new RequestData();
				msg.setIntValue(intValue);
				msg.setStringValue(stringValue);
				channel.writeAndFlush(msg);
				if(line.equals("exit")){
					break;
				}

			}

			// f.channel().closeFuture().sync();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}


