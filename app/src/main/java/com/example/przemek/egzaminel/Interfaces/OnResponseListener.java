package com.example.przemek.egzaminel.Interfaces;

public interface OnResponseListener {

    void onResponse(String tag, Object... params);
    void onResponseError(String tag, Object... params);

}
