package pl.com.ttpsc.services;

import org.apache.log4j.Logger;
import pl.com.ttpsc.data.Subject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SubjectService {

    private static SubjectService subjectService;

    private SubjectService () {}

    public static SubjectService getInstance(){
        if ( subjectService == null){
            subjectService = new SubjectService();
        }
        return subjectService;
    }

    final static Logger logger = Logger.getLogger(SubjectService.class);

    static final String GET_ID_FROM_SUBJECTS = "SELECT Id FROM Subjects WHERE SubjectName = ?";
    static final String INSERT_ABSENCE_FROM_SUBJECT = "INSERT INTO Absences (DateAbsence, IdStudent, IdSubject) " +
            "VALUES (?, ?, ?)";
    static final String INSERT_NUMBER_OF_LESSONS_PER_YEAR = "UPDATE Subjects SET NumberOfLessons = ? WHERE SubjectName = ?";
    static final String GET_SUBJECT_FROM_ID = "SELECT SubjectName FROM Subjects WHERE Id = ?";
    static final String GET_NUMBER_OF_LESSONS_FOR_SUBJECT = "SELECT NumberOfLessons FROM Subjects WHERE SubjectName = ?";
    static final String DELETE_STUDENT_ABSENCE = "DELETE FROM Absences WHERE DateAbsence = ? AND IdStudent = ? AND IdSubject = ?";
    static final String GET_DATA_FROM_ABSENCES = "SELECT DateAbsence, IdStudent, IdSubject FROM Absences ";


    public int getIdFromSubject (String subject) throws SQLException {

        int idFromSubject = 0;
        if (Subject.ifValueExists(subject)) {
            PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ID_FROM_SUBJECTS);
            preparedStatement.setString(1, subject);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                idFromSubject = resultSet.getInt("Id");
            }
        }
        return idFromSubject;
    }

    public void insertAbsenceFromSubject (String date, int idStudent, int idSubject) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_ABSENCE_FROM_SUBJECT);
        preparedStatement.setString(1,date );
        preparedStatement.setInt(2, idStudent);
        preparedStatement.setInt(3, idSubject);
        preparedStatement.execute();
    }

    public void setNumberOfLessonsPerYear () throws SQLException {
        logger.debug("Setting number of lesson per year");
        String [] data = getDatatoSetNumberOfLessons();
        if (data[0].equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String subject = data[0];
            int numberOfLessons = Integer.parseInt(data[1]);
            insertNumberOfLessonsPerYear(numberOfLessons, subject);
            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
        }
    }

    public void insertNumberOfLessonsPerYear (int numberOfLessons, String subjectName) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_NUMBER_OF_LESSONS_PER_YEAR);
        preparedStatement.setInt(1,numberOfLessons);
        preparedStatement.setString(2, subjectName);
        preparedStatement.execute();
    }

    public String getSubjectFromId (int idSubject) throws SQLException {
        String subject = "";
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_SUBJECT_FROM_ID);
        preparedStatement.setInt(1, idSubject);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            subject = resultSet.getString("SubjectName");
        }

        return subject;
    }

    public int getNumberOfLessonsForSubject (String subject) throws SQLException {
        int numberOfLessons = 0;
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_NUMBER_OF_LESSONS_FOR_SUBJECT);
        preparedStatement.setString(1, subject);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            numberOfLessons = resultSet.getInt("NumberOfLessons");
        }
        return numberOfLessons;
    }

    public void deleteStudentAbsence (String date, int idStudent, int idSubject) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(DELETE_STUDENT_ABSENCE);
        preparedStatement.setString(1, date);
        preparedStatement.setInt(2, idStudent);
        preparedStatement.setInt(3, idSubject);
        preparedStatement.execute();
    }

    public boolean checkingIfSuchAbsenceExists (String date, int idStudent, int idSubject) throws SQLException {
        boolean checking = false;
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_DATA_FROM_ABSENCES);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String dateFromBase = resultSet.getString("DateAbsence");
            int idStudentFromBase = resultSet.getInt("IdStudent");
            int idSubjectFromBase = resultSet.getInt("IdSubject");
            if (dateFromBase.equals(date) & idStudentFromBase == idStudent & idSubjectFromBase == idSubject){
                checking = true;
            }
        }
        return checking;
    }

    public String [] getDatatoSetNumberOfLessons () throws SQLException {
        String [] data = new String[1];
        boolean checking = true;
        do{
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(GeneralMessages_en.ENTER_DATA_11);
                String subject = scanner.nextLine();

                if (subject.equalsIgnoreCase("x")) {
                    checking = false;
                    data[0] = subject;
                } else {

                    System.out.println(GeneralMessages_en.ENTER_DATA_16);
                    int numberOfLessons = scanner.nextInt();

                    int idSubject = getIdFromSubject(subject);
                    if (idSubject > 0) {
                        data = new String[2];
                        data[0] = subject;
                        data[1] = String.valueOf(numberOfLessons);
                        checking = false;
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_7);
                    }
                }
            }catch (InputMismatchException e){
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                logger.error(e.getMessage(), e);
            }

        }while (checking);
        return data;
    }
}


