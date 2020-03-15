package utils.observers;

import utils.events.ClientChangeEvent;

public interface ClientObserver<E extends ClientChangeEvent> {
    void updateClient(E e);
}
