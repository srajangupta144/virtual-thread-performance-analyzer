package platformAndVirtualThreadAnalyzer.threadAnalyzer.exceptions;

public class BenchmarkException extends RuntimeException {

    public BenchmarkException(String message,
                              Throwable cause) {

        super(message, cause);
    }
}