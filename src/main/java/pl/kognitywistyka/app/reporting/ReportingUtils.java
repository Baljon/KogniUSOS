package pl.kognitywistyka.app.reporting;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.user.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by RJ on 2017-06-25.
 */

/** Report generating facility.
 * There is just one public method generateReport; all other methods are intended for in-class use only.
 * The tool used is iTextPDF.
 */
public class ReportingUtils {

    /** Method creating a .pdf report listing of students to be enrolled to courses, then saves it.
     * @param courses List<Course>; the method assumes that all the courses are offered by a single faculty.
     * @return: File .pdf doc named "[facultyNameHere]EnrollmentList.pdf".
     */
    public static File generateReport(List<Course> courses) {

        String facultyName = courses.get(0).getFaculty();
        File reportFile = new File(facultyName + "EnrollmentList.pdf");

        try {
            Document report = new Document();
            PdfWriter writer = PdfWriter.getInstance(report, new FileOutputStream(reportFile));
            report.open();
            addCogSciMetadata(report, facultyName);
            addDateLine(report);
            addTitleParagraph(report, facultyName);
            addReportText(report, facultyName);
            addStudentList(report, courses);
            for (int i = 0; i < 4; i++) {
                report.add(new Paragraph(""));
            }
            addSignatureForm(report);
            report.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return reportFile;
        }
    }

    /**Create a base font with PL characters
     * @return BaseFont object, PL-adjusted
     * @throws IOException
     * @throws DocumentException
     */
    private static BaseFont getPolishBaseFont() throws IOException, DocumentException {
        BaseFont litwoOjczyznoMoja = null;
        litwoOjczyznoMoja = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.CACHED);
        return litwoOjczyznoMoja;
    }

    /**Add metadata to the document:
     * @param doc document to be formatted
     * @param facultyName for the title
     * Author & Creator are "Kognitywistyka I' by default.
     * Title is of the form '[facultyNameHere] enrollment list'; creation date is also added.
     */
    private static void addCogSciMetadata(Document doc, String facultyName) {
        doc.addTitle(facultyName + " enrollment list");
        doc.addAuthor("Kognitywistyka I");
        doc.addCreator("Kognitywistyka I");
        doc.addCreationDate();
    }

    /** Adds simple left-aligned line containing the current date: dd-MM-yyyy
     * @param doc
     * @throws DocumentException
     */
    private static void addDateLine(Document doc) throws DocumentException {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateLine = sdf.format(currentDate);
        Paragraph dateParagraph = new Paragraph(dateLine);
        dateParagraph.setAlignment(Element.ALIGN_LEFT);
        doc.add(dateParagraph);
    }

    /** Adds title to the report.
     * @param doc
     * @param facultyName
     * @throws DocumentException
     * @throws IOException
     */
    private static void addTitleParagraph(Document doc, String facultyName) throws DocumentException, IOException {
        BaseFont litwoOjczyznoMoja = getPolishBaseFont();
        Font polishFuckup  = new Font(litwoOjczyznoMoja, 18, Font.BOLD);
        Paragraph titlePar = new Paragraph();
        //Empty line
        titlePar.add(new Paragraph(" "));
        titlePar.setFont(polishFuckup);
        titlePar.add("Lista studentów do zarejestrowania na kursy wydziału");
    }

    /**Adds plain & concise report text.
     * @param doc
     * @param facultyName
     * @throws IOException
     * @throws DocumentException
     */
    private static void addReportText(Document doc, String facultyName) throws IOException, DocumentException {
        String text = "Uprzejmie proszę o zarejestrowanie na zajęcia organizowane przez " +
                facultyName + " następujących studentów kierunku kognitywistyka:";
        Paragraph textPar = new Paragraph();
        Font polishFuckup = new Font(getPolishBaseFont(), 12, Font.NORMAL);
        textPar.add(text);
        doc.add(textPar);
    }

    /** For each course from the list, adds to doc a list of all students to be enrolled.
     * @param doc
     * @param courses
     * @throws IOException
     * @throws DocumentException
     */
    private static void addStudentList(Document doc, List<Course> courses) throws IOException, DocumentException {
        Paragraph listPar = new Paragraph();
        //3 empty lines:
        for (int i = 0; i < 5; i++) {
            listPar.add(new Paragraph(" "));
        }
        //For each course, list all students to be enrolled.
        for (Course course: courses) {
            Paragraph currentCoursePar = new Paragraph();
            Paragraph title = new Paragraph(course.toString());
            title.setFont(new Font(getPolishBaseFont(), 14, Font.BOLD));
            currentCoursePar.add(title);
            Paragraph listing = new Paragraph();
            listing.setFont(new Font(getPolishBaseFont(), 12, Font.NORMAL));
            com.itextpdf.text.List studentList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
            studentList.setListSymbol("•");
            for (Student student : course.getStudents()) {
                studentList.add(new ListItem(student.toString()));
            }
            listing.add(studentList);
            currentCoursePar.add(listing);
            currentCoursePar.add(new Paragraph(" "));
            listPar.add(currentCoursePar);
        }
        doc.add(listPar);
    }

    private static void addSignatureForm(Document doc) throws IOException, DocumentException {
        Paragraph sign = new Paragraph();
        sign.setFont(new Font(getPolishBaseFont(), 12, Font.NORMAL));
        sign.setAlignment(Element.ALIGN_RIGHT);
        sign.add("Admin Admin, Kierownik studiów kognitywistycznych");
        doc.add(sign);
    }
}
