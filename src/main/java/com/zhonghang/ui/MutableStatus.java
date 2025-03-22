package com.zhonghang.ui;

public class MutableStatus<T> {

    private final Composable composable;

    protected T value;

    public MutableStatus(Composable composable, T value) {
        this.composable = composable;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        this.composable.isStatusChanged = true;
    }
}
