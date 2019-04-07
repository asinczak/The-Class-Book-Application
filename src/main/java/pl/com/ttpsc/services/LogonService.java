package pl.com.ttpsc.services;


import pl.com.ttpsc.data.SettingsAfterLogon;

import java.io.Console;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    Map<String, String> map = new HashMap<>();

    private static String GET_LOGIN_AND_PASSWORD = "SELECT Login, Password FROM Logon";
    private static String UPDATE_PASSWORD = "UPDATE Logon SET Password = ? WHERE Login = ?";
    private static String SELECT_ID_USER_FORM_LOGON = "SELECT IdUser FROM Logon WHERE Login = ? AND Password = ?";


    public boolean checkingIfLogonIsCorrect (String login, String password) throws SQLException {
        boolean checking = false;
        map = getLoginAndPassword();
        for (Map.Entry <String, String> entry : map.entrySet()) {
            if (login.equals(entry.getKey()) & password.equals(entry.getValue())) {
                checking = true;
            }
        }
        return checking;
        }

        public Map<String, String> getLoginAndPassword () throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_LOGIN_AND_PASSWORD);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            String login = resultSet.getString("Login");
            String password = resultSet.getString("Password");
            map.put(login, password);
        }
        return map;
    }

    public void logging () {
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_17);
            String login = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_18);
            String password = scanner.nextLine();

            try {
                if (checkingIfLogonIsCorrect(login, password)){
                    System.out.println(GeneralMessages_en.CORRECT_STATEMENT_3);
                    setWhoIsLogged(login, password);
                    checking = false;
                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } while (checking);
    }

    public void updatePassword (String login, String newPassword) throws SQLException {

        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(UPDATE_PASSWORD);
        preparedStatement.setString(1, newPassword);
        preparedStatement.setString(2, login);
        preparedStatement.execute();
    }

    public void changingPasswordWithoutChecking () {
       boolean checking = true;
       do {
           Scanner scanner = new Scanner(System.in);
           System.out.println(GeneralMessages_en.ENTER_DATA_20);
           String login = scanner.nextLine();

           System.out.println(GeneralMessages_en.ENTER_DATA_19);
           String newPassword1 = scanner.nextLine();

           System.out.println(GeneralMessages_en.ENTER_DATA_21);
           String newPassword2 = scanner.nextLine();

           try {
               if (changingPassword(login, newPassword1, newPassword2)){
               checking = false;
                   System.out.println(GeneralMessages_en.CORRECT_STATEMENT_4);
           } else {
                   System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
               }
           } catch (SQLException e) {
               e.printStackTrace();
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

    public boolean changingPassword (String login, String newPassword1, String newPassword2) throws SQLException {
        boolean checking =false;
        map = getLoginAndPassword();
        for (Map.Entry <String, String> entry : map.entrySet()) {
            if (login.equals(entry.getKey()) & newPassword1.equals(newPassword2)) {
                updatePassword(login, newPassword1);
                checking = true;
            }
        }
        return checking;
    }

    public void changingPasswordWithChecking () {
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_20);
            String login = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_22);
            String newPassword1 = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_21);
            String newPassword2 = scanner.nextLine();

            try {
                if (checkingPasswordFormat(newPassword1) & changingPassword(login, newPassword1, newPassword2)) {
                    checking = false;
                    System.out.println(GeneralMessages_en.CORRECT_STATEMENT_4);

                } else {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                }
            } catch (SQLException e) {
                e.printStackTrace();
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

        SettingsAfterLogon.map.put(idUser, role );
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



}
