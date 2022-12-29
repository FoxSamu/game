package samu.game;

public class ModuleException extends GameException {
    private final Module<?> module;

    public ModuleException(Module<?> module) {
        this(module, null, null);
    }

    public ModuleException(Module<?> module, String message) {
        this(module, message, null);
    }

    public ModuleException(Module<?> module, Throwable cause) {
        this(module, null, cause);
    }

    public ModuleException(Module<?> module, String message, Throwable cause) {
        super(message, cause);
        this.module = module;
    }

    public ModuleException(Module<?> module, boolean fatal) {
        this(module, fatal, null, null);
    }

    public ModuleException(Module<?> module, boolean fatal, String message) {
        this(module, fatal, message, null);
    }

    public ModuleException(Module<?> module, boolean fatal, Throwable cause) {
        this(module, fatal, null, cause);
    }

    public ModuleException(Module<?> module, boolean fatal, String message, Throwable cause) {
        super(fatal, message, cause);
        this.module = module;
    }

    public Module<?> module() {
        return module;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null) return "[in module " + module.id() + "] " + msg;
        return "in module " + module.id();
    }
}
