package application.exceptions;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ContrasenasNoCoincidentes extends Alert {

	/**
	 * Alerta de Contraseñas no coincidentes
	 * 
	 * @param alertType tipo alerta
	 * @param stage     stage afectado
	 */
	public ContrasenasNoCoincidentes(AlertType alertType, Stage stage) {
		super(alertType);
		this.setTitle("Contraseñas no coinciden");
		this.setHeaderText("Comprueba que las contraseñas coincidan");
		this.initOwner(stage);
		this.show();
	}

}
