package pl.com.ttpsc.services;

import org.apache.log4j.Logger;
import pl.com.ttpsc.data.SchoolClass;
import pl.com.ttpsc.data.Student;
import pl.com.ttpsc.data.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class DisplayService {

    private static DisplayService displayService;

    private DisplayService() {
    }

    public static DisplayService getInstance() {
        if (displayService == null) {
            displayService = new DisplayService();
        }
        return displayService;
    }

    final static Logger logger = Logger.getLogger(DisplayService.class);

    StudentService studentService = StudentService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();
    ClassService classService = ClassService.getInstance();
    UserConverter userConverter = UserConverter.getInstance();
    TeacherService teacherService = TeacherService.getInstance();
    GuardianService guardianService = GuardianService.getInstance();
    LogonService logonService = LogonService.getInstance();
    UserService userService = UserService.getInstance();
    ExcusesService excusesService = ExcusesService.getInstance();


    public void displayAllGradesOfStudentForStudent() throws SQLException {
        logger.debug("Displaying all grades for student");
        int idStudent = logonService.getIdUserWhoHasLogged();
            ResultSet resultSet = studentService.getAllGradesOfStudent(idStudent);
            while (resultSet.next()) {
                int idSubject = resultSet.getInt("IdSubject");
                int grade = resultSet.getInt("Grade");
                String subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(subjectName + ": " + grade);
            }
    }

    public void displayAllStudentAbsencesForStudent() throws SQLException {
        logger.debug("Displaying all absences for student");
        int idStudent = logonService.getIdUserWhoHasLogged();

            ResultSet resultSet = studentService.getAllStudentAbsences(idStudent);
            while (resultSet.next()) {
                String date = resultSet.getString("DateAbsence");
                int idSubject = resultSet.getInt("IdSubject");
                String subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(subjectName + ": " + date);
            }
    }

    public void displayAllUsers() throws SQLException {
        logger.debug("Displaying all users");
        List<User> list = userConverter.convert();
        System.out.println(list);
    }

    public void displayAllTeachers() throws SQLException {
        logger.debug("Displaying all teachers");
            List<User> list = userConverter.convert();
            list.forEach(user -> {
                if (user.getWhoIs().equals("TEACHER")) {
                    System.out.println(user);
                }
            });
    }

    public void displayAllClassesAtSchool() throws SQLException {
        logger.debug("Displaying all classes at school");
        List<SchoolClass> list = teacherService.getListClassWithTeacher();
        for (int i = 0; i < list.size(); i++) {
            String className = list.get(i).getNameClass();
            String teacher = list.get(i).getTeacher();
            List<String> studentList = list.get(i).getStudentList();
            System.out.println("Class: " + className + ", Teacher: " + teacher);
            System.out.println("List of students :");
            studentList.forEach(s -> System.out.println(s));
            System.out.println("*****************************");
        }
    }

    public void displayAllGradesOfStudentForGuardian() throws SQLException {
        logger.debug("Displaying all grades for guardian");
        int idGuardian = logonService.getIdUserWhoHasLogged();

            List<Student> studentList = guardianService.getListOfStudents(idGuardian);
            for (Student student : studentList) {
                String studentName = student.getName();
                String studentSurname = student.getSurname();
                System.out.println("______________________________________");
                System.out.println("Student: " + studentName + " " + studentSurname);
                int idStudent = userService.getIdFromUser(studentName, studentSurname);
                ResultSet resultSet = guardianService.selectGradesFromAssignStudent(idStudent);
                while (resultSet.next()) {
                    String subject = resultSet.getString("SubjectName");
                    int grade = resultSet.getInt("Grade");

                    System.out.println("Subject: " + subject + ", Grade: " + grade);
                }

            }
    }

    public void displayAllAbsencesOfStudentForGuardian() throws SQLException {
        logger.debug("Displaying all absences for guardian");
        int idGuardian = logonService.getIdUserWhoHasLogged();

            List<Student> studentList = guardianService.getListOfStudents(idGuardian);
            for (Student student : studentList) {
                String studentName = student.getName();
                String studentSurname = student.getSurname();
                System.out.println("______________________________________");
                System.out.println("Student: " + studentName + " " + studentSurname);
                int idStudent = userService.getIdFromUser(studentName, studentSurname);
                ResultSet resultSet = studentService.getAllStudentAbsences(idStudent);
                while (resultSet.next()) {
                    String date = resultSet.getString("DateAbsence");
                    int idSubject = resultSet.getInt("IdSubject");
                    String subjectName = subjectService.getSubjectFromId(idSubject);
                    System.out.println(subjectName + ": " + date);
                }
            }
    }

    public void displayStudentsWithTooLowGrades() throws SQLException {
        logger.debug("Displaying students with too low grades");
        int idGuardian = logonService.getIdUserWhoHasLogged();

            List<Student> studentList = guardianService.getListOfStudents(idGuardian);
            for (Student student : studentList) {
                String studentName = student.getName();
                String studentSurname = student.getSurname();

                int idStudent = userService.getIdFromUser(studentName, studentSurname);
                ResultSet resultSet = guardianService.selectGradesFromAssignStudent(idStudent);
                while (resultSet.next()) {
                    String subject = resultSet.getString("SubjectName");
                    int grade = resultSet.getInt("Grade");
                    if (grade < 3) {
                        System.out.println("______________________________________");
                        System.out.println("Student: " + studentName + " " + studentSurname);
                        System.out.println("Subject: " + subject + ", Grade: " + grade);
                    }
                }

            }
    }

    public void displayIfStudentsHaveTooManyAbsences() throws SQLException {
        logger.debug("Displaying students with too many absences");
        boolean checking = true;
        int idGuardian = logonService.getIdUserWhoHasLogged();

            List<Student> studentList = guardianService.getListOfStudents(idGuardian);
            for (Student student : studentList) {
                String studentName = student.getName();
                String studentSurname = student.getSurname();

                int idStudent = userService.getIdFromUser(studentName, studentSurname);
               Map <String, Integer> map = studentService.getMapOfAbsencesOfStudent(idStudent);
                for (String key : map.keySet()){
                    Integer value = map.get(key);
                    int numberOfLessons = subjectService.getNumberOfLessonsForSubject(key);
                    if(value > numberOfLessons * 0.20){
                        System.out.println("___________________________________");
                        System.out.println(studentName +" "+ studentSurname);
                        System.out.println(key +" "+ value);
                        checking = false;
                    }
                }
            }
                if (checking){
                    System.out.println(GeneralMessages_en.WORNING_STATEMENT_11);
                }
            }

}
