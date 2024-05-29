package axl.misc;

import java.lang.reflect.Field;

public final class Unsafe {

    private static sun.misc.Unsafe theUnsafe;

    public static sun.misc.Unsafe getUnsafe() {
        if (theUnsafe == null) {
            try {
                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                theUnsafe = (sun.misc.Unsafe) unsafeField.get(null);
            } catch (Exception ignored) {
            }
        }
        return theUnsafe;
    }

}
