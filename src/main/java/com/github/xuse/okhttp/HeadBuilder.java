package com.github.xuse.okhttp;

public class HeadBuilder extends GetBuilder {
    public HeadBuilder() {
    }

    protected HeadBuilder(GetRequest request) {
        super(request);
    }

    @Override
    public HeadRequest build() {
        url = getUrl();
        return new HeadRequest(this);
    }
}
