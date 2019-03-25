package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Guardian;
import pl.com.ttpsc.data.PersonInSchool;

import java.util.List;
import java.util.Scanner;

public class GuardianService extends PersonService{

    public void createGuardian (){
        boolean studentExist = false;

        if (!checkingIfPersonExists()){
            Guardian guardian = new Guardian();
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println(DisplayService.ENTER_DATA_3);
            String nameOfassignStudent = sc.nextLine();
            System.out.println(DisplayService.ENTER_DATA_4);
            String surnameOfassignStudent = sc.nextLine();

            List <PersonInSchool> list = FileService.getInstance().readFileListOfPersons().getList();
            for (int i = 0; i < list.size(); i++) {
                if (nameOfassignStudent.equals(list.get(i).getName()) & surnameOfassignStudent.equals(list.get(i).getSurname())) {
                    String assignStudent = list.get(i).getName() + " " + list.get(i).getSurname();
                    guardian.setAssignStudent(assignStudent);
                    createPerson(guardian, DisplayService.GUARDIAN);
                    studentExist = true;
                }
            }

            if (studentExist) {
                System.out.println(DisplayService.CORRECT_STATEMENT_1);
                break;
            } else {
                System.out.println(DisplayService.WORNING_STATEMENT_2);
            }
        }while (true);

        } else {
            System.out.println(DisplayService.WORNING_STATEMENT_1);
        }
    }


}
