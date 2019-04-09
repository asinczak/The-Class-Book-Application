package pl.com.ttpsc.services;

import pl.com.ttpsc.data.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    private static UserConverter userConverter;

    private UserConverter (){}

    public static UserConverter getInstance() {
        if (userConverter == null){
            userConverter = new UserConverter();
        }
        return userConverter;
    }
    UserService userService = UserService.getInstance();

    private static String SELECT_ALL_USERS = "SELECT Name, Surname, IdRole FROM Users";

    static List <User> list = new ArrayList<>();

    public List <User> convert () throws SQLException {
            ResultSet resultSet = selectAllUsers();
            while (resultSet.next()){
                String name = resultSet.getString("Name");
                String surname = resultSet.getString("Surname");
                int idRole = resultSet.getInt("IdRole");
                String whoIs = userService.getRoleNameFromRoles(idRole);
                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setWhoIs(whoIs);
                list.add(user);
            }
        return list;
    }

    public static ResultSet selectAllUsers () throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_ALL_USERS);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
