package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Teacher;

public class TeacherService extends PersonService {


    public void createTeacher (){
        if (!checkingIfPersonExists()){
            Teacher teacher = new Teacher();
            createPerson(teacher, DisplayService.TEACHER);
        } else {
            System.out.println(DisplayService.WORNING_STATEMENT_1);
        }
    }
}
