package pl.com.ttpsc.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DisplayService {

    private static String SELSECT_ALL_GRADES_OF_STUDENT = "SELECT IdSubject, Grade FROM Subject_Grade WHERE IdStudent = ?";

    StudentService studentService = StudentService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();

    public void displayAllGradesOfStudent () {
       int idStudent = studentService.pointTheStudent();
       String subjectName = "";
        int idSubject = 0;
        int grade = 0;
        try {
            PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELSECT_ALL_GRADES_OF_STUDENT);
            preparedStatement.setInt(1, idStudent);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                idSubject = resultSet.getInt("IdSubject");
                grade = resultSet.getInt("Grade");
                subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(subjectName +" "+grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
