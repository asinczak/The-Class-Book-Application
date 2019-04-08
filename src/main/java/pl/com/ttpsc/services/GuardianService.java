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

    public void createGuardian () throws SQLException {

        String enteredDataForGuardian = userService.getNameAndSurnameFromUser();
        String eneterdDataForStudent = studentService.getNameAndSurnameAndCheckForStudent();

        if(enteredDataForGuardian.equalsIgnoreCase("x") || eneterdDataForStudent.equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String[] studentData = eneterdDataForStudent.split(" ");
            nameStudent = studentData[0];
            surnameStudent = studentData[1];
            String[] guardianData = enteredDataForGuardian.split(" ");
            nameGuardian = guardianData[0];
            surnameGuardian = guardianData[1];

            userService.addUserToTheDataBase(Roles.GUARDIAN, nameGuardian, surnameGuardian);
            insertGuardianToStudent();
            int idUser = userService.getIdFromUser(nameGuardian, surnameGuardian);
            userService.insertNewUserIntoLogon(nameGuardian, surnameGuardian, idUser);
            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
        }

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

    public void asignStudentToGuardian () throws SQLException {
        String enteredDataForGuardian = getNameAndSurnameForGuardian();
        String eneterdDataForStudent = studentService.getNameAndSurnameAndCheckForStudent();

        if(enteredDataForGuardian.equalsIgnoreCase("x") || eneterdDataForStudent.equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String[] studentData = eneterdDataForStudent.split(" ");
            nameStudent = studentData[0];
            surnameStudent = studentData[1];
            String[] guardianData = enteredDataForGuardian.split(" ");
            nameGuardian = guardianData[0];
            surnameGuardian = guardianData[1];

            int idGuardian = userService.getIdFromUser(nameGuardian, surnameGuardian);
            updateIdGuardian(idGuardian, nameStudent, surnameStudent);
            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
        }
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

    public void sendAnExcuseToTeacher () throws SQLException {
        String enteredDataForTeacher = teacherService.getNameAndSurnameAndCheckForTeacher();

        if(enteredDataForTeacher.equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String[] teacherData = enteredDataForTeacher.split(" ");
            String teacherName = teacherData[0];
            String teacherSurname = teacherData[1];
            String excuse = getExcuseFromGuardian();

            int idGuardian = logonService.getIdUserWhoHasLogged();
            int idTeacher = userService.getIdFromUser(teacherName, teacherSurname);
            excusesService.insertMessageToTeacher(idGuardian, idTeacher, excuse, "NEW");
            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
        }
    }

    public String getNameAndSurnameForGuardian () throws SQLException {
        boolean checking = true;
        String returnData = "";

        do {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(GeneralMessages_en.ENTER_DATA_13);
                String enteredData = scanner.nextLine();

                if (enteredData.equalsIgnoreCase("x")) {
                    checking = false;
                    returnData = enteredData;
                } else {

                    String[] tab = enteredData.split(" ");
                    String name = tab[0];
                    String surname = tab[1];

                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    surname = surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase();

                    if (userService.checkingIfUserExists(name, surname)) {
                        if(checkingIfIsGuardian(name,surname)) {
                            checking = false;
                            returnData = "" + name + " " + surname;
                        } else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_9);
                        }
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                    }
                }

            }catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
        } while (checking) ;
        return returnData;
    }

    public String getExcuseFromGuardian () {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GeneralMessages_en.ENTER_DATA_26);
        String excuse = scanner.nextLine();
        return excuse;
    }
}
