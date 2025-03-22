package com.zhonghang.ui;

public class MouseStatusChangeEvent {

    public boolean isPressed;

    public int[] topLeft;

    public MouseStatusChangeEvent(boolean isPressed, int px, int py) {
        this.isPressed = isPressed;
        this.topLeft = new int[]{px, py};
    }

    public MouseStatusChangeEvent(boolean isPressed, int[] topLeft) {
        this.isPressed = isPressed;
        this.topLeft = topLeft;
    }
}
