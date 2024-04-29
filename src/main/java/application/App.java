package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import application.panels.PaneDistribucion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	// Si el usuario está desconectaco (-1)
	private int userConectado = -1;

	@Override
	public void start(Stage stage) {
		try {

			BorderPane application = new PaneDistribucion(userConectado, stage);

			Scene scene = new Scene(application, 900, 700);
			stage.setTitle("RickyRack");
			stage.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\RickyRack-logo-fondo.png")));
			stage.setScene(scene);
			stage.show();
		} catch (FileNotFoundException e) {
			// THROWS FaltaInterfaz
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}