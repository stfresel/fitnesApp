package com.example.fitnessapp;

import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Im Grunde is gleiche wia Benutzer 1, lei das i am Design manondobasteln konn, ohne wos hin zi mochen
 */
public class BenutzerV2 implements Serializable{
    public Pane pane = new Pane();
    private TextField txt = new TextField();
    private PasswordField[] passwordField = new PasswordField[2];
    private Text[] text = new Text[3];
    private Text textfehler=new Text();
    private Text textregi=new Text();
    private TextField textfieldbenutzer=new TextField();
    private PasswordField passwortfieldbenutzer=new PasswordField();
    private Button login=new Button();

    //--------------------------------------------
    //Atribute für Benutzer und Passwort
    private String benutzername;
    private String passwort;
    private Tagebuch meinTagebuch;
    private Path path = null;
    //------------------------------

    Rectangle loginrec;

    public BenutzerV2() {
    }
    public void initialize(){
        pane.setPrefHeight(Main.pane.getPrefHeight());
        pane.setPrefWidth(Main.pane.getPrefWidth());
        Main.stage.setScene(new Scene(pane));

        //Textfeld
        textfieldbenutzer.setLayoutY(109);
        textfieldbenutzer.setLayoutX(159);
        textfieldbenutzer.setPromptText("Benutzer");
        //Passwortfeld
        passwortfieldbenutzer.setLayoutY(169);
        passwortfieldbenutzer.setLayoutX(159);
        passwortfieldbenutzer.setPromptText("Passwort");

        //Button
        login.setText("Login");
        login.setLayoutY(237);
        login.setLayoutX(159);
        login.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    einloggen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //button Layout
        loginrec = getrec(0, 0, 200, 200, "#00ffb1");


        //Text log in
        textfehler.setLayoutX(159);
        textfehler.setLayoutY(225);
        //Text regristieren
        textregi.setLayoutY(210);
        textregi.setLayoutX(250);
        textregi.setText("Registrieren");
        textregi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                neuesKonto();
            }
        });
        //Hinzufügen
        pane.getChildren().add(textfieldbenutzer);
        pane.getChildren().add(passwortfieldbenutzer);
        pane.getChildren().add(login);
        pane.getChildren().add(textfehler);
        pane.getChildren().add(textregi);
        pane.getChildren().add(loginrec);

    }

    public void neuesKonto() {
        meinTagebuch = new Tagebuch();
        //meinTagebuch.start();
        textfehler.setVisible(false);
        int counter = 400;
        pane.requestFocus();
        textfieldbenutzer.setVisible(false);
        passwortfieldbenutzer.setVisible(false);
        login.setVisible(false);
        textregi.setVisible(false);
        Button buttonReg = new Button();
        buttonReg.setText("Regristrieren");
        System.out.println();
        buttonReg.setLayoutX(pane.getWidth() / 2 - 149.0/ 2);
        buttonReg.setLayoutY(400);
        pane.getChildren().add(buttonReg);

        txt = new TextField();
        txt.setMinHeight(25);
        txt.setMinWidth(149);
        double x = pane.getWidth() / 2 - txt.getMinWidth() / 2;
        txt.setLayoutX(x);
        double y = pane.getHeight() - counter;
        txt.setLayoutY(y);
        txt.setPromptText("Neuen Benutzer");
        counter -= 100;
        pane.getChildren().add(txt);
        for (int i = 0; i < 2; i++) {
            y = pane.getHeight() - counter;
            passwordField[i] = new PasswordField();
            passwordField[i].setLayoutX(x);
            passwordField[i].setLayoutY(y);
            counter -= 100;
            passwordField[i].setPromptText("Neues Passwort");

            pane.getChildren().add(passwordField[i]);

        }
        counter = 400;
        for (int i = 0; i < 3; i++) {
            y = pane.getHeight() - counter;
            text[i] = new Text();
            text[i].setLayoutX(x);
            text[i].setLayoutY(y - 1);
            counter -= 100;
        }


        for (int i = 0; i < 3; i++) {
            text[0].setText("Benutzer");
            text[1].setText("Neues Passwort");
            text[2].setText("Wiederholen des Passwortes");
            pane.getChildren().add(text[i]);
        }
        buttonReg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    speichern();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    // registration speichern
    private void speichern() throws IOException {
        boolean loggedIn = false;
        // Speichern im File, wo alle Benutzernamen und Passwörter stehen
        FileWriter fr = new FileWriter("Benutzer.txt");
        BufferedWriter br = new BufferedWriter(fr);
        textfehler.setFill(Color.RED);
        textfehler.setLayoutY(350);
        textfehler.setLayoutX(pane.getWidth() / 2 - 149.0/ 2);
        if (passwordField[0].getText().equals(passwordField[1].getText()) && txt.getText().length() != 0) {
            loggedIn = true;
            System.out.println("Passwort richtig");
            System.out.println(txt.getText());
            System.out.println(passwordField[1].getText());
            benutzername = txt.getText();
            passwort = passwordField[1].getText();
            br.write(benutzername);
            br.write(" ");
            br.write(passwort);
            br.newLine();

        } else {
            textfehler.setVisible(true);
            if (txt.getText().length() == 0 && !Objects.equals(passwordField[0].getText(), passwordField[1].getText())) {
                textfehler.setText("Kein Benutzername und Passwort falsch");
            } else {
                textfehler.setText("Passwörter stimmen nicht überein");
            }
        }
        br.close();
        if (loggedIn){
            tagebuchSpeichern();
            tagebuchStarten();
        }
    }

    public void tagebuchSpeichern(){
        // Erstellen des Paths zum .ser File
        path = Paths.get(benutzername + passwort + ".ser");
        // Erstellen eines .ser Files wo das Tagebuch gespeichert wird
        try (ObjectOutputStream whereToWrite = new ObjectOutputStream(Files.newOutputStream(path , StandardOpenOption.CREATE))) {
            whereToWrite.writeObject(meinTagebuch);
            System.out.println("Saved Tagebuch");
        } catch (IOException e) {
            System.out.println("Can't serialize " + path.getFileName() + ": " + e.getMessage());
        }
    }

    public void einloggen() throws IOException {
        pane.requestFocus();
        FileReader fr = new FileReader("Benutzer.txt");
        BufferedReader br = new BufferedReader(fr);


        benutzername = textfieldbenutzer.getText();
        passwort = passwortfieldbenutzer.getText();
        boolean loggedIn = false;
        String zeile = br.readLine();
        do {

            String[] benutzer = zeile.split(" ");
            if (Objects.equals(textfieldbenutzer.getText(), benutzer[0]) && Objects.equals(passwortfieldbenutzer.getText(), benutzer[1])) {
                textfehler.setText("");
                System.out.println("Passwort und Benutzer stimmern überein");

                benutzername = textfieldbenutzer.getText();
                passwort = passwortfieldbenutzer.getText();
                loggedIn = true;

            } else {
                pane.requestFocus();

                textfieldbenutzer.setText("");
                passwortfieldbenutzer.setText("");
                textfehler.setFill(Color.RED);
                textfehler.setText("Passwort oder Benutzername falsch!");

            }
            zeile = br.readLine();

        } while (zeile != null);
        br.close();

        if (loggedIn){
            // path wieder erstellen evt funktion?
            path = Paths.get(benutzername + passwort + ".ser");
            try (ObjectInputStream whereToReadFrom = new ObjectInputStream(Files.newInputStream(path))) {
                meinTagebuch = (Tagebuch) whereToReadFrom.readObject();
                System.out.println("auslesen vom file");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Fehler beim Auslesen aus dem Tagebuch" + e.getMessage());
            }
            tagebuchStarten();
        }
    }

    private void tagebuchStarten() {
        if (meinTagebuch.getAnzahlTage() < 1){
            Tag t1 = new Tag(LocalDate.now());
            meinTagebuch.addTag(t1);
        } else if (!Objects.equals(meinTagebuch.getLastDay(), LocalDate.now())){
            Tag t1 = new Tag(LocalDate.now());
            meinTagebuch.addTag(t1);
        }
        meinTagebuch.loadTagebuchScene();
        EventHandler<KeyEvent> keyEventEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.S && !(keyEvent.getTarget() instanceof TextInputControl)) {
                    /*
                    Scene temp = Main.stage.getScene();
                    VBox vBox = temp.();
                    Scene x = new Scene(new Pane());
                    Main.stage.setScene(x);
                    tagebuchSpeichern();

                     */
                    System.out.println(" nicht gespeichert");
                }
            }
        };
        Main.stage.addEventFilter(KeyEvent.ANY, keyEventEventHandler);
    }



    public Rectangle getrec(double posx, double posy, double sizex, double sizey, String color){
        Rectangle rec = new Rectangle();

        rec.setX(posx);
        rec.setY(posy);
        rec.setWidth(sizex);
        rec.setHeight(sizey);
        rec.setFill(Paint.valueOf(color));
        return rec;
    }

    public Circle getcircle(double radius, double posx, double posy, String color){
        Circle circle = new Circle();

        circle.setCenterX(posx);
        circle.setCenterY(posy);
        circle.setRadius(radius);
        circle.setFill(Paint.valueOf(color));
        return circle;
    }
}