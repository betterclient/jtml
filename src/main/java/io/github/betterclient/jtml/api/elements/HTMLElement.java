package io.github.betterclient.jtml.api.elements;

import io.github.betterclient.jtml.api.ElementStyle;
import io.github.betterclient.jtml.api.event.Event;
import io.github.betterclient.jtml.api.event.MouseClickHandler;
import lombok.Getter;

import java.util.Objects;
import java.util.function.Supplier;

public class HTMLElement<T extends io.github.betterclient.jtml.internal.nodes.HTMLElement> {
    @Getter protected final T internal;
    @Getter protected final ElementStyle style;
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
        internal.instance.appendChild(element.internal.instance);
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

    public final void addEventListener(String listener, Event<?> event) {
        switch (listener.toLowerCase()) {
            case "mousedown", "onmousedown":
                onMouseDown((MouseClickHandler) event);
                break;
            case "mouseup", "onmouseup":
                onMouseUp((MouseClickHandler) event);
                break;
        }
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
