package server.exception;

public class TransportLayerException extends Exception {

    public TransportLayerException(String message, Exception e) {
        super(message, e);
    }
}
