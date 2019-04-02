package pl.com.ttpsc.data;

import java.util.Arrays;
import java.util.List;

public enum Subject {

    MATHS("Maths"), SCIENCE("Science"), BIOLOGY("Biology"), PHYSICS("Physics"),
    CHEMISTRY("Chemistry"), GEOGREAPHY("Geography"), HISTORY("History"),
    PHYSICAL_EDUCATION("Physical education"), ART("Art"), MUSIC("Music"), ENGLISH("English");

    String value;

    Subject (String value) {
        this.value = value;
    }

    public String getValue (){
        return value;
    }

    public static boolean ifValueExists (String valueSubject){
        return Arrays.stream(values()).anyMatch(subject -> subject.getValue().equalsIgnoreCase(valueSubject));
    }
}

