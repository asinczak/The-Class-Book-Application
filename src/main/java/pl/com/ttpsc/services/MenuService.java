package pl.com.ttpsc.services;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
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
    GuardianService guardianService = GuardianService.getInstance();
    MenuSettings menuSettings = MenuSettings.getInstance();
    TeacherService teacherService = TeacherService.getInstance();
    PDFservice pdfService = PDFservice.getInstance();
    UserService userService = UserService.getInstance();
    MessagesService messagesService = MessagesService.getInstance();

    Connection connection = null;
    boolean switchGoes = true;

    public void mainMenu() {

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:Users.db");
            if(logonService.logging()) {

                do {
                    int numberMenu = getMenuNumberFromUser();

                    int optionToDo = menuSettings.getOptionToDo(numberMenu);
                    if (optionToDo != 0) {

                        switch (optionToDo) {

                            case 1:
                                studentService.createNewStudent();
                                break;
                            case 2:
                                guardianService.createGuardian();
                                break;
                            case 3:
                                studentService.asignStudentToClass();
                                break;
                            case 4:
                                studentService.addStudentGrade();
                                break;
                            case 5:
                                studentService.changeGradeFromSubject();
                                break;
                            case 6:
                                pdfService.generateStudentCertificate();
                                break;
                            case 7:
                                pdfService.generateCertificatesForAllStudentsInClass();
                                break;
                            case 8:
                                pdfService.generateCertificatesForChosenStudents();
                                break;
                            case 9:
                                guardianService.asignStudentToGuardian();
                                break;
                            case 10:
                                messagesService.manageMessages();
                                break;
                            case 11:
                                studentService.addAbsenceToStudent();
                                break;
                            case 12:
                                displayService.displayAllGradesOfStudentForStudent();
                                break;
                            case 13:
                                displayService.displayAllStudentAbsencesForStudent();
                                break;
                            case 14:
                                displayService.displayAllGradesOfStudentForGuardian();
                                break;
                            case 15:
                                displayService.displayAllAbsencesOfStudentForGuardian();
                                break;
                            case 16:
                                displayService.displayStudentsWithTooLowGrades();
                                break;
                            case 17:
                                displayService.displayIfStudentsHaveTooManyAbsences();
                                break;
                            case 18:

                                break;
                            case 19:
                                guardianService.sendAnExcuseToTeacher();
                                break;
                            case 20:
                                teacherService.verifyStudentsAbsence();
                                break;
                            case 21:
                                teacherService.createTeacher();
                                break;
                            case 22:
                                displayService.displayAllUsers();
                                break;
                            case 23:
                                teacherService.makeNewAssigmentToClassForTeacher();
                                break;
                            case 24:
                                displayService.displayAllClassesAtSchool();
                                break;
                            case 25:
                                subjectService.setNumberOfLessonsPerYear();
                                break;
                            case 26:
                                displayService.displayAllTeachers();
                                break;
                            case 27:
                                logonService.mainChangingPassword();
                                break;
                            case 28:
                                logonService.settingService.turnOnOffCheckingPassword();
                                break;
                            case 29:
                                teacherService.manageExcusesFromGuardian();
                                break;
                            case 30:
                                mainMenu();
                                switchGoes = false;
                                connection.close();
                                break;
                            default:
                                System.out.println(GeneralMessages_en.WORNING_STATEMENT_13);
                        }

                    } else {
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_12);

                    }
                } while (switchGoes);
            } else {
                System.out.println(GeneralMessages_en.CORRECT_STATEMENT_6);
            }
        } catch (SQLException e) {
            System.out.println(GeneralMessages_en.WORNING_STATEMENT_15);
        }
    catch (FileNotFoundException e){
        e.printStackTrace();
    }
    catch (DocumentException e){
        e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void displayMessageForTeacher () throws SQLException {
        int idTeacher = logonService.getIdUserWhoHasLogged();
        if (teacherService.checkingIfThereIsAnyNewExcuseForTeacher(idTeacher)){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_2);
        }
    }

    public void displayMessageForUser () throws SQLException {
        int idUser = logonService.getIdUserWhoHasLogged();
        if (messagesService.checkingIfThereIsAnyNewMessageForUser(idUser)){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_3);
        }
    }

    public int getMenuNumberFromUser () throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("**********************************");
        System.out.println(GeneralMessages_en.MENU_FUNCTION);
        menuSettings.displayMenuWithOptions();
        displayMessageForTeacher();
        displayMessageForUser();

        int numberMenu = 0;
        boolean loop = true;
        while (loop){
            try {
                numberMenu = sc.nextInt();
                loop = false;
            } catch (InputMismatchException e) {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                sc.next();
            }
        }
        return numberMenu;
    }
}


