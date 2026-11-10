package by.itc.bank_app.service;

import by.itc.bank_app.dto.ApplicationResponse;
import by.itc.bank_app.dto.CreateApplicationRequest;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(CreateApplicationRequest request);

    List<ApplicationResponse> getAllApplications();

    ApplicationResponse calculateScore(Long id);
}
