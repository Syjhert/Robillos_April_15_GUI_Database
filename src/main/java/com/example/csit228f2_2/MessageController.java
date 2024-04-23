package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
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

    private void onEditMessageClick(Message message){
        EditMessageController.initPrevMessage(message);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_message.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            HelloApplication.mainStage.setScene(scene);
            HelloApplication.mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void onDeleteMessageClick(int messageID){
        if(Alerts.showConfirm("Message Deletion", "Are you sure you want to delete this message?")){
            SQLOperations.deleteMessage(messageID);
            refreshMessagesWrapper();
        }
    }

    public void initialize(){
        taMessage.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 200 ? change : null));
        refreshMessagesWrapper();
    }

    private void printMessages() {
        vbMessagesWrapper.getChildren().clear();
        for(Message message : messages){
            HBox messageContainer = new HBox();
            messageContainer.getStyleClass().add("message-container");
            messageContainer.setUserData(message.getMessageID());

            VBox contentContainer = new VBox();
            Text content = new Text(message.getContent());
            content.setFont(Font.font("Arial", 20));
            content.setWrappingWidth(480);
            contentContainer.getChildren().add(content);

            VBox infoContainer = new VBox();
            infoContainer.setPrefWidth(90);
            String[] dateTime = message.getDateTimeSent().split(" ");
            Text date = new Text(dateTime[0]);
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourTime = null;
            try {
                _24HourTime = _24HourSDF.parse(dateTime[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Text time = new Text(_12HourSDF.format(_24HourTime));
            Text isEdited = new Text();
            if(message.isEdited()) isEdited.setText("Edited");
            isEdited.setFont(Font.font("Arial", FontPosture.ITALIC, 10));
            infoContainer.getChildren().add(date);
            infoContainer.getChildren().add(time);
            infoContainer.getChildren().add(isEdited);
            infoContainer.setAlignment(Pos.CENTER);

            VBox buttonContainer = new VBox();
            Button btnEdit = new Button("Edit");
            btnEdit.setPrefWidth(100);
            btnEdit.setOnAction(actionEvent -> onEditMessageClick(message));

            Button btnDelete = new Button("Delete");
            btnDelete.setPrefWidth(100);
            btnDelete.setOnAction(actionEvent -> onDeleteMessageClick(message.getMessageID()));

            buttonContainer.getChildren().add(btnEdit);
            buttonContainer.getChildren().add(btnDelete);

            messageContainer.getChildren().add(contentContainer);
            messageContainer.getChildren().add(infoContainer);
            messageContainer.getChildren().add(buttonContainer);

            vbMessagesWrapper.getChildren().add(messageContainer);
        }
    }

    private void refreshMessagesWrapper(){
        messages = SQLOperations.getMessages(CurrentUser.getUserID());
        printMessages();
    }
}