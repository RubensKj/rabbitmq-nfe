package br.com.rubenskj.rabbit.core.events.eventconfiguration;

@FunctionalInterface
public interface ListenerAdapter<E extends Event> extends IListener<E> {
    @Override
    default void onInit(E event) throws Exception {
    }

    @Override
    void execute(E event) throws Exception;

    @Override
    default void onFinally(E event) throws Exception {
    }
}
