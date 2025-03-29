package com.zhonghang.ui;

import java.awt.*;

public class PainterDemo {

    static class VAO {
        int[][]vbo;
        int[][]ebo;
    }

    public static void main(String[] args) {
        new JFrameComposableApp(new Box() {{
            modifier.size(800, 800);
            contentSupplier = content -> {
                content.add(new Box(){{
                    modifier.size(600, 600);
                    modifier.alignment = Alignment.Center;
                    modifier.backgroundColor = Color.lightGray;
                    modifier.cornerRadius = 80;
                }});
                content.add(new Text() {{
                    text = "Point";
                    font = new Font("宋体", Font.PLAIN, 20);
                    modifier.alignment = Alignment.TopEnd;
                    modifier.paddingTop = 80;
                    modifier.paddingEnd = 10;
                    modifier.backgroundColor = Color.lightGray;
                }});
                content.add(new Text() {{
                    text = "Line";
                    font = new Font("宋体", Font.PLAIN, 20);
                    modifier.alignment = Alignment.TopEnd;
                    modifier.paddingTop = 120;
                    modifier.paddingEnd = 10;
                    modifier.backgroundColor = Color.lightGray;
                }});

            };
        }});
    }

}
