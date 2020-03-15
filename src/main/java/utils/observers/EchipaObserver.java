package utils.observers;

import utils.events.EchipaChangeEvent;

public interface EchipaObserver<E extends EchipaChangeEvent> {
    void updateEchipa(E e);
}
