package com.github.xuse.okhttp;

import okhttp3.Request;
import okhttp3.RequestBody;

public class GetRequest extends OkHttpRequest {
    protected GetRequest(GetBuilder builder) {
        super(builder);
    }

    @Override
    public GetBuilder newBuilder() {
        return new GetBuilder(this);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }

    @Override
    public String method() {
        return "GET";
    }
}
