package com.amitapi.netty.server;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.util.concurrent.CompletableFuture;

public class ErrorHttpRequestHandler implements HttpRequestHandler {
	private final HttpResponseStatus status;
	private final String content;

	public ErrorHttpRequestHandler(HttpResponseStatus status, String content) {
		this.status = status;
		this.content = content;
	}

	@Override
	public CompletableFuture<FullHttpResponse> process(FullHttpRequest request) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				status, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,
				"text/plain; charset=UTF-8");
		return CompletableFuture.completedFuture(response);
	}
}
