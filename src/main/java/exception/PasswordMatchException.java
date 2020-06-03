package exception;

public class PasswordMatchException extends Exception {
    public PasswordMatchException(String errorMessage) {
        super(errorMessage);
    }
}
