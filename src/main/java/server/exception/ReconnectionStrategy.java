package server.exception;

@FunctionalInterface
public interface ReconnectionStrategy {
    boolean attemptReconnect() throws InterruptedException;
}
