package pl.com.ttpsc;

import pl.com.ttpsc.data.ListOfPersonsInSchool;
import pl.com.ttpsc.data.Student;
import pl.com.ttpsc.data.Teacher;
import pl.com.ttpsc.services.*;

public class Main {

    public static void main(String[] args) {

//       StudentService ss = new StudentService();
//       ss.createStudent();
//        GuardianService gs = new GuardianService();
////        gs.createGuardian();

//        TeacherService teacher = new TeacherService();
//        teacher.createTeacher();

//        ClassService classService = new ClassService();
//        classService.createClassWithTeacher();

        StudentService ss = new StudentService();
        ss.addStudentToClass();
//        System.out.println(FileService.getInstance().readFileListOfPersons().getList());
    }
}
