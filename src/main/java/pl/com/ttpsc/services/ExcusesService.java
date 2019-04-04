package pl.com.ttpsc.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExcusesService {

    private static ExcusesService excusesService;

    private ExcusesService () {}

    public static ExcusesService getInstance(){
        if (excusesService == null){
            excusesService = new ExcusesService();
        }
        return excusesService;
    }

    static final String INSERT_MESSAGE_TO_TEACHER = "INSERT INTO Excuses (IdGuardian, IdTeacher, Message, Status) VALUES (?,?,?,?)";
    static final String GET_NEW_EXCUSES_FROM_GUARDIAN = "SELECT IdGuardian, IdTeacher, Message FROM Excuses WHERE Status = 'NEW'";
    static final String GET_ALL_MESSAGES_FORM_EXCUSES = "SELECT Id, Message, Status FROM EXCUSES WHERE IdTeacher = ?";
    static final String GET_ID_FROM_EXCUSES = "SELECT Id FROM Excuses WHERE IdTeacher = ?";
    static final String UPDATE_STATUS_OF_EXCUSE = "UPDATE Excuses SET Status = ? WHERE Id = ?";
    static final String DELETE_AN_EXCUSE = "DELETE FROM Excuses WHERE Id = ?";

    public void insertMessageToTeacher (int idGuardian, int idTeacher, String message, String status) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_MESSAGE_TO_TEACHER);
        preparedStatement.setInt(1, idGuardian);
        preparedStatement.setInt(2, idTeacher);
        preparedStatement.setString(3, message);
        preparedStatement.setString(4, status);
        preparedStatement.execute();
    }

    public ResultSet selectDataFromNewExcuses () throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_NEW_EXCUSES_FROM_GUARDIAN);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public ResultSet getAllMessagesFromExcuses (int idTeacher) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ALL_MESSAGES_FORM_EXCUSES);
        preparedStatement.setInt(1, idTeacher );

        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public List<Integer> getListIdFormExcuses (int idTeacher) throws SQLException {
        List <Integer> idList = new ArrayList<>();
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ID_FROM_EXCUSES);
        preparedStatement.setInt(1, idTeacher);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt("Id");
            idList.add(id);
        }
        return idList;
    }

    public void updateStatusOfExcuse(String status, int id) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(UPDATE_STATUS_OF_EXCUSE);
        preparedStatement.setString(1, status);
        preparedStatement.setInt(2, id);
        preparedStatement.execute();
    }

    public void deleteAnExcuse (int id) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(DELETE_AN_EXCUSE);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }
}
