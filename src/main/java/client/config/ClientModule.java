package client.config;

import client.presenter.ClientPresenter;
import client.view.ChatView;
import client.view.ChatWindow;
import com.google.inject.AbstractModule;
import commons.data.DataProcessor;
import commons.data.SimpleDataProcessor;

public class ClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataProcessor.class).to(SimpleDataProcessor.class);
        bind(ChatView.class).to(ChatWindow.class);
        bind(ClientPresenter.class);
    }
}
