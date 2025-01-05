package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.DocumentScreenOptions;
import io.github.betterclient.htmlutil.internal.css.CSSStyle;
import io.github.betterclient.htmlutil.internal.css.compiler.CompiledStyleSheet;
import io.github.betterclient.htmlutil.internal.css.compiler.StyleSheetCompiler;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.DocumentRenderer;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
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
                        map.get(element).MAP.put(stringStringEntry.getKey(), stringStringEntry.getValue());
                        //System.out.println("For " + s + " -> " + stringStringEntry.getKey() + "=" + stringStringEntry.getValue());
                    }
                }
            }
        }
    }

    @Override
    public void render(ElementRenderingContext context) {
        //The document has no rendering.
        //The document stays invisible for transparency.
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
}
