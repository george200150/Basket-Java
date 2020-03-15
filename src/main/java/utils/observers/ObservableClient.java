package utils.observers;

import utils.events.ClientChangeEvent;

public interface ObservableClient<E extends ClientChangeEvent> {
    void addObserverClient(ClientObserver<E> e);
    void removeObserverClient(ClientObserver<E> e);
    void notifyObserversClient(E t);
}