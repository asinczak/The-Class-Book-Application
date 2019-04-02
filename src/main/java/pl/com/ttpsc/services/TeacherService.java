package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Roles;
import pl.com.ttpsc.data.SchoolClass;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TeacherService {

    private static TeacherService teacherService;

    private TeacherService (){}

    public static TeacherService getInstance(){
        if (teacherService == null){
            teacherService = new TeacherService();
        }
        return teacherService;
    }

    ClassService classService = ClassService.getInstance();
    UserService userService = UserService.getInstance();

    static final String ASSIGN_TEACHER_TO_CLASS = "UPDATE Classes SET Teacher = ? WHERE ClassName = ?";
    static final String GET_TEACHER_FROM_ID = "SELECT Name, Surname FROM Users WHERE Id = ?";

    public void createTeacher (){
        boolean checking = true;

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_1);
            String name = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_2);
            String surname = scanner.nextLine();

            name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
            surname = surname.substring(0,1).toUpperCase() + surname.substring(1).toLowerCase();

            try {
                if (userService.addUserToTheDataBase(Roles.TEACHER, name, surname)){
                    System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
                    int idUser = userService.getIdFromUser(name, surname);
                    userService.insertNewUserIntoLogon(name, surname, idUser);
                    checking = false;
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } while (checking);
    }

    public void asssignTeacherToCless (){
        boolean whileGoes = true;

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_6);
            String teacherName = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_7);
            String teacherSurname = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_10);
            String className = scanner.nextLine();

            try {
                if (userService.checkingIfUserExists(teacherName, teacherSurname)) {
                    if (checkingIfIsTeacher(teacherName, teacherSurname)) {
                        int idClass = classService.getIdFromClasses(className);
                        if (idClass > 0) {
                            int idTeacher = userService.getIdFromUser(teacherName, teacherSurname);
                            updateClassAddTeacherId(idTeacher, className);
                            whileGoes = false;
                        } else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_5);
                        }

                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_6);
                    }
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                }
            } catch (SQLException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
        } while (whileGoes);
    }

    public void updateClassAddTeacherId (int idClass, String className) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(ASSIGN_TEACHER_TO_CLASS);
        preparedStatement.setInt(1, idClass);
        preparedStatement.setString(2, className);
        preparedStatement.execute();
    }

    public String getTeacherFromId (int id) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_TEACHER_FROM_ID);
        preparedStatement.setInt(1, id);
        String nameSurname = "" ;
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            nameSurname = name+" "+surname;
        }
        return nameSurname;
    }

    public boolean checkingIfIsTeacher (String name, String surname) throws SQLException {
        boolean checking;
        String role = String.valueOf(Roles.TEACHER);
        int roleId = userService.getRoleIdFromRoles(role);
        int roleIdUser = userService.getRoleIdFromUsers(name, surname);
        if (roleId == roleIdUser){
            checking = true;
        }else {
            checking = false;
        }
        return checking;
    }

    public List<SchoolClass> getListClassWithTeacher () {
        List <SchoolClass> list = new ArrayList<>();
        try {
            ResultSet resultSet = classService.getClassAndTeacher();
            while (resultSet.next()){
                String className = resultSet.getString("ClassName");
                int idTeacher = resultSet.getInt("Teacher");
                String teacher = teacherService.getTeacherFromId(idTeacher);
                SchoolClass schoolClass = new SchoolClass();
                schoolClass.setNameClass(className);
                schoolClass.setTeacher(teacher);
                int idClass = classService.getIdFromClasses(className);
                schoolClass.setStudentList(classService.getListOfStudentfromIdClass(idClass));
                list.add(schoolClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
