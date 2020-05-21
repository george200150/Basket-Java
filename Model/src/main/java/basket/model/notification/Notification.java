package basket.model.notification;


import basket.model.domain.Meci;

public class Notification {
    private NotificationType type;
    private Meci meci;

    public Notification() {
    }

    public Notification(NotificationType type) {
        this.type = type;
    }

    public Notification(NotificationType type, Meci meci) {
        this.type = type;
        this.meci = meci;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Meci getMeci() {
        return meci;
    }

    public void setMeci(Meci meci) {
        this.meci = meci;
    }
}
