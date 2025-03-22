package com.zhonghang.ui;

import java.util.function.Function;

public class AnimateStatus<T> extends MutableStatus<T> {

    private final Function<Long, T> valueUpdateFunction;

    public AnimateStatus(Composable composable, T value, Function<Long, T> valueUpdateFunction) {
        super(composable, value);
        this.valueUpdateFunction = valueUpdateFunction;
        composable.animateStatuses.add(this);
    }

    public void update(long time) {
        T value = valueUpdateFunction.apply(time);
        if (!value.equals(this.value)) {
            setValue(value);
        }
    }
}
