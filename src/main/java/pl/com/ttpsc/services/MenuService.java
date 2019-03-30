package pl.com.ttpsc.services;

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
    boolean switchGoes = true;

    DisplayService displayService = DisplayService.getInstance();
    Connection connection = null;

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

                    displayService.displayAllClassesAtSchool();

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
