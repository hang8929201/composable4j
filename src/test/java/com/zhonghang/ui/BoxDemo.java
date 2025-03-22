package com.zhonghang.ui;

import java.awt.*;

public class BoxDemo {

    public static void main(String[] args) {
        new JFrameComposableApp(new Box() {{
            modifier.size(800, 800);
            contentSupplier = content -> {
                content.add(new Text() {{
                    text = "Hello World!";
                    font = new Font("宋体", Font.PLAIN, 20);
                    modifier.alignment = Alignment.Center;
                }});
            };
        }});
    }

}
