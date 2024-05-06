package application.exceptions;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class FaltaInterfaz extends Alert {

	/**
	 * Alerta de Falta de Interfaz
	 * 
	 * @param alertType tipo alerta
	 * @param stage     stage afectado
	 */
	public FaltaInterfaz(AlertType alertType, Stage stage) {
		super(alertType);
		this.setTitle("Error falta interfaz");
		this.setHeaderText("No se ha encontrado parte de la interfaz gráfica");
		this.setContentText("Compruebe que en sus archivos no falte ninguna imagen para la interfaz");
		this.initOwner(stage);
		this.showAndWait();
		stage.close();
	}

}
