package br.gov.caixa.testes.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TesteRadioButtonUI extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		AnchorPane pane = new AnchorPane();
		pane.setPrefSize(800, 600);
		RadioButton radioButton1 = new RadioButton("Radio Button 1");
		RadioButton radioButton2 = new RadioButton("Radio Button 2");

		// TODO: add RadioButtons to scene

		ToggleGroup toggleGroup = new ToggleGroup();

		radioButton1.setToggleGroup(toggleGroup);
		radioButton2.setToggleGroup(toggleGroup);
		
		radioButton1.setLayoutX(10);
		radioButton1.setLayoutY(10);
		
		radioButton2.setLayoutX(10);
		radioButton2.setLayoutY(50);
		
		// listen to changes in selected toggle
		toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> System.out.println(newVal + " was selected"));
		
		pane.getChildren().addAll(radioButton1,radioButton2);
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setTitle("Teste Radio Button UI");
		
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
