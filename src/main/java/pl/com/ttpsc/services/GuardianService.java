package pl.com.ttpsc.services;

import org.sqlite.SQLiteException;
import pl.com.ttpsc.data.Roles;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

   StudentService studentService = StudentService.getInstance();
   UserService userService = UserService.getInstance();

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

            try {
                if(!userService.checkingIfUserExists(nameGuardian, surnameGuardian)) {
                    if (userService.checkingIfUserExists(nameStudent, surnameStudent)) {
                        if (studentService.checkingIfIsStudent(nameStudent, surnameStudent)) {
                            userService.addUserToTheDataBase(Roles.GUARDIAN, nameGuardian, surnameGuardian);
                            insertGuardianToStudent();
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

            }catch (SQLiteException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_1);

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

}
