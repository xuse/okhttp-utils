package com.github.xuse.okhttp;

import okhttp3.Request;
import okhttp3.RequestBody;

public class HeadRequest extends GetRequest {
    protected HeadRequest(HeadBuilder builder) {
        super(builder);
    }

    @Override
    public HeadBuilder newBuilder() {
        return new HeadBuilder(this);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.head().build();
    }

    @Override
    public String method() {
        return "HEAD";
    }
}
