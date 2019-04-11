package pl.com.ttpsc.services;

import org.apache.log4j.Logger;
import pl.com.ttpsc.data.Roles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class StudentService {

    private static StudentService studentService;

    private StudentService () {}

    public static StudentService getInstance() {
        if (studentService == null){
            studentService = new StudentService();
        }
        return studentService;
    }

    final static Logger logger = Logger.getLogger(StudentService.class);

    SubjectService subjectService = SubjectService.getInstance();
    ClassService classService = ClassService.getInstance();
    UserService userService = UserService.getInstance();
    LogonService logonService = LogonService.getInstance();

    static final String ASSIGN_STUDENT_TO_CLASS = "UPDATE Users SET IdClass = ? WHERE Name = ? AND Surname = ?";
    static final String INSERT_GRADE_TO_STUDENT = "INSERT INTO Subject_Grade (IdSubject, IdStudent, Grade) VALUES (?, ?, ?)";
    static final String UPDATE_STUDENT_GRADE = "UPDATE Subject_Grade SET Grade = ? WHERE IdSubject = ? AND IdStudent = ?";
    static final String SELSECT_ALL_GRADES_OF_STUDENT = "SELECT IdSubject, Grade FROM Subject_Grade WHERE IdStudent = ?";
    static final String SELECT_STUDENT_ABSENCES = "SELECT DateAbsence, IdSubject FROM Absences WHERE IdStudent = ?";
    static final String SELECT_SUBJECT_AND_GRADE_FOR_STUDENT = "SELECT IdSubject, Grade FROM Subject_Grade WHERE IdStudent = ?";


    public void createNewStudent() throws SQLException {
        logger.debug("Creating student");
            String enteredData = userService.getNameAndSurnameFromUser();
            if (enteredData.equalsIgnoreCase("x")){
                System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
            } else {
                String [] tab = enteredData.split(" ");
                String name = tab[0];
                String surname = tab[1];

                userService.addUserToTheDataBase(Roles.STUDENT, name, surname);
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
                int idUser = userService.getIdFromUser(name, surname);
                userService.insertNewUserIntoLogon(name, surname, idUser);
            }
    }

    public void asignStudentToClass() throws SQLException {
        logger.debug("Assigning student to class");
        String enteredData = getNameAndSurnameAndCheckForStudent();
        String className = getClassToAssignStudent();

        if(enteredData.equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {

            int idClass = classService.getIdFromClasses(className);
            String[] tabWithNameAndSurname = enteredData.split(" ");
            String studentName = tabWithNameAndSurname[0];
            String studentSurname = tabWithNameAndSurname[1];

            updateStudentAddClassId(idClass, studentName, studentSurname);
            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
        }
    }

    public void updateStudentAddClassId (int idClass, String name, String surname) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(ASSIGN_STUDENT_TO_CLASS);
        preparedStatement.setInt(1, idClass);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, surname);
        preparedStatement.execute();
    }

    public void addStudentGrade () throws SQLException {
        logger.debug("Adding student a grade");
        String[] tab = getDataToAddStudentGrade();

        if(tab[0].equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String name = tab[0];
            String surname = tab[1];
            String subject = tab[2];
            String grade = tab[3];
            int idSubject = subjectService.getIdFromSubject(subject);
            int idStudent = userService.getIdFromUser(name, surname);
            if (checkingIfStudenthasSuchGrade(idStudent, idSubject)){
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_18);
            } else {
                int gradeAsInt = Integer.parseInt(grade);
                insertGradeToStudent(idSubject, idStudent, gradeAsInt);
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
            }
        }
    }

    public void insertGradeToStudent (int idSubject, int idStudent, int grade) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_GRADE_TO_STUDENT);
        preparedStatement.setInt(1, idSubject);
        preparedStatement.setInt(2, idStudent);
        preparedStatement.setInt(3, grade);
        preparedStatement.execute();
    }

    public void changeGradeFromSubject () throws SQLException {
        logger.debug("Changing grade from subject");
        String[] tab = getDataToAddStudentGrade();

        if(tab[0].equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String name = tab[0];
            String surname = tab[1];
            String subject = tab[2];
            String grade = tab[3];
            int idSubject = subjectService.getIdFromSubject(subject);
            int idStudent = userService.getIdFromUser(name, surname);
            int gradeAsInt = Integer.parseInt(grade);
            if (!checkingIfStudenthasSuchGrade(idStudent, idSubject)){
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_19);
            } else {
                updateStudentGrade(gradeAsInt, idSubject, idStudent);
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
            }
        }
    }

    public void updateStudentGrade (int grade, int idSubject, int idStudent) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(UPDATE_STUDENT_GRADE);
        preparedStatement.setInt(1, grade);
        preparedStatement.setInt(2, idSubject);
        preparedStatement.setInt(3, idStudent);
        preparedStatement.execute();
    }

    public ResultSet getAllGradesOfStudent (int idStudent) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELSECT_ALL_GRADES_OF_STUDENT);
        preparedStatement.setInt(1, idStudent);

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public ResultSet getAllStudentAbsences (int idStudent) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_STUDENT_ABSENCES);
        preparedStatement.setInt(1, idStudent);

        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public boolean checkingIfIsStudent (String name, String surname) throws SQLException {
        boolean checking;
        String role = String.valueOf(Roles.STUDENT);
        int roleId = userService.getRoleIdFromRoles(role);
        int roleIdUser = userService.getRoleIdFromUsers(name, surname);
        if (roleId == roleIdUser){
            checking = true;
        } else {
            checking = false;
        }
        return checking;
    }

    public String[] getDataToAddAbsence () throws SQLException {
        String[] tab = new String[1];
        String enteredData = getNameAndSurnameAndCheckForStudent();
        if (enteredData.equalsIgnoreCase("x")) {
            tab[0] = enteredData;
        } else {
            String[] tabWithNameAndSurname = enteredData.split(" ");
            String studentName = tabWithNameAndSurname[0];
            String studentSurname = tabWithNameAndSurname[1];
            String subject = getSubjectToAddStudentGrade();

            int idStudent = userService.getIdFromUser(studentName, studentSurname);
            int idSubject = subjectService.getIdFromSubject(subject);
            tab = new String[3];
            tab[0] = String.valueOf(idStudent);
            tab[1] = String.valueOf(idSubject);
            tab[2] = enterTheDate();

        }
        return tab;
    }

    public void addAbsenceToStudent() throws SQLException {
        logger.debug("Adding student an absence");
        String[] tab = getDataToAddAbsence();

        if(tab[0].equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            int idStudent = Integer.parseInt(tab[0]);
            int idSubject = Integer.parseInt(tab[1]);
            String date = tab[2];
            subjectService.insertAbsenceFromSubject(date, idStudent, idSubject);
            System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
        }
    }

    public  boolean checkingIfStudenthasSuchGrade (int idStudent, int idSubject) throws SQLException {
        boolean checking = false;
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(SELECT_SUBJECT_AND_GRADE_FOR_STUDENT);
        preparedStatement.setInt(1, idStudent);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int idSubjectFromBase = resultSet.getInt("IdSubject");

            if (idSubject == idSubjectFromBase){
                checking = true;
            }
        }
        return checking;
    }

    public Map<String, Integer> getMapOfAbsencesOfStudent (int idStudent) throws SQLException {
        logger.debug("Getting map with absences");
        Map <String, Integer> map = new HashMap<>();
        List <String> subjectList = new ArrayList<>();

        ResultSet resultSet = getAllStudentAbsences(idStudent);
        while (resultSet.next()){
            int idSubject = resultSet.getInt("IdSubject");

            String subject = subjectService.getSubjectFromId(idSubject);
           subjectList.add(subject);
        }
        for (int i = 0; i<subjectList.size(); i++){
            int counter = 0;
            String subject = subjectList.get(i);
            for (int y = 0; y<subjectList.size(); y++){
                if (subject.equals(subjectList.get(y))){
                    counter++;
                }
            }
            map.put(subject, counter);
        }
        return map;
    }

    public boolean checkingTheFormatDate(String date) throws ParseException {
        if (date == null || !date.matches("[0-9]{4}(-)[0-9]{1,2}(-)[0-9]{1,2}")){
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        dateFormat.parse(date.trim());
        return true;
    }

    public String enterTheDate (){
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_15);
            String date = scanner.nextLine();
            try {
                int comparison = checkingDateToCurrentDate(date);
            if(checkingTheFormatDate(date) & comparison <= 0){
                    return date;

            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
            } catch (ParseException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                logger.error(e.getMessage(), e);
            }
        }while (true);
    }

    public int checkingDateToCurrentDate (String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        dateFormat.format(currentDate);
        Date enterDate = dateFormat.parse(date);
        int comparison = enterDate.compareTo(currentDate);
        return comparison;
    }

        public String getClassToAssignStudent () throws SQLException {
            boolean checking = true;
            String className = "";
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
            } while (checking);
            return className;
        }

    public String getNameAndSurnameAndCheckForStudent () throws SQLException {
        boolean checking = true;
        String returnData = "";

            do {
                try {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_3);
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
                        if (checkingIfIsStudent(name, surname)) {
                            checking = false;
                            returnData = "" + name + " " + surname;
                        } else {
                            System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
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

    public String [] getDataToAddStudentGrade () throws SQLException {

        String[] tab = new String[1];
        String enteredData = getNameAndSurnameAndCheckForStudent();
        if (enteredData.equalsIgnoreCase("x")) {
            tab[0] = enteredData;
        } else {
            String[] tabWithNameAndSurname = enteredData.split(" ");
            String studentName = tabWithNameAndSurname[0];
            String studentSurname = tabWithNameAndSurname[1];
            String subject = getSubjectToAddStudentGrade();

            int idStudent = userService.getIdFromUser(studentName, studentSurname);
            int idSubject = subjectService.getIdFromSubject(subject);
            tab = new String[4];
            tab[0] = studentName;
            tab[1] = studentSurname;
            tab[2] = subject;
            tab[3] = String.valueOf(getGradeToAddStudentGrade(idSubject, idStudent));

        }
        return tab;
    }

    public String getSubjectToAddStudentGrade () throws SQLException {
        String subject = "";
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_11);
            subject = scanner.nextLine();

            int idSubject = subjectService.getIdFromSubject(subject);
            if (idSubject > 0) {
                checking = false;
            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_7);
            }

        } while (checking);
        return subject;
    }

    public int getGradeToAddStudentGrade (int idSubject, int idStudent) throws SQLException {
        int grade = 0;
        boolean checking = true;
        do {
            try {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_12);
            grade = scanner.nextInt();

             if (grade > 0 & grade < 7){
                checking = false;

            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_8);
            }
        } catch (InputMismatchException e) {
            System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                logger.error(e.getMessage(), e);
        }

        } while (checking);

        return grade;
    }

}




