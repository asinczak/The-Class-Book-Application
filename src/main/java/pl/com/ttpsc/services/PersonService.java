package pl.com.ttpsc.services;

import pl.com.ttpsc.data.ListOfPersonsInSchool;
import pl.com.ttpsc.data.PersonInSchool;

import java.util.List;
import java.util.Scanner;

public class PersonService {

    FileService fileService = FileService.getInstance();
    Scanner sc = new Scanner(System.in);

    String name = "";
    String surname = "";


    public void createPerson (PersonInSchool personInSchool, String whoIs){
            personInSchool.setName(name);
            personInSchool.setSurname(surname);
            personInSchool.setWhoIs(whoIs);
        ListOfPersonsInSchool.list.add(personInSchool);
            fileService.writeFileListOfPersons();
    }

    public boolean checkingIfPersonExists () {
        boolean checking = false;

        System.out.println(DisplayService.enterData1);
        name = sc.nextLine();

        System.out.println(DisplayService.enterData2);
        surname = sc.nextLine();

        List <PersonInSchool> list = fileService.readFileListOfPersons().getList();
        for (PersonInSchool person : list) {
            if (name.equals(person.getName()) & surname.equals(person.getSurname())) {
                checking = true;
            }
        }


        return checking;
    }
}
