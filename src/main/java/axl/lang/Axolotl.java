package axl.lang;

public final class Axolotl {
    private static boolean $init = false;

    private Axolotl() { }

    public static void $init() {
        if ($init)
            return;
        Thread.$init();
        Axolotl.$init = true;
    }
}
