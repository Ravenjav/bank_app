package by.itc.bank_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateApplicationRequest (
        @Schema(
                description = "Идентификатор существующего клиента",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull @Positive
        Long clientId,

        @Schema(
                description = "Сумма кредитной заявки",
                example = "15000.00",
                minimum = "0.01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull @Positive
        BigDecimal amount
){}
