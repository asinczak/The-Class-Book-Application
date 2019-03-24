package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Teacher;

public class TeacherService extends PersonService {


    public void createTeacher (){
        if (!checkingIfPersonExists()){
            Teacher teacher = new Teacher();
            createPerson(teacher, DisplayService.teacher);
        } else {
            System.out.println(DisplayService.worningStatement1);
        }
    }
}
