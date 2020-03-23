package br.com.rubenskj.rabbit.core.events.eventconfiguration;

import org.springframework.context.ApplicationListener;

@FunctionalInterface
public interface IListener<E extends Event> extends ApplicationListener<E> {
    @Override
    default void onApplicationEvent(E event) {
        try {
            this.onInit(event);
            this.execute(event);
            this.onFinally(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void onInit(E event) throws Exception {
    }

    void execute(E event) throws Exception;

    default void onFinally(E event) throws Exception {
    }
}
