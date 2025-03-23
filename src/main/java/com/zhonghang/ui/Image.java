package com.zhonghang.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Image extends Composable {

    private BufferedImage originalBufferedImage;

    public String resource;

    public Image() {}

    @Override
    protected void paint(Graphics2D graphics) {
        super.paint(graphics);
        if (originalBufferedImage == null) {
            try {
                originalBufferedImage = ImageIO.read(Objects.requireNonNull(
                        Image.class.getClassLoader().getResourceAsStream(resource)));
            } catch (Exception e) {
                return;
            }
        }
        graphics.drawImage(originalBufferedImage,
                modifier.paddingStart,
                modifier.paddingTop,
                modifier.width,
                modifier.height,
                null);
    }
}
