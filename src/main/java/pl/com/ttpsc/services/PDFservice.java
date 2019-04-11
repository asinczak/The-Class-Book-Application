package pl.com.ttpsc.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import pl.com.ttpsc.data.Student;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PDFservice {

    private static PDFservice pdfService;

    private PDFservice () {}

    public static PDFservice getInstance(){
        if (pdfService == null){
            pdfService = new PDFservice();
        }
        return pdfService;
    }

    final static Logger logger = Logger.getLogger(PDFservice.class);

    UserService userService = UserService.getInstance();
    StudentService studentService = StudentService.getInstance();
    LogonService logonService = LogonService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();
    TeacherService teacherService = TeacherService.getInstance();
    ClassService classService = ClassService.getInstance();
    GuardianService guardianService = GuardianService.getInstance();

    private static String STUDENT_CERTIFICATE;
    private static final String REPORT = "ReportOfStudents.pdf";

    public void createStudentCertifiacte (String name, String surname) throws IOException, DocumentException, SQLException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(STUDENT_CERTIFICATE));
        document.open();
        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1250, BaseFont.CACHED);

        Font font36red = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 36, BaseColor.RED);
        Font font26green = new Font(baseFont, 26, 1, BaseColor.GREEN);
        Font font16green = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.GREEN);

        Paragraph certficate = new Paragraph(GeneralMessages_en.PDF_STATEMENT_1, font36red);
        certficate.setAlignment(Element.ALIGN_CENTER);
        document.add(certficate);

        Paragraph word =new Paragraph(GeneralMessages_en.PDF_STATEMENT_2, font16green);
        word.setAlignment(Element.ALIGN_CENTER);
        document.add(word);

        String student = ""+name+" "+surname;
        Paragraph nameSurname = new Paragraph(student, font26green);
        nameSurname.setAlignment(Element.ALIGN_CENTER);
        document.add(nameSurname);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setSpacingBefore(80f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {1f, 1f};
        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph(GeneralMessages_en.PDF_STATEMENT_3));
        cell1.setBorderColor(BaseColor.DARK_GRAY);
        cell1.setPaddingLeft(50);

        PdfPCell cell2 = new PdfPCell(new Paragraph(GeneralMessages_en.PDF_STATEMENT_4));
        cell2.setBorderColor(BaseColor.DARK_GRAY);
        cell2.setPaddingLeft(50);

        table.addCell(cell1);
        table.addCell(cell2);

        int idStudent = userService.getIdFromUser(name, surname);
        Map<String, Integer> map = getListAllGradesOfStudentForStudent(idStudent);
        for (String subject : map.keySet()){
            String grade = String.valueOf(map.get(subject));
            table.addCell(subject);
            table.addCell(grade);
        }

        document.add(table);

        String place = GeneralMessages_en.PDF_STATEMENT_5;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String date = dateFormat.format(calendar.getTime());

        String placeAndDate = ""+place+", "+date;
        Paragraph theEnd = new Paragraph(placeAndDate, font16green);
        theEnd.setAlignment(Element.ALIGN_LEFT);
        theEnd.setSpacingBefore(270f);
        document.add(theEnd);

        document.close();
    }

    public void generateStudentCertificate() throws SQLException, IOException, DocumentException {
        logger.debug("Generating student's certificate");
        STUDENT_CERTIFICATE = "Student.certificate.pdf";
        String enteredData = studentService.getNameAndSurnameAndCheckForStudent();
        if (enteredData.equalsIgnoreCase("x")){
            System.out.println(GeneralMessages_en.INFO_STATEMENT_6);
        } else {
            String [] tab = enteredData.split(" ");
            String studentName = tab[0];
            String studentSurname = tab[1];
            createStudentCertifiacte(studentName, studentSurname);
        }
    }

    public Map<String, Integer> getListAllGradesOfStudentForStudent(int idStudent) throws SQLException {
        Map <String, Integer> map = new HashMap<>();

        ResultSet resultSet = studentService.getAllGradesOfStudent(idStudent);
        while (resultSet.next()) {
            int idSubject = resultSet.getInt("IdSubject");
            int grade = resultSet.getInt("Grade");
            String subjectName = subjectService.getSubjectFromId(idSubject);

            map.put(subjectName, grade);
        }
        return map;
    }

    public void generateCertificatesForAllStudentsInClass () throws SQLException, IOException, DocumentException {
        logger.debug("Generating certificates for all students in class");
        int index = 0;
        String className = teacherService.getClassName();
        int idClass = classService.getIdFromClasses(className);

        List <String> listOfStudents = classService.getListOfStudentfromIdClass(idClass);
        for(String student : listOfStudents){
            String [] tab = student.split(" ");
            String name = tab[0];
            String surname = tab[1];
            index++;
            STUDENT_CERTIFICATE = "Student.certificate"+index+".pdf";
            createStudentCertifiacte(name, surname);
        }
    }

    public List <String> getListOfStudentsToGenerateCertificates (){
        Scanner scanner = new Scanner(System.in);
        System.out.println(GeneralMessages_en.ENTER_DATA_40);
        String enteredData = scanner.nextLine();
        List<String> list = Arrays.asList(enteredData.split(", "));
        return list;
    }

    public void generateCertificatesForChosenStudents () throws DocumentException, SQLException, IOException {
        logger.debug("Generating certificates for chosen students");
        int index = 0;
        List<String> list = getListOfStudentsToGenerateCertificates();
        for (String students : list){
            String [] tab = students.split(" ");
            String name = tab[0];
            String surname = tab[1];
            index++;
            STUDENT_CERTIFICATE = "Student.certificate"+index+".chosen.pdf";
            createStudentCertifiacte(name, surname);
        }
    }

    public void createReportForStudentsWhoWillFail () throws IOException, DocumentException, SQLException {
        logger.debug("Creating report for student who will fail");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(REPORT));
        document.open();

        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1250, BaseFont.CACHED);

        Font font26green = new Font(baseFont, 20);

        Paragraph headline = new Paragraph(GeneralMessages_en.PDF_STATEMENT_6, font26green);
        headline.setAlignment(Element.ALIGN_CENTER);
        document.add(headline);

        PdfPTable tableWithTooLowGrades = new PdfPTable(1);
        tableWithTooLowGrades.setWidthPercentage(50);
        tableWithTooLowGrades.setSpacingBefore(80f);
        tableWithTooLowGrades.setSpacingAfter(10f);

        float[] columnWidths = {1f};
        tableWithTooLowGrades.setWidths(columnWidths);


        PdfPCell cell2 = new PdfPCell(new Paragraph(GeneralMessages_en.PDF_STATEMENT_8));
        tableWithTooLowGrades.addCell(cell2);

        List <String> listWithTooLowGrades = getListOfStudentsWhoHaveTooLowGrades();
        for (String data : listWithTooLowGrades){
            tableWithTooLowGrades.addCell(data);
        }

        document.add(tableWithTooLowGrades);

        PdfPTable tableWithTooManyAbsences = new PdfPTable(1);
        tableWithTooManyAbsences.setWidthPercentage(50);
        tableWithTooManyAbsences.setSpacingBefore(80f);
        tableWithTooManyAbsences.setSpacingAfter(10f);

        float[] columnWidths2 = {1f};
        tableWithTooManyAbsences.setWidths(columnWidths2);

        PdfPCell cell1 = new PdfPCell(new Paragraph(GeneralMessages_en.PDF_STATEMENT_7));
        tableWithTooManyAbsences.addCell(cell1);

        List <String> listWithTooManyAbsences = getListOfStudentsWhoHaveTooManyAbsences();
        for (String data : listWithTooManyAbsences){
            tableWithTooManyAbsences.addCell(data);
        }

        document.add(tableWithTooManyAbsences);

        document.close();
    }

    public List <String> getListOfStudentsWhoHaveTooLowGrades () throws SQLException {
        int idGuardian = logonService.getIdUserWhoHasLogged();
        List <String> listOfStudentsWithTooLowGrades = new ArrayList<>();

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
                    String data = ""+studentName+" "+studentSurname+"- "+subject+": "+grade;
                    listOfStudentsWithTooLowGrades.add(data);
                }
            }
        }
        return listOfStudentsWithTooLowGrades;
    }

    public List <String> getListOfStudentsWhoHaveTooManyAbsences () throws SQLException {
        int idGuardian = logonService.getIdUserWhoHasLogged();
        List<String> listOfStudentsWithTooManyAbsences  = new ArrayList<>();

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
                    String data = ""+studentName+" "+studentSurname+": "+key;
                   listOfStudentsWithTooManyAbsences.add(data);
                }
            }
        }
        return listOfStudentsWithTooManyAbsences;
    }
}
