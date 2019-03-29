package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Roles;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TeacherService extends UserService {

    static final String ASSIGN_TEACHER_TO_CLASS = "UPDATE Classes SET Teacher = ? WHERE ClassName = ?";

    public void createTeacher (){
        Scanner sc = new Scanner(System.in);
        System.out.println(GeneralMessages_en.ENTER_DATA_1);
        String name = sc.nextLine();

        System.out.println(GeneralMessages_en.ENTER_DATA_2);
        String surname = sc.nextLine();
        UserService.addUserToTheDataBase(Roles.TEACHER, name, surname);
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
                if (UserService.checkingIfUserExists(teacherName, teacherSurname)) {
                    if (UserService.checkingIfIsTeacher(teacherName, teacherSurname)) {
                        int idClass = UserService.getIdFromClasses(className);
                        if (idClass > 0) {
                            int idTeacher = UserService.getIdFromUser(teacherName, teacherSurname);
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
}
