package client.view;

import client.presenter.ChatPresenter;
import commons.datatypes.Message;

public interface ChatView {
    void displayMessage(Message message);

    void displayError(String errorMessage);

    void setChatPresenter(ChatPresenter presenter);

    void setVisible(Boolean visible);
}
