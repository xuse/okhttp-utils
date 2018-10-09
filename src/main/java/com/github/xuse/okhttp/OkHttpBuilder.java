package com.github.xuse.okhttp;

import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.MediaType;

@SuppressWarnings("unchecked")
public abstract class OkHttpBuilder<T extends OkHttpBuilder<T>> {
    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    protected String url;
    protected Builder headers;
    protected long connTimeOut;
    protected long writeTimeOut;
    protected long readTimeOut;

    public OkHttpBuilder() {
        this.headers = new Builder();
    }

    protected OkHttpBuilder(OkHttpRequest request) {
        this.url(request.url);
        this.headers(request.headers);
        this.connTimeOut(request.connTimeOut);
        this.writeTimeOut(request.writeTimeOut);
        this.readTimeOut(request.readTimeOut);
    }

    /**
     * 根据当前实例创建一个 {@link OkHttpRequest} 对象
     */
    public abstract OkHttpRequest build();

    /**
     * 设置请求的 URL 地址
     */
    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    /**
     * 设置请求头
     */
    public T headers(Headers headers) {
        this.headers = headers.newBuilder();
        return (T) this;
    }

    /**
     * 设置请求头
     */
    public T headers(Map<String, String> headers) {
        removeAllHeaders();
        return addHeaders(headers);
    }

    /**
     * 添加请求头
     */
    public T addHeaders(Map<String, String> headers) {
        if (Objects.nonNull(headers))
            for (Entry<String, String> entry : headers.entrySet())
                _addHeader(entry.getKey(), entry.getValue());
        return (T) this;
    }

    /**
     * 设置指定 {@code name} 的请求头的值
     */
    public T setHeader(String name, Object value) {
        headers.set(name, Objects.requireNonNull(value,
                "the header [" + name + "] can not be null.").toString());
        return (T) this;
    }

    /**
     * 添加一个请求头
     */
    public T addHeader(String name, Object value) {
        return _addHeader(name, Objects.requireNonNull(value, "header [" + name + "] is null."));
    }

    /**
     * 添加一个请求头，如果 {@code value} 为空，则不添加
     */
    public T addOptionHeader(String name, Object value) {
        if (!Objects.isEmpty(value))
            _addHeader(name, value);
        return (T) this;
    }

    /**
     * 添加一个请求头，{@code value} 不可为空
     */
    public T addNonEmptyHeader(String name, Object value) {
        return _addHeader(name, Objects.requireNonEmpty(value, "header [" + name + "] is empty."));
    }

    /**
     * 添加一个请求头，{@code value} 为空时添加空字符串
     */
    public T addNullableHeader(String name, Object value) {
        if (Objects.nonNull(value))
            return _addHeader(name, value);
        return _addHeader(name, "");
    }

    /**
     * 根据给定的 {@code name}，删除一个请求头
     */
    public T removeHeader(String name) {
        headers.removeAll(name);
        return (T) this;
    }

    /**
     * 删除所有请求头
     */
    public T removeAllHeaders() {
        for (String name : headers.build().names())
            headers.removeAll(name);
        return (T) this;
    }

    private T _addHeader(String name, Object value) {
        headers.add(name, value.toString());
        return (T) this;
    }

    /**
     * 设置 User-Agent
     */
    public T userAgent(String value) {
        return setHeader("User-Agent", value);
    }

    /**
     * 设置本次请求的连接超时，单位毫秒
     */
    public T connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return (T) this;
    }

    /**
     * 设置本次请求的写入超时，单位毫秒
     */
    public T writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return (T) this;
    }

    /**
     * 设置本次请求的读取超时，单位毫秒
     */
    public T readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return (T) this;
    }
}
