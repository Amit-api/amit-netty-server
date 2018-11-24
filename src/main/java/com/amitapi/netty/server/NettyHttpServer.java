package com.amitapi.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpServer {
	public static final Logger logger = LoggerFactory
			.getLogger(NettyHttpServer.class);
	private final int port;
	private final int maxRequestSize;
	private HttpRequestHandler requestHandler;

	public NettyHttpServer(int port, int maxRequestSize, HttpRequestHandler requestHandler) {
		this.port = port;
		this.maxRequestSize = maxRequestSize;
		this.requestHandler = requestHandler;
	}

	public void run() throws InterruptedException {
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.DEBUG))
					.childHandler(
							new NettyHttpServerInitializer(maxRequestSize, requestHandler));

			logger.info("Amit API started on port: %d, max request size %d",
					port, maxRequestSize);

			Channel ch = b.bind(port).sync().channel();
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
