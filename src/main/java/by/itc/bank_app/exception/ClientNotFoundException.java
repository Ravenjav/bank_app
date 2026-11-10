package by.itc.bank_app.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Клиент с ID " + id + " не найден");
    }
}
