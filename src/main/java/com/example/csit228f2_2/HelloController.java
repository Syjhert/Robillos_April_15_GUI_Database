package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.robot.Robot;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HelloController {
    public ToggleButton tbNight;
    @FXML
    private Label welcomeText;

    @FXML
    private void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private Label lblWelcomeText;
    @FXML
    private Button btnUpdateUsername;
    @FXML
    private Button getBtnUpdatePassword;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField txtUsernameUpdate;
    @FXML
    private TextField txtPasswordUpdate;
    @FXML
    private TextField txtPasswordUpdateRetype;

    public void initialize(){
        lblWelcomeText.setText("Welcome " + SQLOperations.getUsername(HelloApplication.userID) + "!");
        if(HelloApplication.isLatestNewEntry){
            showInformation("New Entry Saved", "This account is new and is now saved!");
        }
    }
    @FXML
    private void onUpdateUsernameClick(){
        SQLOperations.updateUsername(HelloApplication.userID, txtUsernameUpdate.getText());
        txtUsernameUpdate.setText("");
        lblWelcomeText.setText("Welcome " + SQLOperations.getUsername(HelloApplication.userID) + "!");
        showInformation("Data updated successfully", "Your username has been updated!");
    }

    @FXML
    private void onUpdatePasswordClick(){
        String password = txtPasswordUpdate.getText();
        String passwordRetype = txtPasswordUpdateRetype.getText();

        if(!password.equals(passwordRetype)){
            showInformation("Password not match", "Your password and retyped password does not match");
            return;
        }

        if(password.equals("")){
            showInformation("Empty new password", "Please enter new password before updating");
            return;
        }

        SQLOperations.updatePassword(HelloApplication.userID, password);
        txtPasswordUpdate.setText("");
        txtPasswordUpdateRetype.setText("");
        showInformation("Data updated successfully", "Your password has been updated!");
    }

    @FXML
    private void onDeleteClick(){
        if(showConfirm("Data Deletion", "Are you sure you want to delete your data?")){
            SQLOperations.deleteData(HelloApplication.userID);
            showInformation("Data deleted successfully", "Your data has been deleted!");
            returnToMainScene();
        }
    }

    @FXML
    private void onLogoutClick(){
        returnToMainScene();
    }


    public static boolean showConfirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(message);

        String[] options = new String[]{"NO", "YES"};

        List<ButtonType> buttons = new ArrayList<>();
        for (String option : options) {
            buttons.add(new ButtonType(option));
        }

        alert.getButtonTypes().setAll(buttons);

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get().getText().equals("NO")) {
            return false;
        }
        return true;
    }

    public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Notification");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void returnToMainScene(){
        HelloApplication.mainStage.setScene(HelloApplication.mainScene);
        HelloApplication.mainStage.show();
    }
}