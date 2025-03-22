package com.zhonghang.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final BufferedImage bufferedImage =
            new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public static boolean checkIn(int[] topLeft, int[] size) {
        return topLeft[0] >= 0 &&
                topLeft[1] >= 0 &&
                topLeft[0] <= size[0] &&
                topLeft[1] <= size[1];
    }

    public static boolean checkIn(int[] topLeft, Modifier modifier) {
        return topLeft[0] >= modifier.paddingStart &&
                topLeft[1] >= modifier.paddingTop &&
                topLeft[0] <= modifier.paddingStart + modifier.width &&
                topLeft[1] <= modifier.paddingTop + modifier.height;
    }

    public static int[] minus(int[] x, int[] y) {
        return new int[]{x[0] - y[0], x[1] - y[1]};
    }

    public static int[] getStringSize(String text, Font font) {
        FontMetrics fontMetrics = bufferedImage.getGraphics().getFontMetrics(font);
        return new int[]{fontMetrics.stringWidth(text), fontMetrics.getHeight()};
    }

    public static List<Integer> rangeOf(int begin, int end) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = begin; i < end; i++) {
            list.add(i);
        }
        return list;
    }

}
