package pl.com.ttpsc.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement (name = "School_class")
@XmlAccessorType (XmlAccessType.FIELD)
public class SchoolClass {

   private String nameClass;
   private String teacher;

   @XmlElement (name = "Student")
   public static List <String> studentList = new ArrayList<>();

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<String> getStudentList() {
        return studentList;
    }

    @Override
    public String toString() {
        return "Class: "+getNameClass()+" Teacher : "+getTeacher();
    }
}
