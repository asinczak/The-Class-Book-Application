package pl.com.ttpsc.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement (name = "List_of_classes")
@XmlAccessorType (XmlAccessType.NONE)
public class ListOfClasses {

    private static final ListOfClasses listOfClasses = new ListOfClasses();

    private ListOfClasses () {}

    public static ListOfClasses getInstance(){
        return listOfClasses;
    }

    @XmlElement (name = "School_Class")
   public static List <SchoolClass> schoolClassList = new ArrayList<>();

    public List<SchoolClass> getSchoolClassList() {
        return schoolClassList;
    }
}
