package client.view;

import client.presenter.ChatPresenter;
import client.presenter.ClientPresenter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import commons.datatypes.Message;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Singleton
public class ChatWindow extends JFrame implements ChatView {
    private final JTextField messageTextField = new JTextField();
    private final JTextArea chatTextArea = new JTextArea();
    private final JButton sendButton = new JButton("Send");
    private final JMenuItem connectMenuItem = new JMenuItem("Connect to Server");
    private final JMenuItem leaveServerItem = new JMenuItem("Leave the Server");

    private final Provider<ClientPresenter> clientPresenterProvider;

    private ChatPresenter presenter;

    private String username;

    @Inject
    public ChatWindow(Provider<ClientPresenter> clientPresenterProvider) {
        this.clientPresenterProvider = clientPresenterProvider;
        initialize();
    }

    private void initialize() {
        username = JOptionPane.showInputDialog("Enter your username.");

        setTitle("Chat Application");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout());

        panel.add(messageTextField, BorderLayout.CENTER);

        sendButton.addActionListener(e -> {
            var message = new Message(username, Objects.requireNonNullElse(messageTextField.getText(), ""));
            if (!message.contents().isEmpty()) {
                presenter.sendMessage(message);
                messageTextField.setText("");
            }
        });

        panel.add(sendButton, BorderLayout.EAST);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu serverMenu = new JMenu("Server");
        menuBar.add(serverMenu);

        serverMenu.add(connectMenuItem);
        serverMenu.add(leaveServerItem);

        connectMenuItem.addActionListener(e -> {
            String serverAddress = JOptionPane.showInputDialog("Enter server address:");
            if (serverAddress != null) {
                String portStr = JOptionPane.showInputDialog("Enter port:");
                if (portStr != null) {
                    int port = Integer.parseInt(portStr);
                    presenter.connect(serverAddress, port);
                }
            }
        });

        leaveServerItem.addActionListener(e -> {
            presenter.leaveServer();
        });
    }

    @Override
    public void displayMessage(Message message) {
        SwingUtilities.invokeLater(() -> chatTextArea.append(message.sender() + ": " + message.contents() + "\n"));
    }

    @Override
    public void displayError(String errorMessage) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE));
    }

    @Override
    public void setChatPresenter(ChatPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void setVisible(Boolean visible) {
        super.setVisible(visible);
        if (presenter == null) {
            presenter = clientPresenterProvider.get();
            presenter.setChatView(this);
        }
    }
}