package by.itc.bank_app.exception;

public class ApplicationAlreadyCalculatedException extends RuntimeException {
    public ApplicationAlreadyCalculatedException(Long id) {
        super("Рейтинг заявки с ID " + id + " уже рассчитан");
    }
}
