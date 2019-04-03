package pl.com.ttpsc.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuSettings {

    private static MenuSettings menuSettings;

    private MenuSettings (){}

    public static MenuSettings getInstance(){
        if (menuSettings == null){
            menuSettings = new MenuSettings();
        }
        return menuSettings;
    }

    LogonService logonService = LogonService.getInstance();

   private Map<String, String> mapWithRoles = new HashMap<>();

   private Map <Integer, String> mapWithOptionMenu= new HashMap<>();

    public void fillMapWithRoles() throws SQLException {
            mapWithRoles.put("STUDENT", "10 12 13 27 29");
            mapWithRoles.put("GUARDIAN", "10 14 15 16 17 18 19 27 29");
            mapWithRoles.put("TEACHER", "1 2 3 4 5 6 7 8 9 10 11 20 27 29");
            mapWithRoles.put("PRINCIPAL", "10 21 22 23 24 25 26 27 29");
            mapWithRoles.put("ADMIN", "10 27 29");
    }


    public void fillMapWithOptionMenu () {
        mapWithOptionMenu.put(1, GeneralMessages_en.MENU_FUNCTION_1);
        mapWithOptionMenu.put(2, GeneralMessages_en.MENU_FUNCTION_2);
        mapWithOptionMenu.put(3, GeneralMessages_en.MENU_FUNCTION_3);
        mapWithOptionMenu.put(4, GeneralMessages_en.MENU_FUNCTION_4);
        mapWithOptionMenu.put(5, GeneralMessages_en.MENU_FUNCTION_5);
        mapWithOptionMenu.put(6, GeneralMessages_en.MENU_FUNCTION_6);
        mapWithOptionMenu.put(7, GeneralMessages_en.MENU_FUNCTION_7);
        mapWithOptionMenu.put(8, GeneralMessages_en.MENU_FUNCTION_8);
        mapWithOptionMenu.put(9, GeneralMessages_en.MENU_FUNCTION_9);
        mapWithOptionMenu.put(10, GeneralMessages_en.MENU_FUNCTION_10);
        mapWithOptionMenu.put(11, GeneralMessages_en.MENU_FUNCTION_11);
        mapWithOptionMenu.put(12, GeneralMessages_en.MENU_FUNCTION_12);
        mapWithOptionMenu.put(13, GeneralMessages_en.MENU_FUNCTION_13);
        mapWithOptionMenu.put(14, GeneralMessages_en.MENU_FUNCTION_14);
        mapWithOptionMenu.put(15, GeneralMessages_en.MENU_FUNCTION_15);
        mapWithOptionMenu.put(16, GeneralMessages_en.MENU_FUNCTION_16);
        mapWithOptionMenu.put(17, GeneralMessages_en.MENU_FUNCTION_17);
        mapWithOptionMenu.put(18, GeneralMessages_en.MENU_FUNCTION_18);
        mapWithOptionMenu.put(19, GeneralMessages_en.MENU_FUNCTION_19);
        mapWithOptionMenu.put(20, GeneralMessages_en.MENU_FUNCTION_20);
        mapWithOptionMenu.put(21, GeneralMessages_en.MENU_FUNCTION_21);
        mapWithOptionMenu.put(22, GeneralMessages_en.MENU_FUNCTION_22);
        mapWithOptionMenu.put(23, GeneralMessages_en.MENU_FUNCTION_23);
        mapWithOptionMenu.put(24, GeneralMessages_en.MENU_FUNCTION_24);
        mapWithOptionMenu.put(25, GeneralMessages_en.MENU_FUNCTION_25);
        mapWithOptionMenu.put(26, GeneralMessages_en.MENU_FUNCTION_26);
        mapWithOptionMenu.put(27, GeneralMessages_en.MENU_FUNCTION_27);
        mapWithOptionMenu.put(28, GeneralMessages_en.MENU_FUNCTION_28);
        mapWithOptionMenu.put(29, GeneralMessages_en.MENU_FUNCTION_29);

    }

    public List <Integer> getListOfMenuNumbers (String roleName) throws SQLException {
        List <Integer> listOfMenuNumbers = new ArrayList<>();
        for (String role : mapWithRoles.keySet()) {
            if (role.equals(roleName)) {
                String menuNumbers = mapWithRoles.get(role);
                String tab[] = menuNumbers.split(" +");
                for (String number : tab) {
                    listOfMenuNumbers.add(Integer.parseInt(number));
                }
            }
        }
        return listOfMenuNumbers;
    }

    public void displayMenuWithOptions() throws SQLException {
       String roleName = logonService.getRoleNameOfUserWhoHasLogged();
       List<Integer> list= getListOfMenuNumbers(roleName);

       for (Integer numberOptionMenu : mapWithOptionMenu.keySet()){
           for (int numberFromList : list){
               if (numberOptionMenu == numberFromList){
                   String menuOption = mapWithOptionMenu.get(numberOptionMenu);
                   System.out.println(menuOption);
               }
           }
       }
    }

    public boolean checkingIfUserHasAccessToSuchOption (int numberFromUser) throws SQLException {
        boolean checking = false;
        String roleName = logonService.getRoleNameOfUserWhoHasLogged();
        List <Integer> listOfMenuNumbers = getListOfMenuNumbers(roleName);
        for (int numberFromList : listOfMenuNumbers){
            if (numberFromList == numberFromUser){
                checking = true;
            }
        }
        return checking;
    }

    public void fillAllTablesWithData () throws SQLException {
        fillMapWithRoles();
        fillMapWithOptionMenu();
    }


}