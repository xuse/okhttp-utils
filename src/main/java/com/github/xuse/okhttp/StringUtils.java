package com.github.xuse.okhttp;

public class StringUtils {

	public static boolean isNotEmpty(String debug) {
		return debug != null && debug.length() > 0;
	}

	public static String substringBefore(String proxy, String key) {
		int index = proxy.indexOf(key);
		if (index < 0) {
			return proxy;
		}
		return proxy.substring(0, index);
	}

	public static String substringAfter(String proxy, String key) {
		int index = proxy.indexOf(key);
		if (index < 0) {
			return "";
		}
		return proxy.substring(index + key.length());
	}
}
