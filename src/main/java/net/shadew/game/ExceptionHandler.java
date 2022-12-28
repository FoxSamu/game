package net.shadew.game;

public interface ExceptionHandler<S> {
    void onException(Throwable exc, S source);
}
