package by.itc.bank_app.service;

import by.itc.bank_app.entity.Application;
import by.itc.bank_app.exception.ApplicationAlreadyCalculatedException;
import by.itc.bank_app.exception.ApplicationNotFoundException;
import by.itc.bank_app.mapper.ApplicationMapper;
import by.itc.bank_app.repository.ApplicationRepository;
import by.itc.bank_app.repository.ClientRepository;
import by.itc.bank_app.repository.jdbc.ApplicationJdbcRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTest {
    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApplicationJdbcRepository applicationJdbcRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @ParameterizedTest
    @CsvSource({
            "9999.99, 10",
            "10000.00, 50",
            "50000.00, 50",
            "50000.01, 100"
    })
    void calculateScore_correctRiskScore(
            String amount,
            int expectedScore
    ) {
        // Arrange — подготовка данных
        Long applicationId = 1L;

        Application application = new Application();
        application.setId(applicationId);
        application.setAmount(new BigDecimal(amount));

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));

        applicationService.calculateScore(applicationId);

        assertEquals(expectedScore, application.getRiskScore());

        verify(applicationRepository).findById(applicationId);
    }

    @Test
    void calculateScore_exceptionWhenApplicationNotFound() {
        Long applicationId = 999L;

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.empty());

        assertThrows(
                ApplicationNotFoundException.class,
                () -> applicationService.calculateScore(applicationId)
        );

        verify(applicationRepository).findById(applicationId);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void calculateScore_exceptionWhenAlreadyCalculated() {
        Long applicationId = 1L;

        Application application = new Application();
        application.setId(applicationId);
        application.setAmount(new BigDecimal("15000.00"));
        application.setRiskScore(50);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));

        assertThrows(
                ApplicationAlreadyCalculatedException.class,
                () -> applicationService.calculateScore(applicationId)
        );

        assertEquals(50, application.getRiskScore());

        verify(applicationRepository).findById(applicationId);
        verifyNoInteractions(applicationMapper);
    }
}
