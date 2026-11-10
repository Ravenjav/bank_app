package by.itc.bank_app.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(Long id) {
        super("Заявка с ID " + id + " не найдена");
    }
}
