package application.exceptions;

import javafx.scene.control.Alert;

public class UsuarioExistente extends Alert {

	/**
	 * Alerta de Usuario Existente
	 * 
	 * @param alertType tipo alerta
	 */
	public UsuarioExistente(AlertType alertType) {
		super(alertType);
		this.setTitle("Error usuario existente");
		this.setHeaderText("El nombre de usuario está escogido ya");
		this.setContentText("Pruebe a escoger otro nombre");
		this.show();
	}

}
