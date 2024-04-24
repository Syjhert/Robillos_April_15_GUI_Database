package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Objects;

public class EditMessageController {
    private static Message prevMessage = null;
    public static void initPrevMessage(Message message){
        prevMessage = message;
    }
    @FXML
    private TextArea taEditMessage;

    public void initialize(){
        taEditMessage.setText(prevMessage.getContent());
    }
    private void goBackToMessages(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("messages.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            HelloApplication.mainStage.setScene(scene);
            HelloApplication.mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onCloseEditMessageClick(){
        goBackToMessages();
    }
    @FXML
    private void onDoneEditMessageClick(){
        String newContent = taEditMessage.getText();
        SQLOperations.updateMessageContent(prevMessage, newContent);
        goBackToMessages();
    }
}