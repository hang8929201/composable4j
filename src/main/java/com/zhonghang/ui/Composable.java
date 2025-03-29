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
            if (modifier.width > 0 && modifier.height > 0) {
                bufferedImage = new BufferedImage(modifier.width, modifier.height, BufferedImage.TYPE_INT_ARGB);
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

        Graphics2D graphics = bufferedImage == null ?
                null : (Graphics2D) bufferedImage.getGraphics();

        if (graphics != null && isContentChanged) {
            paint(graphics);
        }

        for (Composable composable : content) {
            composable.update(time);
            composable.alignmentTo(this);
            if (composable.bufferedImage != null && graphics != null &&
                    (isContentChanged || composable.isContentChanged)) {
                isContentChanged = true;
                BufferedImage image = Utils.createRoundedCornerImageV2(
                        composable.bufferedImage, composable.modifier.cornerRadius);
                graphics.drawImage(
                        image,
                        composable.topLeft[0],
                        composable.topLeft[1],
                        null);
                if (DEBUG_VIEW) {
                    graphics.setColor(Color.blue);
                    graphics.setStroke(new BasicStroke(1));
                    graphics.drawRect(
                            composable.topLeft[0],
                            composable.topLeft[1],
                            image.getWidth(),
                            image.getHeight()
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
        topLeft[0] = modifier.paddingStart;
        topLeft[1] = modifier.paddingTop;
        switch (modifier.alignment) {
            case TopCenter:
                topLeft[0] = (parent.modifier.width - modifier.width) / 2;
                break;
            case TopEnd:
                topLeft[0] = parent.modifier.width - modifier.width - modifier.paddingEnd;
                break;
            case Center:
                topLeft[0] = (parent.modifier.width - modifier.width) / 2;
                topLeft[1] = (parent.modifier.height - modifier.height) / 2;
                break;
            case CenterStart:
                topLeft[1] = (parent.modifier.height - modifier.width) / 2;
                break;
        }
    }

    protected void paint(Graphics2D graphics) {

        // clean
        Composite composite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, modifier.width, modifier.height);
        graphics.setComposite(composite);

        // draw background
        if (modifier.backgroundColor != null) {
            graphics.setColor(modifier.backgroundColor);
            graphics.fillRect(0, 0, modifier.width, modifier.height);
        }

        // draw behind
        if (modifier.drawBehind != null) {
            modifier.drawBehind.accept(graphics);
        }

        if (DEBUG_VIEW) {
            graphics.setColor(Color.red);
            graphics.setStroke(new BasicStroke(2));
            graphics.drawRect(0, 0, modifier.width, modifier.height);
        }
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
