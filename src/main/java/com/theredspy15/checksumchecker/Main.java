package com.theredspy15.checksumchecker;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main extends Application {

    // NEXT VERSION

    // Added javadoc
    // Added comments
    // Added code quality badges on readme

    private final Font font = new Font("IMPACT",20);
    private Label instructionLbl = new Label("Enter the checksum to compare against");
    private JFXButton selectFile = new JFXButton("Select AND check file");
    private VBox layout = new VBox(10);
    private JFXTextField checksumField = new JFXTextField();
    private FileChooser fileChooser = new FileChooser();
    private Hyperlink gitHubLink = new Hyperlink("Github page");
    private JFXComboBox<String> algorithmSelector = new JFXComboBox<>();
    private File file;
    private boolean equalSums = false;

    public static void main(String[] args){

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        gitHubLink.setOnAction(e -> getHostServices().showDocument("https://github.com/TheRedSpy15/Checkersum-Checker"));
        gitHubLink.setStyle("-fx-text-fill : GOLD");
        gitHubLink.setFont(font);

        instructionLbl.setFont(font);
        instructionLbl.setStyle("-fx-text-fill : WHITE");

        checksumField.setFont(font);
        checksumField.setStyle("-fx-text-fill : WHITE");

        selectFile.setFont(font);
        selectFile.setStyle("-jfx-button-type: RAISED; -fx-background-color: GRAY; -fx-text-fill: WHITE;");

        algorithmSelector.setStyle("-fx-background-color: GRAY; -fx-text-fill: WHITE;");
        algorithmSelector.getItems().addAll("MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512");
        algorithmSelector.setPromptText("Select Hash");

        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files (.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files (.png, .jpg, .gif, .bmp)", "*.png", "*.jpg", "*.gif", "bmp"),
                new FileChooser.ExtensionFilter("Audio Files (.wav, .mp3, .aac, .m4a)", "*.wav", "*.mp3", "*.aac", "*.m4a"),
                new FileChooser.ExtensionFilter("Executables (.exe)", "*.exe"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        selectFile.setOnAction(
                e -> {
                    file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        checkFile(file);
                    }
                });

        checksumField.setOnAction(
                e -> {
                    file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        checkFile(file);
                    }
                });

        layout.getChildren().addAll(instructionLbl, checksumField, algorithmSelector, selectFile, gitHubLink);
        layout.setPadding(new Insets(20,20,20,20));
        layout.setStyle("-fx-background-color : #36454f");

        JFXDecorator decorator = new JFXDecorator(primaryStage, layout);
        decorator.setTitle("Checksum Checker - 5.0");

        Scene checksumScene = new Scene(decorator, 600,275);

        primaryStage.setScene(checksumScene);

        primaryStage.show();
    }

    private void checkFile(File file) {

        final String filePath = file.getAbsolutePath();
        StringBuilder sum = new StringBuilder();
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithmSelector.getValue());
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] dataBytes = new byte[1024];
            int nread;

            while((nread = fileInputStream.read(dataBytes)) != -1)
                messageDigest.update(dataBytes, 0, nread);

            byte[] mdbytes = messageDigest.digest();

            for (byte mdbyte : mdbytes) sum.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
        }
        catch(NoSuchAlgorithmException | IOException e)
        {
            e.printStackTrace();
        }

        equalSums = checksumField.getText().contentEquals(sum);

        outputResult(sum.toString());
    }

    private void outputResult(String sum){

        Stage notificationStage = new Stage();

        Label sumLbl = new Label("File checksum: " + sum);
        Label equalLbl = new Label("Equal: " + equalSums);
        Label fileLbl = new Label("File: " + file);

        sumLbl.setFont(font);
        equalLbl.setFont(font);
        fileLbl.setFont(font);

        if (equalSums) equalLbl.setStyle("-fx-text-fill : GREEN");
        else equalLbl.setStyle("-fx-text-fill : RED");

        sumLbl.setStyle("-fx-text-fill : WHITE");
        fileLbl.setStyle("-fx-text-fill : WHITE");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20,20,20,20));
        vBox.getChildren().addAll(fileLbl, sumLbl, equalLbl);

        vBox.setStyle("-fx-background-color : #36454f");

        JFXDecorator decorator = new JFXDecorator(notificationStage, vBox);

        Scene scene = new Scene(decorator);
        notificationStage.setScene(scene);

        notificationStage.showAndWait();
    }
}
