package axl.lang;

public final class GotoUtils {
    public static void go(String label) throws GotoException {
        go(label, null);
    }

    public static void go(String label, Object value) throws GotoException {
        throw new GotoException(label, value);
    }

    public static Object label(String name, Runnable block) throws GotoException {
        try {
            block.run();
            return null;
        } catch (GotoException e) {
            if (e.getName().equals(name))
                return e.getValue();
            throw e;
        }
    }

    public static Object label(String name, Supplier block) throws GotoException {
        try {
            return block.get();
        } catch (GotoException e) {
            if (e.getName().equals(name))
                return e.getValue();
            throw e;
        }
    }

    @FunctionalInterface
    public interface Runnable {
        void run() throws GotoException;
    }

    @FunctionalInterface
    public interface Supplier {
        Object get() throws GotoException;
    }
}
