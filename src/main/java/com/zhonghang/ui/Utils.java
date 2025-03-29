package com.zhonghang.ui;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
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
        return topLeft[0] >= 0 &&
                topLeft[1] >= 0 &&
                topLeft[0] <= modifier.width &&
                topLeft[1] <= modifier.height;
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

    public static BufferedImage createRoundedCornerImage(BufferedImage image, int cornerRadius) {

        if (cornerRadius == 0) {
            return image;
        }

        // 创建一个新的图像，大小与原始图像相同
        BufferedImage outputImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        // 获取Graphics2D对象以在新图像上进行绘制
        Graphics2D g2 = outputImage.createGraphics();

        // 设置抗锯齿渲染以获得更平滑的边缘
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // 创建一个覆盖整个图像的圆角矩形遮罩
        RoundRectangle2D roundRect = new RoundRectangle2D.Double(
                0,  // x坐标
                0,  // y坐标
                image.getWidth(),  // 宽度
                image.getHeight(), // 高度
                cornerRadius,      // 水平圆角半径
                cornerRadius       // 垂直圆角半径
        );

        // 使用Area对象应用遮罩
        Area area = new Area(roundRect);
        g2.setClip(area);

        // 绘制原始图像到新图像上，应用遮罩
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return outputImage;
    }

    public static BufferedImage createRoundedCornerImageV2(BufferedImage image, int cornerRadius) {

        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage outputImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = outputImage.createGraphics();

        // 设置抗锯齿渲染
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制圆角矩形区域
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // 设置混合模式
        g2.setComposite(AlphaComposite.SrcIn);

        // 绘制图像
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return outputImage;
    }
}
