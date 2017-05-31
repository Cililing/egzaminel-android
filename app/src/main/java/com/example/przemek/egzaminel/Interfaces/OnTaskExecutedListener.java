package com.example.przemek.egzaminel.Interfaces;

public interface OnTaskExecutedListener {

    public void onTaskFinished(Object... params);
    public void onTaskFailed(Object... params);
}
