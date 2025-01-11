package io.github.betterclient.htmlutil.internal.display;
import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Node;

public class FlexDisplay extends DisplayMode {
    public FlexDisplay() {
        super("flex");
    }

    @Override
    public void loadPositions(HTMLNode<?> node, ElementRenderingContext context) {
        if (node.children.isEmpty()) {
            //Children-less elements are what I like to call... actually I don't call them anything
            return;
        }

        boolean row = node.style.calculate("flex-direction").equals("row");

        node.x = 5;
        node.y = 5;

        int currentX = 0;
        int currentY = 0;
        //Column/Row handler
        for (HTMLNode<? extends Node> child : node.children) {
            ElementDimensions dimensions = child.getDimensions(context);

            if (child.display$moveDown) {
                currentY += dimensions.height;

                continue;
            } else {
                if (row) {
                    if (node.children.indexOf(child) != 0) {
                        //Revert above currentY+= after moveDown
                        HTMLNode<? extends Node> before = node.children.get(node.children.indexOf(child) - 1);
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

        //Precalculated/css or screen sizes
        int screenWidth = node.width <= 0 ? context.screenWidth() : node.width;
        int screenHeight = node.height <= 0 ? context.screenHeight() : node.height;

        if (justify_content.equals("center") && row) {
            currentX = (screenWidth - currentX) / 2;

            for (HTMLNode<? extends Node> child : node.children) {
                child.x = currentX;
                currentX += child.getDimensions(context).width;
            }
        } else if (justify_content.equals("right") && row) {
            currentX = screenWidth - currentX - 2; //...

            for (HTMLNode<? extends Node> child : node.children) {
                child.x = currentX;
                currentX += child.getDimensions(context).width;
            }
        }

        //Align-items
        String align_items = node.style.calculate("align-items");
        if (row) {
            currentY = 0;
            for (HTMLNode<? extends Node> child : node.children) {
                ElementDimensions dimensions = child.getDimensions(context);

                if (child.display$moveDown) {
                    currentY += dimensions.height;

                    continue;
                } else {
                    if (node.children.indexOf(child) != 0) {
                        //Revert above currentY+= after moveDown
                        HTMLNode<? extends Node> before = node.children.get(node.children.indexOf(child) - 1);
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
            for (HTMLNode<? extends Node> child : node.children) {
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
    }
}