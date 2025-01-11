package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.DocumentScreenOptions;
import io.github.betterclient.htmlutil.internal.css.CSSStyle;
import io.github.betterclient.htmlutil.internal.css.compiler.CompiledStyleSheet;
import io.github.betterclient.htmlutil.internal.css.compiler.StyleSheetCompiler;
import io.github.betterclient.htmlutil.internal.display.DisplayMode;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.DocumentRenderer;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import io.github.betterclient.htmlutil.internal.render.UIRenderingContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTMLDocument extends HTMLElement {
    List<Map<String, CompiledStyleSheet>> styleSheets = new ArrayList<>();

    public HTMLDocument(String src) {
        super(null, Jsoup.parseBodyFragment(src).body());
        this.loadStyleElements();
    }

    public void openAsScreen(DocumentScreenOptions options) {
        this.reloadCSS();
        new DocumentRenderer(this, options).open();
    }

    private void reloadCSS() {
        Map<Node, CSSStyle> map = this.createCSSMap();
        Element instance = this.instance;

        for (Map<String, CompiledStyleSheet> compilation : styleSheets) {
            for (String s : compilation.keySet()) {
                for (Element element : instance.select(s)) {
                    for (Map.Entry<String, String> stringStringEntry : compilation.get(s).getProperties().entrySet()) {
                        //System.out.println("For " + s + " -> " + stringStringEntry.getKey() + "=" + stringStringEntry.getValue());v
                        CSSStyle style = map.get(element);
                        style.MAP.put(stringStringEntry.getKey(), stringStringEntry.getValue());
                    }
                }
            }
        }

        loadPositions(this);
    }

    public void loadPositions(HTMLNode<?> node) {
        for (HTMLNode<? extends Node> child : node.children) {
            loadPositions(child);
        }

        DisplayMode mode = DisplayMode.create(node.style);
        mode.loadPositions(node, new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT));
    }

    @Override
    public void render(ElementRenderingContext context) {
        //The document has no rendering.
        //The document stays invisible for transparency.
    }
    //TODO: add document.renderAsHud()

    private void loadStyleElements() {
        for (Element element : this.instance.select("style")) {
            this.addStyleSheet(element.data());
        }
    }

    public void addStyleSheet(String src) {
        styleSheets.add(StyleSheetCompiler.compile(src));
        this.reloadCSS();
    }

    public Map<Node, CSSStyle> createCSSMap() {
        Map<Node, CSSStyle> map = new HashMap<>();

        this.appendChildren(this, map);

        return map;
    }

    private void appendChildren(HTMLNode<?> node, Map<Node, CSSStyle> styleMap) {
        for (HTMLNode<? extends Node> child : node.children) {
            this.appendChildren(child, styleMap);
        }

        styleMap.put(node.instance, node.style);
    }

    @Override
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(io.github.betterclient.htmlutil.api.elements.HTMLDocument document) {
        return null; //nuh uh
    }

    public void reloadInlineCSS(HTMLNode<?> findDocument) {
        String[] list = findDocument.instance.attr("style").split("[:;]");
        if (list.length != 0 && list.length != 1) {
            for (int i = 0; i < list.length; i+=2) {
                findDocument.style.MAP.put(list[i].trim(), list[i+1].trim());
            }
        }

        for (HTMLNode<? extends Node> child : findDocument.children) {
            reloadInlineCSS(child);
        }
    }
}
