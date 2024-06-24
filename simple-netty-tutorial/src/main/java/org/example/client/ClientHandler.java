package org.example.client;

import org.example.message.RequestData;
import org.example.message.ResponseData;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx)
		throws Exception {

		// RequestData msg = new RequestData();
		// msg.setIntValue(123);
		// msg.setStringValue(
		// 	"all work and no play makes jack a dull boy");
		// ChannelFuture future = ctx.writeAndFlush(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
		throws Exception {
		ResponseData data = (ResponseData) msg;
		log.info(data.toString());
		// ctx.close();
	}
}
