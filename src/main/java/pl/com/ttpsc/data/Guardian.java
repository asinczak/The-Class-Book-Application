package pl.com.ttpsc.data;

import java.util.ArrayList;
import java.util.List;

public class Guardian extends User {

    private String assignStudent = "";

    private List<Student> studentList;

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

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
