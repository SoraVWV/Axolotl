package axl.lang;

public class GotoException extends InterruptedException {
    private final String name;
    private final Object value;

    public GotoException(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
