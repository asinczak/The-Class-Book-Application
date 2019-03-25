package pl.com.ttpsc.services;

import pl.com.ttpsc.data.ListOfClasses;
import pl.com.ttpsc.data.PersonInSchool;
import pl.com.ttpsc.data.SchoolClass;
import java.util.List;
import java.util.Scanner;

public class ClassService {

    public void createClassWithTeacher () {
        boolean teacherExists = false;

        do {
            Scanner sc = new Scanner(System.in);
            System.out.println(DisplayService.ENTER_DATA_5);
            String className = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_6);
            String teacherName = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_7);
            String teacherSurname = sc.nextLine();

            List<PersonInSchool> list = FileService.getInstance().readFileListOfPersons().getList();
            for (int i = 0; i < list.size(); i++) {
                if (teacherName.equals(list.get(i).getName()) & teacherSurname.equals(list.get(i).getSurname())) {
                    String teacher = list.get(i).getName()+" "+list.get(i).getSurname();

                    SchoolClass schoolClass = new SchoolClass();
                    schoolClass.setNameClass(className);
                    schoolClass.setTeacher(teacher);
                    ListOfClasses.schoolClassList.add(schoolClass);
                    FileService.getInstance().writeFileListOfClasses();
                    teacherExists = true;

                }
            }

            if (teacherExists){
                System.out.println(DisplayService.CORRECT_STATEMENT_2);
                break;
            } else {
                System.out.println(DisplayService.WORNING_STATEMENT_2);
            }
        } while (true);
    }
}
