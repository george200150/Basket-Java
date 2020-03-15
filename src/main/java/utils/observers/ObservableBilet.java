package utils.observers;

import utils.events.BiletChangeEvent;

public interface ObservableBilet<E extends BiletChangeEvent> {
    void addObserverBilet(BiletObserver<E> e);
    void removeObserverBilet(BiletObserver<E> e);
    void notifyObserversBilet(E t);
}