package pl.com.ttpsc.services;
import org.sqlite.SQLiteException;
import pl.com.ttpsc.data.Roles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserService {

    private static UserService userService;

    private UserService () {}

    public static UserService getInstance() {
        if (userService == null){
            userService = new UserService();
        }
        return userService;
    }

    static final String INSERT_USER = "INSERT INTO Users (Name, Surname, IdRole) VALUES (?, ?, ?)";
    static final String GET_ID_ROLE_FROM_ROLES = "SELECT Id FROM Roles WHERE RoleName = ?";
    static final String GET_ID_FROM_USER = "SELECT Id FROM Users WHERE Name = ? AND Surname = ?";
    static final String GET_ID_ROLE_FROM_USER = "SELECT IdRole FROM Users WHERE Name = ? AND Surname = ?";
    static final String GET_USER_NAME_SURNAME_FROM_USERS = "SELECT Name, Surname FROM Users WHERE Name = ? AND Surname = ?";
    static final String GET_ROLE_FROM_ROLES = "SELECT RoleName FROM Roles WHERE Id = ?";


    public void addUserToTheDataBase (Roles roles, String name, String surname) {
        String role = String.valueOf(roles);
        boolean checkingPerson = true;

        do {
            try {
                if (!checkingIfUserExists(name, surname)) {
                    int roleId = getRoleIdFromRoles(role);
                    PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_USER);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, surname);
                    preparedStatement.setInt(3, roleId);

                    preparedStatement.execute();
                    checkingPerson = false;
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_1);
                }

            }catch (SQLiteException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_1);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } while (checkingPerson);
    }

    public int getRoleIdFromRoles(String role) throws SQLException {

        int roleId = 0;
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ID_ROLE_FROM_ROLES);
        preparedStatement.setString(1, role);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            roleId = resultSet.getInt("Id");
        }
        return roleId;
    }

    public int getIdFromUser (String userName, String userSurname) throws SQLException {
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

    public int getRoleIdFromUsers (String userName, String userSurname) throws SQLException {
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

    public boolean checkingIfUserExists (String name, String surname) throws SQLException {
        boolean checking;
        String nameFromDataBase = "";
        String surnameFromDataBase = "";

        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_USER_NAME_SURNAME_FROM_USERS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, surname);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            nameFromDataBase = resultSet.getString("Name");
            surnameFromDataBase = resultSet.getString("Surname");
        }

        if (name.equals(nameFromDataBase) & surname.equals(surnameFromDataBase)){
            checking = true;
        } else {
            checking = false;
        }

        return checking;
    }

    public String getRoleNameFromRoles (int idRole) throws SQLException {
        String roleName = "";
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ROLE_FROM_ROLES);
        preparedStatement.setInt(1, idRole);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            roleName = resultSet.getString("RoleName");
        }
        return roleName;
    }
}
