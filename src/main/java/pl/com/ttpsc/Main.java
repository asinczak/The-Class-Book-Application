package pl.com.ttpsc;

import pl.com.ttpsc.data.ListOfPersonsInSchool;
import pl.com.ttpsc.data.Student;
import pl.com.ttpsc.services.FileService;
import pl.com.ttpsc.services.GuardianService;
import pl.com.ttpsc.services.StudentService;

public class Main {

    public static void main(String[] args) {

//       StudentService ss = new StudentService();
//       ss.createStudent();
        GuardianService gs = new GuardianService();
        gs.createGuardian();

//        System.out.println(FileService.getInstance().readFileListOfPersons().getList());
    }
}
