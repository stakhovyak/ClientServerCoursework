package server.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.exception.TransportLayerException;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SocketClientHandler implements ClientHandler, Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SocketClientHandler.class);

    private final Socket socket;

    private final BiConsumer<byte[], ClientHandler> consumer;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public SocketClientHandler(Socket socket, BiConsumer<byte[], ClientHandler> consumer) {
        this.socket = socket;
        this.consumer = consumer;
    }

    // TODO: Handle the TransportLayerException
    // TODO: Put error handling in separate class!
    @Override
    public void sendData(Supplier<byte[]> dataSupplier) throws TransportLayerException {
        try {
            var data = Objects.requireNonNull(dataSupplier.get());
            socket.getOutputStream().write(data);
            LOG.info("The byte array sent.");
        } catch (IOException e) {
            LOG.error("Exception sending data.");
            throw new TransportLayerException("Exception: ", e);
        }
    }

    public void run() {
        receiveData(() -> {
            try {
                var objectInputStream = new ObjectInputStream(socket.getInputStream());
                LOG.info("ObjectInputStream was created.");
                return (byte[]) Objects.requireNonNullElse(objectInputStream.readObject(), " ".getBytes());
            } catch (IOException | ClassNotFoundException e) {
                LOG.info("Client's Socket closed unexpectedly.");
//                try {
//                    if (!reconnect()) {
//                        closed.set(true);
//                        LOG.info("Client thread is closed.");
//                    }
//                } catch (InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
            }
            return null;
        });

        try {
            socket.close();
            closed.set(true);
        } catch (IOException e) {
            LOG.error("Exception closing socket: ", e);
        }
    }

    @Override
    public void receiveData(Supplier<byte[]> dataSupplier) {
        while (!closed.get()) {
            var received = Objects.requireNonNull(dataSupplier.get());
            LOG.info("The sequence of bytes was read.");
            consumer.accept(received, this);
        }
    }
}

