package server.broadcast;

import commons.datatypes.Message;
import server.transport.ClientHandler;

public interface BroadcastManager {

    void broadcast(Message message);

    void addClient(String sender, ClientHandler clientHandler);

    void removeClient(String clientId);
}
