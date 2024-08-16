package com.sera.tutorial.spring.mqtttutorial;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
@Component
public class ByteDataHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ByteBuf) {

			ByteBuf buf = (ByteBuf)msg;
			System.out.println("Received ByteBuf: " + byteBufToHex(buf));
			ctx.writeAndFlush(buf); // Fix: cxt.writeAndFlush(buf) -> cxt.writeAndFlush(msg)
		} else {
			// Pass to the next handler if it's not a ByteBuf
			ctx.fireChannelRead(msg);
		}
	}

	private String byteBufToHex(ByteBuf buf) {
		StringBuilder sb = new StringBuilder();
		while (buf.isReadable()) {
			sb.append(String.format("%02X ", buf.readByte()));
		}
		buf.resetReaderIndex();  // Reset the reader index after reading
		return sb.toString();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}
}
