package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;


public class AccountSettingsController {

    @FXML
    private Label lblWelcomeText;
    @FXML
    private TextField txtUsernameUpdate;
    @FXML
    private TextField txtPasswordUpdate;
    @FXML
    private TextField txtPasswordUpdateRetype;

    public void initialize(){
        lblWelcomeText.setText("Welcome " + SQLOperations.getUsername(CurrentUser.getUserID()) + "!");
    }
    @FXML
    private void onUpdateUsernameClick(){
        SQLOperations.updateUsername(CurrentUser.getUserID(), txtUsernameUpdate.getText());
        txtUsernameUpdate.setText("");
        lblWelcomeText.setText("Welcome " + SQLOperations.getUsername(CurrentUser.getUserID()) + "!");
        Alerts.showInformation("Data updated successfully", "Your username has been updated!");
    }

    @FXML
    private void onUpdatePasswordClick(){
        String password = txtPasswordUpdate.getText();
        String passwordRetype = txtPasswordUpdateRetype.getText();

        if(!password.equals(passwordRetype)){
            Alerts.showInformation("Password not match", "Your password and retyped password does not match");
            return;
        }

        if(password.equals("")){
            Alerts.showInformation("Empty new password", "Please enter new password before updating");
            return;
        }

        SQLOperations.updatePassword(CurrentUser.getUserID(), password);
        txtPasswordUpdate.setText("");
        txtPasswordUpdateRetype.setText("");
        Alerts.showInformation("Data updated successfully", "Your password has been updated!");
    }

    @FXML
    private void onDeleteClick(){
        if(Alerts.showConfirm("Account Deletion", "Are you sure you want to delete your account?")){
            SQLOperations.deleteUser(CurrentUser.getUserID());
            Alerts.showInformation("Account deleted successfully", "Your data has been deleted!");
            HelloApplication.returnToMainScene();
        }
    }

    @FXML
    private void onLogoutClick(){
        HelloApplication.returnToMainScene();
    }

    @FXML
    private void onCloseAccountSettingsClick(){
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
}