package utils.observers;

import utils.events.MeciChangeEvent;

public interface ObservableMeci<E extends MeciChangeEvent> {
    void addObserverMeci(MeciObserver<E> e);
    void removeObserverMeci(MeciObserver<E> e);
    void notifyObserversMeci(E t);
}