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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main extends Application {

    private Label instructionLbl = new Label("Enter the (what should be) checksum of the file your selected");
    private Label warningLbl = new Label("NOTE: only supports : MD5, SHA-1, and SHA-256");
    private Button selectFile = new Button("Select AND check file");
    private VBox layout = new VBox(10);
    private TextField checksumField = new TextField();
    private TextField typeField = new TextField();
    private FileChooser fileChooser = new FileChooser();
    private Hyperlink gitHubLink = new Hyperlink("Github page");

    public static void main(String args[]){

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        gitHubLink.setOnAction(e -> getHostServices().showDocument("https://github.com/TheRedSpy15/Checkersum-Checker"));

        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        selectFile.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            checkFile(file);
                        }
                    }
                });

        primaryStage.setTitle("Checksum Checker 1.0 - By TheRedSpy15");

        layout.getChildren().addAll(instructionLbl, checksumField, typeField, selectFile, warningLbl, gitHubLink);
        layout.setPadding(new Insets(20,20,20,20));

        Scene checksumScene = new Scene(layout);

        primaryStage.setScene(checksumScene);

        primaryStage.show();
    }

    private void checkFile(File file){

        boolean result;

        String type = typeField.getText();

        String filepath = file.getAbsolutePath();
        StringBuilder sum = new StringBuilder();
        try
        {
            MessageDigest md = MessageDigest.getInstance(type);
            FileInputStream fis = new FileInputStream(filepath);
            byte[] dataBytes = new byte[1024];
            int nread = 0;

            while((nread = fis.read(dataBytes)) != -1)
                md.update(dataBytes, 0, nread);

            byte[] mdbytes = md.digest();

            for (byte mdbyte : mdbytes) sum.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
        }
        catch(NoSuchAlgorithmException | IOException e)
        {
            e.printStackTrace();
        }

        if (checksumField.getText().contentEquals(sum)) result = true;
        else result = false;

        System.out.print("File checksum: " + sum);

        outputResult(result, sum.toString());
    }

    private void outputResult(boolean result, String sum){

        Stage notificationStage = new Stage();

        Label label = new Label("File checksum: " + sum + " | Equal: " + result);

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(20,20,20,20));
        stackPane.getChildren().add(label);

        Scene scene = new Scene(stackPane);
        notificationStage.setScene(scene);

        notificationStage.showAndWait();
    }
}
