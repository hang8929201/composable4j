package com.zhonghang.ui;

import java.awt.*;

public class Text extends Composable {

    public String text;
    public Color textColor = Color.black;
    public Font font = new Font("宋体", Font.PLAIN, 12);

    @Override
    public void update(long time) {
        int[] size = Utils.getStringSize(text, font);
        modifier.width = Math.max(size[0], modifier.width);
        modifier.height = Math.max(size[1], modifier.height);
        super.update(time);
    }

    @Override
    protected void paint(Graphics2D graphics) {
        super.paint(graphics);
        graphics.setFont(font);
        graphics.setColor(textColor);
        graphics.drawString(text, modifier.paddingStart, modifier.height * 4 / 5 + modifier.paddingTop);
    }
}
