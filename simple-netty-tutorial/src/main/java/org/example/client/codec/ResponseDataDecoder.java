package org.example.client.codec;

import java.util.List;

import org.example.message.ResponseData;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class ResponseDataDecoder
	extends ReplayingDecoder<ResponseData> {

	@Override
	protected void decode(ChannelHandlerContext ctx,
		ByteBuf in, List<Object> out) throws Exception {

		ResponseData data = new ResponseData();
		data.setIntValue(in.readInt());
		out.add(data);
	}
}