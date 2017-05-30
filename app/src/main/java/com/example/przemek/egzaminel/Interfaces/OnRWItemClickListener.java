package com.example.przemek.egzaminel.Interfaces;

import java.util.Objects;

public interface OnRWItemClickListener<T> {

    void onClick(T item, int pos);
    boolean onLongClick(T item, int pos);
}
