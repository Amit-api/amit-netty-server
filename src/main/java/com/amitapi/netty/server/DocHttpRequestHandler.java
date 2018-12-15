package com.amitapi.netty.server;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class DocHttpRequestHandler implements HttpRequestHandler {
	private final Class<?> location;
	private final String filename;
	private volatile ByteBuf content;
	private final ByteBuf error;

	public DocHttpRequestHandler(Class<?> location, String filename) {
		this.location = location;
		this.filename = filename;
		this.error = Unpooled
				.copiedBuffer("doc unavailable", CharsetUtil.UTF_8);
	}

	@Override
	public CompletableFuture<FullHttpResponse> process(FullHttpRequest request) {
		if (request.method() != HttpMethod.GET) {
			return null;
		}

		ByteBuf content = error;
		HttpResponseStatus status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
		String contentType = "text/plain; charset=UTF-8";

		try {
			content = getContent();
			status = HttpResponseStatus.OK;
			contentType = "text/html;";
		} catch (IOException e) {
		}

		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				status, content.copy());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
		return CompletableFuture.completedFuture(response);

	}

	private ByteBuf getContent() throws IOException {
		if (content != null) {
			return content;
		}

		try (InputStream in = location.getClassLoader().getResourceAsStream(
				"doc/" + filename)) {
			if (in == null) {
				throw new IOException("not found");
			}
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = in.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			content = Unpooled.copiedBuffer(buffer.toByteArray());
		}

		return content;
	}
}
