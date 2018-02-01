package pl.java.scalatech;

public final class Profiles {

    public static final String DEV = "dev";
    public static final String TEST = "test";
    public static final String PROD = "prod";

    private Profiles() {
        throw new AssertionError();
    }
}
