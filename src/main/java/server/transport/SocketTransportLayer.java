package server.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class SocketTransportLayer implements TransportLayer {

    private static final Logger LOG = LoggerFactory.getLogger(SocketTransportLayer.class);

    // TODO: make configurable
    public static final int PORT = 1111;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void start(BiConsumer<byte[], ClientHandler> consumer) {
        try (var serverSocket = new ServerSocket(PORT)) {
            while (!serverSocket.isClosed()) {
                LOG.info("Listening to incoming connections.");
                var socket = serverSocket.accept();
                LOG.info("New connection was accepted");
                var clientHandler = new SocketClientHandler(socket, consumer);
                executorService.submit(clientHandler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
