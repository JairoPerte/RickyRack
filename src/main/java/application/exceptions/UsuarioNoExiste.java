package application.exceptions;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class UsuarioNoExiste extends Alert {

	/**
	 * Alerta de usuario no existente
	 * 
	 * @param alertType tipo alerta
	 * @param stage     stage afectado
	 */
	public UsuarioNoExiste(AlertType alertType, Stage stage) {
		super(alertType);
		this.setTitle("Usuario No Existente");
		this.setHeaderText("Dicho usuario no existe");
		this.setContentText("Pruebe que no halla escrito mal el nombre del usuario");
		this.initOwner(stage);
		this.show();
	}

}
