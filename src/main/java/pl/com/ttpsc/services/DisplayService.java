package pl.com.ttpsc.services;

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

    StudentService studentService = StudentService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();
    ClassService classService = ClassService.getInstance();
    UserConverter userConverter = UserConverter.getInstance();
    TeacherService teacherService = TeacherService.getInstance();
    GuardianService guardianService = GuardianService.getInstance();
    LogonService logonService = LogonService.getInstance();
    UserService userService = UserService.getInstance();


    public void displayAllGradesOfStudentForStudent() {
        try {
            ResultSet resultSet = studentService.getAllGradesOfStudent();
            while (resultSet.next()) {
                int idSubject = resultSet.getInt("IdSubject");
                int grade = resultSet.getInt("Grade");
                String subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(subjectName + ": " + grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllStudentAbsencesForStudent() {
        int idStudent = logonService.getIdUserWhoHasLogged();
        try {
            ResultSet resultSet = studentService.getAllStudentAbsences(idStudent);
            while (resultSet.next()) {
                String date = resultSet.getString("DateAbsence");
                int idSubject = resultSet.getInt("IdSubject");
                String subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(subjectName + ": " + date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllUsers() {
        try {
            ResultSet resultSet = UserConverter.selectAllUsers();
            List<User> list = userConverter.convert(resultSet);
            System.out.println(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllTeachers() {
        try {
            ResultSet resultSet = UserConverter.selectAllUsers();
            List<User> list = userConverter.convert(resultSet);
            list.forEach(user -> {
                if (user.getWhoIs().equals("TEACHER")) {
                    System.out.println(user);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllClassesAtSchool() {

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

    public void displayAllGradesOfStudentForGuardian() {
        int idGuardian = logonService.getIdUserWhoHasLogged();

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllAbsencesOfStudentForGuardian() {
        int idGuardian = logonService.getIdUserWhoHasLogged();

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayStudentsWithTooLowGrades() {
        int idGuardian = logonService.getIdUserWhoHasLogged();

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayIfStudentsHaveTooManyAbsences() {
        boolean checking = true;
        int idGuardian = logonService.getIdUserWhoHasLogged();

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
