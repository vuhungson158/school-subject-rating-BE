package kiis.edu.rating.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String entityName, Long id) {
        super(String.format("No %s with id : %d", entityName, id));
    }
}
