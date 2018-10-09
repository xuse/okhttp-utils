package com.github.xuse.okhttp;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class HttpTransport {
	private static Logger log = LoggerFactory.getLogger(HttpTransport.class);
	static OkHttpClient client = null;
	static {
		try {
			OkHttpClient.Builder builder = new OkHttpClient.Builder().followRedirects(false)
					.connectionPool(new ConnectionPool()).readTimeout(15, TimeUnit.SECONDS)
					.writeTimeout(15, TimeUnit.SECONDS)
					.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager())
					.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
			String debug = System.getProperty("sms.http.debug");
			if (StringUtils.isNotEmpty(debug)) {
				builder.addNetworkInterceptor(new TraceInterceptor(debug));
			}
			String proxy = System.getProperty("hikvision.proxy");
			if (StringUtils.isNotEmpty(proxy)) {
				String ip = StringUtils.substringBefore(proxy, ":");
				int port = Integer.parseInt(StringUtils.substringAfter(proxy, ":"));
				builder.proxy(new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ip, port)));
				log.info("Using proxy for common okhttp client: {}", proxy);
			}
			client = builder.build();
		} catch (Exception e) {
			log.error("Class Load error", e);
		}
	}

	/**
	 * 使用 GET 请求
	 *
	 * @return {@link GetBuilder} 对象
	 */
	public static GetBuilder get() {
		return new GetBuilder();
	}

	/**
	 * 使用 POST Form 请求
	 *
	 * @return {@link PostFormBuilder} 对象
	 */
	public static PostFormBuilder post() {
		return new PostFormBuilder();
	}

	/**
	 * 使用 POST String 请求
	 *
	 * @return {@link PostStringBuilder} 对象
	 */
	public static PostStringBuilder postString(String content) {
		return new PostStringBuilder().content(content);
	}

	/**
	 * 使用 HEAD 请求
	 *
	 * @return {@link HeadBuilder} 对象
	 */
	public static HeadBuilder head() {
		return new HeadBuilder();
	}
}
