package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Student;

public class StudentService extends PersonService{


    public void createStudent (){
        if (!checkingIfPersonExists()){
             Student student = new Student();
             createPerson(student, DisplayService.student);
        } else {
            System.out.println(DisplayService.worningStatement1);
        }
    }
}
