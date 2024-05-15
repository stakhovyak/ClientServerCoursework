package server;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.broadcast.BroadcastManager;
import commons.data.DataProcessor;
import server.transport.TransportLayer;

public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private final BroadcastManager broadcastManager;
    private final TransportLayer transportLayer;
    private final DataProcessor dataProcessor;

    @Inject
    public Server(BroadcastManager broadcastManager, TransportLayer transportLayer, DataProcessor dataProcessor) {
        this.broadcastManager = broadcastManager;
        this.transportLayer = transportLayer;
        this.dataProcessor = dataProcessor;
    }

    public void start() {
        transportLayer.start((data, clientHandler) -> {
            LOG.info("Deserializing message.");
            var message = dataProcessor.deserialize(data);
            LOG.info("Got message {}, adding new client to broadcastManager.", message);
            broadcastManager.addClient(message.sender(), clientHandler);
            LOG.info(broadcastManager.toString());
            broadcastManager.broadcast(message);
            LOG.info("Message broadcasted.");
        });
    }
}
