package application.exceptions;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CampoObligatorios extends Alert {

	public CampoObligatorios(AlertType alertType, Stage stage) {
		super(alertType);
		this.setTitle("Campos Obligatorios");
		this.setHeaderText("Falta algún campo por rellenar");
		this.setContentText("Compruebe que tiene todos los campos rellenos");
		this.initOwner(stage);
		this.show();
	}

}
