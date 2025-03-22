package com.zhonghang.ui;

import java.awt.*;
import java.util.function.Consumer;

public class Modifier {

    public Alignment alignment = Alignment.TopLeft;
    public Color backgroundColor;
    public int height;
    public int width;
    public int paddingStart;
    public int paddingEnd;
    public int paddingTop;
    public int paddingBottom;
    public Runnable onClicked;
    public Runnable onMouseEnter;
    public Runnable onMouseExit;
    public Consumer<Graphics2D> drawBehind;

    public void size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void padding(int start, int end, int top, int bottom) {
        this.paddingStart = start;
        this.paddingEnd = end;
        this.paddingTop = top;
        this.paddingBottom = bottom;
    }

}
