package application.exceptions;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ConexionFallida extends Alert {

	/**
	 * Alerta de Conexión Fallida
	 * 
	 * @param alertType tipo alerta
	 * @param stage     stage afectado
	 */
	public ConexionFallida(AlertType alertType, Stage stage) {
		super(alertType);
		this.setTitle("Error al conectar la base de datos");
		this.setHeaderText("Conexión Fallida a la base de datos");
		this.setContentText("No es problema tuyo sino nuestro :(");
		this.initOwner(stage);
		this.showAndWait();
		stage.close();
	}

}
