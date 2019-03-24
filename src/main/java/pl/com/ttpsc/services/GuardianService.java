package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Guardian;
import pl.com.ttpsc.data.PersonInSchool;

import java.util.List;
import java.util.Scanner;

public class GuardianService extends PersonService{

    public void createGuardian (){
        if (!checkingIfPersonExists()){
            Guardian guardian = new Guardian();

            Scanner sc = new Scanner(System.in);
            System.out.println(DisplayService.enterData3);
            String nameOfassignStudent = sc.nextLine();
            System.out.println(DisplayService.enterData4);
            String surnameOfassignStudent = sc.nextLine();

            List <PersonInSchool> list = FileService.getInstance().readFileListOfPersons().getList();
            for (PersonInSchool person : list) {
                if (nameOfassignStudent.equals(person.getName()) & surnameOfassignStudent.equals(person.getSurname())) {
                    String assignStudent = person.getName() + " " + person.getSurname();
                    guardian.setAssignStudent(assignStudent);
                }
            }

            createPerson(guardian, DisplayService.guardian);
        } else {
            System.out.println(DisplayService.worningStatement1);
        }
    }
}
