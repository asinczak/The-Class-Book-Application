package pl.com.ttpsc.services;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class MenuSettings {

    private static MenuSettings menuSettings;

    private MenuSettings (){}

    public static MenuSettings getInstance(){
        if (menuSettings == null){
            menuSettings = new MenuSettings();
        }
        return menuSettings;
    }

    final static Logger logger = Logger.getLogger(MenuSettings.class);

    LogonService logonService = LogonService.getInstance();

   private Map <String, List<Integer>> mapWithRoles = new HashMap<>();

   private Map <Integer, String> mapWithOptionMenu= new HashMap<>();

   private Map <Integer, Integer> mapWithMenuForLoggedUser = new LinkedHashMap<>();

    public void fillMapWithRoles() throws SQLException {
            mapWithRoles.put("STUDENT", fillStudentMap());
            mapWithRoles.put("GUARDIAN", fillGuardianMap());
            mapWithRoles.put("TEACHER", fillTeacherMap());
            mapWithRoles.put("PRINCIPAL", fillPrincipalMap());
            mapWithRoles.put("ADMIN", fillAdminMap());
    }

    public List<Integer> fillStudentMap (){
        List <Integer> list = new ArrayList<>();
        list.add(10);
        list.add(12);
        list.add(13);
        list.add(27);
        list.add(30);
        return list;
    }

    public List<Integer> fillGuardianMap (){
        List <Integer> list = new ArrayList<>();
        list.add(10);
        list.add(14);
        list.add(15);
        list.add(16);
        list.add(17);
        list.add(18);
        list.add(19);
        list.add(27);
        list.add(30);
        return list;
    }

    public List<Integer> fillTeacherMap () {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(11);
        list.add(20);
        list.add(27);
        list.add(29);
        list.add(30);
        return list;
    }

    public List<Integer> fillPrincipalMap (){
        List <Integer> list = new ArrayList<>();
        list.add(10);
        list.add(21);
        list.add(22);
        list.add(23);
        list.add(24);
        list.add(25);
        list.add(26);
        list.add(27);
        list.add(30);
        return list;
    }

    public List<Integer> fillAdminMap (){
        List <Integer> list = new ArrayList<>();
        list.add(10);
        list.add(27);
        list.add(28);
        list.add(30);
        return list;
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
        mapWithOptionMenu.put(30, GeneralMessages_en.MENU_FUNCTION_30);
    }

    public void getListOfMenuNumbers () throws SQLException {
        mapWithMenuForLoggedUser.clear();
        fillAllTablesWithData();
        String roleName = logonService.getRoleNameOfUserWhoHasLogged();
        for (String role : mapWithRoles.keySet()) {
            if (role.equals(roleName)) {
                List<Integer> listWithMenuOptions = mapWithRoles.get(role);
                int indeks = 0;
                for (int number : listWithMenuOptions) {
                    indeks++;
                    mapWithMenuForLoggedUser.put(indeks, number);
                }
            }
        }
    }

    public void displayMenuWithOptions() throws SQLException {
        logger.debug("Displaying menu for user");
        getListOfMenuNumbers();
        for (Integer numberOptionMenu : mapWithOptionMenu.keySet()){
            String menuOption = mapWithOptionMenu.get(numberOptionMenu);
            for (int indeks : mapWithMenuForLoggedUser.keySet()) {
                int optionMenu = mapWithMenuForLoggedUser.get(indeks);
                if (numberOptionMenu == optionMenu) {
                    System.out.println(indeks + menuOption);
                }
            }
       }
    }

    public int getOptionToDo (int numberFromUser) throws SQLException {
        logger.info("Getting option to make");
        int optionTodo = 0;

        for (int indeks : mapWithMenuForLoggedUser.keySet()) {
            int optionMenuFromMap = mapWithMenuForLoggedUser.get(indeks);
            if (numberFromUser == indeks){
                optionTodo = optionMenuFromMap;
            }
        }
        return optionTodo;
    }

    public void fillAllTablesWithData () throws SQLException {
        fillMapWithRoles();
        fillMapWithOptionMenu();
    }

}
