package pl.com.ttpsc.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayService {

    private static String SELSECT_ALL_GRADES_OF_STUDENT = "SELECT IdSubject, Grade FROM Subject_Grade WHERE IdStudent = ?";
    private static String SELECT_STUDENT_ABSENCES = "SELECT DateAbsence, IdSubject FROM Absences WHERE IdStudent = ?";

    StudentService studentService = StudentService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();


    public void displayAllGradesOfStudent () {
       int idStudent = studentService.pointTheStudent();

        try {
            PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELSECT_ALL_GRADES_OF_STUDENT);
            preparedStatement.setInt(1, idStudent);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idSubject = resultSet.getInt("IdSubject");
                int grade = resultSet.getInt("Grade");
                String subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(subjectName + " " + grade);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllStudentAbsences (){
        int idStudent = studentService.pointTheStudent();

        try {
            PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_STUDENT_ABSENCES);
            preparedStatement.setInt(1, idStudent);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String date = resultSet.getString("DateAbsence");
                int idSubject = resultSet.getInt("IdSubject");
                String subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(date +" "+subjectName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
