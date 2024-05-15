package server.broadcast;

import commons.datatypes.Message;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import commons.data.DataProcessor;
import server.exception.TransportLayerException;
import server.transport.ClientHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastManagerImpl implements BroadcastManager {

    private static final Logger LOG = LoggerFactory.getLogger(BroadcastManagerImpl.class);

    private final Map<String, ClientHandler> clientsMap = new ConcurrentHashMap<>();

    private final DataProcessor dataProcessor;

    @Inject
    public BroadcastManagerImpl(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    @Override
    public void broadcast(Message message) {
        clientsMap.forEach((clientId, clientHandler) -> {
            try {
                LOG.info("Serializing the message and sending.");
                clientHandler.sendData(() -> dataProcessor.serialize(message));
            } catch (TransportLayerException e) {
                LOG.error("Exception sending data to {}", clientId, e);
                removeClient(clientId);
            }
        });
    }

    @Override
    public void removeClient(String clientId) {
        clientsMap.remove(clientId);
    }

    @Override
    public void addClient(String clientId, ClientHandler clientHandler) {
        clientsMap.put(clientId, clientHandler);
    }
}
