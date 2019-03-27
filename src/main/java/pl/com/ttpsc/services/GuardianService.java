package pl.com.ttpsc.services;

import org.sqlite.SQLiteException;
import pl.com.ttpsc.data.Roles;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class GuardianService extends UserService {

   static final String ISERT_DATA_TO_GUARDIAN = "INSERT INTO Users (Name, Surname, IdRole) VALUES (?, ?, ?)";
   static final String INSERT_GUARDIAN_TO_STUDENT = "UPDATE Users SET IdGuardian = ? WHERE Name = ? AND Surname = ?";

   String nameGuardian = "";
   String surnameGuardian = "";
   String nameStudent = "";
   String surnameStudent = "";

    public void createGuardian (){

            boolean checkingPerson = true;
        do {
            Scanner sc = new Scanner(System.in);

            System.out.println(DisplayService.ENTER_DATA_1);
            nameGuardian = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_2);
            surnameGuardian = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_3);
            nameStudent = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_4);
            surnameStudent = sc.nextLine();


            try {
                if (checkingIfIsStudent()) {
                    insertDataToGuardian();
                    insertGuardianToStudent();
                    checkingPerson = false;
                } else {
                    System.out.println(DisplayService.WORNING_STATEMENT_3);
                }

            }catch (SQLiteException e) {
                System.out.println(DisplayService.WORNING_STATEMENT_1);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } while (checkingPerson);

    }

    public void insertDataToGuardian () throws SQLException {
        String role = String.valueOf(Roles.GUARDIAN);
        int roleId = UserService.getRoleIdFromRoles(role);

        PreparedStatement preparedStatement1 = MenuService.getInstance().connection.prepareStatement(ISERT_DATA_TO_GUARDIAN);
        preparedStatement1.setString(1, nameGuardian);
        preparedStatement1.setString(2, surnameGuardian);
        preparedStatement1.setInt(3, roleId);
        preparedStatement1.execute();

    }



    public void insertGuardianToStudent () throws SQLException {
        PreparedStatement preparedStatement3 = MenuService.getInstance().connection.prepareStatement(INSERT_GUARDIAN_TO_STUDENT);
        preparedStatement3.setInt(1, UserService.getIdFromUser(nameGuardian, surnameGuardian));
                preparedStatement3.setString(2, nameStudent);
                preparedStatement3.setString(3, surnameStudent);
        preparedStatement3.execute();
    }

    public boolean checkingIfIsStudent () throws SQLException {
        boolean checking;
        String role = String.valueOf(Roles.STUDENT);
        int roleId = UserService.getRoleIdFromRoles(role);
        int roleIdUser = UserService.getRoleIdFromUsers(nameStudent, surnameStudent);
        if (roleId == roleIdUser){
            checking = true;
        } else {
            checking = false;
        }
        return checking;
    }


}
