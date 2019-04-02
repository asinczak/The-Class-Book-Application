package pl.com.ttpsc.data;

import java.util.ArrayList;
import java.util.List;

public class SchoolClass {

   private String nameClass;
   private String teacher;

   public List <String> studentList = new ArrayList<>();

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

    public void setStudentList(List<String> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "Class: "+getNameClass()+" Teacher : "+getTeacher();
    }
}
