package server.transport;

import server.exception.TransportLayerException;

import java.util.function.Supplier;

public interface ClientHandler {

    void sendData(Supplier<byte[]> dataSupplier) throws TransportLayerException;

    void receiveData(Supplier<byte[]> dataSupplier);
}
