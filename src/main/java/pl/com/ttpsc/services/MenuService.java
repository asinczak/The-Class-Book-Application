package pl.com.ttpsc.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuService {

    private static  final MenuService menuServisce = new MenuService();

    private MenuService (){}

    public static MenuService getInstance(){
        return menuServisce;
    }
    boolean switchGoes = true;

    Connection connection = null;

    public void displayMenu () {

        try {
            connection = DriverManager.getConnection ("jdbc:sqlite:Users.db");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        do {

            Scanner sc = new Scanner(System.in);
            System.out.println(DisplayService.ENTER_DATA_9);
            int numberMenu = sc.nextInt();

            switch (numberMenu) {

                case 1:
                    GuardianService guardianService = new GuardianService();
                    guardianService.createGuardian();

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
