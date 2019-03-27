package pl.com.ttpsc.services;
import org.sqlite.SQLiteException;
import pl.com.ttpsc.data.Roles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserService {

    static final String INSERT_USER = "INSERT INTO Users (Name, Surname, IdRole) VALUES (?, ?, ?)";
    static final String GET_ID_ROLE_FROM_ROLES = "SELECT Id FROM Roles WHERE RoleName = ?";
    static final String GET_ID_FROM_USER = "SELECT Id FROM Users WHERE Name = ? AND Surname = ?";
    static final String GET_ID_ROLE_FROM_USER = "SELECT IdRole FROM Users WHERE Name = ? AND Surname = ?";


    public static void addUserToTheDataBase (Roles roles) {
        String role = String.valueOf(roles);
        boolean checkingPerson = true;

        do {

            Scanner sc = new Scanner(System.in);
            System.out.println(DisplayService.ENTER_DATA_1);
            String name = sc.nextLine();

            System.out.println(DisplayService.ENTER_DATA_2);
            String surname = sc.nextLine();


            try {
                int roleId = getRoleIdFromRoles(role);
                PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_USER);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.setInt(3, roleId);

                preparedStatement.execute();
                checkingPerson = false;

            }catch (SQLiteException e) {
                System.out.println(DisplayService.WORNING_STATEMENT_1);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } while (checkingPerson);
    }

    public static int getRoleIdFromRoles(String role) throws SQLException {

        int roleId = 0;
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ID_ROLE_FROM_ROLES);
        preparedStatement.setString(1, role);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            roleId = resultSet.getInt("Id");
        }
        return roleId;
    }

    public static int getIdFromUser (String userName, String userSurname) throws SQLException {
        PreparedStatement preparedStatement2 = MenuService.getInstance().connection.prepareStatement(GET_ID_FROM_USER);
        preparedStatement2.setString(1, userName);
        preparedStatement2.setString(2, userSurname);

        int idUser = 0;
        ResultSet resultSet = preparedStatement2.executeQuery();
        while (resultSet.next()) {
            idUser = resultSet.getInt("Id");
        }
        return idUser;
    }

    public static int getRoleIdFromUsers (String userName, String userSurname) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ID_ROLE_FROM_USER);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, userSurname);

        int idUser = 0;
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            idUser = resultSet.getInt("IdRole");
        }
        return idUser;

    }


}
