package br.gov.caixa.testes.ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TesteRadioButtonWithForUI extends Application {

	private ToggleGroup toggleGroup;
	
	@Override
	public void start(Stage stage) throws Exception {
		AnchorPane pane = new AnchorPane();
		pane.setPrefSize(800, 600);
		
		toggleGroup = new ToggleGroup();
		
		for(int i = 1; i <= 10; i++){
			RadioButton radioButton = new RadioButton("Radio Button " + i);
			radioButton.setToggleGroup(toggleGroup);
			radioButton.setLayoutX(10);
			radioButton.setLayoutY(10 + (radioButton.getHeight()+30)*(i-1) );
			pane.getChildren().addAll(radioButton);
		}

				
		// listen to changes in selected toggle
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observer, Toggle oldToggle, Toggle newToggle) {
				System.out.println(toggleGroup.getSelectedToggle() + " was selected");
				RadioButton selectedRadio = (RadioButton)toggleGroup.getSelectedToggle();
				System.out.println(selectedRadio.getText());
			}
		});
		
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setTitle("Teste Radio Button UI");
		
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
