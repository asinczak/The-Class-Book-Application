package pl.com.ttpsc.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
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

    UserService userService = UserService.getInstance();
    StudentService studentService = StudentService.getInstance();
    LogonService logonService = LogonService.getInstance();
    SubjectService subjectService = SubjectService.getInstance();
    TeacherService teacherService = TeacherService.getInstance();
    ClassService classService = ClassService.getInstance();

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

        Paragraph certficate = new Paragraph("Certificate", font36red);
        certficate.setAlignment(Element.ALIGN_CENTER);
        document.add(certficate);

        Paragraph word =new Paragraph("for", font16green);
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

        PdfPCell cell1 = new PdfPCell(new Paragraph("Subject"));
        cell1.setBorderColor(BaseColor.DARK_GRAY);
        cell1.setPaddingLeft(50);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Grade"));
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

        String place = "Lodz";

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

    public void createReportForStudentsWhoWillFail () throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(REPORT));
        document.open();

        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1250, BaseFont.CACHED);

        Font font26green = new Font(baseFont, 20);

        Paragraph headline = new Paragraph("Report of students who are going to fail promotion", font26green);
        headline.setAlignment(Element.ALIGN_CENTER);
        document.add(headline);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setSpacingBefore(80f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {1f, 1f};
        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Fail because of too many absences"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Fail because of too low grades"));

        table.addCell(cell1);
        table.addCell(cell2);
    }
}
