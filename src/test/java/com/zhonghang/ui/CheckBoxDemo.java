package com.zhonghang.ui;

import java.awt.*;

public class CheckBoxDemo {

    public static void main(String[] args) {
        new JFrameComposableApp(new Box() {{
            modifier.size(400, 500);
            modifier.backgroundColor = Color.white;
            identifier = "root";
            contentSupplier = content -> {
                content.add(new Text() {{
                    text = "TODO LIST:";
                    font = new Font("宋体", Font.PLAIN, 40);
                    modifier.paddingTop = 100;
                    modifier.alignment = Alignment.TopCenter;
                }});
                String[] items = {"洗衣", "做饭", "跑步", "弹琴"};
                for (Integer i : Utils.rangeOf(0, items.length)) {
                    String item = items[i];
                    int paddingTop = i * 50 + 170;
                    content.add(new Box() {{
                        identifier = "inner box";
                        modifier.size(200, 50);
                        modifier.backgroundColor = Color.white;
                        modifier.paddingTop = paddingTop;
                        modifier.alignment = Alignment.TopCenter;
                        MutableStatus<Boolean> checked = new MutableStatus<>(this, false);
                        contentSupplier = content -> {
                            content.add(new Image() {{
                                resource = checked.getValue() ? "checkboxchecked.png" : "checkbox.png";
                                modifier.size(40, 40);
                                modifier.onClicked = () -> checked.setValue(!checked.getValue());
                                modifier.alignment = Alignment.CenterStart;
                            }});
                            content.add(new Text() {{
                                text = item;
                                font = new Font("宋体", Font.PLAIN, 20);
                                modifier.paddingStart = 50;
                                modifier.alignment = Alignment.CenterStart;
                            }});
                        };
                    }});
                }
            };
        }});
    }
}
