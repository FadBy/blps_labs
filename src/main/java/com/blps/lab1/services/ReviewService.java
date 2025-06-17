package com.blps.lab1.services;

import com.blps.lab1.dto.ReviewDTO;
import com.blps.lab1.entities.*;
import com.blps.lab1.exceptions.ReviewInvalidException;
import com.blps.lab1.jca.MailConnection;
import com.blps.lab1.jca.MailConnectionFactory;
import com.blps.lab1.jca.MailManagedConnectionFactory;
import com.blps.lab1.repositories.EmployeeRepository;
import com.blps.lab1.repositories.ResponseRepository;
import com.blps.lab1.repositories.ReviewRepository;
import com.blps.lab1.repositories.VacancyRepository;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.blps.lab1.utils.TransactionHelper;
import com.blps.lab1.utils.TransactionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.*;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private HRService hrService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private TransactionHelper transactionHelper;
    @Autowired
    private MailConnectionFactory mailConnectionFactory;

    public Resource processNewReviews() throws Exception {
        Review review = reviewRepository.findAllByStatus(ReviewStatus.NEW).stream().findFirst().orElse(null);
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

        Response response = null;
        if (review != null) {
            response = processReview(review);
        }
        generatePDF(response, pdfStream);

        return new ByteArrayResource(pdfStream.toByteArray());
    }

    public Review save(ReviewDTO review) {
        if (review.getRating() < 1.0 || review.getRating() > 5.0) {
            throw new ReviewInvalidException("Rating must be between 1 and 5");
        }
        var employee = employeeRepository.findByName(review.getEmployeeName())
                .orElseThrow(() -> new ReviewInvalidException("Employee not found"));
        var vacancy = vacancyRepository.findVacancyById(review.getVacancyId())
                .orElseThrow(() -> new ReviewInvalidException("Vacancy not found"));
        var newReview = new Review();
        newReview.setRating(review.getRating());
        newReview.setText(review.getText());
        newReview.setStatus(ReviewStatus.NEW);
        newReview.setDate(review.getDate());
        newReview.setEmployee(employee);
        newReview.setStatus(ReviewStatus.NEW);
        newReview.setProblemCategory(review.getProblemCategory());
        newReview.setVacancy(vacancy);
        return reviewRepository.save(newReview);
    }

    public List<Review> getReviewsByVacancy(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);
        if (vacancy == null) {
            return new ArrayList<>();
        }
        return reviewRepository.findAllByVacancy(vacancy);
    }

    private Response processReview(Review review) {
        String message;
        if (review.getRating() < 3.5) {
            message = hrService.escalateReview(review);
        } else {
            message = templateService.createResponseTemplate(review);
        }
        var response = prepareResponse(review, message);
        publishResponseTransactional(response);
        return response;
    }

    private void publishResponse(Response response) {
        transactionHelper.executeInTransaction(() -> {
            responseRepository.save(response);
            reviewRepository.updateStatus(response.getReview().getId(), ReviewStatus.PROCESSED);
            try {
                sendMail(response);
            } catch (ResourceException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void publishResponse(Response response, ByteArrayOutputStream pdfStream) {
        responseRepository.save(response);
        reviewRepository.updateStatus(response.getReview().getId(), ReviewStatus.PROCESSED);
    }

    private void generatePDF(Response response, ByteArrayOutputStream pdfStream) throws Exception {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, pdfStream);

            document.open();

            document.add(new Paragraph("Review Response Report"));
            document.add(new Paragraph("----------------------------------------"));

            document.add(new Paragraph(" "));

            if (response == null) {
                document.add(new Paragraph("No new reviews found"));
                return;
            }

            document.add(new Paragraph("Review ID: " + response.getReview().getId()));
            document.add(new Paragraph("Employee: " + response.getReview().getEmployee().getName()));
            document.add(new Paragraph("Vacancy: " + response.getReview().getVacancy().getTitle()));
            document.add(new Paragraph("Rating: " + response.getReview().getRating()));
            document.add(new Paragraph("Date: " + response.getReview().getDate()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Review Text:"));
            document.add(new Paragraph(response.getReview().getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("HR Response:"));
            document.add(new Paragraph(response.getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Response Date: " + response.getPublishDate()));
        } catch (DocumentException e) {
            throw new Exception("Error generating PDF", e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    @Transactional
    public void publishResponseTransactional(Response response) {
        responseRepository.save(response);
        reviewRepository.updateStatus(response.getReview().getId(), ReviewStatus.PROCESSED);
        try {
            sendMail(response);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        }
    }

    private Response prepareResponse(Review review, String message) {
        Response response = new Response();
        response.setText(message);
        response.setStatus(ResponseStatus.PUBLISHED);
        response.setPublishDate(LocalDate.now());
        response.setReview(review);
        return response;
    }

    public void sendMail(Response response) throws ResourceException {
        String mail = response.getReview().getEmployee().getName();
        if (!mail.contains("@")) return;
        MailConnection connection = mailConnectionFactory.getConnection();
        connection.sendEmail(mail, "Ответ на отзыв", response.getText());
    }
}