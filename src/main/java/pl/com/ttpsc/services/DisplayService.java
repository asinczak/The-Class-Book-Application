package pl.com.ttpsc.services;

import pl.com.ttpsc.data.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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


    public void displayAllGradesOfStudent () {
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

//        for (int i = 0; i<map.size(); i++) {

                try {
                    Map<String, String> map = classService.getMapClassWithTeacher();
                    for (Map.Entry <String, String> entry : map.entrySet()) {
                        int idClass = classService.getIdFromClasses(entry.getKey());
                        List<String> list = classService.getListOfStudentfromIdClass(idClass);
                        System.out.println(entry.getKey() + " " + entry.getValue());
                        System.out.println("List of students :");
                        list.forEach(s -> System.out.println(s));
                        System.out.println("*****************************");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


//        }

//        map.forEach((k, v) ->System.out.println("Class: "+ k +": Teacher: " +v));

    }
}
