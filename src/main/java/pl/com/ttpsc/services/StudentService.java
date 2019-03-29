package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Roles;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentService extends UserService {

    private static StudentService studentService;

    private StudentService () {}

    public static StudentService getInstance() {
        if (studentService == null){
            studentService = new StudentService();
        }
        return studentService;
    }

    SubjectService subjectService = SubjectService.getInstance();

    static final String ASSIGN_STUDENT_TO_CLASS = "UPDATE Users SET IdClass = ? WHERE Name = ? AND Surname = ?";
    static final String INSERT_GRADE_TO_STUDENT = "INSERT INTO Subject_Grade (IdSubject, IdStudent, Grade) VALUES (?, ?, ?)";
    static final String UPDATE_STUDENT_GRADE = "UPDATE Subject_Grade SET Grade = ? WHERE IdSubject = ? AND IdStudent = ?";
    static final String ASSIGN_STUDENT_TO_GUARDIAN = "UPDATE Users SET IdGuardian = ? WHERE Name = ? AND Surname = ?";

    public void addStudentToDataBase () {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GeneralMessages_en.ENTER_DATA_1);
        String name = scanner.nextLine();

        System.out.println(GeneralMessages_en.ENTER_DATA_2);
        String surname = scanner.nextLine();
        UserService.addUserToTheDataBase(Roles.STUDENT, name, surname);
    }


    public void asignStudentToClass() {
        boolean whileGoes = true;

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_3);
            String studentName = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_4);
            String studentSurname = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_10);
            String className = scanner.nextLine();

            try {
                if (UserService.checkingIfUserExists(studentName, studentSurname)) {
                    if (UserService.checkingIfIsStudent(studentName, studentSurname)){
                        int idClass = UserService.getIdFromClasses(className);
                        if (idClass > 0) {
                            updateStudentAddClassId(idClass, studentName, studentSurname);
                            whileGoes = false;
                        } else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_5);
                        }
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
                    }
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                }
            } catch (SQLException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
        } while (whileGoes);
    }

    public void updateStudentAddClassId (int idClass, String name, String surname) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(ASSIGN_STUDENT_TO_CLASS);
        preparedStatement.setInt(1, idClass);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, surname);
        preparedStatement.execute();
    }

    public void addStudentGrade () {
       boolean checking = true;
       int grade = 0;

       do {
           Scanner scanner = new Scanner(System.in);
           System.out.println(GeneralMessages_en.ENTER_DATA_1);
           String studentName = scanner.nextLine();

           System.out.println(GeneralMessages_en.ENTER_DATA_2);
           String studentSurname = scanner.nextLine();

           System.out.println(GeneralMessages_en.ENTER_DATA_11);
           String subject = scanner.nextLine();

           System.out.println(GeneralMessages_en.ENTER_DATA_12);
           grade = scanner.nextInt();

           try {
               if (UserService.checkingIfUserExists(studentName, studentSurname)){
                   if (UserService.checkingIfIsStudent(studentName, studentSurname)) {
                       if (grade > 0 & grade < 7) {
                           int idSubject = subjectService.getIdFromSubject(subject);
                           if (idSubject > 0) {
                               int idStudent = UserService.getIdFromUser(studentName, studentSurname);
                               insertGradeToStudent(idSubject, idStudent, grade);
                               checking = false;
                           } else {
                               System.out.println(GeneralMessages_en.WORNING_STATEMENT_7);
                           }
                       }else {
                           System.out.println(GeneralMessages_en.WORNING_STATEMENT_8);
                       }
                   } else {
                       System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
                   }
               } else {
                   System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }


       } while (checking);
    }

    public void insertGradeToStudent (int idSubject, int idStudent, int grade) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_GRADE_TO_STUDENT);
        preparedStatement.setInt(1, idSubject);
        preparedStatement.setInt(2, idStudent);
        preparedStatement.setInt(3, grade);
        preparedStatement.execute();
    }

    public void changeGradeFromSubject () {
        boolean checking = true;
        int grade = 0;

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_1);
            String studentName = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_2);
            String studentSurname = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_11);
            String subject = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_12);
            grade = scanner.nextInt();

            try {
                if (UserService.checkingIfUserExists(studentName, studentSurname)){
                    if (UserService.checkingIfIsStudent(studentName, studentSurname)) {
                        if (grade > 0 & grade < 7) {
                            int idSubject = subjectService.getIdFromSubject(subject);
                            if (idSubject > 0) {
                                int idStudent = UserService.getIdFromUser(studentName, studentSurname);
                                updateStudentGrade(grade, idSubject, idStudent);
                                checking = false;
                            } else {
                                System.out.println(GeneralMessages_en.WORNING_STATEMENT_7);
                            }
                        }else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_8);
                        }
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
                    }
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } while (checking);
    }

    public void updateStudentGrade (int grade, int idSubject, int idStudent) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(UPDATE_STUDENT_GRADE);
        preparedStatement.setInt(1, grade);
        preparedStatement.setInt(2, idSubject);
        preparedStatement.setInt(3, idStudent);
        preparedStatement.execute();
    }

    public void asignStudentToGuardian () {
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_13);
            String guardianName = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_14);
            String guardianSurname = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_3);
            String studentName = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_4);
            String studentSurname = scanner.nextLine();

            try {
                if (UserService.checkingIfUserExists(guardianName, guardianSurname)){
                    if (UserService.checkingIfIsGuardian(guardianName, guardianSurname)){
                        if (UserService.checkingIfUserExists(studentName, studentSurname)){
                            if (UserService.checkingIfIsStudent(studentName, studentSurname)){
                                int idGuardian = UserService.getIdFromUser(guardianName, guardianSurname);
                                updateIdGuardian(idGuardian, studentName, studentSurname);
                                checking = false;
                            } else {
                                System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
                            }
                        } else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                        }
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_9);
                    }
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } while (checking);
    }

    public void updateIdGuardian (int idGuardian, String studentName, String studentSurname) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(ASSIGN_STUDENT_TO_GUARDIAN);
        preparedStatement.setInt(1, idGuardian);
        preparedStatement.setString(2, studentName);
        preparedStatement.setString(3, studentSurname);
        preparedStatement.execute();
    }

    public int pointTheStudent () {
        boolean checking = true;
        int idStudent = 0;

       do {
           Scanner scanner = new Scanner(System.in);
           System.out.println(GeneralMessages_en.ENTER_DATA_1);
           String studentName = scanner.nextLine();

           System.out.println(GeneralMessages_en.ENTER_DATA_2);
           String studentSurname = scanner.nextLine();

           try {
               if (UserService.checkingIfUserExists(studentName, studentSurname)) {
                   if (UserService.checkingIfIsStudent(studentName, studentSurname)) {
                       idStudent = UserService.getIdFromUser(studentName, studentSurname);
                       checking = false;
                   } else {
                       System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
                   }
               } else {
                   System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }while (checking);
        return idStudent;
    }
}
