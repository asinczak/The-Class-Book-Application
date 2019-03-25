package pl.com.ttpsc.services;

import pl.com.ttpsc.data.PersonInSchool;
import pl.com.ttpsc.data.SchoolClass;
import pl.com.ttpsc.data.Student;

import java.util.List;
import java.util.Scanner;

public class StudentService extends PersonService{

    public void createStudent (){
        if (!checkingIfPersonExists()){
             Student student = new Student();
             createPerson(student, DisplayService.STUDENT);
        } else {
            System.out.println(DisplayService.WORNING_STATEMENT_1);
        }
    }

    public void addStudentToClass () {
        boolean checkingStudent = false;

        do {
            Scanner sc = new Scanner(System.in);
            System.out.println(DisplayService.ENTER_DATA_3);
            String studentName = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_4);
            String studentSurname = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_8);
            String className = sc.nextLine();

            List<PersonInSchool> listOfPersons = FileService.getInstance().readFileListOfPersons().getList();

            for (int i = 0; i < listOfPersons.size(); i++) {

                if (studentName.equals(listOfPersons.get(i).getName()) & studentSurname.equals(listOfPersons.get(i).getSurname())) {
                    String student = listOfPersons.get(i).getName() + " " + listOfPersons.get(i).getSurname();
                    List <SchoolClass> classList = FileService.getInstance().readFileListOfClasses().getSchoolClassList();
                    for (int y = 0; y < classList.size(); y++){
                    if (className.equals(classList.get(y).getNameClass())) {
                        SchoolClass.studentList.add(student);
                        FileService.getInstance().writeFileListOfClasses();
                        checkingStudent = true;
                        }
                    }
                }
            }

            if (checkingStudent){
                System.out.println(DisplayService.CORRECT_STATEMENT_1);
                break;
            } else  {
                System.out.println(DisplayService.WORNING_STATEMENT_3);
            }


        } while (true);
    }
}
