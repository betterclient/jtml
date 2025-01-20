package io.github.betterclient.jtml.api.event;

import io.github.betterclient.jtml.internal.render.ElementRenderingContext;

public record MouseClickEvent(ElementRenderingContext context, double mouseX, double mouseY, int button) { }