package pl.com.ttpsc.services;

import pl.com.ttpsc.data.SettingsAfterLogon;
import pl.com.ttpsc.data.User;

import java.io.Console;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LogonService {

    private static LogonService logonService;

    private LogonService () {}

    public static LogonService getInstance() {
        if (logonService == null){
            logonService = new LogonService();
        }
        return logonService;
    }

    SettingService settingService = SettingService.getInstance();
    UserService userService = UserService.getInstance();
    UserConverter userConverter = UserConverter.getInstance();


    private static String GET_LOGIN_AND_PASSWORD = "SELECT Password FROM Logon WHERE Login = ?";
    private static String UPDATE_PASSWORD = "UPDATE Logon SET Password = ? WHERE IdUser = ?";
    private static String SELECT_ID_USER_FORM_LOGON = "SELECT IdUser FROM Logon WHERE Login = ? AND Password = ?";
    private static String SELECT_LOGIN_PASSWORD_FROM_ID ="SELECT Login, Password FROM Logon WHERE IdUser = ?";


    public boolean checkingIfLogonIsCorrect (String login, String passwordFromUser) throws SQLException {
        boolean checking = false;
        String passwordFromBase = getLoginAndPassword(login);

            if (passwordFromBase.equals(passwordFromUser)) {
                checking = true;
        }
        return checking;
        }

        public String getLoginAndPassword (String login) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_LOGIN_AND_PASSWORD);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();

        String password = "";
        while (resultSet.next()){
            password = resultSet.getString("Password");
        }
        return password;
    }

    public boolean logging () throws SQLException {
        displayAllUsersAndLoginsWithPassswords();
        String loggingData = getDataToLogOn();
        if (loggingData.equalsIgnoreCase("x")){
            return false;
        } else {
            String[] tab = loggingData.split(" ");
            String login = tab[0];
            String password = tab[1];

            setWhoIsLogged(login, password);
            System.out.println(GeneralMessages_en.CORRECT_STATEMENT_3);
        }
        return true;
    }

    public void updatePassword (int idUser, String newPassword) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(UPDATE_PASSWORD);
        preparedStatement.setString(1, newPassword);
        preparedStatement.setInt(2, idUser);
        preparedStatement.execute();
    }

    public void changingPasswordWithoutChecking () throws SQLException {
       boolean checking = true;
       do {
           String [] loggingData = getDataToChangeLoggingData();
           String newPassword1 = loggingData[0];
           String newPassword2 = loggingData[1];

           int idUser = getIdUserWhoHasLogged();
               if (changingPassword(idUser, newPassword1, newPassword2)){
               checking = false;
                   System.out.println(GeneralMessages_en.CORRECT_STATEMENT_4);
           } else {
                   System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
               }

       }while (checking);
    }

    public boolean checkingPasswordFormat (String password) {
        boolean checking = false;
        String pattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";

        if (password.matches(pattern)){
            checking = true;
        }
        return checking;
    }

    public boolean changingPassword (int idUser, String newPassword1, String newPassword2) throws SQLException {
        boolean checking =false;
            if (newPassword1.equals(newPassword2)) {
                updatePassword(idUser, newPassword1);
                checking = true;
        }
        return checking;
    }

    public void changingPasswordWithChecking () throws SQLException {
        boolean checking = true;
        do {
            String [] loggingData = getDataToChangeLoggingData();
            String newPassword1 = loggingData[0];
            String newPassword2 = loggingData[1];

                int idUser = getIdUserWhoHasLogged();
                if (checkingPasswordFormat(newPassword1) & changingPassword(idUser, newPassword1, newPassword2)) {
                    checking = false;
                    System.out.println(GeneralMessages_en.CORRECT_STATEMENT_4);

                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                }
        }while (checking);

    }

    public void mainChangingPassword () {
        try {
            if (settingService.selectValueFromSettings().equals("ON")){
                changingPasswordWithChecking();
            } else {
                changingPasswordWithoutChecking();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setWhoIsLogged (String login, String password) throws SQLException {
        int idUser = selectIdUserFromLogon(login,password);
        int idRole = userService.getIdRoleFromIdUser(idUser);
        String role = userService.getRoleNameFromRoles(idRole);

            SettingsAfterLogon.map.clear();
            SettingsAfterLogon.map.put(idUser, role);

    }

    public int selectIdUserFromLogon (String login, String password) throws SQLException {
        int idUser = 0;
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_ID_USER_FORM_LOGON);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            idUser = resultSet.getInt("IdUser");
        }
        return idUser;
    }

    public int getIdUserWhoHasLogged() {
        int idUser = 0;
        Map <Integer, String> map =  SettingsAfterLogon.map;
        for (Integer key : map.keySet()){
            idUser = key;
        }
        return idUser;
    }

    public String getRoleNameOfUserWhoHasLogged () {
        String roleName = "";
        Map <Integer, String> map = SettingsAfterLogon.map;
        for (Integer idRole : map.keySet()){
            roleName = map.get(idRole);
        }
        return roleName;
    }

    public String getDataToLogOn () throws SQLException {
        String loggingData = "";
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_17);
            String login = scanner.nextLine();

            if (login.equalsIgnoreCase("x")){
                checking = false;
                loggingData = login;
            } else {
                System.out.println(GeneralMessages_en.ENTER_DATA_18);
                String password = scanner.nextLine();

                if (checkingIfLogonIsCorrect(login, password)) {
                    loggingData = "" + login + " " + password;
                    checking = false;
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                }
            }
        } while (checking);
        return loggingData;
    }

    public String [] getDataToChangeLoggingData () throws SQLException {
            Scanner scanner = new Scanner(System.in);

        String newPassword1 = "";
        if (settingService.selectValueFromSettings().equals("ON")){
            newPassword1 = getPasswordWithChecking();
        } else {
            newPassword1 = getPasswordWithoutChecking();
        }

            System.out.println(GeneralMessages_en.ENTER_DATA_21);
            String newPassword2 = scanner.nextLine();

            String [] loggingData = new String [2];
            loggingData[0] = newPassword1;
            loggingData[1] = newPassword2;

        return loggingData;
    }

    public String getPasswordWithoutChecking () {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GeneralMessages_en.ENTER_DATA_19);
        String newPassword1 = scanner.nextLine();
        return newPassword1;
    }
    public String getPasswordWithChecking () {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GeneralMessages_en.ENTER_DATA_22);
        String newPassword2 = scanner.nextLine();
        return newPassword2;
    }

    public ResultSet getLoginAndPasswordFromId (int idUser) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_LOGIN_PASSWORD_FROM_ID);
        preparedStatement.setInt(1, idUser);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public void displayAllUsersAndLoginsWithPassswords () throws SQLException {
        System.out.println(GeneralMessages_en.INFO_STATEMENT_7);
        List<User> userList = userConverter.convert();
        for (User user : userList){
            String name = user.getName();
            String surname = user.getSurname();
            String whoIs = user.getWhoIs();
            int idUser = userService.getIdFromUser(name, surname);

            ResultSet resultSet = logonService.getLoginAndPasswordFromId(idUser);
            while(resultSet.next()){
                String login = resultSet.getString("Login");
                String password = resultSet.getString("Password");
                System.out.println(name+" "+surname+" | "+whoIs+" | Login: "+login+" | Password: "+password);
            }
        }
        System.out.println("===================================================");
    }

}
