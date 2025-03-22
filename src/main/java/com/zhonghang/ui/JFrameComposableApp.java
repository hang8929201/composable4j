package com.zhonghang.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ArrayBlockingQueue;

public class JFrameComposableApp {

    private final ArrayBlockingQueue<Runnable> runnableLists = new ArrayBlockingQueue<>(100);

    private final Composable composable;

    private boolean isMousePressed;

    public JFrameComposableApp(Composable composable) {

        this.composable = composable;
        MouseAdapter mouseAdapter = getMouseAdapter();

        JPanel jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 0, composable.modifier.width, composable.modifier.height);
                g.drawImage(composable.bufferedImage, 0, 0, null);
            }
        };
        jPanel.setSize(composable.modifier.width, composable.modifier.height);
        jPanel.setPreferredSize(new Dimension(composable.modifier.width, composable.modifier.height));
        jPanel.addMouseListener(mouseAdapter);
        jPanel.addMouseMotionListener(mouseAdapter);

        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        new Thread(() -> {
            long last = System.currentTimeMillis();
            while (true) {
                long current = System.currentTimeMillis();
                // fps max 100
                if (current - last > 10) {
                    last = current;
                    composable.update(current);
                    if (composable.isContentChanged) {
                        composable.isContentChanged = false;
                        jPanel.repaint();
                    }
                }
                if (!runnableLists.isEmpty()) {
                    runnableLists.poll().run();
                }
            }
        }).start();
    }

    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isMousePressed = true;
                mouseStatusChange(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMousePressed = false;
                mouseStatusChange(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseStatusChange(e);
            }

            private void mouseStatusChange(MouseEvent e) {
                MouseStatusChangeEvent event = new MouseStatusChangeEvent(isMousePressed, e.getX(), e.getY());
                runnableLists.offer(() -> composable.handleMouseEvent(event));
            }
        };
    }
}
