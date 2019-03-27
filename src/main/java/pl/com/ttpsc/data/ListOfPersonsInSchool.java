package pl.com.ttpsc.data;

import java.util.ArrayList;
import java.util.List;

public class ListOfPersonsInSchool {

    private static final ListOfPersonsInSchool listOfPersons = new ListOfPersonsInSchool();

    private ListOfPersonsInSchool () {}

    public static ListOfPersonsInSchool getInstance(){
        return listOfPersons;
    }

   public static List <User> list = new ArrayList<>();


    public List<User> getList() {
        return list;
    }
}
