package pl.com.ttpsc.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Guardian extends PersonInSchool {

    private String assignStudent = "";

    public String getAssignStudent () {
        return assignStudent;
    }

    public void setAssignStudent(String assignStudent) {
        this.assignStudent = assignStudent;
    }

    public String toString () {
        return "" +getName()+" "+getSurname()+" "+getWhoIs()+" "+getAssignStudent();

    }
}
