package io.github.betterclient.htmlutil.api.event;

public interface Event<T> {
    void handle(T event);
}
