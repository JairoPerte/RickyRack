package application.exceptions;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ContrasenaErronea extends Alert {

	public ContrasenaErronea(AlertType alertType, Stage stage, int numIntentos) {
		super(alertType);
		if (numIntentos != 4) {
			this.setTitle("Contraseña Erronea");
			this.setHeaderText("Contraseña Erronea");
			this.setContentText("Este es su " + numIntentos + " intento, al 3 intento se cierra la aplicación");
			this.initOwner(stage);
			this.show();
		}
	}

}
