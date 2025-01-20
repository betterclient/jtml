package io.github.betterclient.htmlutil.api.event;

import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;

public record MouseClickEvent(ElementRenderingContext context, double mouseX, double mouseY, int button) { }