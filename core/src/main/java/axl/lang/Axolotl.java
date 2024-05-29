package axl.lang;

public final class Axolotl {

    private Axolotl(){}

    private static boolean $init = false;

    public static void $init() {
        if ($init)
            return;

        Thread.$init();
        Axolotl.$init = true;
    }

}
