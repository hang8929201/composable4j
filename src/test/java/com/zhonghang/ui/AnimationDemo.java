package com.zhonghang.ui;

import java.awt.*;

public class AnimationDemo {

    public static void main(String[] args) {
        new JFrameComposableApp(new Box() {{
            identifier = "root";
            modifier.size(800, 800);
            modifier.backgroundColor = Color.white;
            AnimateStatus<Integer> animatedY = new AnimateStatus<>(
                    this, 0, t -> (int) ((t / 10) % 800));
            contentSupplier = content -> {
                content.add(new Box() {{
                    modifier.size(100, 100);
                    modifier.paddingStart = 350;
                    modifier.paddingTop = animatedY.getValue();
                    modifier.backgroundColor = Color.BLACK;
                }});
            };
        }});
    }
}
