package com.ivik.util;

import com.ivik.main.MandelbrotApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Sir Royal Air Benny on 9-3-2016.
 */
public class SaveWindow {

    static String filePath;

    public static void display() {
        final Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); // block interaction with other windows.
        window.setResizable(false);
        window.setTitle("Save Current Image");

        Label lbl = new Label("Filepath:");
        final TextField textInput = new TextField();

        // Cancel button
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        // Save Button
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filePath = textInput.getText();
                if(!filePath.equals("")) {
                    MandelbrotApp.saveImageToDisk(filePath);
                    window.close();
                } else {
                    // Call another window, prompting the user to specify a filepath.
                }
            }
        });

        HBox box1 = new HBox();
        box1.setAlignment(Pos.CENTER);
        box1.setSpacing(10);
        box1.getChildren().addAll(lbl, textInput);

        HBox box2 = new HBox();
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(10);
        box2.getChildren().addAll(cancelBtn, saveBtn);

        VBox root = new VBox();
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(box1, box2);

        Scene scene = new Scene(root, 300, 200);
        window.setScene(scene);
        window.show();
    }

}
