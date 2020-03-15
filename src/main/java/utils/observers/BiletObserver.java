package utils.observers;

import utils.events.BiletChangeEvent;

public interface BiletObserver<E extends BiletChangeEvent> {
    void updateBilet(E e);
}
