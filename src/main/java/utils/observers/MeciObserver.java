package utils.observers;

import utils.events.MeciChangeEvent;

public interface MeciObserver<E extends MeciChangeEvent> {
    void updateMeci(E e);
}