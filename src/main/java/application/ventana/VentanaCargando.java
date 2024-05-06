package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import application.exceptions.FaltaInterfaz;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaCargando extends Stage {
	public VentanaCargando() {
		try {
			Scene escena = new Scene(new Label(), 400, 200);
			this.setTitle("Espere un momento...");
			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\cargar.gif")));
			this.initModality(Modality.NONE);
			this.setScene(escena);
			this.show();
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}
	}

}
