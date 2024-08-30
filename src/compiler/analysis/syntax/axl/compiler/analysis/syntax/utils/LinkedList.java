package axl.compiler.analysis.syntax.utils;

public class LinkedList<T> {

    private final LinkedList<T> prev;
    public final T value;

    public LinkedList(LinkedList<T> prev, T value) {
        this.prev = prev;
        this.value = value;
    }

    public boolean contains(T value) {
        if (this.value == value)
            return true;
        if (this.prev == null)
            return false;
        return this.prev.contains(value);
    }
}
