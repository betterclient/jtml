package io.github.betterclient.jtml.internal.display;

import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class FlexDisplay extends DisplayMode {
    static final DisplayMode INSTANCE = new FlexDisplay();

    public FlexDisplay() {
        super("flex");
    }

    @Override
    public void loadPositions(HTMLNode<?> node, ElementRenderingContext context) {
        List<HTMLNode<? extends Node>> children = new ArrayList<>(node.children);
        if (children.isEmpty()) {
            //Children-less elements are what I like to call... actually I don't call them anything
            return;
        }

        boolean row = node.style.calculate("flex-direction").equals("row");

        int currentX = 0;
        int currentY = 0;
        //Column/Row handler
        for (HTMLNode<? extends Node> child : children) {
            ElementDimensions dimensions = child.getDimensions(context);

            if (child.display$moveDown) {
                currentY += dimensions.height;

                continue;
            } else {
                if (row) {
                    if (children.indexOf(child) != 0) {
                        //Revert above currentY+= after moveDown
                        HTMLNode<? extends Node> before = children.get(children.indexOf(child) - 1);
                        if (!before.display$moveDown) {
                            currentY = 0;
                        }
                    }
                }
            }

            child.x = currentX;
            child.y = currentY;

            if (row) currentX += dimensions.width;
            else currentY += dimensions.height;
        }

        //justify-content, only works for row I think
        String justify_content = node.style.calculate("justify-content");

        //Precalculated/css or parent sizes
        int screenWidth = node.width > 0 ? node.width : context.screenWidth();
        int screenHeight = node.height > 0 ? node.height : context.screenHeight();

        if (justify_content.equals("center") && row) {
            currentX = (screenWidth - currentX) / 2;

            for (HTMLNode<? extends Node> child : children) {
                child.x = currentX;
                currentX += child.getDimensions(context).width;
            }
        } else if (justify_content.equals("right") && row) {
            currentX = screenWidth - currentX - 2; //...

            for (HTMLNode<? extends Node> child : children) {
                child.x = currentX;
                currentX += child.getDimensions(context).width;
            }
        }

        //Align-items
        String align_items = node.style.calculate("align-items");
        if (row) {
            currentY = 0;
            for (HTMLNode<? extends Node> child : children) {
                ElementDimensions dimensions = child.getDimensions(context);

                if (child.display$moveDown) {
                    currentY += dimensions.height;

                    continue;
                } else {
                    if (children.indexOf(child) != 0) {
                        //Revert above currentY+= after moveDown
                        HTMLNode<? extends Node> before = children.get(children.indexOf(child) - 1);
                        if (!before.display$moveDown) {
                            currentY = 0;
                        }
                    }
                }

                switch(align_items) {
                    case "start":
                        child.y = currentY;
                        break;
                    case "center":
                        child.y = ((screenHeight + currentY) - dimensions.height) / 2;
                        break;
                    case "end":
                        child.y = screenHeight - (dimensions.height * 2);
                }

            }
        } else {
            for (HTMLNode<? extends Node> child : children) {
                ElementDimensions dimensions = child.getDimensions(context);

                switch (align_items) {
                    case "start": break;
                    case "center":
                        child.x = (screenWidth - dimensions.width) / 2;
                        break;
                    case "end":
                        String s = child.style.calculate("width-offset");
                        s = s.replace("px", "");
                        float i = Float.parseFloat(s) * 2;

                        child.x = (int) (screenWidth - dimensions.width - i); //We need a lil offset here :)
                        break;
                }
            }
        }

        for (HTMLNode<? extends Node> child : children) {
            child.x -= node.x;
            child.y -= node.y;
        }
    }
}