package com.theredspy15.checksumchecker;

/*MIT License

Copyright (c) [2018] [TheRedSpy15]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private final  Label instructionLbl = new Label("Enter the checksum to compare against");
    private final Font fontSize = new Font(20);
    private Button selectFile = new Button("Select AND check file");
    private VBox layout = new VBox(10);
    private TextField checksumField = new TextField();
    private FileChooser fileChooser = new FileChooser();
    private Hyperlink gitHubLink = new Hyperlink("Github page");
    private MenuButton algorithmSelector = new MenuButton();
    private File file;

    private String algorithm = "MD5";

    public static void main(String args[]){

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        gitHubLink.setOnAction(e -> getHostServices().showDocument("https://github.com/TheRedSpy15/Checkersum-Checker"));
        gitHubLink.setStyle("-fx-text-fill : GOLD");

        instructionLbl.setFont(fontSize);
        instructionLbl.setStyle("-fx-text-fill : WHITE");

        final MenuItem md5 = new MenuItem("MD5");
        final MenuItem sha1 = new MenuItem("SHA-1");
        final MenuItem sha224 = new MenuItem("SHA-224");
        final MenuItem sha256 = new MenuItem("SHA-256");
        final MenuItem sha384 = new MenuItem("SHA-384");
        final MenuItem sha512 = new MenuItem("SHA-512");

        md5.setOnAction(e -> {
            algorithm = "MD5";
            algorithmSelector.setText("MD5");
        });
        sha1.setOnAction(e -> {
            algorithm = "SHA-1";
            algorithmSelector.setText("SHA-1");
        });
        sha224.setOnAction(e -> {
            algorithm = "SHA-224";
            algorithmSelector.setText("SHA-224");
        });
        sha256.setOnAction(e -> {
            algorithm = "SHA-256";
            algorithmSelector.setText("SHA-256");
        });
        sha384.setOnAction(e -> {
            algorithm = "SHA-384";
            algorithmSelector.setText("SHA-384");
        });
        sha512.setOnAction(e -> {
            algorithm = "SHA-512";
            algorithmSelector.setText("SHA-512");
        });

        algorithmSelector.getItems().addAll(md5, sha1, sha224, sha256, sha384, sha512);
        algorithmSelector.setText("MD5");

        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
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

        primaryStage.setTitle("Checksum Checker 4.0 - By TheRedSpy15");

        layout.getChildren().addAll(instructionLbl, checksumField, algorithmSelector, selectFile, gitHubLink);
        layout.setPadding(new Insets(20,20,20,20));
        layout.setStyle("-fx-background-color : #003153");

        Scene checksumScene = new Scene(layout, 600,200);

        primaryStage.setScene(checksumScene);

        primaryStage.show();
    }

    private void checkFile(File file) {

        final String hashType = algorithm;

        final String filepath = file.getAbsolutePath();
        StringBuilder sum = new StringBuilder();
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(hashType);
            FileInputStream fileInputStream = new FileInputStream(filepath);
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

        final boolean result = checksumField.getText().contentEquals(sum);

        outputResult(result, sum.toString());
    }

    private void outputResult(boolean result, String sum){

        Stage notificationStage = new Stage();

        Label sumLbl = new Label("File checksum: " + sum);
        Label equalLbl = new Label("Equal: " + result);
        Label fileLbl = new Label("File: " + file);

        sumLbl.setFont(fontSize);
        equalLbl.setFont(fontSize);
        fileLbl.setFont(fontSize);

        if (result) equalLbl.setStyle("-fx-text-fill : GREEN");
        else equalLbl.setStyle("-fx-text-fill : RED");

        sumLbl.setStyle("-fx-text-fill : WHITE");
        fileLbl.setStyle("-fx-text-fill : WHITE");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20,20,20,20));
        vBox.getChildren().addAll(fileLbl, sumLbl, equalLbl);

        vBox.setStyle("-fx-background-color : #003153");

        Scene scene = new Scene(vBox);
        notificationStage.setScene(scene);

        notificationStage.showAndWait();
    }
}
