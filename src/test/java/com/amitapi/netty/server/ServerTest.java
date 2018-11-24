package com.amitapi.netty.server;

import static io.netty.handler.codec.http.HttpVersion.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class ServerTest {
	public static class Handler implements HttpRequestHandler {
		@Override
		public CompletableFuture<FullHttpResponse> process(
				FullHttpRequest request) {

			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
					OK, Unpooled.copiedBuffer("Hello\r\n", CharsetUtil.UTF_8));
			response.headers().set(HttpHeaderNames.CONTENT_TYPE,
					"text/plain; charset=UTF-8");
			return CompletableFuture.completedFuture(response);
		}
	}

	//@Test
	public void test() throws InterruptedException {
		NettyHttpServer app = new NettyHttpServer(8080, 100000, new Handler());
		app.run();
	}
}
