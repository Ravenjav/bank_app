package by.itc.bank_app.repository.jdbc;

import by.itc.bank_app.dto.ApplicationResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationJdbcRepository {

    JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_SQL = """
            SELECT
                a.id,
                a.client_id,
                c.full_name AS client_name,
                a.amount,
                a.risk_score,
                a.created_at
            FROM applications a
            JOIN clients c ON c.id = a.client_id
            ORDER BY a.id
            """;

    public List<ApplicationResponse> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_SQL,
                (rs, rowNum) -> new ApplicationResponse(
                        rs.getLong("id"),
                        rs.getLong("client_id"),
                        rs.getString("client_name"),
                        rs.getBigDecimal("amount"),
                        rs.getObject("risk_score", Integer.class),
                        rs.getTimestamp("created_at").toLocalDateTime()
                )
        );
    }
}
