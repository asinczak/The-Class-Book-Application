package pl.com.ttpsc.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SettingService {

    private static SettingService settingService;

    private SettingService () {}

    public static SettingService getInstance(){
        if (settingService == null){
            settingService = new SettingService();
        }
        return settingService;
    }

    static final String UPDATE_SETTINGS = "UPDATE Settings SET Value = ? WHERE Key = ?";
    static final String SELECT_VALUE_FROM_SETTINGS = "SELECT Value FROM Settings WHERE Key = ?";
    final String KEY_SETTINGS_1 = "CheckingFormat";

    public void turnOnOffCheckingPassword () throws SQLException {
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_23);
            String changing = scanner.nextLine();
                if (changing.equals("ON") | changing.equals("OFF")) {
                    updateSettings(changing);
                    checking = false;
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
        }while (checking);
    }

    public void updateSettings (String changing) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(UPDATE_SETTINGS);
        preparedStatement.setString(1, changing);
        preparedStatement.setString(2, KEY_SETTINGS_1);
        preparedStatement.execute();
    }

    public String selectValueFromSettings () throws SQLException {
        String checking = "";
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_VALUE_FROM_SETTINGS);
        preparedStatement.setString(1, KEY_SETTINGS_1);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            checking = resultSet.getString("Value");
        }
        return checking;
    }
}
