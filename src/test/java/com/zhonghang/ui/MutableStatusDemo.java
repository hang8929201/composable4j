package com.zhonghang.ui;

import java.awt.*;
import java.util.Arrays;

public class MutableStatusDemo {

    public static void main(String[] args) {

        Composable.DEBUG_VIEW = false;

        new JFrameComposableApp(new Box() {{
            Composable root = this;
            modifier.size(800, 800);
            modifier.backgroundColor = Color.cyan;
            MutableStatusList<Boolean> select = new MutableStatusList<>(
                    this, Arrays.asList(false, false, false, false, false));
            String[] texts = {"new game", "archive", "settings", "exit", "profile"};
            contentSupplier = content -> {
                content.add(new Text() {{
                    text = "DEMO GAME TITLE";
                    textColor = Color.black;
                    font = new Font("宋体", Font.PLAIN, 60);
                    modifier.alignment = Alignment.TopCenter;
                    modifier.paddingTop = 100;
                }});
                for (Integer index : Utils.rangeOf(0, texts.length)) {
                    content.add(new Text() {{
                        text = texts[index];
                        textColor = Color.black;
                        font = new Font("宋体", Font.PLAIN, select.getValue(index) ? 40 : 20);
                        modifier.alignment = Alignment.TopCenter;
                        modifier.paddingTop = 200 + 60 * index - (select.getValue(index) ? 10 : 0);
                        modifier.onMouseEnter = () -> select.setValue(index, true);
                        modifier.onMouseExit = () -> select.setValue(index, false);
                        modifier.onClicked = () -> {
                            if (index == 3) {
                                System.exit(0);
                            } else if (index == 4) {
                                System.out.println(root.profile());
                            }
                        };
                    }});
                }
            };
        }});
    }

}
