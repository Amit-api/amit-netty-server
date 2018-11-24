package com.amitapi.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
	private final int maxRequestSize;
	private final HttpRequestHandler requestHandler;
	
	public NettyHttpServerInitializer(int maxRequestSize, HttpRequestHandler requestHandler) {
		this.maxRequestSize = maxRequestSize;
		this.requestHandler = requestHandler;
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline p = channel.pipeline();
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(maxRequestSize));
		p.addLast(new HttpContentCompressor());
		p.addLast(new NettyHttpServerHandler(requestHandler));
	}
}
