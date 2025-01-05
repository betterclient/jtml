package io.github.betterclient.htmlutil.api.elements;

import io.github.betterclient.htmlutil.api.ElementStyle;
import io.github.betterclient.htmlutil.api.event.MouseClickHandler;

import java.util.Objects;
import java.util.function.Supplier;

public class HTMLElement<T extends io.github.betterclient.htmlutil.internal.elements.HTMLElement> {
    protected final T internal;
    protected final ElementStyle style;
    protected final HTMLDocument document;

    public HTMLElement(HTMLDocument document, Supplier<T> internal) {
        this.internal = internal.get();
        this.style = new ElementStyle(this.internal.style);
        this.document = Objects.requireNonNullElseGet(document, () -> (HTMLDocument) this);
    }

    /**
     * Append an element to this element
     * @param element element to append
     */
    public final void appendElement(HTMLElement<?> element) {
        internal.children.add(element.getInternal());
        document.internalToMapped.put(element.getInternal(), element);
        document.internal.reload();
    }

    /**
     * Set the html id of this element
     * @param id id to set
     */
    public final void setID(String id) {
        internal.instance.id(id);
        document.internal.reload();
    }

    T getInternal() {
        return internal;
    }

    public final ElementStyle getStyle() {
        return style;
    }

    /**
     * Register an on-click listener for this element (mouse up event)
     * @param click event target
     */
    public final void onMouseUp(MouseClickHandler click) {
        this.internal.mouseUp.add(click);
    }

    /**
     * Register an on-click listener for this element (mouse down event)
     * @param click event target
     */
    public final void onMouseDown(MouseClickHandler click) {
        this.internal.mouseDown.add(click);
    }
}
