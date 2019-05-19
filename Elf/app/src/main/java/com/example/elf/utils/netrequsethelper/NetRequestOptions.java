package com.example.elf.utils.netrequsethelper;

public class NetRequestOptions {

    /**
     * 通过Request这个类来传网络请求需要的参数
     */

    private String url;//接口
    private String params;//参数,POST传参数时才需要
    private String method;//方法,确定是GET还是POST
    private String property;//配置请求的Content-Type

    public NetRequestOptions() {
    }

    //从内部类的数据中
    public NetRequestOptions(NetRequestOptions origin) {
        this.url = origin.url;
        this.params = origin.params;
        this.method = origin.method;
        this.property = origin.property;
    }

    public String getUrl() {
        return url;
    }

    public String getParams() {
        return params;
    }

    public String getMethod() {
        return method;
    }

    public String getProperty() {
        return property;
    }

    //通过Request.Builder这个内部类来创建Request的对象
    public static class Builder {
        private NetRequestOptions target;

        public Builder() {
            target = new NetRequestOptions();
        }

        public Builder url(String url) {
            target.url = url;
            return this;//返回的是当前这个Request的对象target
        }

        public Builder params(String params) {
            target.params = params;
            return this;
        }

        public Builder method(String method) {
            target.method = method;
            return this;
        }

        public Builder property(String property) {
            target.property = property;
            return this;
        }

        public NetRequestOptions build() {
            return new NetRequestOptions(target);
        }
    }

/**
 * Builder模式的使用以及this表示的对象问题
 * https://www.jianshu.com/p/5c14eecbf6b5
 * https://blog.csdn.net/sunhl951/article/details/80023347
 */

}
