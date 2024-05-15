package client.transport;

import client.presenter.ChatPresenter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import commons.datatypes.Message;
import commons.data.DataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

@Singleton
public class ClientConnectionHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ClientConnectionHandler.class);

    private final Socket socket;
    private final DataProcessor dataProcessor;
    private ChatPresenter chatPresenter;

    @Inject
    public ClientConnectionHandler(Socket socket, DataProcessor dataProcessor) {
        this.socket = socket;
        this.dataProcessor = dataProcessor;
    }

    public void setChatPresenter(ChatPresenter chatPresenter) {
        this.chatPresenter = chatPresenter;
    }

    public void sendMessage(Message message) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(dataProcessor.serialize(message));
        } catch (IOException e) {
            chatPresenter.onError("Error sending message: " + e.getMessage());
        }
    }

    public void connect(String serverAddress, int port) {
        try {
            socket.connect(new java.net.InetSocketAddress(serverAddress, port));
            new Thread(this).start();
            chatPresenter.onMessageReceived(new Message("CLIENT", "Connected to the server."));
        } catch (IOException e) {
            chatPresenter.onError("Error connecting to server: " + e.getMessage());
        }
    }

    public void leaveServer() {
        if (socket.isConnected()) {
            try {
                socket.close();
                chatPresenter.onMessageReceived(new Message("CLIENT","You left the chat."));
            } catch (IOException e) {
                LOG.error("Exception closing socket: ", e);
            }
        } else {
            chatPresenter.onError("The socket is not connected!");
        }
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) != -1) {
                Message message = dataProcessor.deserialize(buffer);
                chatPresenter.onMessageReceived(message);
            }
        } catch (IOException e) {
            chatPresenter.onError("Connection closed: " + e.getMessage());
        }
    }
}