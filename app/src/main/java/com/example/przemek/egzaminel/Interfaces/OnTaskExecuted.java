package com.example.przemek.egzaminel.Interfaces;

public interface OnTaskExecuted {

    public void onTaskFinished(Object... params);
    public void onTaskFailed(Object... params);
}
