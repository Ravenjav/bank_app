package by.itc.bank_app.service;

import by.itc.bank_app.dto.ApplicationResponse;
import by.itc.bank_app.dto.CreateApplicationRequest;
import by.itc.bank_app.entity.Application;
import by.itc.bank_app.entity.Client;
import by.itc.bank_app.exception.ApplicationAlreadyCalculatedException;
import by.itc.bank_app.exception.ApplicationNotFoundException;
import by.itc.bank_app.exception.ClientNotFoundException;
import by.itc.bank_app.mapper.ApplicationMapper;
import by.itc.bank_app.repository.ApplicationRepository;
import by.itc.bank_app.repository.ClientRepository;
import by.itc.bank_app.repository.jdbc.ApplicationJdbcRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationServiceImpl implements ApplicationService{

    ApplicationRepository applicationRepository;
    ApplicationJdbcRepository applicationJdbcRepository;
    ClientRepository clientRepository;
    ApplicationMapper applicationMapper;

    @Override
    @Transactional
    public ApplicationResponse createApplication(CreateApplicationRequest request) {

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ClientNotFoundException(request.clientId()));

        Application application = new Application();
        application.setClient(client);
        application.setAmount(request.amount());

        Application saved = applicationRepository.saveAndFlush(application);

        log.info("Создана заявка ID={}", saved.getId());

        return applicationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllApplications() {

        return applicationJdbcRepository.findAll();
    }

    @Override
    @Transactional
    public ApplicationResponse calculateScore(Long id) {

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));

        if (application.getRiskScore() != null) {
            throw new ApplicationAlreadyCalculatedException(id);
        }

        application.setRiskScore(calculateRiskScore(application.getAmount()));

        log.info("Рейтинг заявки ID={} рассчитан: {}", id, application.getRiskScore());

        return applicationMapper.toResponse(application);
    }

    private int calculateRiskScore(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(10_000)) < 0) {
            return 10;
        }

        if (amount.compareTo(BigDecimal.valueOf(50_000)) <= 0) {
            return 50;
        }

        return 100;
    }
}
