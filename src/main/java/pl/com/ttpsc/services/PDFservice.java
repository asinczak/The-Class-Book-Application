package pl.com.ttpsc.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Scanner;

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

    private static final String STUDENT_CERTIFICATE = "student.certificate.pdf";

    public void createStudentCertifiacte (String name, String surname) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(STUDENT_CERTIFICATE));
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 34, BaseColor.RED);
        Font font1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.GREEN);
        Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.GREEN);

        Paragraph certficate = new Paragraph("Certificate", font);
        certficate.setAlignment(Element.ALIGN_CENTER);
        document.add(certficate);

        Paragraph word =new Paragraph("for", font2);
        word.setAlignment(Element.ALIGN_CENTER);
        document.add(word);

        String student = ""+name+" "+surname;
        Paragraph nameSurname = new Paragraph(student, font1);
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
        cell1.setPaddingLeft(10);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Grade"));
        cell2.setBorderColor(BaseColor.DARK_GRAY);
        cell2.setPaddingLeft(10);

        table.addCell(cell1);
        table.addCell(cell2);

        document.add(table);

        document.close();
    }

    public void generateStudentCertificate() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GeneralMessages_en.ENTER_DATA_30);
        String studentName = scanner.nextLine();

        System.out.println(GeneralMessages_en.ENTER_DATA_31);
        String studentSurname = scanner.nextLine();

        if (userService.checkingIfUserExists(studentName, studentSurname)){
            if(studentService.checkingIfIsStudent(studentName, studentSurname)){

                try {

                    createStudentCertifiacte(studentName, studentSurname);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_4);
            }

        } else {
            System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
        }
    }
}
