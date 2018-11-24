package com.amitapi.netty.server;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.concurrent.CompletableFuture;

public interface HttpRequestHandler {	
	CompletableFuture<FullHttpResponse> process(FullHttpRequest request);
}
