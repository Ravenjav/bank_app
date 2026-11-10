package by.itc.bank_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicationResponse (
    Long id,
    Long clientId,
    String clientName,
    BigDecimal amount,
    Integer riskScore,
    LocalDateTime createdAt
){}
