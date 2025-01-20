package io.github.betterclient.jtml.api.event;

public interface Event<T> {
    void handle(T event);
}
