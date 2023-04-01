package RedMine;

public class ValidationFailedException extends RuntimeException{
    public ValidationFailedException(String message) {
        super(message);
    }

}
