package server.transport;

import java.util.function.BiConsumer;

public interface TransportLayer {

    void start(BiConsumer<byte[], ClientHandler> handle);
}
