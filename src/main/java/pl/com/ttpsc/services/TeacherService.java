package pl.com.ttpsc.services;

import org.apache.log4j.Logger;
import pl.com.ttpsc.data.Roles;
import pl.com.ttpsc.data.SchoolClass;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class TeacherService {

    private static TeacherService teacherService;

    private TeacherService (){}

    public static TeacherService getInstance(){
        if (teacherService == null){
            teacherService = new TeacherService();
        }
        return teacherService;
    }

    final static Logger logger = Logger.getLogger(TeacherService.class);

    ClassService classService = ClassService.getInstance();
    UserService userService = UserService.getInstance();
    ExcusesService excusesService =ExcusesService.getInstance();
    LogonService logonService = LogonService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();

    static final String ASSIGN_TEACHER_TO_CLASS = "UPDATE Classes SET Teacher = ? WHERE ClassName = ?";
    static final String GET_TEACHER_FROM_ID = "SELECT Name, Surname FROM Users WHERE Id = ?";

    public void createTeacher () throws SQLException {
        logger.debug("Creating teacher");
        String enteredData = userService.getNameAndSurnameFromUser();
            if (enteredData.equalsIgnoreCase("x")){
                System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
            } else {
                String[] tab = enteredData.split(" ");
                String name = tab[0];
                String surname = tab[1];

                userService.addUserToTheDataBase(Roles.TEACHER, name, surname);
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
                int idUser = userService.getIdFromUser(name, surname);
                userService.insertNewUserIntoLogon(name, surname, idUser);
            }
        }

    public void makeNewAssigmentToClassForTeacher() throws SQLException {
        logger.debug("Making assignment to class for teacher");
        String nameAndSurname = getNameAndSurnameAndCheckForTeacher();
        if (nameAndSurname.equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String[] tab = nameAndSurname.split(" ");
            String teacherName = tab[0];
            String teacherSurname = tab[1];
            String className = getClassName();
            int idTeacher = userService.getIdFromUser(teacherName, teacherSurname);
            updateClassAddTeacherId(idTeacher, className);
            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
        }
    }

    public void updateClassAddTeacherId (int idClass, String className) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(ASSIGN_TEACHER_TO_CLASS);
        preparedStatement.setInt(1, idClass);
        preparedStatement.setString(2, className);
        preparedStatement.execute();
    }

    public String getTeacherFromId (int id) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_TEACHER_FROM_ID);
        preparedStatement.setInt(1, id);
        String nameSurname = "" ;
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            nameSurname = name+" "+surname;
        }
        return nameSurname;
    }

    public boolean checkingIfIsTeacher (String name, String surname) throws SQLException {
        boolean checking;
        String role = String.valueOf(Roles.TEACHER);
        int roleId = userService.getRoleIdFromRoles(role);
        int roleIdUser = userService.getRoleIdFromUsers(name, surname);
        if (roleId == roleIdUser){
            checking = true;
        }else {
            checking = false;
        }
        return checking;
    }

    public List<SchoolClass> getListClassWithTeacher () throws SQLException {
        List <SchoolClass> list = new ArrayList<>();
            ResultSet resultSet = classService.getClassAndTeacher();
            while (resultSet.next()){
                String className = resultSet.getString("ClassName");
                int idTeacher = resultSet.getInt("Teacher");
                String teacher = teacherService.getTeacherFromId(idTeacher);
                SchoolClass schoolClass = new SchoolClass();
                schoolClass.setNameClass(className);
                schoolClass.setTeacher(teacher);
                int idClass = classService.getIdFromClasses(className);
                schoolClass.setStudentList(classService.getListOfStudentfromIdClass(idClass));
                list.add(schoolClass);
            }
        return list;
    }

    public boolean checkingIfThereIsAnyNewExcuseForTeacher (int idTeacher) throws SQLException {
        boolean checking = false;
        ResultSet resultSet = excusesService.selectDataFromNewExcuses();
        while (resultSet.next()){
            int idTeacherFromBase = resultSet.getInt("IdTeacher");
            if (idTeacherFromBase == idTeacher){
                checking = true;
            }
        }
        return checking;
    }

    public void manageExcusesFromGuardian() throws SQLException {
        logger.debug("Displaying menu with option to manage excuses from guardian");
        int idTeacher = logonService.getIdUserWhoHasLogged();
        boolean checking = true;
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(GeneralMessages_en.ENTER_DATA_32);
                int number = scanner.nextInt();

                switch (number) {
                    case 1:
                        displayNewExcuses(idTeacher);
                        break;
                    case 2:
                        displayAllMessagesFromExcuses(idTeacher);
                        break;
                    case 3:
                        deleteChosenExcuse(idTeacher);
                        break;
                    case 4:
                        checking = false;
                    default:
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                }
            }catch (InputMismatchException e){
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                logger.error(e.getMessage(), e);
            }
        } while (checking);

    }

    public void verifyStudentsAbsence() throws SQLException {
        logger.debug("Verifying students absences");
        int idTeacher = logonService.getIdUserWhoHasLogged();

        if (!displayAllMessagesFromExcuses(idTeacher)){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_8);
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            boolean checking = true;
            do {
                try {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println(GeneralMessages_en.ENTER_DATA_27);
                    int number = scanner.nextInt();

                    switch (number) {
                        case 1:
                            approveStudentsAbsence(idTeacher);
                            checking = false;
                            break;
                        case 2:
                            discardStudentsAbsence(idTeacher);
                            checking = false;
                            break;
                        case 3:
                            checking = false;
                            break;
                        default:
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                    }
                } catch (InputMismatchException e) {
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                    logger.error(e.getMessage(), e);
                }
            } while (checking);
        }
    }

    private void discardStudentsAbsence(int idTeacher) throws SQLException {
        logger.debug("Discarding student's absence");
        displayAllMessagesFromExcuses(idTeacher);
        boolean checking = true;
       do {

           Scanner scanner = new Scanner(System.in);
           System.out.println(GeneralMessages_en.ENTER_DATA_28);
           int number = scanner.nextInt();

           List<Integer> idList = excusesService.getListIdFormExcuses(idTeacher);
           for (int id : idList) {
               if (number == id) {
                   excusesService.updateStatusOfExcuse("DISCARDED", id);
                   checking = false;
                   System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
               }
           }
           if(checking){
               System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
           }
       } while (checking);
    }

    private void approveStudentsAbsence(int idTeacher) throws SQLException {
        logger.debug("Approving student's absence");
        displayAllMessagesFromExcuses(idTeacher);
        boolean checking = true;
        do {

            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_29);
            int number = scanner.nextInt();

            List<Integer> idList = excusesService.getListIdFormExcuses(idTeacher);
            for (int id : idList) {
                if (number == id) {

                    System.out.println(GeneralMessages_en.ENTER_DATA_15);
                    String date = scanner.next();

                    System.out.println(GeneralMessages_en.ENTER_DATA_30);
                    String studentName = scanner.next();

                    System.out.println(GeneralMessages_en.ENTER_DATA_31);
                    String studentSurname = scanner.next();

                    int idStudent = userService.getIdFromUser(studentName, studentSurname);

                    System.out.println(GeneralMessages_en.ENTER_DATA_11);
                    String subject = scanner.next();

                    int idSubject = subjectService.getIdFromSubject(subject);

                    if (subjectService.checkingIfSuchAbsenceExists(date, idStudent, idSubject)){
                        excusesService.updateStatusOfExcuse("APPROVED", id);
                        subjectService.deleteStudentAbsence(date, idStudent, idSubject);
                        checking = false;
                        System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                    }
                }
            }
            if(checking){
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
        } while (checking);
    }

    public boolean displayAllMessagesFromExcuses (int idTeacher) throws SQLException {
        boolean checkingIfTeacherHasAnyExcuses = false;
        ResultSet resultSet = excusesService.getAllMessagesFromExcuses(idTeacher);

            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String excuse = resultSet.getString("Message");
                String status = resultSet.getString("Status");
                int idGuardian = resultSet.getInt("IdGuardian");
                if (idGuardian > 0) {
                    checkingIfTeacherHasAnyExcuses = true;
                    String guardian = userService.getUserNameSurnameFromId(idGuardian);
                    String displayNew = "";
                    System.out.println(GeneralMessages_en.INFO_STATEMENT_1);
                    if (status.equals("NEW")) {
                        displayNew = "###### " + status + " ######";
                        System.out.println("Message number: " + id + "\nFrom: " + guardian +
                                "\nMessage: " + excuse + "\nStatus:" + displayNew +
                                "\n___________________________________________");
                    } else {
                        System.out.println("Message number: " + id + "\nFrom: " + guardian +
                                "\nMessage: " + excuse + "\nStatus:" + status +
                                "\n___________________________________________");
                    }
                }
        }
             return checkingIfTeacherHasAnyExcuses;
    }

    public void displayNewExcuses (int idTeacher) throws SQLException {
        ResultSet resultSet = excusesService.getAllMessagesFromExcuses(idTeacher);
        while (resultSet.next()) {
            int id = resultSet.getInt("Id");
            String excuse = resultSet.getString("Message");
            String status = resultSet.getString("Status");
            int idGuardian = resultSet.getInt("IdGuardian");
            String guardian = userService.getUserNameSurnameFromId(idGuardian);
            if (status.equals("NEW")) {
                System.out.println(GeneralMessages_en.INFO_STATEMENT_1);
                System.out.println("Message number: " + id +"\nFrom: "+guardian+
                        "\nMessage: " + excuse + "\nStatus:" + status +
                        "\n___________________________________________");
                excusesService.updateStatusOfExcuse("READ", id);
            } else {
                System.out.println(GeneralMessages_en.INFO_STATEMENT_5);
            }
        }
    }

    public void deleteChosenExcuse (int idTeacher) throws SQLException {
        List<Integer> idList = excusesService.getListIdFormExcuses(idTeacher);
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_33);
            int excuseNumber = scanner.nextInt();

            for (int id : idList) {
                if (id == (excuseNumber)) {
                    excusesService.deleteAnExcuse(excuseNumber);
                    System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
                    checking = false;
                }
            }
            if (checking){
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
        }while (checking);
    }

    public String getNameAndSurnameAndCheckForTeacher () throws SQLException {
        logger.debug("Getting teacher's name and surname");
        boolean checking = true;
        String returnData = "";

        do {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(GeneralMessages_en.ENTER_DATA_6);
                String enteredData = scanner.nextLine();

                if (enteredData.equalsIgnoreCase("x")) {
                    checking = false;
                    returnData = enteredData;
                } else {

                    String[] tab = enteredData.split(" ");
                    String name = tab[0];
                    String surname = tab[1];

                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    surname = surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase();

                    if (userService.checkingIfUserExists(name, surname)) {
                        if (checkingIfIsTeacher(name, surname)) {
                            checking = false;
                            returnData = "" + name + " " + surname;
                        } else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_6);
                        }
                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
                    }
                }

            }catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                logger.error(e.getMessage(), e);
            }
        } while (checking) ;
        return returnData;
    }

    public String getClassName () throws SQLException {
        String className = "";

        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_10);
            className = scanner.nextLine();
            int idClass = classService.getIdFromClasses(className);
            if (idClass > 0) {
                checking = false;
            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_5);
            }
        }while (checking) ;
        return className;
    }

}
