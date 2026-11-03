/*
package com.smartexam.studentidcard.generator;

import com.smartexam.student.entity.Student;
import com.smartexam.studentidcard.entity.StudentIdCard;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StudentIdCardPdfGenerator {

    private static final PDFont BOLD_FONT =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

    private static final PDFont NORMAL_FONT =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

    private static final float CARD_WIDTH = 360;
    private static final float CARD_HEIGHT = 240;

    public void generate(
            StudentIdCard idCard,
            Path outputPath
    ) throws IOException {

        Student student = idCard.getStudent();

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student is required for ID card generation"
            );
        }

        if (outputPath == null) {
            throw new IllegalArgumentException(
                    "Output path is required"
            );
        }

        Files.createDirectories(
                outputPath.getParent()
        );

        try (PDDocument document = new PDDocument()) {

            PDPage page =
                    new PDPage(
                            new PDRectangle(
                                    CARD_WIDTH,
                                    CARD_HEIGHT
                            )
                    );

            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(
                                 document,
                                 page
                         )) {

                float pageWidth =
                        page.getMediaBox().getWidth();

                float pageHeight =
                        page.getMediaBox().getHeight();


                // ==================================================
                // HEADER
                // ==================================================

                content.setNonStrokingColor(
                        40,
                        70,
                        180
                );

                content.addRect(
                        0,
                        pageHeight - 55,
                        pageWidth,
                        55
                );

                content.fill();


                // ==================================================
                // SMARTEXAM
                // ==================================================

                content.setNonStrokingColor(
                        255,
                        255,
                        255
                );

                drawCenteredText(
                        content,
                        "SMARTEXAM",
                        pageWidth / 2,
                        pageHeight - 25,
                        BOLD_FONT,
                        17
                );


                // ==================================================
                // STUDENT ID CARD
                // ==================================================

                drawCenteredText(
                        content,
                        "STUDENT ID CARD",
                        pageWidth / 2,
                        pageHeight - 43,
                        NORMAL_FONT,
                        9
                );


                // ==================================================
                // STUDENT NAME
                // ==================================================

                content.setNonStrokingColor(
                        25,
                        25,
                        25
                );

                String studentName =
                        buildStudentName(student);

                drawText(
                        content,
                        studentName,
                        25,
                        pageHeight - 78,
                        BOLD_FONT,
                        15
                );


                // ==================================================
                // STUDENT DETAILS
                // ==================================================

                float y =
                        pageHeight - 102;

                y = drawField(
                        content,
                        "Student Code",
                        student.getStudentCode(),
                        25,
                        y
                );

                y = drawField(
                        content,
                        "Admission No",
                        student.getAdmissionNo(),
                        25,
                        y
                );

                y = drawField(
                        content,
                        "Class",
                        getClassName(student),
                        25,
                        y
                );

                y = drawField(
                        content,
                        "Card No",
                        idCard.getCardNumber(),
                        25,
                        y
                );


                // ==================================================
                // DATE OF BIRTH
                // ==================================================

                if (student.getDateOfBirth() != null) {

                    drawField(
                            content,
                            "Date of Birth",
                            student.getDateOfBirth().toString(),
                            25,
                            y
                    );
                }


                // ==================================================
                // FOOTER
                // ==================================================

                content.setNonStrokingColor(
                        100,
                        100,
                        100
                );

                drawText(
                        content,
                        "This card is property of SmartExam.",
                        25,
                        18,
                        NORMAL_FONT,
                        7
                );
            }

            document.save(
                    outputPath.toFile()
            );
        }
    }


    // ==============================================================
    // DRAW FIELD
    // ==============================================================

    private float drawField(
            PDPageContentStream content,
            String label,
            String value,
            float x,
            float y
    ) throws IOException {

        content.beginText();

        content.setFont(
                BOLD_FONT,
                8
        );

        content.newLineAtOffset(
                x,
                y
        );

        content.showText(
                safeText(label) + ": "
        );

        content.setFont(
                NORMAL_FONT,
                8
        );

        content.showText(
                safeText(value)
        );

        content.endText();

        return y - 16;
    }


    // ==============================================================
    // DRAW NORMAL TEXT
    // ==============================================================

    private void drawText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            PDFont font,
            float fontSize
    ) throws IOException {

        content.beginText();

        content.setFont(
                font,
                fontSize
        );

        content.newLineAtOffset(
                x,
                y
        );

        content.showText(
                safeText(text)
        );

        content.endText();
    }


    // ==============================================================
    // DRAW CENTERED TEXT
    // ==============================================================

    private void drawCenteredText(
            PDPageContentStream content,
            String text,
            float centerX,
            float y,
            PDFont font,
            float fontSize
    ) throws IOException {

        String safeText =
                safeText(text);

        float textWidth =
                font.getStringWidth(safeText)
                        / 1000f
                        * fontSize;

        float x =
                centerX - (textWidth / 2);


        content.beginText();

        content.setFont(
                font,
                fontSize
        );

        content.newLineAtOffset(
                x,
                y
        );

        content.showText(
                safeText
        );

        content.endText();
    }


    // ==============================================================
    // STUDENT NAME
    // ==============================================================

    private String buildStudentName(
            Student student
    ) {

        String firstName =
                student.getFirstName() == null
                        ? ""
                        : student.getFirstName().trim();

        String lastName =
                student.getLastName() == null
                        ? ""
                        : student.getLastName().trim();

        return (
                firstName
                        + " "
                        + lastName
        ).trim();
    }


    // ==============================================================
    // CLASS NAME
    // ==============================================================

    private String getClassName(
            Student student
    ) {
        System.out.println("-------------------->"+student.getClassRoom());
        if (student.getClassRoom() == null) {
            return "-";
        }

        if (student.getClassRoom().getName() == null) {
            return "-";
        }

        return student
                .getClassRoom()
                .getName();
    }


    // ==============================================================
    // SAFE TEXT
    // ==============================================================

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.isBlank()) {

            return "-";
        }

        return value;
    }
}*/
package com.smartexam.studentidcard.generator;

import com.smartexam.student.entity.Student;
import com.smartexam.studentidcard.entity.StudentIdCard;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StudentIdCardPdfGenerator {

    // ============================================================
    // Card Size
    // ============================================================

    private static final float CARD_WIDTH = 360f;
    private static final float CARD_HEIGHT = 240f;

    // ============================================================
    // Colors - RGB 0-255
    // ============================================================

    private static final float TEAL_R = 15 / 255f;
    private static final float TEAL_G = 158 / 255f;
    private static final float TEAL_B = 170 / 255f;

    private static final float DARK_BLUE_R = 8 / 255f;
    private static final float DARK_BLUE_G = 91 / 255f;
    private static final float DARK_BLUE_B = 130 / 255f;

    private static final float TEXT_R = 20 / 255f;
    private static final float TEXT_G = 55 / 255f;
    private static final float TEXT_B = 75 / 255f;

    private static final float WHITE = 1f;

    private static final float LIGHT_GRAY = 235 / 255f;


    // ============================================================
    // Fonts
    // ============================================================

    private final PDType1Font boldFont =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

    private final PDType1Font normalFont =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );


    // ============================================================
    // Generate
    // ============================================================

    public void generate(
            StudentIdCard idCard,
            Path outputPath
    ) throws IOException {

        Student student = idCard.getStudent();

        Files.createDirectories(
                outputPath.getParent()
        );

        try (PDDocument document = new PDDocument()) {

            PDPage page =
                    new PDPage(
                            new PDRectangle(
                                    CARD_WIDTH,
                                    CARD_HEIGHT
                            )
                    );

            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(
                                 document,
                                 page
                         )) {

                // =================================================
                // Background
                // =================================================

                setColor(
                        content,
                        250,
                        252,
                        253
                );

                content.addRect(
                        0,
                        0,
                        CARD_WIDTH,
                        CARD_HEIGHT
                );

                content.fill();


                // =================================================
                // Top School Header
                // =================================================

                drawHeader(
                        content,
                        student
                );


                // =================================================
                // Student Photo
                // =================================================

                drawStudentPhoto(
                        document,
                        content,
                        student
                );


                // =================================================
                // Student ID Card Ribbon
                // =================================================

                drawIdCardRibbon(
                        content
                );


                // =================================================
                // Student Details
                // =================================================

                drawStudentDetails(
                        content,
                        student
                );


                // =================================================
                // Academic Session
                // =================================================

                drawAcademicSession(
                        content,
                        idCard
                );


                // =================================================
                // Identification Number
                // =================================================

                drawIdentificationNumber(
                        content,
                        idCard
                );


                // =================================================
                // Bottom decorative line
                // =================================================

                drawBottomDecoration(
                        content
                );
            }

            document.save(
                    outputPath.toFile()
            );
        }
    }


    // ============================================================
    // Header
    // ============================================================

    private void drawHeader(
            PDPageContentStream content,
            Student student
    ) throws IOException {

        setColor(
                content,
                15,
                158,
                170
        );

        content.addRect(
                0,
                CARD_HEIGHT - 55,
                CARD_WIDTH,
                55
        );

        content.fill();


        // School name

        setColor(
                content,
                255,
                255,
                255
        );

        String schoolName =
                getSchoolName(student);

        drawText(
                content,
                schoolName,
                18,
                CARD_HEIGHT - 32,
                20,
                boldFont
        );


        // Small subtitle

        drawText(
                content,
                "STUDENT IDENTIFICATION",
                18,
                CARD_HEIGHT - 47,
                7,
                normalFont
        );
    }


    // ============================================================
    // Student Photo
    // ============================================================

    private void drawStudentPhoto(
            PDDocument document,
            PDPageContentStream content,
            Student student
    ) throws IOException {

        float x = 20;
        float y = 83;
        float width = 105;
        float height = 112;


        // Photo background

        setColor(
                content,
                255,
                255,
                255
        );

        content.addRect(
                x,
                y,
                width,
                height
        );

        content.fill();


        // Photo border

        setColor(
                content,
                15,
                158,
                170
        );

        content.setLineWidth(
                3
        );

        content.addRect(
                x,
                y,
                width,
                height
        );

        content.stroke();


        String photoUrl =
                student.getPhotoUrl();

        if (photoUrl == null ||
                photoUrl.isBlank()) {

            drawPhotoPlaceholder(
                    content,
                    x,
                    y,
                    width,
                    height
            );

            return;
        }


        try {

            BufferedImage image =
                    loadImage(photoUrl);

            if (image == null) {

                drawPhotoPlaceholder(
                        content,
                        x,
                        y,
                        width,
                        height
                );

                return;
            }


            PDImageXObject pdImage =
                    PDImageXObject.createFromByteArray(
                            document,
                            imageToBytes(image),
                            "student-photo"
                    );


            content.drawImage(
                    pdImage,
                    x + 4,
                    y + 4,
                    width - 8,
                    height - 8
            );

        } catch (Exception ex) {

            drawPhotoPlaceholder(
                    content,
                    x,
                    y,
                    width,
                    height
            );
        }
    }


    // ============================================================
    // Photo Placeholder
    // ============================================================

    private void drawPhotoPlaceholder(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height
    ) throws IOException {

        setColor(
                content,
                235,
                245,
                247
        );

        content.addRect(
                x + 4,
                y + 4,
                width - 8,
                height - 8
        );

        content.fill();


        setColor(
                content,
                15,
                100,
                130
        );

        drawCenteredText(
                content,
                "PHOTO",
                x + width / 2,
                y + height / 2,
                11,
                boldFont
        );
    }


    // ============================================================
    // ID Card Ribbon
    // ============================================================

    private void drawIdCardRibbon(
            PDPageContentStream content
    ) throws IOException {

        float x = 135;
        float y = 166;
        float width = 150;
        float height = 25;


        setColor(
                content,
                8,
                91,
                130
        );

        content.moveTo(
                x,
                y
        );

        content.lineTo(
                x + width,
                y
        );

        content.lineTo(
                x + width - 15,
                y + height
        );

        content.lineTo(
                x,
                y + height
        );

        content.closePath();

        content.fill();


        setColor(
                content,
                255,
                255,
                255
        );

        drawText(
                content,
                "STUDENT ID CARD",
                x + 14,
                y + 8,
                10,
                boldFont
        );
    }


    // ============================================================
    // Student Details
    // ============================================================

    private void drawStudentDetails(
            PDPageContentStream content,
            Student student
    ) throws IOException {

        float xLabel = 140;
        float xValue = 202;

        float y = 150;


        drawDetail(
                content,
                "NAME",
                buildStudentName(student),
                xLabel,
                xValue,
                y
        );

        y -= 22;


        drawDetail(
                content,
                "CLASS",
                getClassName(student),
                xLabel,
                xValue,
                y
        );

        y -= 22;


        drawDetail(
                content,
                "DOB",
                student.getDateOfBirth() == null
                        ? "-"
                        : student.getDateOfBirth().toString(),
                xLabel,
                xValue,
                y
        );

        y -= 22;


        drawDetail(
                content,
                "ADMISSION",
                safeText(student.getAdmissionNo()),
                xLabel,
                xValue,
                y
        );

        y -= 22;


        drawDetail(
                content,
                "STUDENT CODE",
                safeText(student.getStudentCode()),
                xLabel,
                xValue,
                y
        );
    }


    // ============================================================
    // Detail Row
    // ============================================================

    private void drawDetail(
            PDPageContentStream content,
            String label,
            String value,
            float labelX,
            float valueX,
            float y
    ) throws IOException {

        setColor(
                content,
                8,
                91,
                130
        );

        drawText(
                content,
                label,
                labelX,
                y,
                7,
                boldFont
        );


        setColor(
                content,
                20,
                55,
                75
        );

        drawText(
                content,
                ":",
                labelX + 50,
                y,
                8,
                boldFont
        );


        drawText(
                content,
                safeText(value),
                valueX,
                y,
                8,
                boldFont
        );
    }


    // ============================================================
    // Academic Session
    // ============================================================

    private void drawAcademicSession(
            PDPageContentStream content,
            StudentIdCard idCard
    ) throws IOException {

        setColor(
                content,
                8,
                91,
                130
        );

        String session =
                getAcademicSessionName(idCard);

        drawCenteredText(
                content,
                session,
                73,
                68,
                11,
                boldFont
        );
    }


    // ============================================================
    // Identification Number
    // ============================================================

    private void drawIdentificationNumber(
            PDPageContentStream content,
            StudentIdCard idCard
    ) throws IOException {

        float x = 170;
        float y = 25;
        float width = 170;
        float height = 27;


        setColor(
                content,
                8,
                91,
                130
        );

        content.addRect(
                x,
                y,
                width,
                height
        );

        content.fill();


        setColor(
                content,
                255,
                255,
                255
        );

        drawText(
                content,
                "IDENTIFICATION NO:",
                x + 10,
                y + 9,
                7,
                normalFont
        );


        drawText(
                content,
                safeText(idCard.getCardNumber()),
                x + 102,
                y + 9,
                9,
                boldFont
        );
    }


    // ============================================================
    // Bottom Decoration
    // ============================================================

    private void drawBottomDecoration(
            PDPageContentStream content
    ) throws IOException {

        setColor(
                content,
                15,
                158,
                170
        );

        content.moveTo(
                135,
                52
        );

        content.lineTo(
                160,
                25
        );

        content.lineTo(
                170,
                25
        );

        content.lineTo(
                145,
                52
        );

        content.closePath();

        content.fill();


        content.moveTo(
                145,
                52
        );

        content.lineTo(
                170,
                25
        );

        content.lineTo(
                180,
                25
        );

        content.lineTo(
                155,
                52
        );

        content.closePath();

        content.fill();
    }


    // ============================================================
    // Draw Text
    // ============================================================

    private void drawText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float fontSize,
            PDType1Font font
    ) throws IOException {

        content.beginText();

        content.setFont(
                font,
                fontSize
        );

        content.newLineAtOffset(
                x,
                y
        );

        content.showText(
                safeText(text)
        );

        content.endText();
    }


    // ============================================================
    // Center Text
    // ============================================================

    private void drawCenteredText(
            PDPageContentStream content,
            String text,
            float centerX,
            float y,
            float fontSize,
            PDType1Font font
    ) throws IOException {

        String safe =
                safeText(text);

        float width =
                font.getStringWidth(safe)
                        / 1000f
                        * fontSize;

        drawText(
                content,
                safe,
                centerX - width / 2,
                y,
                fontSize,
                font
        );
    }


    // ============================================================
    // School Name
    // ============================================================

    private String getSchoolName(
            Student student
    ) {

        /*
         * If your Student entity has:
         *
         * private Organization organization;
         *
         * use:
         *
         * return student.getOrganization().getName();
         *
         * For now this fallback keeps PDF generation safe.
         */

        try {

            if (student.getOrganization() != null &&
                    student.getOrganization().getName() != null &&
                    !student.getOrganization().getName().isBlank()) {

                return student
                        .getOrganization()
                        .getName();

            }

        } catch (Exception ignored) {
        }

        return "SMARTEXAM SCHOOL";
    }


    // ============================================================
    // Academic Session
    // ============================================================

    private String getAcademicSessionName(
            StudentIdCard idCard
    ) {

        /*
         * If StudentIdCard has:
         *
         * private AcademicSession academicSession;
         *
         * then use:
         *
         * return idCard.getAcademicSession().getName();
         */

        return "2026 - 2027";
    }


    // ============================================================
    // Class Name
    // ============================================================

    private String getClassName(
            Student student
    ) {

        if (student.getClassRoom() == null) {
            return "-";
        }

        return safeText(
                student
                        .getClassRoom()
                        .getName()
        );
    }


    // ============================================================
    // Student Name
    // ============================================================

    private String buildStudentName(
            Student student
    ) {

        String firstName =
                student.getFirstName() == null
                        ? ""
                        : student.getFirstName();

        String lastName =
                student.getLastName() == null
                        ? ""
                        : student.getLastName();

        return (
                firstName + " " + lastName
        ).trim();
    }


    // ============================================================
    // Color
    // ============================================================

    private void setColor(
            PDPageContentStream content,
            int r,
            int g,
            int b
    ) throws IOException {

        content.setNonStrokingColor(
                r / 255f,
                g / 255f,
                b / 255f
        );
    }


    // ============================================================
    // Safe Text
    // ============================================================

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.isBlank()) {

            return "-";
        }

        return value;
    }


    // ============================================================
    // Load Photo
    // ============================================================

    private BufferedImage loadImage(
            String photoUrl
    ) throws Exception {

        if (photoUrl.startsWith("http://") ||
                photoUrl.startsWith("https://")) {

            try (InputStream inputStream =
                         URI.create(photoUrl)
                                 .toURL()
                                 .openStream()) {

                return ImageIO.read(
                        inputStream
                );
            }
        }

        return ImageIO.read(
                Path.of(photoUrl)
                        .toFile()
        );
    }


    // ============================================================
    // Convert Image
    // ============================================================

    private byte[] imageToBytes(
            BufferedImage image
    ) throws IOException {

        java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();

        ImageIO.write(
                image,
                "png",
                output
        );

        return output.toByteArray();
    }
}