package com.github.xuse.okhttp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class OkHttpRequest {
    protected String url;
    protected Headers headers;
    protected long connTimeOut;
    protected long writeTimeOut;
    protected long readTimeOut;

    protected Call call;
    protected final Builder builder;

    protected OkHttpRequest(OkHttpBuilder<?> builder) {
        this.url = builder.url;
        this.headers = builder.headers.build();
        this.connTimeOut = builder.connTimeOut;
        this.writeTimeOut = builder.writeTimeOut;
        this.readTimeOut = builder.readTimeOut;
        this.builder = new Builder().url(url).headers(headers);
    }

    /**
     * 根据当前实例创建一个 {@link OkHttpBuilder} 对象
     */
    public abstract OkHttpBuilder<?> newBuilder();

    /**
     * 返回当前请求的谓词
     */
    public abstract String method();

    protected abstract RequestBody buildRequestBody();

    protected abstract Request buildRequest(RequestBody requestBody);

    protected RequestBody wrapRequestBody(RequestBody requestBody) {
        return requestBody;
    }

    protected Request generateRequest() {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody);
        return buildRequest(wrappedRequestBody);
    }

    protected Call buildCall() {
        OkHttpClient okHttpClient = HttpTransport.client;
        if (connTimeOut > 0 || writeTimeOut > 0 || readTimeOut > 0) {
            connTimeOut = connTimeOut > 0 ? connTimeOut : okHttpClient.connectTimeoutMillis();
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : okHttpClient.writeTimeoutMillis();
            readTimeOut = readTimeOut > 0 ? readTimeOut : okHttpClient.readTimeoutMillis();
            return okHttpClient.newBuilder()
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .build()
                    .newCall(generateRequest());
        } else
            return okHttpClient.newCall(generateRequest());
    }

    /**
     * 返回当前实例的 URL 地址
     */
    public String url() {
        return url;
    }


    /**
     * 返回当前实例的 {@code headers}
     */
    public Headers headers() {
        return headers;
    }

    /**
     * 返回当前实例的连接超时，单位 ms
     */
    public long connTimeOut() {
        return connTimeOut;
    }

    /**
     * 返回当前实例的写入超时，单位 ms
     */
    public long writeTimeOut() {
        return writeTimeOut;
    }

    /**
     * 返回当前实例的读取超时，单位 ms
     */
    public long readTimeOut() {
        return readTimeOut;
    }

    /**
     * 返回当前实例的 {@link Call} 对象
     */
    public synchronized Call call() {
        if (Objects.nonNull(call))
            return call;
        return (call = buildCall());
    }

    /**
     * 返回当前实例的 {@link Request} 对象
     */
    public Request request() {
        return call().request();
    }

    /**
     * 执行同步网络请求，并返回 {@link Response} 对象
     *
     * @throws IOException
     */
    public Response response() throws IOException {
        return call().execute();
    }

    /**
     * 执行请求
     * @throws IOException 
     */
    public Response execute() throws IOException {
    	Call call=call();
        if (call().isExecuted())
            throw new IllegalStateException("Already Executed");
        return call.execute();
    }
    
    /**
     * 执行请求
     * @throws IOException 
     */
    public void execute(okhttp3.Callback callback){
    	Call call=call();
        if (call().isExecuted())
            throw new IllegalStateException("Already Executed");
        call.enqueue(callback);
    }

    /**
     * 取消本次请求
     */
    public void cancel() {
        call().cancel();
    }
}
