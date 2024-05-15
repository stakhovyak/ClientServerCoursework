package client;

import client.config.ClientModule;
import client.view.ChatView;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class AppStarter {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ClientModule());
        ChatView chatView = injector.getInstance(ChatView.class);
        chatView.setVisible(true);
    }
}
