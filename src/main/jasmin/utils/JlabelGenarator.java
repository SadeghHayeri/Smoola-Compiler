package jasmin.utils;

public class JlabelGenarator {
    private static final String PREFIX = "Label";
    private static int counter = 0;

    public static String unique() {
        return PREFIX + "_" + counter++;
    }

    public static String unique(String name) {
        return name + "_" + counter++;
    }
}
