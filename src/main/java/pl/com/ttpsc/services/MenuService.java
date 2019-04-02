package pl.com.ttpsc.services;

import pl.com.ttpsc.data.SettingsAfterLogon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuService {

    private static MenuService menuServisce;

    private MenuService (){}

    public static MenuService getInstance(){
        if (menuServisce == null){
            menuServisce = new MenuService();
        }
        return menuServisce;
    }


    DisplayService displayService = DisplayService.getInstance();
    LogonService logonService = LogonService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();
    StudentService studentService = StudentService.getInstance();
    SettingService settingService = SettingService.getInstance();

    Connection connection = null;
    boolean switchGoes = true;

    public void displayMenu () {

        try {
            connection = DriverManager.getConnection ("jdbc:sqlite:Users.db");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        do {

            Scanner sc = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_9);
            int numberMenu = sc.nextInt();

            switch (numberMenu) {

                case 1:

                    logonService.logging();


                    break;

                case 2:
                    switchGoes = false;
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        } while (switchGoes);


    }
}
