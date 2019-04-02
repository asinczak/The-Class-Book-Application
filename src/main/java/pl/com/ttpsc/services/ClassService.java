package pl.com.ttpsc.services;

import pl.com.ttpsc.data.ClassNames;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassService {

    private static ClassService classService;

    private ClassService () {}

    public static ClassService getInstance() {
        if (classService == null){
            classService = new ClassService();
        }
        return classService;
    }


    static final String GET_CLASS_AND_TEACHER = "Select ClassName, Teacher FROM Classes";
    static final String GET_STUDENTS_FROM_CLASS = "SELECT Name, Surname FROM Users WHERE IdClass = ?";
    static final String GET_ID_FROM_CLASESS = "SELECT Id FROM Classes WHERE ClassName = ?";


    public ResultSet getClassAndTeacher () throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_CLASS_AND_TEACHER);

        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public ResultSet selectStudentsFromClass (int idClass) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_STUDENTS_FROM_CLASS);
        preparedStatement.setInt(1, idClass);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public List<String> getListOfStudentfromIdClass (int idClass) throws SQLException {
        ResultSet resultSet = selectStudentsFromClass(idClass);
        List<String> list = new ArrayList<>();
        while (resultSet.next()){
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String name_surname = name+" "+surname;
            list.add(name_surname);
        }
        return list;
    }

    public int getIdFromClasses (String className) throws SQLException {

        int idFromClasses = 0;
        if (ClassNames.ifValueExists(className)) {
            PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ID_FROM_CLASESS);
            preparedStatement.setString(1, className);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                idFromClasses = resultSet.getInt("Id");
            }
        }
        return idFromClasses;
    }
}
