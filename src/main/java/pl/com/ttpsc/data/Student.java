package pl.com.ttpsc.data;

import java.util.HashMap;
import java.util.Map;

public class Student extends User {

   private int grade;

   private Map<Subject, Integer> gradeBook = new HashMap<>();

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Map<Subject, Integer> getGradeBook() {
        return gradeBook;
    }
}
