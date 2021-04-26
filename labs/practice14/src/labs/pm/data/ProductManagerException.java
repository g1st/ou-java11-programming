package labs.pm.data;
import java.nio.file.Path;

public class ProductManagerException extends Exception {
    public ProductManagerException() {
        super();
    }

    public ProductManagerException(String message) {
        super(message);
    }

    public ProductManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
