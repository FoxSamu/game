package samu.game;

public class ServiceException extends GameException {
    private final Service<?> service;

    public ServiceException(Service<?> service) {
        this(service, null, null);
    }

    public ServiceException(Service<?> service, String message) {
        this(service, message, null);
    }

    public ServiceException(Service<?> service, Throwable cause) {
        this(service, null, cause);
    }

    public ServiceException(Service<?> service, String message, Throwable cause) {
        super(message, cause);
        this.service = service;
    }

    public ServiceException(Service<?> service, boolean fatal) {
        this(service, fatal, null, null);
    }

    public ServiceException(Service<?> service, boolean fatal, String message) {
        this(service, fatal, message, null);
    }

    public ServiceException(Service<?> service, boolean fatal, Throwable cause) {
        this(service, fatal, null, cause);
    }

    public ServiceException(Service<?> service, boolean fatal, String message, Throwable cause) {
        super(fatal, message, cause);
        this.service = service;
    }

    public Service<?> service() {
        return service;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null) return "[in service " + service.id() + "] " + msg;
        return "in service " + service.id();
    }
}
