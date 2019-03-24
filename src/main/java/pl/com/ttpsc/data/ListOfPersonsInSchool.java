package pl.com.ttpsc.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement (name = "List_of_persons")
@XmlAccessorType(XmlAccessType.NONE)
public class ListOfPersonsInSchool {

    private static final ListOfPersonsInSchool listOfPersons = new ListOfPersonsInSchool();

    private ListOfPersonsInSchool () {}

    public static ListOfPersonsInSchool getInstance(){
        return listOfPersons;
    }

    @XmlElement (name = "Person")
   public static List <PersonInSchool> list = new ArrayList<>();


    public List<PersonInSchool> getList() {
        return list;
    }
}
