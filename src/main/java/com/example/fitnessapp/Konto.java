package com.example.fitnessapp;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Konto implements Serializable {
    private Koerperdaten meineKoerperdaten;

    private transient GridPane gridPaneCalcPart;

    public HBox loadKonto() {
        HBox hBox = new HBox();
        hBox.setPrefSize(Main.stage.getScene().getWidth(), Main.stage.getScene().getHeight());
        gridPaneCalcPart = new GridPane();
        calcPart(gridPaneCalcPart);
        hBox.getChildren().add(0,datenAnsicht());
        hBox.getChildren().add(1, gridPaneCalcPart);
        return hBox;
    }

    public void calcPart(GridPane gridPane) {
        System.out.println("-------------------------------------------");
        meineKoerperdaten.tagesUmsatzBerechnen();
        gridPane.getChildren().clear();
        //gridPane.getChildren().removeAll();
        gridPane.addRow(0, new Label("BMI: " + meineKoerperdaten.getBMI()));
        gridPane.addRow(1,new Label("täglicher Bedarf"));
        gridPane.addRow(2,new Label("Kalorien       : " + meineKoerperdaten.getTagesUmsatz().getKcal()));
        gridPane.addRow(3,new Label("Kohlenhydrate  : " + meineKoerperdaten.getTagesUmsatz().getKohlenhydrate()));
        gridPane.addRow(4,new Label("Proteine       : " + meineKoerperdaten.getTagesUmsatz().getProtein()));
        gridPane.addRow(5,new Label("Fette          : " + meineKoerperdaten.getTagesUmsatz().getFett()));
    }

    public GridPane datenAnsicht() {
        GridPane gridPane = new GridPane();
        Button speichernBtn = new Button("speichern");
        NumericTextField alterTextField = new NumericTextField();
        NumericTextField groesseTextField = new NumericTextField();
        NumericTextField gewichtTextField = new NumericTextField();
        ComboBox<String> geschlechtCombobox = new ComboBox<>();
        Text textfehler = new Text();
        geschlechtCombobox.getItems().addAll("weiblich", "männlich");
        gridPane.setPrefSize(Main.stage.getScene().getWidth(), Main.stage.getScene().getHeight());

        // Wenn es direkt nach dem Registrieren geöffnet wird
        if (meineKoerperdaten == null){
            speichernBtn.setVisible(true);
            meineKoerperdaten = new Koerperdaten();

            // nur speichern
            speichernBtn.setOnMouseClicked(new EventHandler<>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println(gewichtTextField.getText().length());
                    if (geschlechtCombobox.getSelectionModel().getSelectedItem()==null ||
                            alterTextField.getText().length() < 1 || groesseTextField.getText().length() < 1 ||
                            gewichtTextField.getText().length() < 1){
                        setFehlermeldung(textfehler);

                    }else {
                        System.out.println("Combobox Geschlecht id: " + geschlechtCombobox.getValue());
                        meineKoerperdaten.setKoerperdaten(groesseTextField.getDouble(), gewichtTextField.getDouble(), alterTextField.getDouble(), geschlechtCombobox.getValue());
                        Main.benutzer.getHome().startHome();

                    }

                }
            });
        } else {
            groesseTextField.setText(String.valueOf(meineKoerperdaten.getGroesse()));
            gewichtTextField.setText(String.valueOf(meineKoerperdaten.getGewicht()));
            alterTextField.setText(String.valueOf(meineKoerperdaten.getAlter()));
            geschlechtCombobox.setValue(meineKoerperdaten.getGeschlecht());
            //wenn man etwas verändert will
            speichernBtn.setOnMouseClicked(new EventHandler<>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (geschlechtCombobox.getSelectionModel().getSelectedItem()==null ||
                            alterTextField.getText().length() < 1 || groesseTextField.getText().length() < 1 ||
                            gewichtTextField.getText().length() < 1){
                        setFehlermeldung(textfehler);
                    }
                    meineKoerperdaten.setKoerperdaten(groesseTextField.getDouble(), gewichtTextField.getDouble(), alterTextField.getDouble(), geschlechtCombobox.getValue());
                    calcPart(gridPaneCalcPart);
                }
            });


        }
        ArrayList<Label> labellist = new ArrayList<>();
        labellist.add(new Label("Informationen zum Profil"));
        labellist.add(new Label("Benutzername: " + Main.benutzer.getBenutzername()));
        labellist.add(new Label("Passwort: " + Main.benutzer.getPasswort()));
        labellist.add(new Label("Körperdaten"));
        labellist.add(new Label("Größe (in m): "));

        labellist.add(new Label("Gewicht (in kg): "));
        labellist.add(new Label("Alter (in Jahren): "));
        labellist.add(new Label("Geschlecht: "));

        labellist.get(1).setId("textfield-login");

        gridPane.addRow(0, labellist.get(0));
        gridPane.addRow(1, labellist.get(1));
        gridPane.addRow(2, labellist.get(2));

        gridPane.addRow(3, labellist.get(3));
        gridPane.addRow(4, labellist.get(4), groesseTextField);
        gridPane.addRow(5, labellist.get(5), gewichtTextField);
        gridPane.addRow(6, labellist.get(6), alterTextField);
        gridPane.addRow(7, labellist.get(7), geschlechtCombobox);
        gridPane.addRow(8,new Label(),textfehler);


        gridPane.addRow(9, speichernBtn);

        return gridPane;
    }

    private void setFehlermeldung(Text textfehler){
        /*
           textfehler.setLayoutX(50);
           textfehler.setLayoutY(225);
          */
        textfehler.setFill(Color.RED);
        textfehler.setText("Bitte gib deine vollständigen Daten an");
        textfehler.setVisible(true);
    }

}