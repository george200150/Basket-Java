package utils.observers;

import utils.events.EchipaChangeEvent;

public interface ObservableEchipa<E extends EchipaChangeEvent> {
    void addObserverEchipa(EchipaObserver<E> e);
    void removeObserverEchipa(EchipaObserver<E> e);
    void notifyObserversEchipa(E t);
}