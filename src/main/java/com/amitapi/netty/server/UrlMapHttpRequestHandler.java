package com.amitapi.netty.server;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class UrlMapHttpRequestHandler implements HttpRequestHandler {
	private final Map<String, HttpRequestHandler> handlerMap = new HashMap<>();
	private final HttpRequestHandler defaultHandler;

	public UrlMapHttpRequestHandler(HttpRequestHandler defaultHandler) {
		this.defaultHandler = defaultHandler;
	}

	@Override
	public CompletableFuture<FullHttpResponse> process(FullHttpRequest request) {
		HttpRequestHandler result = handlerMap.get(request.uri().toLowerCase());
		if (result != null) {
			return result.process(request);
		}
		return defaultHandler.process(request);
	}

	public void registerHandler(String uri, HttpRequestHandler handler) {
		handlerMap.put(uri, handler);
	}
}
