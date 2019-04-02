package pl.com.ttpsc.services;

import pl.com.ttpsc.data.SchoolClass;
import pl.com.ttpsc.data.Student;
import pl.com.ttpsc.data.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DisplayService {

    private static DisplayService displayService;

    private DisplayService () {}

    public static DisplayService getInstance(){
        if (displayService == null){
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


    public void displayAllGradesOfStudentForStudent () {
        try {
        ResultSet resultSet = studentService.getAllGradesOfStudent();
        while (resultSet.next()) {
            int idSubject = resultSet.getInt("IdSubject");
            int grade = resultSet.getInt("Grade");
            String subjectName = subjectService.getSubjectFromId(idSubject);
            System.out.println(subjectName+": "+grade);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public void displayAllStudentAbsences (){
        try {
            ResultSet resultSet = studentService.getAllStudentAbsences();
            while (resultSet.next()){
                String date = resultSet.getString("DateAbsence");
                int idSubject = resultSet.getInt("IdSubject");
                String subjectName = subjectService.getSubjectFromId(idSubject);
                System.out.println(subjectName+": "+date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllUsers () {
        try {
            ResultSet resultSet = UserConverter.selectAllUsers();
            List<User> list = userConverter.convert(resultSet);
            System.out.println(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllTeachers () {
        try {
            ResultSet resultSet = UserConverter.selectAllUsers();
            List<User> list = userConverter.convert(resultSet);
            list.forEach(user -> {if (user.getWhoIs().equals("TEACHER")){
                System.out.println(user);}
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllClassesAtSchool () {

        List <SchoolClass> list = teacherService.getListClassWithTeacher();
            for (int i = 0; i<list.size(); i++) {
                String className = list.get(i).getNameClass();
                String teacher = list.get(i).getTeacher();
                List<String> studentList = list.get(i).getStudentList();
                System.out.println("Class: "+className + ", Teacher: " + teacher);
                System.out.println("List of students :");
                studentList.forEach(s -> System.out.println(s));
                System.out.println("*****************************");
            }
    }

    public void displayAllGradesForStudentForGuardian () {
        int idGuardian;
        List <Student> studentList = guardianService.assignListOfStudents(idGuardian);

    }
}
