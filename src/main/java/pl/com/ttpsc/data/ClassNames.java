package pl.com.ttpsc.data;

import java.util.Arrays;

public enum ClassNames {
    MATHEMATICAL("Mathematical"), LINGUAL ("Lingual"), NATURAL_SCIENTIFIC("Natural scientific"),
    SPORTING("Sporting"), GENERAL("General");

    String value;

     ClassNames (String className){
        this.value = className;
    }

    public String getValue() {
        return value;
    }

    public static ClassNames getByValue(String value) {
        return Arrays.stream(values()).filter(cn -> cn.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
    }

    public static boolean ifValueExists (String valueClass) {
        return Arrays.stream(values()).anyMatch(cn -> cn.getValue().equalsIgnoreCase(valueClass));
    }
}
