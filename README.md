# Bank Application

Веб-приложение для учёта банковских заявок и расчёта рейтинга риска по сумме.

**Технологии:** Java 21, Spring Boot 3.2.2, Spring Data JPA, JDBC, H2, Thymeleaf, JavaScript, Gradle.

## Запуск

Для запуска требуется Java 21.

Скачать готовый `bank-app.jar` из раздела [Releases](../../releases/latest) и выполнить:

```bash
  java -jar bank-app.jar
```

После запуска доступны:

- **Веб-интерфейс:** http://localhost:8081/applications
- **Swagger UI:** http://localhost:8081/swagger-ui/index.html

База данных H2 встроена в приложение. Таблицы и тестовые данные создаются автоматически при запуске.

## REST API

| Метод | Endpoint | Описание |
|---|---|---|
| GET | `/api/applications` | Получение списка заявок |
| POST | `/api/applications` | Создание заявки |
| POST | `/api/applications/{id}/calculate` | Расчёт рейтинга |

## Сборка и запуск из исходного кода

```bash
  ./gradlew clean build bootRun
```
