package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

public class MessageController {
    @FXML
    private TextArea taMessage;
    @FXML
    private VBox vbMessagesWrapper;

    List<Message> messages;

    @FXML
    private void onSendMessageClick(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeNow = dtf.format(now);
        String content = taMessage.getText();
        Message newMessage = new Message(CurrentUser.getUserID(), content, dateTimeNow);
        SQLOperations.insertMessage(newMessage);

        taMessage.setText("");

        refreshMessagesWrapper();
    }
    @FXML
    private void onAccountSettingsClick() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account_settings.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            HelloApplication.mainStage.setScene(scene);
            HelloApplication.mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onEditMessageClick(int messageID){
        Button btnDoneEdit = new Button("Done");
        HBox messageContainer = null;
        for(Node node : vbMessagesWrapper.getChildren()){
            if(node.getUserData().equals(messageID)){
                messageContainer = (HBox) node;
                break;
            }
        }
        VBox contentContainer = (VBox) messageContainer.lookup("contentContainer");
        String content = "";
        for(Message message : messages){
            if(message.getMessageID() == messageID){
                content = message.getContent();
                break;
            }
        }
        TextArea textArea = new TextArea(content);
        contentContainer.getChildren().add(textArea);

        VBox buttonContainer = (VBox) messageContainer.lookup("buttonContainer");

        refreshMessagesWrapper();
    }
    private void onDeleteMessageClick(int messageID){
        if(Alerts.showConfirm("Message Deletion", "Are you sure you want to delete this message?")){
            SQLOperations.deleteMessage(messageID);
            Alerts.showInformation("Message deleted successfully", "Message has been deleted!");
            refreshMessagesWrapper();
        }
    }

    public void initialize(){
        taMessage.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 200 ? change : null));
        refreshMessagesWrapper();
    }

    private void printMessages(){
        vbMessagesWrapper.getChildren().clear();
        for(Message message : messages){
            HBox messageContainer = new HBox();
            VBox.setMargin(messageContainer, new Insets(0, 0, 0, 10));
            messageContainer.setUserData(message.getMessageID());

            VBox contentContainer = new VBox();
            contentContainer.setId("contentContainer");
            Text content = new Text(message.getContent());
            content.setFont(new Font("Arial", 20));
            content.setWrappingWidth(600);
            contentContainer.getChildren().add(content);

            VBox buttonContainer = new VBox();
            buttonContainer.setId("buttonContainer");
            Button btnEdit = new Button("Edit");
            btnEdit.setPrefWidth(100);
            btnEdit.setId("btnEdit");
            btnEdit.setOnAction(actionEvent -> onEditMessageClick(message.getMessageID()));

            Button btnDelete = new Button("Delete");
            btnDelete.setPrefWidth(100);
            btnDelete.setId("btnDelete");
            btnDelete.setOnAction(actionEvent -> onDeleteMessageClick(message.getMessageID()));

            buttonContainer.getChildren().add(btnEdit);
            buttonContainer.getChildren().add(btnDelete);

            messageContainer.getChildren().add(contentContainer);
            messageContainer.getChildren().add(buttonContainer);

            vbMessagesWrapper.getChildren().add(messageContainer);
        }
    }

    private void refreshMessagesWrapper(){
        messages = SQLOperations.getMessages(CurrentUser.getUserID());
        printMessages();
    }
}