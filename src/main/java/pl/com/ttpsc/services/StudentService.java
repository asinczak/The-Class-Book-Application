package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Student;

import java.util.Scanner;

public class StudentService {

    FileService fileService = FileService.getInstance();
    Scanner sc = new Scanner(System.in);

public void createStudent (){
    Student student = new Student();
    System.out.println("Please enter student's name :");
    String name = sc.nextLine();
    System.out.println("Please enter student's surname :");
    String surname = sc.nextLine();
    student.setName(name);
    student.setSurname(surname);
    student.setWhoIs("Student");
    fileService.writePerson(student);
}
}
