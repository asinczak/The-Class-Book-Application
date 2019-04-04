package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Guardian;
import pl.com.ttpsc.data.Roles;
import pl.com.ttpsc.data.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuardianService {

    private static GuardianService guardianService;

    private GuardianService () {}

    public static GuardianService getInstance() {
        if (guardianService == null){
            guardianService = new GuardianService();
        }
        return guardianService;
    }

    static final String INSERT_GUARDIAN_TO_STUDENT = "UPDATE Users SET IdGuardian = ? WHERE Name = ? AND Surname = ?";
    static final String ASSIGN_STUDENT_TO_GUARDIAN = "UPDATE Users SET IdGuardian = ? WHERE Name = ? AND Surname = ?";
    static final String SELECT_STUDENT_ASSSIGN_TO_GUARDIAN = "SELECT Name, Surname FROM Users WHERE IdGuardian = ?";
    static final String SELECT_GRADES_FROM_ASSIGN_STUDENT = "SELECT C.SubjectName, D.Grade FROM Subjects AS C, Subject_Grade AS D WHERE C.Id = D.IdSubject AND D.IdStudent = ?";


    StudentService studentService = StudentService.getInstance();
    UserService userService = UserService.getInstance();
    TeacherService teacherService = TeacherService.getInstance();
    LogonService logonService = LogonService.getInstance();
    ExcusesService excusesService = ExcusesService.getInstance();

   String nameGuardian = "";
   String surnameGuardian = "";
   String nameStudent = "";
   String surnameStudent = "";

    public void createGuardian (){

            boolean checkingPerson = true;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_1);
            nameGuardian = sc.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_2);
            surnameGuardian = sc.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_3);
            nameStudent = sc.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_4);
            surnameStudent = sc.nextLine();

            nameGuardian = nameGuardian.substring(0,1).toUpperCase() + nameGuardian.substring(1).toLowerCase();
            surnameGuardian = surnameGuardian.substring(0,1).toUpperCase() + surnameGuardian.substring(1).toLowerCase();
            nameStudent = nameStudent.substring(0,1).toUpperCase() + nameStudent.substring(1).toLowerCase();
            nameGuardian = nameGuardian.substring(0,1).toUpperCase() + nameGuardian.substring(1).toLowerCase();

            try {
                if(!userService.checkingIfUserExists(nameGuardian, surnameGuardian)) {
                    if (userService.checkingIfUserExists(nameStudent, surnameStudent)) {
                        if (studentService.checkingIfIsStudent(nameStudent, surnameStudent)) {
                            userService.addUserToTheDataBase(Roles.GUARDIAN, nameGuardian, surnameGuardian);
                            insertGuardianToStudent();
                            int idUser = userService.getIdFromUser(nameGuardian, surnameGuardian);
                            userService.insertNewUserIntoLogon(nameGuardian, surnameGuardian, idUser);
                            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
                            checkingPerson = false;
                        } else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
                        }
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                    }
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_1);
                }

            } catch (SQLException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }

        } while (checkingPerson);

    }


    public void insertGuardianToStudent () throws SQLException {
        PreparedStatement preparedStatement3 = MenuService.getInstance().connection.prepareStatement(INSERT_GUARDIAN_TO_STUDENT);
        preparedStatement3.setInt(1, userService.getIdFromUser(nameGuardian, surnameGuardian));
                preparedStatement3.setString(2, nameStudent);
                preparedStatement3.setString(3, surnameStudent);
        preparedStatement3.execute();
    }

    public boolean checkingIfIsGuardian (String name, String surname) throws SQLException {
        boolean checking;
        String role = String.valueOf(Roles.GUARDIAN);
        int roleId = userService.getRoleIdFromRoles(role);
        int roleIdUser = userService.getRoleIdFromUsers(name, surname);
        if (roleId == roleIdUser) {
            checking = true;
        } else {
            checking = false;
        }
        return checking;
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
                if (userService.checkingIfUserExists(guardianName, guardianSurname)){
                    if (guardianService.checkingIfIsGuardian(guardianName, guardianSurname)){
                        if (userService.checkingIfUserExists(studentName, studentSurname)){
                            if (studentService.checkingIfIsStudent(studentName, studentSurname)){
                                int idGuardian = userService.getIdFromUser(guardianName, guardianSurname);
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

    public List <Student> getListOfStudents (int idUser) throws SQLException {
        List <Student> studentList = new ArrayList<>();

        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_STUDENT_ASSSIGN_TO_GUARDIAN);
        preparedStatement.setInt(1, idUser);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            Student student = new Student();
            student.setName(name);
            student.setSurname(surname);
            studentList.add(student);
            Guardian guardian = new Guardian();
            guardian.setStudentList(studentList);
        }
        return studentList;
    }

    public ResultSet selectGradesFromAssignStudent (int idStudent) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_GRADES_FROM_ASSIGN_STUDENT);
        preparedStatement.setInt(1, idStudent);

        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public void sendAnExcuseToTeacher () {
        boolean checking = true;

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_24);
            String teacherName = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_25);
            String teacherSurname = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_26);
            String excuse = scanner.nextLine();

            try {
                if (userService.checkingIfUserExists(teacherName, teacherSurname)) {
                    if (teacherService.checkingIfIsTeacher(teacherName, teacherSurname)) {
                        int idGuardian = logonService.getIdUserWhoHasLogged();
                        int idTeacher = userService.getIdFromUser(teacherName, teacherSurname);
                        excusesService.insertMessageToTeacher(idGuardian, idTeacher, excuse, "NEW");
                        checking = false;
                        System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);

                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_6);
                    }
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }while (checking);
    }

}
