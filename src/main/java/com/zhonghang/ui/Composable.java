package com.zhonghang.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Composable {

    public static boolean DEBUG_VIEW = false;

    public Modifier modifier = new Modifier();
    public Consumer<List<Composable>> contentSupplier;
    public List<Composable> content;
    public List<AnimateStatus<?>> animateStatuses = new ArrayList<>();
    public BufferedImage bufferedImage;
    public String identifier;

    // metrics
    public int paintTotalTime;
    public int paintCount;
    public int supplyTotalTime;
    public int supplyCount;

    public int[] topLeft = {0, 0};
    public int[] size;

    // status
    public boolean isStatusChanged;
    public boolean isContentChanged;
    private Boolean isMouseIn;
    private boolean isMousePressed;

    public void update(long time) {

        for (AnimateStatus<?> animateStatus : animateStatuses) {
            animateStatus.update(time);
        }

        if (content == null) {
            isStatusChanged = true;
        }

        if (bufferedImage == null) {
            size = getSize();
            if (size[0] > 0 && size[1] > 0) {
                bufferedImage = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_ARGB);
            }
            isContentChanged = true;
        }

        if (isStatusChanged) {
            isStatusChanged = false;
            content = new ArrayList<>();
            if (contentSupplier != null) {
                supplyCount++;
                long s = System.currentTimeMillis();
                contentSupplier.accept(content);
                supplyTotalTime += System.currentTimeMillis() - s;
            }
            isContentChanged = true;
        }

        long s = System.currentTimeMillis();
        Graphics2D graphics = bufferedImage == null ? null : (Graphics2D) bufferedImage.getGraphics();
        if (graphics != null && isContentChanged) {
            paint(graphics);
        }

        for (Composable composable : content) {
            composable.update(time);
            composable.alignmentTo(this);
            if (composable.bufferedImage != null && graphics != null &&
                    (isContentChanged || composable.isContentChanged)) {
                isContentChanged = true;
                graphics.drawImage(
                        composable.bufferedImage,
                        composable.topLeft[0],
                        composable.topLeft[1],
                        null);
                if (DEBUG_VIEW) {
                    graphics.setColor(Color.blue);
                    graphics.setStroke(new BasicStroke(1));
                    graphics.drawRect(
                            composable.topLeft[0],
                            composable.topLeft[1],
                            composable.bufferedImage.getWidth(),
                            composable.bufferedImage.getHeight()
                    );
                }
                composable.isContentChanged = false;
            }
        }
        if (isContentChanged) {
            paintCount++;
            paintTotalTime += System.currentTimeMillis() - s;
        }
    }

    public void alignmentTo(Composable parent) {
        switch (modifier.alignment) {
            case TopCenter:
                topLeft[0] = (parent.size[0] - size[0]) / 2;
                break;
            case Center:
                topLeft[0] = (parent.size[0] - size[0]) / 2;
                topLeft[1] = (parent.size[1] - size[1]) / 2;
                break;
        }
    }

    protected void paint(Graphics2D graphics) {
        Composite composite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, size[0], size[1]);
        graphics.setComposite(composite);
        if (modifier.backgroundColor != null) {
            graphics.setColor(modifier.backgroundColor);
            graphics.fillRect(modifier.paddingStart, modifier.paddingTop, modifier.width, modifier.height);
        }
        if (modifier.drawBehind != null) {
            modifier.drawBehind.accept(graphics);
        }
        if (DEBUG_VIEW) {
            graphics.setColor(Color.red);
            graphics.setStroke(new BasicStroke(2));
            graphics.drawRect(modifier.paddingStart, modifier.paddingTop, modifier.width, modifier.height);
        }
    }

    private int[] getSize() {
        int[] size = {modifier.width, modifier.height};
        size[0] += modifier.paddingStart;
        size[0] += modifier.paddingEnd;
        size[1] += modifier.paddingTop;
        size[1] += modifier.paddingBottom;
        return size;
    }

    public void handleMouseEvent(MouseStatusChangeEvent mouseEvent) {

        isMouseIn = true;
        if (isMousePressed) {
            if (!mouseEvent.isPressed) {
                Runnable onClicked = modifier.onClicked;
                if (onClicked != null) {
                    onClicked.run();
                }
            }
        }
        isMousePressed = mouseEvent.isPressed;

        for (Composable composable : content) {
            int[] topLeft = Utils.minus(mouseEvent.topLeft, composable.topLeft);
            if (Utils.checkIn(topLeft, composable.modifier)) {
                if (composable.isMouseIn == null) {
                    composable.isMouseIn = true;
                }
                if (!composable.isMouseIn) {
                    Runnable onMouseEnter = composable.modifier.onMouseEnter;
                    if (onMouseEnter != null) {
                        onMouseEnter.run();
                    }
                }
                composable.handleMouseEvent(new MouseStatusChangeEvent(mouseEvent.isPressed, topLeft));
            } else {
                if (composable.isMouseIn == null) {
                    composable.isMouseIn = false;
                }
                if (composable.isMouseIn) {
                    Runnable onMouseExit = composable.modifier.onMouseExit;
                    if (onMouseExit != null) {
                        onMouseExit.run();
                    }
                }
            }
        }
    }

    public Map<String, Object> profile() {
        Map<String, Object> map = new HashMap<>();
        map.put("supply avg time", supplyTotalTime == 0 ? 0 : supplyTotalTime / supplyCount);
        map.put("supply count", supplyCount);
        map.put("paint avg time", paintTotalTime == 0 ? 0 : paintTotalTime / paintCount);
        map.put("paint count", paintCount);
        if (content != null && !content.isEmpty()) {
            map.put("content", content.stream().map(Composable::profile).collect(Collectors.toList()));
        }
        return map;
    }
}
