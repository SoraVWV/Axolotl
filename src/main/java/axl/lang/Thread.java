package axl.lang;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Thread {
    private Thread() {} // TODO:@SoraVWV threads

    static void $init() {
        try {
            Unsafe unsafe = axl.misc.Unsafe.getUnsafe();

            Field holderField = java.lang.Thread.class.getDeclaredField("holder");
            unsafe.putBoolean(holderField, 12, true);
            java.lang.Object holder = holderField.get(java.lang.Thread.currentThread());

            Field rootGroupField = holder.getClass().getDeclaredField("group");
            unsafe.putObject(holder, unsafe.objectFieldOffset(rootGroupField), new ThrowableHandler());
        } catch (java.lang.Exception ignored) {
        }
    }

    private static final class ThrowableHandler extends ThreadGroup {
        private static final List<UncaughtExceptionHandler> handlers = new ArrayList<>();

        private ThrowableHandler() {
            super("Axolotl Thread Group");
        }

        @Override
        public void uncaughtException(java.lang.Thread t, Throwable e) {
            if (e instanceof GotoException e1) {
                System.err.println("Attention! Moving to a non-existent label \"" + e1.getName() + "\"!");
                for (StackTraceElement s : e.getStackTrace())
                    System.err.println("|> " + s);
                System.exit(0);
            }

            for (UncaughtExceptionHandler it : handlers)
                if (it.uncaughtException(e))
                    return;

            if (e instanceof axl.lang.Exception) {
                System.err.println("I caught the exception \"axl.lang.Exception\"!");

                // TODO:@SoraVWV AxolotlException handlers

                return;
            }

            super.uncaughtException(t, e);
        }
    }

    public interface UncaughtExceptionHandler {
        boolean uncaughtException(Throwable e);

        String getName();
    }

    public static boolean addUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        for (UncaughtExceptionHandler i : ThrowableHandler.handlers)
            if (i.getName().equals(handler.getName()))
                return false;

        return ThrowableHandler.handlers.add(handler);
    }

    public static boolean removeUncaughtExceptionHandler(String handler) {
        for (UncaughtExceptionHandler i : ThrowableHandler.handlers) {
            if (i.getName().equals(handler)) {
                ThrowableHandler.handlers.remove(i);
                return true;
            }
        }

        return false;
    }
}
