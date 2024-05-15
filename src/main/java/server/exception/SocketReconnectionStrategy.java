package server.exception;

public class SocketReconnectionStrategy implements ReconnectionStrategy {
    private static final int MAX_ATTEMPTS = 3;
    private static final int RECONNECT_INTERVAL = 1000;

    @Override
    public boolean attemptReconnect() throws InterruptedException {
        return false;
    }
}
