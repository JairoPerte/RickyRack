package application.ventana;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaCargando extends Stage {
	public VentanaCargando() {
		try {
			Scene escena = new Scene(new Label(), 400, 200);
			this.setTitle("Espere un momento...");
			this.initModality(Modality.NONE);
			this.setScene(escena);
			this.show();
		} catch (NullPointerException e) {

		}
	}

}
