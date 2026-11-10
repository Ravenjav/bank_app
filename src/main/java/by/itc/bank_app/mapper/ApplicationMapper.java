package by.itc.bank_app.mapper;

import by.itc.bank_app.dto.ApplicationResponse;
import by.itc.bank_app.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {
    public ApplicationResponse toResponse(Application entity) {
        return new ApplicationResponse(
                entity.getId(),
                entity.getClient().getId(),
                entity.getClient().getFullName(),
                entity.getAmount(),
                entity.getRiskScore(),
                entity.getCreatedAt()
        );
    }
}
