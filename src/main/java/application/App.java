package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	@Override
	public void start(Stage stage) {
		try {
			Label label = new Label("Nada de momento");
			Scene scene = new Scene(new StackPane(label), 640, 480);
			stage.setTitle("RickyRack");
			stage.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\RickyRack-logo-fondo.png")));
			stage.setScene(scene);
			stage.show();
		} catch (FileNotFoundException e) {
			// THROWS FaltaInterfaz
		}
	}

	public static void main(String[] args) {
		launch();
	}

}