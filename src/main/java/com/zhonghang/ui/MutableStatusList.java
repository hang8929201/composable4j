package com.zhonghang.ui;

import java.util.List;

public class MutableStatusList<T> {

    private final Composable composable;

    private final List<T> list;

    public MutableStatusList(Composable composable, List<T> list) {
        this.composable = composable;
        this.list = list;
    }

    public void setValue(int index, T value) {
        list.set(index, value);
        composable.isStatusChanged = true;
    }

    public T getValue(int index) {
        return list.get(index);
    }
}
