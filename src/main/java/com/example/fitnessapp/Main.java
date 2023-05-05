package com.example.fitnessapp;

import javafx.application.Application;
import javafx.css.Style;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {
    static ArrayList<Zutat> gespeicherteZutaten = new ArrayList<>();

    static Benutzer benutzer = new Benutzer();

    static Stage stage;
    @Override
    public void start(Stage stage1) {
        stage = stage1;
        Pane pane = new Pane();
        pane.setPrefSize(500, 500);
        stage.setTitle("FitnessApp");
        Scene scene = new Scene(pane);
        String css = Objects.requireNonNull(this.getClass().getResource("styles.css")).toExternalForm();
        scene.getStylesheets().add(css);
        System.out.println(scene.getStylesheets());
        stage.setScene(scene);
        stage.show();
        Controller c = new Controller(benutzer);
        c.start();
    }

    public static void main(String[] args) {
        launch();
    }
}