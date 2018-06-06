package com.homechef.ict.ict_homechef.ConnectUtil;

public interface HttpCallback<T> {

    void onError(Throwable t);

    void onSuccess(int code, T receivedData);

    void onFailure(int code);

}
