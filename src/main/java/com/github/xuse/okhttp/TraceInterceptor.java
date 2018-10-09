package com.github.xuse.okhttp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 本拦截器用于打印HTTP报文，仅限在开发调试用，请勿用于生产环境。
 * 
 * @author jiyi
 */
public class TraceInterceptor implements Interceptor {
	private Logger log = LoggerFactory.getLogger(TraceInterceptor.class);

	private final boolean logHeader;

	public TraceInterceptor(String arg) {
		logHeader = "all".equals(arg);
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		printRequest(request);
		Response resp = chain.proceed(request);
		return printResponse(resp);
	}

	private void printRequest(Request request) throws IOException {
		String type = request.header("Content-Type");
		if (type == null || type.startsWith("text")|| type.startsWith("application/json")
				|| type.startsWith("application/x-www-form-urlencoded")) {
			StringBuilder sb = new StringBuilder("==========Request:==========\r\n");
			sb.append(request.method()).append(' ').append(request.url()).append("\r\n");
			if (logHeader)
				sb.append(request.headers()).append("\r\n");
			if (request.body() != null) {
				Buffer buf = new Buffer();
				request.body().writeTo(buf);
				sb.append(buf.readUtf8());
			}
			log.info(sb.toString());
		} else if (logHeader) {
			log.info("==========Request:==========\r\n{}", request.headers());
		}
	}

	private Response printResponse(Response resp) throws IOException {
		String type = resp.header("Content-Type");
		if (type == null || type.startsWith("application/json") || type.startsWith("plain/text")) {
			StringBuilder sb = new StringBuilder("==========Response:========\r\n");
			sb.append("Code:").append(resp.code()).append("\r\n");
			if (logHeader)
				sb.append(resp.headers()).append("\r\n");
			String s = resp.body().string();
			sb.append(s);
			log.info(sb.toString());
			return resp.newBuilder().headers(resp.headers()).body(ResponseBody.create(MediaType.parse(type), s))
					.build();
		} else if (logHeader) {
			log.info("==========Response:========\r\n{}", resp.headers());
		}
		return resp;
	}

}
