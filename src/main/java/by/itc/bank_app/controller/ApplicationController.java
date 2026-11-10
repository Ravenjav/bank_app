package by.itc.bank_app.controller;

import by.itc.bank_app.dto.ApplicationResponse;
import by.itc.bank_app.dto.CreateApplicationRequest;
import by.itc.bank_app.dto.ErrorResponse;
import by.itc.bank_app.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/applications")
@Tag(
        name = "Банковские заявки",
        description = "Управление заявками и расчёт кредитного рейтинга"
)
public class ApplicationController {
    ApplicationService applicationService;

    @Operation(
            summary = "Создание новой заявки",
            description = "Регистрация заявки для существующего клиента банка"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Заявка создана"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные входные данные",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Клиент не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @Valid @RequestBody CreateApplicationRequest request
    ) {
        log.info("Создание заявки для клиента: {}", request.clientId());

        ApplicationResponse response =
                applicationService.createApplication(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Получение списка заявок",
            description = "Возвращает все зарегистрированные банковские заявки"
    )
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getApplications() {
        log.info("Получение списка заявок");

        return ResponseEntity.ok(
                applicationService.getAllApplications()
        );
    }

    @Operation(
            summary = "Расчёт рейтинга заявки",
            description = "Выполняет расчёт рейтинга по сумме заявки"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Рейтинг рассчитан"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный идентификатор заявки",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Заявка не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Рейтинг уже рассчитан",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/{id}/calculate")
    public ResponseEntity<ApplicationResponse> calculateScore(
            @Parameter(
                    description = "Идентификатор заявки",
                    required = true,
                    example = "1"
            )
            @PathVariable @Positive Long id
    ) {
        log.info("Расчёт рейтинга заявки: {}", id);

        return ResponseEntity.ok(
                applicationService.calculateScore(id)
        );
    }
}
