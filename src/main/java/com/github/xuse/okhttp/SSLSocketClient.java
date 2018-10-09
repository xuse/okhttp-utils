package com.github.xuse.okhttp;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLSocketClient {
	private static final X509TrustManager trustAllCerts = new X509TrustManager() {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	};

	// 获取这个SSLSocketFactory
	public static SSLSocketFactory getSSLSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] {getTrustManager()}, new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 获取TrustManager
	public static X509TrustManager getTrustManager() {
		return trustAllCerts;
	}

	public static HostnameVerifier getHostnameVerifier() {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		};
		return hostnameVerifier;
	}
}