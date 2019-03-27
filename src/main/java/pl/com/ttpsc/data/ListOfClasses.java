package pl.com.ttpsc.data;

import java.util.ArrayList;
import java.util.List;


public class ListOfClasses {

    private static final ListOfClasses listOfClasses = new ListOfClasses();

    private ListOfClasses () {}

    public static ListOfClasses getInstance(){
        return listOfClasses;
    }

   public static List <SchoolClass> schoolClassList = new ArrayList<>();

    public List<SchoolClass> getSchoolClassList() {
        return schoolClassList;
    }
}
